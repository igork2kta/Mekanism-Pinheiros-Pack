package mekanism.common.multiblock;

import io.netty.buffer.ByteBuf;

import java.util.ArrayList;

import net.minecraftforge.fml.common.FMLCommonHandler;
import mekanism.common.PacketHandler;
import mekanism.common.tile.TileEntityBasicBlock;

public class TileEntityInternalMultiblock extends TileEntityBasicBlock
{
	public String multiblockUUID;

	@Override
	public void onUpdate() {}
	
	@Override
	public void handlePacketData(ByteBuf dataStream)
	{
		super.handlePacketData(dataStream);
		
		if(FMLCommonHandler.instance().getEffectiveSide().isClient())
		{
			if(dataStream.readBoolean())
			{
				multiblockUUID = PacketHandler.readString(dataStream);
			}
			else {
				multiblockUUID = null;
			}
		}
	}

	@Override
	public ArrayList<Object> getNetworkedData(ArrayList<Object> data)
	{
		super.getNetworkedData(data);
		
		if(multiblockUUID != null)
		{
			data.add(true);
			data.add(multiblockUUID);
		}
		else {
			data.add(false);
		}
		
		return data;
	}
	
	public void setMultiblock(String id)
	{
		multiblockUUID = id;
	}
}