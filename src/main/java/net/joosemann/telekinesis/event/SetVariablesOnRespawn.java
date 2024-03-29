package net.joosemann.telekinesis.event;

import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.joosemann.telekinesis.JooseModTelekinesisFabric;
import net.joosemann.telekinesis.util.IEntityDataSaver;
import net.joosemann.telekinesis.util.KeyInputHandler;
import net.minecraft.server.network.ServerPlayerEntity;

public class SetVariablesOnRespawn implements ServerPlayerEvents.AfterRespawn {
    @Override
    public void afterRespawn(ServerPlayerEntity oldPlayer, ServerPlayerEntity newPlayer, boolean alive) {

        // Reset the telekinesis data on death
        // There's probably a way to preserve the data between deaths, but I'm not sure how to do it (and it's not a huge issue)

        IEntityDataSaver iPlayer = (IEntityDataSaver) newPlayer;

        JooseModTelekinesisFabric.telekinesisData = iPlayer.getTelekinesisData();
        KeyInputHandler.telekinesisIsActive = false;

        // Update the list of players, whenever one joins
        JooseModTelekinesisFabric.players.remove(oldPlayer);
        JooseModTelekinesisFabric.players.add(newPlayer);
    }
}
