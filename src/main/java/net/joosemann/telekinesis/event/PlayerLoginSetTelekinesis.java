package net.joosemann.telekinesis.event;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.joosemann.telekinesis.networking.packet.TelekinesisC2SPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;

public class PlayerLoginSetTelekinesis implements ServerPlayConnectionEvents.Join {
    /*@Override
    public void onLoginInit(ServerLoginNetworkHandler handler, MinecraftServer server) {


        JooseModTelekinesisFabric.LOGGER.info(handler.getConnectionInfo());
        *//*String[] playerInfo = handler.getConnectionInfo().split("]");
        String playerName = playerInfo.*//*
    }*/

    @Override
    public void onPlayReady(ServerPlayNetworkHandler handler, PacketSender sender, MinecraftServer server) {
        // Set up telekinesis persistent (nbt) data, so that it is preserved across sessions
        TelekinesisC2SPacket.onJoin(handler.player);
    }
}
