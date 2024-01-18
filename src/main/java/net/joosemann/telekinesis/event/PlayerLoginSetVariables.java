package net.joosemann.telekinesis.event;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.joosemann.telekinesis.JooseModTelekinesisFabric;
import net.joosemann.telekinesis.util.IEntityDataSaver;
import net.joosemann.telekinesis.util.KeyInputHandler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.ArrayList;
import java.util.List;

public class PlayerLoginSetVariables implements ServerPlayConnectionEvents.Join {

    @Override
    public void onPlayReady(ServerPlayNetworkHandler handler, PacketSender sender, MinecraftServer server) {

        // Set up telekinesis (nbt) data, so that it is preserved across sessions
        JooseModTelekinesisFabric.telekinesisData = ((IEntityDataSaver) handler.player).getTelekinesisData();

        KeyInputHandler.telekinesisIsActive = JooseModTelekinesisFabric.telekinesisData;

        // Update the list of players, whenever one joins
        JooseModTelekinesisFabric.players.add(handler.player);

        deleteDuplicatePlayers();
    }

    public void deleteDuplicatePlayers() {
        // Delete duplicate players from the list of players. This ensures that telekinesis works with killing mobs correctly.
        List<ServerPlayerEntity> listOfUniquePlayers = new ArrayList<>(JooseModTelekinesisFabric.players);

        JooseModTelekinesisFabric.players.forEach(player -> {
            if (player.isRemoved()) { listOfUniquePlayers.remove(player); }
        });

        JooseModTelekinesisFabric.players = new ArrayList<>(listOfUniquePlayers);
    }
}
