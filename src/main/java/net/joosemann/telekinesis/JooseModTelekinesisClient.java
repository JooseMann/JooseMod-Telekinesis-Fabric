package net.joosemann.telekinesis;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.joosemann.telekinesis.client.TelekinesisHudOverlay;
import net.joosemann.telekinesis.networking.packet.TelekinesisTogglePacket;
import net.joosemann.telekinesis.util.KeyInputHandler;
import net.minecraft.util.Identifier;

public class JooseModTelekinesisClient implements ClientModInitializer {

    // Identifier for toggling telekinesis.
    public static Identifier telekinesisIdentifier = new Identifier(JooseModTelekinesisFabric.MOD_ID, "telekinesis-toggle");

    @Override
    public void onInitializeClient() {
        // Register things that are client-side only

        KeyInputHandler.register();

        startPacketReceiver();

        HudRenderCallback.EVENT.register(new TelekinesisHudOverlay());
    }

    public static void startPacketReceiver() {
        // Toggle Telekinesis Event
        ClientPlayNetworking.registerGlobalReceiver(telekinesisIdentifier, (client, handler, buf, responseSender) -> {
            if (client.player == null) { return; }
            client.execute(() -> {

                // Flip Telekinesis (enable / disable it)
                KeyInputHandler.telekinesisIsActive = !KeyInputHandler.telekinesisIsActive;
            });
        });
    }

    public static void sendTelekinesisPacket() {
        ClientPlayNetworking.send(telekinesisIdentifier, TelekinesisTogglePacket.createPacket());
    }
}
