package mekanism.common.transmitters.grid;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import mekanism.api.Coord4D;
import mekanism.api.transmitters.DynamicNetwork;
import mekanism.api.transmitters.IGridTransmitter;
import mekanism.common.MekanismLang;
import mekanism.common.content.transporter.PathfinderCache;
import mekanism.common.content.transporter.TransitRequest;
import mekanism.common.content.transporter.TransitRequest.TransitResponse;
import mekanism.common.content.transporter.TransporterManager;
import mekanism.common.content.transporter.TransporterStack;
import mekanism.common.util.MekanismUtils;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.chunk.IChunk;

public class InventoryNetwork extends DynamicNetwork<TileEntity, InventoryNetwork, Void> {

    public InventoryNetwork() {
    }

    public InventoryNetwork(UUID networkID) {
        super(networkID);
    }

    public InventoryNetwork(Collection<InventoryNetwork> networks) {
        for (InventoryNetwork net : networks) {
            if (net != null) {
                adoptTransmittersAndAcceptorsFrom(net);
                net.deregister();
            }
        }
        register();
    }

    public List<AcceptorData> calculateAcceptors(TransitRequest request, TransporterStack stack, Long2ObjectMap<IChunk> chunkMap) {
        List<AcceptorData> toReturn = new ArrayList<>();
        for (Coord4D coord : possibleAcceptors) {
            if (coord == null || coord.equals(stack.homeLocation)) {
                continue;
            }
            EnumSet<Direction> sides = acceptorDirections.get(coord);
            if (sides == null || sides.isEmpty()) {
                continue;
            }
            TileEntity acceptor = MekanismUtils.getTileEntity(getWorld(), chunkMap, coord);
            if (acceptor == null) {
                continue;
            }

            AcceptorData data = null;
            for (Direction side : sides) {
                Direction opposite = side.getOpposite();
                TransitResponse response = TransporterManager.getPredictedInsert(acceptor, stack.color, request, opposite);
                if (!response.isEmpty()) {
                    if (data == null) {
                        toReturn.add(data = new AcceptorData(coord, response, opposite));
                    } else {
                        data.sides.add(opposite);
                    }
                }
            }
        }
        return toReturn;
    }

    @Override
    public void commit() {
        super.commit();
        // update the cache when the network has been changed (called when transmitters are added)
        PathfinderCache.onChanged(this);
    }

    @Override
    public void deregister() {
        super.deregister();
        // update the cache when the network has been removed (when transmitters are removed)
        PathfinderCache.onChanged(this);
    }

    @Override
    public void absorbBuffer(IGridTransmitter<TileEntity, InventoryNetwork, Void> transmitter) {
    }

    @Override
    public void clampBuffer() {
    }

    @Override
    protected synchronized void updateCapacity(IGridTransmitter<TileEntity, InventoryNetwork, Void> transmitter) {
        //The capacity is always zero so no point in doing calculations.
    }

    @Override
    public synchronized void updateCapacity() {
        //The capacity is always zero so no point in doing calculations.
    }

    @Override
    public String toString() {
        return "[InventoryNetwork] " + transmitters.size() + " transmitters, " + possibleAcceptors.size() + " acceptors.";
    }

    @Override
    public ITextComponent getNeededInfo() {
        return null;
    }

    @Override
    public ITextComponent getStoredInfo() {
        return null;
    }

    @Override
    public ITextComponent getFlowInfo() {
        return null;
    }

    @Override
    public ITextComponent getTextComponent() {
        return MekanismLang.NETWORK_DESCRIPTION.translate(MekanismLang.INVENTORY_NETWORK, transmitters.size(), possibleAcceptors.size());
    }

    public static class AcceptorData {

        private Coord4D location;
        private TransitResponse response;
        private Set<Direction> sides;

        public AcceptorData(Coord4D coord, TransitResponse ret, Direction side) {
            location = coord;
            response = ret;
            sides = EnumSet.of(side);
        }

        public TransitResponse getResponse() {
            return response;
        }

        public Coord4D getLocation() {
            return location;
        }

        public Set<Direction> getSides() {
            return sides;
        }
    }
}