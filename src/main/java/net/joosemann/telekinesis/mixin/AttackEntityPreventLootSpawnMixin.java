package net.joosemann.telekinesis.mixin;

import net.joosemann.telekinesis.JooseModTelekinesisFabric;
import net.joosemann.telekinesis.event.AttackEntityHandler;
import net.joosemann.telekinesis.event.TelekinesisItemDrop;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class AttackEntityPreventLootSpawnMixin {

    @Unique
    private static int counter = 0;


    @Inject(at = @At("HEAD"), method = "dropStack(Lnet/minecraft/item/ItemStack;F)Lnet/minecraft/entity/ItemEntity;", cancellable = true)
    private void preventLootSpawn(ItemStack item, float yOffset, CallbackInfoReturnable<Entity> cir) {
        // This prevents loot from spawning in the event that telekinesis is enabled and an entity is killed by the player.

        // ! This runs once per type of item (ex. if a sheep is killed, both wool and mutton are dropped, so this runs twice. If a zombie is killed, only rotten flesh is dropped so this runs once).
        // So, make a counter and when it equals the number of items in the list, set causedByPlayer to false
        // ? This seems to work most of the time, and the times it doesn't work are when more than one item is dropped, and in that case TelekinesisItemDrop runs for that last item and deletes it. This seems to work fine then, right...?
        
        if (JooseModTelekinesisFabric.telekinesisData && AttackEntityHandler.causedByPlayer) {
            cir.setReturnValue(null);
        }

        if (counter >= TelekinesisItemDrop.itemsToDrop.size()) {
            AttackEntityHandler.causedByPlayer = false;
            counter = 0;
        }

        counter++;
    }

}
