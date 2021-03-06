/*
 * Copyright (c) 2015, 2016, 2017 Adrian Siekierka
 *
 * This file is part of Charset.
 *
 * Charset is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Charset is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Charset.  If not, see <http://www.gnu.org/licenses/>.
 */

package pl.asie.charset.module.laser.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.asie.charset.lib.Properties;
import pl.asie.charset.lib.block.BlockBase;
import pl.asie.charset.module.laser.CharsetLaser;

import javax.annotation.Nullable;

public class BlockJar extends BlockBase implements ITileEntityProvider {
	private static final AxisAlignedBB[] BOXES = new AxisAlignedBB[3]; // X, Y, Z

	static {
		BOXES[0] = new AxisAlignedBB(0, 2/16f, 2/16f, 1, 14/16f, 14/16f);
		BOXES[1] = new AxisAlignedBB(2/16f, 0, 2/16f, 14/16f, 1, 14/16f);
		BOXES[2] = new AxisAlignedBB(2/16f, 2/16f, 0, 14/16f, 14/16f, 1);
	}

	public BlockJar() {
		super(Material.GLASS);
		setOpaqueCube(false);
		setFullCube(false);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return BOXES[state.getValue(Properties.FACING).getAxis().ordinal()];
	}

	@Override
	public boolean rotateBlock(World world, BlockPos pos, EnumFacing axis) {
		if (super.rotateBlock(world, pos, axis)) {
			TileEntity tile = world.getTileEntity(pos);
			if (tile instanceof TileJar) {
				((TileJar) tile).updateRotations();
			}
			return true;
		} else {
			return false;
		}
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, Properties.FACING, CharsetLaser.LASER_COLOR);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(Properties.FACING, EnumFacing.getFront(meta & 7));
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(Properties.FACING).ordinal();
	}

	@Nullable
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileJar();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.TRANSLUCENT;
	}

	@Override
	public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> itemList) {
		for (int i = 0; i <= 7; i++) {
			itemList.add(new ItemStack(this, 1, i));
		}
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		TileEntity tile = worldIn.getTileEntity(pos);
		if (tile instanceof TileJar) {
			return state.withProperty(CharsetLaser.LASER_COLOR, ((TileJar) tile).getColor());
		} else {
			return state;
		}
	}

	@Override
	public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
		return this.getDefaultState().withProperty(Properties.FACING, EnumFacing.getDirectionFromEntityLiving(pos, placer));
	}
}
