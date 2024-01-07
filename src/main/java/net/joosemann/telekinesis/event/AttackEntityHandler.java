package net.joosemann.telekinesis.event;

import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.joosemann.telekinesis.JooseModTelekinesisFabric;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.Registries;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;

public class AttackEntityHandler implements ServerEntityCombatEvents.AfterKilledOtherEntity {

    @Override
    public void afterKilledOtherEntity(ServerWorld world, Entity entity, LivingEntity killedEntity) {
        // If the player's weapon has the mastery enchantment, add a few experience orbs
        if (entity instanceof PlayerEntity player) {

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
