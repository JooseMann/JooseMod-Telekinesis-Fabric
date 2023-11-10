package net.joosemann.telekinesis.networking.packet;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.joosemann.telekinesis.JooseModTelekinesisFabric;
import net.joosemann.telekinesis.util.IEntityDataSaver;
import net.joosemann.telekinesis.util.KeyInputHandler;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;

public class TelekinesisC2SPacket {

    public static NbtCompound playerNbt;

    public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler,
                               PacketByteBuf buf, PacketSender sender) {

        KeyInputHandler.telekinesisIsActive = !KeyInputHandler.telekinesisIsActive;

        playerNbt = ((IEntityDataSaver) player).getPersistentData();

        if (playerNbt.contains("telekinesis-enabled")) { playerNbt.remove("telekinesis-enabled"); }
        else { playerNbt.putBoolean("telekinesis-enabled", true); }

        player.sendMessage(Text.literal(playerNbt.contains("telekinesis-enabled") ? "Telekinesis Enabled!" : "Telekinesis Disabled!"));

        player.saveNbt(playerNbt);
        // JooseModTelekinesisFabric.LOGGER.info(playerNbt.toString());
    }
}
