package net.joosemann.telekinesis.event;

import net.fabricmc.fabric.api.networking.v1.ServerLoginConnectionEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerLoginNetworkHandler;

public class PlayerLoginSetTelekinesis implements ServerLoginConnectionEvents.Init {
    @Override
    public void onLoginInit(ServerLoginNetworkHandler handler, MinecraftServer server) {

    }
}
