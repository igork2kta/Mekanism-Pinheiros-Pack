package mekanism.common.tile;

import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.List;

import mekanism.api.Coord4D;
import mekanism.api.MekanismConfig.general;
import mekanism.api.lasers.ILaserReceptor;
import mekanism.api.util.StackUtils;
import mekanism.common.LaserManager;
import mekanism.common.LaserManager.LaserInfo;
import mekanism.common.Mekanism;
import mekanism.common.network.PacketTileEntity.TileEntityMessage;
import mekanism.common.security.ISecurityTile;
import mekanism.common.tile.component.TileComponentSecurity;
import mekanism.common.util.InventoryUtils;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class TileEntityLaserTractorBeam extends TileEntityContainerBlock implements ILaserReceptor, ISecurityTile
{
	public static final double MAX_ENERGY = 5E9;
	public double collectedEnergy = 0;
	public double lastFired = 0;

	public boolean on = false;

	public Coord4D digging;
	public double diggingProgress;

	public static int[] availableSlotIDs = InventoryUtils.getIntRange(0, 26);
	
	public TileComponentSecurity securityComponent = new TileComponentSecurity(this);

	public TileEntityLaserTractorBeam()
	{
		super("LaserTractorBeam");
		inventory = new ItemStack[27];
	}

	@Override
	public void receiveLaserEnergy(double energy, EnumFacing side)
	{
		setEnergy(getEnergy() + energy);
	}

	@Override
	public boolean canLasersDig()
	{
		return false;
	}

	@Override
	public void onUpdate()
	{
		if(worldObj.isRemote)
		{
			if(on)
			{
				MovingObjectPosition mop = LaserManager.fireLaserClient(this, facing, lastFired, worldObj);
				Coord4D hitCoord = mop == null ? null : new Coord4D(mop, worldObj);

				if(hitCoord == null || !hitCoord.equals(digging))
				{
					digging = hitCoord;
					diggingProgress = 0;
				}

				if(hitCoord != null)
				{
					Block blockHit = hitCoord.getBlock(worldObj);
					TileEntity tileHit = hitCoord.getTileEntity(worldObj);
					float hardness = blockHit.getBlockHardness(worldObj, hitCoord.getPos());
					if(!(hardness < 0 || (tileHit instanceof ILaserReceptor && !((ILaserReceptor)tileHit).canLasersDig())))
					{
						diggingProgress += lastFired;

						if(diggingProgress < hardness * general.laserEnergyNeededPerHardness)
						{
							Mekanism.proxy.addHitEffects(hitCoord, mop);
						}
					}
				}

			}
		}
		else {
			if(collectedEnergy > 0)
			{
				double firing = collectedEnergy;

				if(!on || firing != lastFired)
				{
					on = true;
					lastFired = firing;
					Mekanism.packetHandler.sendToAllAround(new TileEntityMessage(Coord4D.get(this), getNetworkedData(new ArrayList<Object>())), Coord4D.get(this).getTargetPoint(50D));
				}

				LaserInfo info = LaserManager.fireLaser(this, facing, firing, worldObj);
				Coord4D hitCoord = info.movingPos == null ? null : new Coord4D(info.movingPos, worldObj);

				if(hitCoord == null || !hitCoord.equals(digging))
				{
					digging = hitCoord;
					diggingProgress = 0;
				}

				if(hitCoord != null)
				{
					Block blockHit = hitCoord.getBlock(worldObj);
					TileEntity tileHit = hitCoord.getTileEntity(worldObj);
					float hardness = blockHit.getBlockHardness(worldObj, hitCoord.getPos());
					if(!(hardness < 0 || (tileHit instanceof ILaserReceptor && !((ILaserReceptor)tileHit).canLasersDig())))
					{
						diggingProgress += firing;

						if(diggingProgress >= hardness * general.laserEnergyNeededPerHardness)
						{
							List<ItemStack> drops = LaserManager.breakBlock(hitCoord, false, worldObj);
							if(drops != null) receiveDrops(drops);
							diggingProgress = 0;
						}
					}
				}

				setEnergy(getEnergy() - firing);
			}
			else if(on)
			{
				on = false;
				diggingProgress = 0;
				Mekanism.packetHandler.sendToAllAround(new TileEntityMessage(Coord4D.get(this), getNetworkedData(new ArrayList<Object>())), Coord4D.get(this).getTargetPoint(50D));
			}
		}
	}

	public void setEnergy(double energy)
	{
		collectedEnergy = Math.max(0, Math.min(energy, MAX_ENERGY));
	}

	public double getEnergy()
	{
		return collectedEnergy;
	}

	public void receiveDrops(List<ItemStack> drops)
	{
		outer:
		for(ItemStack drop : drops)
		{
			for(int i = 0; i < inventory.length; i++)
			{
				if(inventory[i] == null)
				{
					inventory[i] = drop;
					continue outer;
				}
				ItemStack slot = inventory[i];
				if(StackUtils.equalsWildcardWithNBT(slot, drop))
				{
					int change = Math.min(drop.stackSize, slot.getMaxStackSize() - slot.stackSize);
					slot.stackSize += change;
					drop.stackSize -= change;
					if(drop.stackSize <= 0) continue outer;
				}
			}
			dropItem(drop);
		}
	}

	public void dropItem(ItemStack stack)
	{
		EntityItem item = new EntityItem(worldObj, getPos().getX() + 0.5, getPos().getY() + 1, getPos().getZ() + 0.5, stack);
		item.motionX = worldObj.rand.nextGaussian() * 0.05;
		item.motionY = worldObj.rand.nextGaussian() * 0.05 + 0.2;
		item.motionZ = worldObj.rand.nextGaussian() * 0.05;
		item.setPickupDelay(10);
		worldObj.spawnEntityInWorld(item);
	}

	@Override
	public boolean canInsertItem(int i, ItemStack itemStack, EnumFacing side)
	{
		return false;
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side)
	{
		return availableSlotIDs;
	}

	@Override
	public ArrayList<Object> getNetworkedData(ArrayList<Object> data)
	{
		super.getNetworkedData(data);

		data.add(on);
		data.add(collectedEnergy);
		data.add(lastFired);

		return data;
	}

	@Override
	public void handlePacketData(ByteBuf dataStream)
	{
		super.handlePacketData(dataStream);
		
		if(FMLCommonHandler.instance().getEffectiveSide().isClient())
		{
			on = dataStream.readBoolean();
			collectedEnergy = dataStream.readDouble();
			lastFired = dataStream.readDouble();
		}
	}
	
	@Override
	public TileComponentSecurity getSecurity()
	{
		return securityComponent;
	}
}