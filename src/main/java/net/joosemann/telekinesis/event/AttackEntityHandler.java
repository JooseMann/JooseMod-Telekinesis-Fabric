package net.joosemann.telekinesis.event;

import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.joosemann.telekinesis.JooseModTelekinesisFabric;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;

import java.util.HashMap;
import java.util.UUID;

public class AttackEntityHandler implements ServerEntityCombatEvents.AfterKilledOtherEntity {

    public static HashMap<UUID, Boolean> playerAttackHashMap = new HashMap<>();
    public static boolean shouldEntityDropItem = false;
    public static boolean enemyKilled = false;
    public static boolean causedByPlayer = false;


    @Override
    public void afterKilledOtherEntity(ServerWorld world, Entity entity, LivingEntity killedEntity) {
        // If the player's weapon has the mastery enchantment, add a few experience orbs
        if (entity instanceof PlayerEntity player) {

            if (!(entity instanceof ServerPlayerEntity)) { return; }

            // Used for AttackEntityPreventLootSpawnMixin, in order to determine if an entity that was just killed was killed by the player.
            causedByPlayer = true;

            // Put the player's UUID in the HashMap, so that it can be used to see whether a specific player killed an entity when an item is dropped.
            // This is so that, if the former statement is true, telekinesis will take effect. See TelekinesisItemDrop for more information
            playerAttackHashMap.put(player.getUuid(), true);

            // This part is to ensure that telekinesis works on a multiplayer server, too
            JooseModTelekinesisFabric.sendAttackTelekinesisPacket((ServerPlayerEntity) entity);

            // Get the level of the enchantment, if there is none, then returns 0
            int enchantmentLevel = EnchantmentHelper.getLevel
                    (Registries.ENCHANTMENT.get(JooseModTelekinesisFabric.masteryIdentifier), player.getMainHandStack());

            if (enchantmentLevel != 0) {
                // Get the extra xp to drop
                int xpToDrop = (int) Math.ceil(killedEntity.getXpToDrop() * (0.25 * enchantmentLevel));

                // Spawn new experience into the world
                Vec3d enemyPos = killedEntity.getPos();
                world.spawnEntity(new ExperienceOrbEntity(world, enemyPos.x, enemyPos.y, enemyPos.z, xpToDrop));
            }
        }

    }
}
