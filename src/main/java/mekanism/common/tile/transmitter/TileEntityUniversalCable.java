package mekanism.common.tile.transmitter;

import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import mekanism.api.Action;
import mekanism.api.NBTConstants;
import mekanism.api.block.IHasTileEntity;
import mekanism.api.energy.IEnergyContainer;
import mekanism.api.energy.IMekanismStrictEnergyHandler;
import mekanism.api.energy.IStrictEnergyHandler;
import mekanism.api.inventory.AutomationType;
import mekanism.api.math.FloatingLong;
import mekanism.api.providers.IBlockProvider;
import mekanism.api.tier.AlloyTier;
import mekanism.api.tier.BaseTier;
import mekanism.api.transmitters.TransmissionType;
import mekanism.common.block.attribute.Attribute;
import mekanism.common.block.states.BlockStateHelper;
import mekanism.common.block.states.TransmitterType;
import mekanism.common.capabilities.energy.BasicEnergyContainer;
import mekanism.common.capabilities.proxy.ProxyStrictEnergyHandler;
import mekanism.common.integration.EnergyCompatUtils;
import mekanism.common.registries.MekanismBlocks;
import mekanism.common.tier.CableTier;
import mekanism.common.transmitters.grid.EnergyNetwork;
import mekanism.common.upgrade.transmitter.TransmitterUpgradeData;
import mekanism.common.upgrade.transmitter.UniversalCableUpgradeData;
import mekanism.common.util.CableUtils;
import mekanism.common.util.NBTUtils;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

public class TileEntityUniversalCable extends TileEntityTransmitter<IStrictEnergyHandler, EnergyNetwork, FloatingLong> implements IMekanismStrictEnergyHandler {

    public final CableTier tier;

    private ProxyStrictEnergyHandler readOnlyStrictEnergyHandler;
    private final Map<Direction, ProxyStrictEnergyHandler> strictEnergyHandlers;
    private final List<IEnergyContainer> energyContainers;
    public BasicEnergyContainer buffer;
    public double currentPower;
    public FloatingLong lastWrite = FloatingLong.ZERO;

    public TileEntityUniversalCable(IBlockProvider blockProvider) {
        super(((IHasTileEntity<TileEntityUniversalCable>) blockProvider.getBlock()).getTileType());
        this.tier = Attribute.getTier(blockProvider.getBlock(), CableTier.class);
        strictEnergyHandlers = new EnumMap<>(Direction.class);
        buffer = BasicEnergyContainer.create(getCableCapacity(), BasicEnergyContainer.alwaysFalse, BasicEnergyContainer.alwaysTrue, this);
        energyContainers = Collections.singletonList(buffer);
    }

    /**
     * Lazily get and cache an IStrictEnergyHandler instance for the given side, and make it be read only if something else is trying to interact with us using the null
     * side
     */
    private IStrictEnergyHandler getEnergyHandler(@Nullable Direction side) {
        if (side == null) {
            if (readOnlyStrictEnergyHandler == null) {
                readOnlyStrictEnergyHandler = new ProxyStrictEnergyHandler(this, null, null);
            }
            return readOnlyStrictEnergyHandler;
        }
        ProxyStrictEnergyHandler energyHandler = strictEnergyHandlers.get(side);
        if (energyHandler == null) {
            strictEnergyHandlers.put(side, energyHandler = new ProxyStrictEnergyHandler(this, side, null));
        }
        return energyHandler;
    }

    @Override
    public void tick() {
        if (isRemote()) {
            double targetPower = getTransmitter().hasTransmitterNetwork() ? getTransmitter().getTransmitterNetwork().energyScale : 0;
            if (Math.abs(currentPower - targetPower) > 0.01) {
                currentPower = (9 * currentPower + targetPower) / 10;
            }
        } else {
            updateShare();
            Set<Direction> connections = getConnections(ConnectionType.PULL);
            if (!connections.isEmpty()) {
                for (IStrictEnergyHandler connectedAcceptor : CableUtils.getConnectedAcceptors(getPos(), getWorld(), connections)) {
                    if (connectedAcceptor != null) {
                        FloatingLong received = connectedAcceptor.extractEnergy(getAvailablePull(), Action.SIMULATE);
                        if (!received.isEmpty() && takeEnergy(received, Action.SIMULATE).isEmpty()) {
                            //If we received some energy and are able to insert it all
                            FloatingLong remainder = takeEnergy(received, Action.EXECUTE);
                            connectedAcceptor.extractEnergy(received.subtract(remainder), Action.EXECUTE);
                        }
                    }
                }
            }
        }
        super.tick();
    }

