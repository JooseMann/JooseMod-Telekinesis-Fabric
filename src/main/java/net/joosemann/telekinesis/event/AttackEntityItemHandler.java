package net.joosemann.telekinesis.event;

import net.joosemann.telekinesis.util.AttackEntityItemCallback;
import net.joosemann.telekinesis.util.IAttackEntityItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;

import java.util.List;

public class AttackEntityItemHandler implements AttackEntityItemCallback.IAttackEntityItemCallback {
    @Override
    public ActionResult interact(PlayerEntity player, LivingEntity entity) {
        List<ItemStack> itemsToDrop = ((IAttackEntityItem) entity).getGeneratedLoot();

        itemsToDrop.forEach(stack -> {
            int matchingSlot = player.getInventory().getOccupiedSlotWithRoomForStack(stack);

            // If there is a matching slot to put the itemStack into, then do so
            if (matchingSlot != -1) {
                player.getInventory().insertStack(matchingSlot, stack);
            }
            // Otherwise, if there's an open spot, then just go there
            else if (player.getInventory().getEmptySlot() != -1) {
                player.getInventory().insertStack(player.getInventory().getEmptySlot(), stack);
            }
            // Finally, if the inventory is full then just drop the item like normal
            else {
                AttackEntityHandler.shouldEntityDropItem = true;
            }
        });

        return ActionResult.SUCCESS;
    }
}
