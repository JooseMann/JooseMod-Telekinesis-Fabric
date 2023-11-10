package net.joosemann.telekinesis;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.joosemann.telekinesis.enchantments.ExperienceEnchantment;
import net.joosemann.telekinesis.event.AttackEntityHandler;
import net.joosemann.telekinesis.event.PlayerLoginSetTelekinesis;
import net.joosemann.telekinesis.event.TelekinesisBlockBreak;
import net.joosemann.telekinesis.event.TelekinesisItemDrop;
import net.joosemann.telekinesis.networking.ModMessages;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JooseModTelekinesisFabric implements ModInitializer {

	public static final String MOD_ID = "joosemod-telekinesis-fabric";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static Identifier experienceIdentifier = new Identifier("joosemod", "experience");


	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("JooseMod (Telekinesis) has been activated!");

		AttackEntityCallback.EVENT.register(new AttackEntityHandler());
		// PlayerBlockBreakEvents.AFTER.register(new Telekinesis());
		PlayerBlockBreakEvents.BEFORE.register(new TelekinesisBlockBreak());
		ServerEntityEvents.ENTITY_LOAD.register(new TelekinesisItemDrop());
		ServerPlayConnectionEvents.JOIN.register(new PlayerLoginSetTelekinesis());

		ModMessages.registerC2SPackets();

		Registry.register(Registries.ENCHANTMENT, new Identifier("joosemod", "experience"), new ExperienceEnchantment
				(Enchantment.Rarity.UNCOMMON, EnchantmentTarget.DIGGER, new EquipmentSlot[] {EquipmentSlot.MAINHAND} ));
	}
}