    private FloatingLong getAvailablePull() {
        if (getTransmitter().hasTransmitterNetwork()) {
            return getCableCapacity().min(getTransmitter().getTransmitterNetwork().energyContainer.getNeeded());
        }
        return getCableCapacity().min(buffer.getNeeded());
    }

    @Nonnull
    @Override
    public List<IEnergyContainer> getEnergyContainers(@Nullable Direction side) {
        if (getTransmitter().hasTransmitterNetwork()) {
            //TODO: Do we want this to fallback to local if the one on the network is empty?
            return getTransmitter().getTransmitterNetwork().getEnergyContainers(side);
        }
        return energyContainers;
    }

    @Override
    public void onContentsChanged() {
        markDirty();
    }

    @Nonnull
    @Override
    public FloatingLong insertEnergy(int container, @Nonnull FloatingLong amount, @Nullable Direction side, @Nonnull Action action) {
        IEnergyContainer energyContainer = getEnergyContainer(container, side);
        if (energyContainer == null) {
            return amount;
        } else if (side == null) {
            return energyContainer.insert(amount, action, AutomationType.INTERNAL);
        }
        //If we have a side only allow inserting if our connection allows it
        ConnectionType connectionType = getConnectionType(side);
        if (connectionType == ConnectionType.NORMAL || connectionType == ConnectionType.PULL) {
            return energyContainer.insert(amount, action, AutomationType.EXTERNAL);
        }
        return amount;
    }

    @Override
    public void updateShare() {
        if (getTransmitter().hasTransmitterNetwork() && getTransmitter().getTransmitterNetworkSize() > 0) {
            FloatingLong last = getSaveShare();
            if (!last.equals(lastWrite)) {
                lastWrite = last;
                markDirty();
            }
        }
    }

    private FloatingLong getSaveShare() {
        if (getTransmitter().hasTransmitterNetwork()) {
            EnergyNetwork transmitterNetwork = getTransmitter().getTransmitterNetwork();
            if (!transmitterNetwork.energyContainer.isEmpty()) {
                int size = transmitterNetwork.transmittersSize();
                FloatingLong toSave = transmitterNetwork.energyContainer.getEnergy().divide(size);
                if (transmitterNetwork.firstTransmitter().equals(getTransmitter())) {
                    toSave.plusEqual(transmitterNetwork.energyContainer.getEnergy().modulo(size));
                }
                return toSave;
            }
            //TODO: Figure out unless the above works fine
            //return EnergyNetwork.round(getTransmitter().getTransmitterNetwork().energyContainer.getEnergy() * (1F / getTransmitter().getTransmitterNetwork().transmittersSize()));
        }
        return FloatingLong.ZERO;
    }

    @Override
    public TransmitterType getTransmitterType() {
        return TransmitterType.UNIVERSAL_CABLE;
    }

    @Override
    public void read(CompoundNBT nbtTags) {
        super.read(nbtTags);
        NBTUtils.setFloatingLongIfPresent(nbtTags, NBTConstants.ENERGY_STORED, buffer::setEnergy);
    }

    @Nonnull
    @Override
    public CompoundNBT write(CompoundNBT nbtTags) {
        super.write(nbtTags);
        nbtTags.put(NBTConstants.ENERGY_STORED, lastWrite.serializeNBT());
        return nbtTags;
    }

    @Override
    public TransmissionType getTransmissionType() {
        return TransmissionType.ENERGY;
    }

    @Override
    public EnergyNetwork createNetworkByMerging(Collection<EnergyNetwork> networks) {
        return new EnergyNetwork(networks);
    }

    @Override
    public boolean isValidAcceptor(TileEntity acceptor, Direction side) {
        return CableUtils.isValidAcceptorOnSide(acceptor, side);
    }

