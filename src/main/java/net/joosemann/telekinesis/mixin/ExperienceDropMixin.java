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
        int enchantmentLevel = EnchantmentHelper.getLevel(Registries.ENCHANTMENT.get(JooseModTelekinesisFabric.experienceIdentifier), tool);
        if (enchantmentLevel != 0) {

            int chosenXp = (int) (Math.ceil((experience.get(world.random) - experience.getMin()) * 0.33) + ((enchantmentLevel * 2) - 1)); // OR Just + enchantmentLevel

            JooseModTelekinesisFabric.LOGGER.info("chosenXp: " + chosenXp);

            world.spawnEntity(new ExperienceOrbEntity(world, pos.getX(), pos.getY(), pos.getZ(), chosenXp));
        }
    }
}
