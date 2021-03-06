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

package pl.asie.charset.lib.ui;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.Nullable;
import java.util.function.Function;

/**
 * Created by asie on 2/10/17.
 */
public class GuiHandlerCharset implements IGuiHandler {
	public static class Request {
		public final EntityPlayer player;
		public final World world;
		public final int x, y, z;

		private Request(EntityPlayer player, World world, int x, int y, int z) {
			this.player = player;
			this.world = world;
			this.x = x;
			this.y = y;
			this.z = z;

		}
	}

	public static final int POCKET_TABLE = 0x100;
	public static final int KEYRING = 0x101;
	public static final int CHISEL = 0x102;

	public static final GuiHandlerCharset INSTANCE = new GuiHandlerCharset();
	private static final TIntObjectMap<Function<Request, Object>> map = new TIntObjectHashMap<>();

	public void register(int id, Side side, Function<Request, Object> supplier) {
		int rId = id * 2 + (side == Side.CLIENT ? 1 : 0);
		if (map.containsKey(rId)) {
			throw new RuntimeException("GuiHandler ID " + id + "[" + side.name() + "] is taken!");
		}
		map.put(rId, supplier);
	}

	private GuiHandlerCharset() {

	}

	@Nullable
	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		Function<Request, Object> supplier = map.get(id * 2);
		return supplier != null ? supplier.apply(new Request(player, world, x, y, z)) : null;
	}

	@Nullable
	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		Function<Request, Object> supplier = map.get(id * 2 + 1);
		return supplier != null ? supplier.apply(new Request(player, world, x, y, z)) : null;
	}
}
