package net.joosemann.telekinesis.util;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.joosemann.telekinesis.JooseModTelekinesisClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class KeyInputHandler {
    public static final String KEY_CATEGORY = "key.category.joosemod-telekinesis.joosemod";

    public static final String KEY_TELEKINESIS = "key.joosemod-telekinesis.telekinesis";

    public static KeyBinding toggleTelekinesis;

    public static boolean telekinesisIsActive = false;

    // Register the input handler
    public static void registerKeyInputs() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            // If the telekinesis toggle button was pressed, flip the nbt data and telekinesisIsActive via a C2S packet
            if (toggleTelekinesis.wasPressed() && client.player != null) {
                JooseModTelekinesisClient.sendTelekinesisPacket();
            }
        });
    }

    // Register the keybinding
    public static void register() {
        toggleTelekinesis = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                KEY_TELEKINESIS, InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_ALT, KEY_CATEGORY
        ));

        registerKeyInputs();
    }
}
