package net.joosemann.telekinesis.networking.packet;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.joosemann.telekinesis.util.IEntityDataSaver;
import net.joosemann.telekinesis.util.KeyInputHandler;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class TelekinesisC2SPacket {

    public static NbtCompound playerNbt;

    public static void onJoin(ServerPlayerEntity player) {
        playerNbt = ((IEntityDataSaver) player).getPersistentData();

        // Ensure that telekinesisIsActive is synced with the nbt data
        KeyInputHandler.telekinesisIsActive = playerNbt.contains("telekinesis-enabled");
    }

    // Although the parameters are unused, they have to be there for the packet to be registered correctly
    public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler,
                               PacketByteBuf buf, PacketSender sender) {
        // Flip telekinesisIsActive and the nbt data when the packet is sent (when the toggle telekinesis button is pressed)

        KeyInputHandler.telekinesisIsActive = !KeyInputHandler.telekinesisIsActive;

        playerNbt = ((IEntityDataSaver) player).getPersistentData();

        if (playerNbt.contains("telekinesis-enabled")) { playerNbt.remove("telekinesis-enabled"); }
        else { playerNbt.putBoolean("telekinesis-enabled", true); }

        player.sendMessage(Text.literal(playerNbt.contains("telekinesis-enabled") ? "Telekinesis Enabled!" : "Telekinesis Disabled!"));

        player.saveNbt(playerNbt);
        // JooseModTelekinesisFabric.LOGGER.info(playerNbt.toString());
    }
}
