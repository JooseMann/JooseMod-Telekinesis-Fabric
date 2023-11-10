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

        MinecraftClient client = MinecraftClient.getInstance();

        if (TelekinesisC2SPacket.playerNbt == null || !TelekinesisC2SPacket.playerNbt.contains("telekinesis-enabled")) { return; }

        int x = 0;
        int y = 0;


        if (client != null) {
            int width = client.getWindow().getScaledWidth();
            int height = client.getWindow().getScaledHeight();

            x = width / 2;
            y = height;

        }

        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShaderTexture(0, TELEKINESIS_ICON);

        drawContext.drawTexture(TELEKINESIS_ICON, x + 110, y - 20, 0, 0, 16, 16, 16, 16);
    }
}
