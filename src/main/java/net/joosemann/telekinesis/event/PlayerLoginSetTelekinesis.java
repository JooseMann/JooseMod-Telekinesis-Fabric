package net.joosemann.telekinesis.event;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.joosemann.telekinesis.JooseModTelekinesisFabric;
import net.joosemann.telekinesis.util.IEntityDataSaver;
import net.joosemann.telekinesis.util.KeyInputHandler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;

public class PlayerLoginSetTelekinesis implements ServerPlayConnectionEvents.Join {

    @Override
    public void onPlayReady(ServerPlayNetworkHandler handler, PacketSender sender, MinecraftServer server) {
        // Set up telekinesis (nbt) data, so that it is preserved across sessions

        JooseModTelekinesisFabric.telekinesisData = ((IEntityDataSaver) handler.player).getTelekinesisData();

        JooseModTelekinesisFabric.LOGGER.info("SERVER: Telekinesis Data: " + JooseModTelekinesisFabric.telekinesisData);

        KeyInputHandler.telekinesisIsActive = JooseModTelekinesisFabric.telekinesisData;

        /*

        // ! This always works in single player, but it also syncs with multiplayer servers...
        // JooseModTelekinesisFabric.telekinesisData = ((IEntityDataSaver) player).getTelekinesisData();

        System.out.println("telekinesisData onJoin: " + JooseModTelekinesisFabric.telekinesisData);

        // Ensure that telekinesisIsActive is synced with the nbt data
        KeyInputHandler.telekinesisIsActive = JooseModTelekinesisFabric.telekinesisData;

        */

        // TelekinesisC2SPacket.onJoin(handler.player);
    }
}
