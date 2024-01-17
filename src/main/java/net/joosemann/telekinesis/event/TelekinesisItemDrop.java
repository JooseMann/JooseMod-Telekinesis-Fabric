package net.joosemann.telekinesis.event;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.joosemann.telekinesis.JooseModTelekinesisFabric;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.server.world.ServerWorld;

import java.util.Objects;

public class TelekinesisItemDrop implements ServerEntityEvents.Load {

    // The item name of the item that was dropped before the current event. Used to tell if a block is being dropped more than once (or if there are multiple items being dropped at once).
    public static String oldName = "";

    // The tick count of the server. Used to test if an item is being dropped at the same tick as another (in which case, something like Vein Miner is active).
    public static int serverTickCount = 0;

    @Override
    public void onLoad(Entity entity, ServerWorld world) {

        if (entity instanceof ItemEntity itemEntity) {
            // Get the current tick count and name of the item
            int currentTickCount = world.getServer().getTicks();

            String currentName = itemEntity.getName().toString();

            // Return if telekinesis is disabled
            if (!JooseModTelekinesisFabric.telekinesisData) { return; }

            /* Assuming that Telekinesis is enabled, remove the item if:
            * A block was just broken (one at a time), and the inventory of the player is not full, OR
            * Multiple blocks were broken on the same tick and have the same name.
            * */
            if ((TelekinesisBlockBreak.blockBroken && !TelekinesisBlockBreak.shouldDropItem) || (serverTickCount == currentTickCount && Objects.equals(currentName, oldName))) {
                deleteItem(itemEntity);
            }

            // Update the "old" counters for the next use
            serverTickCount = world.getServer().getTicks();
            oldName = itemEntity.getName().toString();
        }

        // Reset the variables that allow for one block to be broken successfully for the next use
        TelekinesisBlockBreak.blockBroken = false;
        TelekinesisBlockBreak.shouldDropItem = false;
    }

    // Delete the itemEntity from the world.
    public static void deleteItem(ItemEntity itemEntity) {
        itemEntity.remove(Entity.RemovalReason.KILLED);
    }
}
