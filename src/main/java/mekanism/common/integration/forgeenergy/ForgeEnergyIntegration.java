package mekanism.common.integration.forgeenergy;

import mekanism.api.Action;
import mekanism.api.math.FloatingLong;
import mekanism.api.energy.IStrictEnergyHandler;
import mekanism.common.integration.EnergyCompatUtils.EnergyType;
import net.minecraftforge.energy.IEnergyStorage;

public class ForgeEnergyIntegration implements IEnergyStorage {

    private final IStrictEnergyHandler handler;

    public ForgeEnergyIntegration(IStrictEnergyHandler handler) {
        this.handler = handler;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        if (maxReceive <= 0) {
            return 0;
        }
        FloatingLong toInsert = EnergyType.FORGE.convertFrom(maxReceive);
        return EnergyType.FORGE.convertToAsInt(toInsert.subtract(handler.insertEnergy(toInsert, Action.get(!simulate))));
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        return maxExtract <= 0 ? 0 : EnergyType.FORGE.convertToAsInt(handler.extractEnergy(EnergyType.FORGE.convertFrom(maxExtract), Action.get(!simulate)));
    }

    @Override
    public int getEnergyStored() {
        if (handler.getEnergyContainerCount() > 0) {
            //TODO: Improve on this
            return EnergyType.FORGE.convertToAsInt(handler.getEnergy(0));
        }
        return 0;
    }

    @Override
    public int getMaxEnergyStored() {
        if (handler.getEnergyContainerCount() > 0) {
            //TODO: Improve on this
            return EnergyType.FORGE.convertToAsInt(handler.getMaxEnergy(0));
        }
        return 0;
    }

    @Override
    public boolean canExtract() {
        return !handler.extractEnergy(FloatingLong.ONE, Action.SIMULATE).isEmpty();
    }

    @Override
    public boolean canReceive() {
        return handler.insertEnergy(FloatingLong.ONE, Action.SIMULATE).smallerThan(FloatingLong.ONE);
    }
}