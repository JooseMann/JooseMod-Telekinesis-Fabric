package net.joosemann.telekinesis.mixin;

import net.joosemann.telekinesis.JooseModTelekinesisFabric;
import net.joosemann.telekinesis.util.AttackEntityItemCallback;
import net.joosemann.telekinesis.util.IAttackEntityItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(LivingEntity.class)
public abstract class AttackEntityItemMixin implements IAttackEntityItem {
    
    @Unique
    private List<ItemStack> generatedLoot;
    
    @Unique
    public List<ItemStack> getGeneratedLoot() {
        return generatedLoot;
    }
    
    @Shadow public abstract long getLootTableSeed();

    @Inject(method = "dropLoot", at = @At("TAIL"), locals = LocalCapture.CAPTURE_FAILSOFT)
    private void attackEntityItem(DamageSource damageSource, boolean causedByPlayer, CallbackInfo ci, Identifier identifier, LootTable lootTable, LootContextParameterSet.Builder builder, LootContextParameterSet lootContextParameterSet) {

        // * There seems to be a bug where the item will still drop on the floor, even if the player didn't deal the final blow.
        // ? Maybe caused by a de-sync between the causedByPlayer in this file and AttackEntityHandler.causedByPlayer in AttackEntityPreventLootSpawnMixin?

        // JooseModTelekinesisFabric.LOGGER.info("DropLoot called");

        LivingEntity entity = (LivingEntity) (Object) this;
        
        if (!causedByPlayer || !JooseModTelekinesisFabric.telekinesisData) {
            return;
        }
        
        if (entity.getAttacker() instanceof PlayerEntity player) {
            
            this.generatedLoot = lootTable.generateLoot(lootContextParameterSet, this.getLootTableSeed());
            
            AttackEntityItemCallback.EVENT.invoker().interact(player, entity);
        }
        
        
        // ! NOTE: This is a terrible hack, since it generates its own loot and therefore is probably not compatible with other mods. Should work for now, BUT FIND ANOTHER FIX!!
        /*

        if (!causedByPlayer) {
            return;
        }

        LivingEntity entity = (LivingEntity) (Object) this;
        Identifier identifier = entity.getLootTable();

        if (identifier != null && entity.getAttacker() instanceof PlayerEntity player) {
            LootManager manager = Objects.requireNonNull(entity.getWorld().getServer()).getLootManager();

            LootTable lootTable = manager.getLootTable(identifier);

            if (lootTable != null) {

                Map<LootContextParameter<?>, Object> parameters = new HashMap<>();

                parameters.put(LootContextParameters.THIS_ENTITY, entity);
                parameters.put(LootContextParameters.DAMAGE_SOURCE, damageSource);
                parameters.put(LootContextParameters.KILLER_ENTITY, player);

                List<ItemStack> itemsToDrop = lootTable.generateLoot(new LootContextParameterSet((ServerWorld) entity.getWorld(), parameters, null, player.getLuck()));

                TelekinesisItemDrop.itemsToDrop = itemsToDrop;
                TelekinesisItemDrop.entityPos = entity.getPos();

                AttackEntityItemCallback.EVENT.invoker().interact(player, entity.getWorld(), entity, itemsToDrop);
            }
        }*/

    }
}
