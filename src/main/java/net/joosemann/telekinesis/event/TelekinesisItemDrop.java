package net.joosemann.telekinesis.event;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;

import java.util.Objects;

public class TelekinesisItemDrop implements ServerEntityEvents.Load {

    // The item name of the item that was dropped before the current event. Used to tell if a block is being dropped more than once (or if there are multiple items being dropped at once).
    public static String oldItemName;

    @Override
    public void onLoad(Entity entity, ServerWorld world) {

        // If the entity is an ItemEntity, then we need to see if a block was broken or if it just spawned in naturally
        if (entity instanceof ItemEntity itemEntity) {

            // If a block was just broken, then see if the item should be dropped or not
            if (!TelekinesisBlockBreak.blockBroken) { return; }

            // The name of the item, stored so that we can see if multiple items are being dropped at once
            oldItemName = itemEntity.getName().getString();

            ItemStack item = itemEntity.getStack();

            // If the item should not be dropped, then just kill it
            // See TelekinesisBlockBreak for details on how shouldDropItem is set.
            if (item != null && !TelekinesisBlockBreak.shouldDropItem) {
                itemEntity.remove(Entity.RemovalReason.KILLED);
            }

            // If the item was the same as the last one, then we run the code again until it is not.
            TelekinesisBlockBreak.blockBroken = Objects.equals(oldItemName, itemEntity.getName().getString());
        }
        else {
            // Otherwise, just set it false to reset the cycle.
            TelekinesisBlockBreak.blockBroken = false;
        }

        // Reset the variable to false so that it works again next time
        TelekinesisBlockBreak.shouldDropItem = false;
    }
}
