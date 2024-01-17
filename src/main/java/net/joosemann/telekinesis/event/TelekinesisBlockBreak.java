package net.joosemann.telekinesis.event;

import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.joosemann.telekinesis.util.IEntityDataSaver;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class TelekinesisBlockBreak implements PlayerBlockBreakEvents.Before {

    public static boolean shouldDropItem = false;
    public static boolean blockBroken = false;
    public static List<ItemStack> itemStacks = new ArrayList<ItemStack>();

    @Override
    public boolean beforeBlockBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity) {

        // The context of the block being broken, so that all items will be dropped (not just the possible drops once)
        LootContextParameterSet.Builder context = new LootContextParameterSet.Builder((ServerWorld) world)
                .add(LootContextParameters.ORIGIN, Vec3d.of(pos)).add(LootContextParameters.TOOL, player.getMainHandStack());

        // List of all items that are dropped by the blocks
        itemStacks = state.getDroppedStacks(context);

        // Logic for Telekinesis

        // If we can not get the telekinesis, no items are dropped, and / or telekinesis is not active, then let the block break like normal.
        if (!(player instanceof IEntityDataSaver)) {
            return true;
        }

        if (itemStacks.isEmpty() || !((IEntityDataSaver) player).getTelekinesisData()) {
            return true;
        }

        // Let the computer know that we just broke a block, so that if the item can't be put into the inventory, just drop it.
        blockBroken = true;

        // For each item dropped, give it to the player.
        itemStacks.forEach(itemStack -> {
            int matchingSlot = player.getInventory().getOccupiedSlotWithRoomForStack(itemStack);

            // If there is a matching slot to put the itemStack into, then do so
            if (matchingSlot != -1) {
                // Find the ItemEntity that is dropped from the block. Since the ItemStack is the same, it corresponds to the ItemEntity
                ItemEntity itemEntity = new ItemEntity(world, player.getX(), player.getY(), player.getZ(), itemStack);

                // Delete the item and add it to the player's inventory
                TelekinesisItemDrop.deleteItem(itemEntity);

                player.getInventory().insertStack(matchingSlot, itemStack);
            }
            // Otherwise, if there's an open spot, then just go there
            else if (player.getInventory().getEmptySlot() != -1) {
                // Same as with matchingSlot, finding the ItemEntity that corresponds to the ItemStack and deleting it, before adding it to the player's inventory.
                ItemEntity itemEntity = new ItemEntity(world, player.getX(), player.getY(), player.getZ(), itemStack);

                TelekinesisItemDrop.deleteItem(itemEntity);

                player.getInventory().insertStack(player.getInventory().getEmptySlot(), itemStack);
            }
            // Finally, if the inventory is full then just drop the item like normal
            else {
                shouldDropItem = true;
            }
        });

        return true;
    }
}
