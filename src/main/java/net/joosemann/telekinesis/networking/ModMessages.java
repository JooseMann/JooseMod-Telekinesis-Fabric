package net.joosemann.telekinesis.networking;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.joosemann.telekinesis.JooseModTelekinesisFabric;
import net.joosemann.telekinesis.networking.packet.TelekinesisC2SPacket;
import net.minecraft.util.Identifier;

public class ModMessages {
    public static final Identifier TELEKINESIS_ID = new Identifier(JooseModTelekinesisFabric.MOD_ID, "telekinesis");

    public static void registerC2SPackets() {
        ServerPlayNetworking.registerGlobalReceiver(TELEKINESIS_ID, TelekinesisC2SPacket::receive);
    }

    public static void registerS2CPackets() {

    }
}