    @Override
    public EnergyNetwork createNewNetwork() {
        return new EnergyNetwork();
    }

    @Nonnull
    @Override
    public FloatingLong getBuffer() {
        return buffer.getEnergy();
    }

    @Override
    public boolean noBufferOrFallback() {
        return getBufferWithFallback().isEmpty();
    }

    @Nonnull
    @Override
    public FloatingLong getBufferWithFallback() {
        FloatingLong buffer = getBuffer();
        //If we don't have a buffer try falling back to the network's buffer
        if (buffer.isEmpty() && getTransmitter().hasTransmitterNetwork()) {
            return getTransmitter().getTransmitterNetwork().getBuffer();
        }
        return buffer;
    }

    @Override
    public void takeShare() {
        if (getTransmitter().hasTransmitterNetwork()) {
            EnergyNetwork transmitterNetwork = getTransmitter().getTransmitterNetwork();
            if (!transmitterNetwork.energyContainer.isEmpty() && !lastWrite.isEmpty()) {
                transmitterNetwork.energyContainer.setEnergy(transmitterNetwork.energyContainer.getEnergy().subtract(lastWrite));
                buffer.setEnergy(lastWrite);
            }
        }
    }

    public FloatingLong getCableCapacity() {
        return tier.getCableCapacity();
    }

    @Override
    public int getCapacity() {
        return getCableCapacity().intValue();
    }

    /**
     * @return remainder
     */
    private FloatingLong takeEnergy(FloatingLong amount, Action action) {
        if (getTransmitter().hasTransmitterNetwork()) {
            return getTransmitter().getTransmitterNetwork().energyContainer.insert(amount, action, AutomationType.INTERNAL);
        }
        return buffer.insert(amount, action, AutomationType.INTERNAL);
    }

    @Override
    public IStrictEnergyHandler getCachedAcceptor(Direction side) {
        return EnergyCompatUtils.getStrictEnergyHandler(getCachedTile(side), side.getOpposite());
    }

    @Override
    protected boolean canUpgrade(AlloyTier alloyTier) {
        return alloyTier.getBaseTier().ordinal() == tier.getBaseTier().ordinal() + 1;
    }

    @Nonnull
    @Override
    protected BlockState upgradeResult(@Nonnull BlockState current, @Nonnull BaseTier tier) {
        switch (tier) {
            case BASIC:
                return BlockStateHelper.copyStateData(current, MekanismBlocks.BASIC_UNIVERSAL_CABLE.getBlock().getDefaultState());
            case ADVANCED:
                return BlockStateHelper.copyStateData(current, MekanismBlocks.ADVANCED_UNIVERSAL_CABLE.getBlock().getDefaultState());
            case ELITE:
                return BlockStateHelper.copyStateData(current, MekanismBlocks.ELITE_UNIVERSAL_CABLE.getBlock().getDefaultState());
            case ULTIMATE:
                return BlockStateHelper.copyStateData(current, MekanismBlocks.ULTIMATE_UNIVERSAL_CABLE.getBlock().getDefaultState());
        }
        return current;
    }

    @Nullable
    @Override
    protected UniversalCableUpgradeData getUpgradeData() {
        return new UniversalCableUpgradeData(redstoneReactive, connectionTypes, buffer);
    }

    @Override
    protected void parseUpgradeData(@Nonnull TransmitterUpgradeData upgradeData) {
        if (upgradeData instanceof UniversalCableUpgradeData) {
            UniversalCableUpgradeData data = (UniversalCableUpgradeData) upgradeData;
            redstoneReactive = data.redstoneReactive;
            connectionTypes = data.connectionTypes;
            buffer.setEnergy(data.buffer.getEnergy());
        } else {
            super.parseUpgradeData(upgradeData);
        }
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction side) {
        if (EnergyCompatUtils.isEnergyCapability(capability)) {
            List<IEnergyContainer> energyContainers = getEnergyContainers(side);
            return energyContainers.isEmpty() ? LazyOptional.empty() : EnergyCompatUtils.getEnergyCapability(capability, getEnergyHandler(side));
        }
        return super.getCapability(capability, side);
    }
}