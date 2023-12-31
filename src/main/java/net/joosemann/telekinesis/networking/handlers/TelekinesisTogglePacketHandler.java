package net.joosemann.telekinesis.networking.handlers;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.joosemann.telekinesis.JooseModTelekinesisClient;
import net.joosemann.telekinesis.JooseModTelekinesisFabric;
import net.joosemann.telekinesis.util.IEntityDataSaver;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;

public class TelekinesisTogglePacketHandler {
    // On the server side, execute the code to toggle telekinesis.
    public static void register() {
        ServerPlayNetworking.registerGlobalReceiver(JooseModTelekinesisClient.telekinesisIdentifier, ((server, player, handler, buf, responseSender) -> {
            server.execute(() -> {

                // The version of the player that has the telekinesis data
                IEntityDataSaver iPlayer = (IEntityDataSaver) player;

                // Flip the telekinesis data and update it.
                iPlayer.flipTelekinesisData();
                JooseModTelekinesisFabric.telekinesisData = iPlayer.getTelekinesisData();

                // Tell the player that the telekinesis data was updated
                handler.player.sendMessage(Text.literal(JooseModTelekinesisFabric.telekinesisData ? "Telekinesis Enabled!" : "Telekinesis Disabled!"));

                // Send a packet to the client to update KeyInputHandler.telekinesisIsActive
                ServerPlayNetworking.send(player, JooseModTelekinesisClient.telekinesisIdentifier, new PacketByteBuf(Unpooled.buffer()));
            });
        }));
    }
}
