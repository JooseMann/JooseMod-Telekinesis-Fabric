package net.joosemann.telekinesis;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.joosemann.telekinesis.client.TelekinesisHudOverlay;
import net.joosemann.telekinesis.networking.ModMessages;
import net.joosemann.telekinesis.util.KeyInputHandler;

public class JooseModTelekinesisClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // Register things that are client-side only

        KeyInputHandler.register();
        ModMessages.registerS2CPackets();

        HudRenderCallback.EVENT.register(new TelekinesisHudOverlay());
    }
}
