package net.joosemann.telekinesis.event;

import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.joosemann.telekinesis.networking.packet.TelekinesisC2SPacket;
import net.joosemann.telekinesis.util.KeyInputHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TelekinesisBlockBreak implements PlayerBlockBreakEvents.Before {
    /*Event<Telekinesis> EVENT = EventFactory.createArrayBacked(Telekinesis.class,
            (listeners) -> (player) -> {
                for (Telekinesis listener : listeners) {
                    ActionResult result = listener.interact(player)
                }
            });*/
    /*@Override
    public void afterBlockBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity) {


        player.sendMessage(Text.literal("Block Broken!"));



        if (blockEntity != null) {
            BlockPos blockPos = blockEntity.getPos();
            blockPos = player.getBlockPos();
        }

        // pos = player.getBlockPos();
    }*/

    public static boolean shouldDropItem = false;
    public static boolean blockBroken = false;

    @Override
    public boolean beforeBlockBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity) {

        // List of all items that are dropped by the blocks
        List<ItemStack> itemStacks = Block.getDroppedStacks(state, (ServerWorld) world, pos, blockEntity);

        // Logic for Telekinesis

        if (itemStacks.isEmpty() || !TelekinesisC2SPacket.playerNbt.contains("telekinesis-enabled")) { return true; }

        blockBroken = true;

        // TODO: This assumes that all items are the same, so we only need to pick the first item... do I need a workaround??
        int matchingSlot = player.getInventory().getOccupiedSlotWithRoomForStack(itemStacks.get(0));

        // If there is a matching slot to put the itemStack into, then do so
        if (KeyInputHandler.telekinesisIsActive && matchingSlot != -1) {
            itemStacks.forEach(itemStack -> player.getInventory().insertStack(matchingSlot, itemStack));
        }
        // Otherwise, if there's an open spot, then just go there
        else if (KeyInputHandler.telekinesisIsActive && player.getInventory().getEmptySlot() != -1) {
            itemStacks.forEach(itemStack -> player.getInventory().insertStack(itemStack));
        }
        // Finally, if the inventory is full then just drop the item like normal
        else {
            shouldDropItem = true;
        }

        return true;
    }
}
