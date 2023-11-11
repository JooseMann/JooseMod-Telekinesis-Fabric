package net.joosemann.telekinesis.mixin;

import net.joosemann.telekinesis.JooseModTelekinesisFabric;
import net.minecraft.block.Block;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.intprovider.IntProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public abstract class ExperienceDropMixin {
    @Inject(method = "dropExperienceWhenMined", at = @At("HEAD"))
    public void experienceEnchantmentDrops(ServerWorld world, BlockPos pos, ItemStack tool, IntProvider experience, CallbackInfo info) {
        // If the player's tool has the experience enchantment, add extra experience to the world
        // See AttackEntityHandler for details
        int enchantmentLevel = EnchantmentHelper.getLevel(Registries.ENCHANTMENT.get(JooseModTelekinesisFabric.experienceIdentifier), tool);
        if (enchantmentLevel != 0) {

            // This is a slightly different calculation than in AttackEntityHandler
            // experience.get(world.random) does the same thing as getXpToDrop() (in the other class)
            int chosenXp = (int) (Math.ceil(experience.get(world.random) * 0.25 * enchantmentLevel));

            world.spawnEntity(new ExperienceOrbEntity(world, pos.getX(), pos.getY(), pos.getZ(), chosenXp));
        }
    }
}
