package net.joosemann.telekinesis.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.joosemann.telekinesis.JooseModTelekinesisFabric;
import net.joosemann.telekinesis.networking.packet.TelekinesisC2SPacket;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.util.Identifier;

public class TelekinesisHudOverlay implements HudRenderCallback {
    private static final Identifier TELEKINESIS_ICON = new Identifier(JooseModTelekinesisFabric.MOD_ID,
            "textures/gui/telekinesis_icon.png");

    @Override
    public void onHudRender(DrawContext drawContext, float tickDelta) {
        // Draw the telekinesis icon on the screen, if it is enabled

        MinecraftClient client = MinecraftClient.getInstance();

        // If telekinesis is not enabled, return
        if (TelekinesisC2SPacket.playerNbt == null || !TelekinesisC2SPacket.playerNbt.contains("telekinesis-enabled") || client == null) { return; }

        // Get the base x and y positions
        int width = client.getWindow().getScaledWidth();

        int x = width / 2;
        // height is the same as y, so just replaced height with y.
        int y = client.getWindow().getScaledHeight();

        // Draw the icon itself, with x and y positions so that it is to the right of the hotbar
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShaderTexture(0, TELEKINESIS_ICON);

        drawContext.drawTexture(TELEKINESIS_ICON, x + 110, y - 20, 0, 0, 16, 16, 16, 16);
    }
}
