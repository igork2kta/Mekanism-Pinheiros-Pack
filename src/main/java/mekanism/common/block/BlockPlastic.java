package mekanism.common.block;

import static mekanism.common.block.states.BlockStatePlastic.colorProperty;

import java.util.List;

import mekanism.api.EnumColor;
import mekanism.common.Mekanism;
import mekanism.common.block.states.BlockStatePlastic;
import mekanism.common.block.states.BlockStatePlastic.PlasticBlockType;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockPlastic extends Block
{
	public PlasticBlockType type;
	
	public BlockPlastic(PlasticBlockType blockType)
	{
		super(Material.wood);
		type = blockType;
		setHardness(type == PlasticBlockType.REINFORCED ? 50F : 5F);
		setResistance(type == PlasticBlockType.REINFORCED ? 2000F : 10F);
		setCreativeTab(Mekanism.tabMekanism);
		
		if(type == PlasticBlockType.SLICK)
		{
			slipperiness = 0.98F;
		}
	}

	@Override
	protected BlockState createBlockState()
	{
		return new BlockStatePlastic(this);
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return getDefaultState().withProperty(colorProperty, EnumDyeColor.byMetadata(meta));
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(colorProperty).getMetadata();
	}

	@Override
	public Vec3 modifyAcceleration(World world, BlockPos pos, Entity e, Vec3 motion)
	{
		if(type == PlasticBlockType.ROAD)
		{
			double boost = 1.6;

			double a = Math.atan2(motion.xCoord, motion.zCoord);
			return new Vec3(motion.xCoord + Math.sin(a) * boost * slipperiness, motion.yCoord, motion.zCoord + Math.cos(a) * boost * slipperiness);
		}
		
		return motion;
	}

	@Override
	public int damageDropped(IBlockState state)
	{
		return getMetaFromState(state);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item item, CreativeTabs creativetabs, List<ItemStack> list)
	{
		for(int i = 0; i < EnumColor.DYES.length; i++)
		{
			list.add(new ItemStack(item, 1, i));
		}
	}

	@Override
	public int colorMultiplier(IBlockAccess world, BlockPos pos, int renderPass)
	{
		return getRenderColor(world.getBlockState(pos));
	}

	@Override
	public int getRenderColor(IBlockState state)
	{
		EnumDyeColor color = state.getValue(colorProperty);
		EnumColor dye = EnumColor.DYES[color.getDyeDamage()];
		
		return (int)(dye.getColor(0)*255) << 16 | (int)(dye.getColor(1)*255) << 8 | (int)(dye.getColor(2)*255);
	}

	@Override
	public int getLightValue()
	{
		if(type == PlasticBlockType.GLOW)
		{
			return 10;
		}

		return 0;
	}
}