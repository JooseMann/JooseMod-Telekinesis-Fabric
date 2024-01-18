package net.joosemann.telekinesis.networking.packet;

import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.packet.Packet;

public class AttackTelekinesisPacket implements Packet {
    // Create the packet
    public static PacketByteBuf createPacket() {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());

        return buf;
    }

    // Write and apply methods are empty, since only the createPacket() method is called.
    // However, they are still needed because we are implementing Packet.
    @Override
    public void write(PacketByteBuf buf) {

    }

    @Override
    public void apply(PacketListener listener) {

    }
}
