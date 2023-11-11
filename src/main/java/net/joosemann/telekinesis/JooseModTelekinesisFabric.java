package net.joosemann.telekinesis;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.joosemann.telekinesis.enchantments.ExperienceEnchantment;
import net.joosemann.telekinesis.enchantments.MasteryEnchantment;
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
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static Identifier experienceIdentifier = new Identifier("joosemod", "experience");
	public static Identifier masteryIdentifier = new Identifier("joosemod", "mastery");

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("JooseMod (Telekinesis) has been activated!");

		// Initialize Events
		ServerEntityCombatEvents.AFTER_KILLED_OTHER_ENTITY.register(new AttackEntityHandler());
		PlayerBlockBreakEvents.BEFORE.register(new TelekinesisBlockBreak());
		ServerEntityEvents.ENTITY_LOAD.register(new TelekinesisItemDrop());
		ServerPlayConnectionEvents.JOIN.register(new PlayerLoginSetTelekinesis());

		// Initialize C2S packets
		ModMessages.registerC2SPackets();

		// Register custom enchantments
		Registry.register(Registries.ENCHANTMENT, experienceIdentifier, new ExperienceEnchantment
				(Enchantment.Rarity.UNCOMMON, EnchantmentTarget.DIGGER, new EquipmentSlot[] {EquipmentSlot.MAINHAND} ));
		Registry.register(Registries.ENCHANTMENT, masteryIdentifier, new MasteryEnchantment
				(Enchantment.Rarity.UNCOMMON, EnchantmentTarget.WEAPON, new EquipmentSlot[] {EquipmentSlot.MAINHAND} ));
	}
}