package net.joosemann.telekinesis.event;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;

//@Environment(EnvType.SERVER)
public class TelekinesisItemDrop implements ServerEntityEvents.Load {

    @Override
    public void onLoad(Entity entity, ServerWorld world) {
        /*if (entity instanceof ItemEntity itemEntity && TelekinesisBlockBreak.blockBroken) {
            List<ItemStack> itemStacks = TelekinesisBlockBreak.itemStackList;

            System.out.println(itemStacks.size());

            for (int i = 0; i <= itemStacks.size(); i++) {
                ItemStack item = itemStacks.get(i);

                System.out.println(item);

                if (item != null && TelekinesisBlockBreak.shouldDropItem) {
                    itemEntity.remove(Entity.RemovalReason.KILLED);
                }
            }
        }

        TelekinesisBlockBreak.blockBroken = false;
        TelekinesisBlockBreak.shouldDropItem = false;*/

        // If a block was just broken, then see if the item should be dropped or not
        if (entity instanceof ItemEntity itemEntity && TelekinesisBlockBreak.blockBroken) {

            ItemStack item = itemEntity.getStack();

            // If the item should not be dropped, then just kill it
            // See TelekinesisBlockBreak for details on how shouldDropItem is set.
            if (item != null && !TelekinesisBlockBreak.shouldDropItem) {
                itemEntity.remove(Entity.RemovalReason.KILLED);
            }
        }

        // Reset the variables to false so that it works again next time
        TelekinesisBlockBreak.blockBroken = false;
        TelekinesisBlockBreak.shouldDropItem = false;
    }
}
