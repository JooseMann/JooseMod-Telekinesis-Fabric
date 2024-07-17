package net.joosemann.telekinesis.event;

import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.joosemann.telekinesis.util.IEntityDataSaver;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class TelekinesisBlockBreak implements PlayerBlockBreakEvents.Before {

    public static boolean shouldDropItem = false;
    public static boolean blockBroken = false;
    public static List<ItemStack> itemStacks = new ArrayList<>();

    @Override
    public boolean beforeBlockBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, BlockEntity blockEntity) {

        if (player.isCreative()) {
            return true;
        }

        LootContextParameterSet.Builder context;

        // The context of the block being broken, so that all items will be dropped (not just the possible drops once)
        // If blockEntity is not null, add it to the context (in order to preserve the nbt data of the block that was broken)
        if (blockEntity == null) {
            context = new LootContextParameterSet.Builder((ServerWorld) world)
                    .add(LootContextParameters.ORIGIN, Vec3d.of(pos)).add(LootContextParameters.TOOL, player.getMainHandStack()).add(LootContextParameters.BLOCK_STATE, state);
        }
        else {
            context = new LootContextParameterSet.Builder((ServerWorld) world)
                    .add(LootContextParameters.ORIGIN, Vec3d.of(pos)).add(LootContextParameters.TOOL, player.getMainHandStack())
                    .add(LootContextParameters.BLOCK_STATE, state).add(LootContextParameters.BLOCK_ENTITY, blockEntity);
        }

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

        // * For now, chests and trapped chests will just drop normally.
        if (state.getBlock() == Blocks.CHEST || state.getBlock() == Blocks.TRAPPED_CHEST) {
            return true;
        }

        // Special case for sugar cane, since it should be able to break blocks above it and still get the item
        if (state.getBlock() == Blocks.SUGAR_CANE) {
            boolean continueChecking = true;

            BlockPos nextPos = new BlockPos(pos);

            // Continue checking the block above the last one, to see if there is also sugar cane there.
            while (continueChecking) {
                nextPos = new BlockPos(nextPos.getX(), nextPos.getY() + 1, nextPos.getZ());

                continueChecking = checkForNearbyBlock(world, nextPos, Blocks.SUGAR_CANE);

                if (continueChecking) {
                    ItemStack itemToAdd = new ItemStack(Items.SUGAR_CANE);

                    itemToAdd.setCount(1);

                    itemStacks.add(itemToAdd);
                }
            }
        }

        // Let the computer know that we just broke a block, so that if the item can't be put into the inventory, just drop it.
        blockBroken = true;

        // For each item dropped, give it to the player.
        itemStacks.forEach(itemStack -> {


            int matchingSlot = player.getInventory().getOccupiedSlotWithRoomForStack(itemStack);

            // If there is a matching slot to put the itemStack into, then do so
            if (matchingSlot != -1) {
                player.getInventory().insertStack(matchingSlot, itemStack);
            }
            // Otherwise, if there's an open spot, then just go there
            else if (player.getInventory().getEmptySlot() != -1) {
                player.getInventory().insertStack(player.getInventory().getEmptySlot(), itemStack);
            }
            // Finally, if the inventory is full then just drop the item like normal
            else {
                shouldDropItem = true;
            }
        });

        return true;
    }

    // Used for sugar cane special case, see above
    public boolean checkForNearbyBlock(World world, BlockPos pos, Block blockToCheck) {
        if (blockToCheck == Blocks.AIR || blockToCheck == Blocks.CHEST || blockToCheck == Blocks.TRAPPED_CHEST) {
            return false;
        }
        else return world.getBlockState(pos).getBlock() == blockToCheck;
    }
}
