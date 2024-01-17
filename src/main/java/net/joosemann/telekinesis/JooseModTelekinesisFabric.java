package net.joosemann.telekinesis;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.joosemann.telekinesis.enchantments.ExperienceEnchantment;
import net.joosemann.telekinesis.enchantments.MasteryEnchantment;
import net.joosemann.telekinesis.event.*;
import net.joosemann.telekinesis.networking.handlers.TelekinesisTogglePacketHandler;
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
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	// Identifiers for the enchantments: used to set them up and get them later.
	public static Identifier experienceIdentifier = new Identifier("joosemod", "experience");
	public static Identifier masteryIdentifier = new Identifier("joosemod", "mastery");

	// Public version of the telekinesisData in PlayerEntityMixin. Used when a player is not available to check for it directly with
	public static boolean telekinesisData = false;

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("JooseMod (Telekinesis) has been activated!");

		// Initialize Events
		ServerEntityCombatEvents.AFTER_KILLED_OTHER_ENTITY.register(new AttackEntityHandler());
		ServerPlayConnectionEvents.JOIN.register(new PlayerLoginSetTelekinesis());
		ServerEntityEvents.ENTITY_LOAD.register(new TelekinesisItemDrop());
		PlayerBlockBreakEvents.BEFORE.register(new TelekinesisBlockBreak());
		ServerPlayerEvents.AFTER_RESPAWN.register(new SetTelekinesisOnRespawn());

		// Initialize packets
		TelekinesisTogglePacketHandler.register();

		// Register custom enchantments
		Registry.register(Registries.ENCHANTMENT, experienceIdentifier, new ExperienceEnchantment
				(Enchantment.Rarity.UNCOMMON, EnchantmentTarget.DIGGER, new EquipmentSlot[]{EquipmentSlot.MAINHAND}));
		Registry.register(Registries.ENCHANTMENT, masteryIdentifier, new MasteryEnchantment
				(Enchantment.Rarity.UNCOMMON, EnchantmentTarget.WEAPON, new EquipmentSlot[]{EquipmentSlot.MAINHAND}));
	}
}