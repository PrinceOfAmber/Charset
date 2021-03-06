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

package pl.asie.charset.lib.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetHandler;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.FMLCommonHandler;
import pl.asie.charset.lib.utils.PacketUtils;
import pl.asie.charset.lib.utils.Utils;

public abstract class Packet {
	protected static final IThreadListener getThreadListener(INetHandler handler) {
		IThreadListener listener = FMLCommonHandler.instance().getWorldThread(handler);
		return listener == null ? Utils.getThreadListener() : listener;
	}

	protected static final EntityPlayer getPlayer(INetHandler handler) {
		return PacketUtils.getPlayer(handler);
	}

	public abstract void readData(INetHandler handler, PacketBuffer buf);

	public abstract void apply(INetHandler handler);

	public abstract void writeData(PacketBuffer buf);

	public abstract boolean isAsynchronous();
}
