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

        List<ItemStack> itemStacks = Block.getDroppedStacks(state, (ServerWorld) world, pos, blockEntity);

        if (itemStacks.isEmpty() || !TelekinesisC2SPacket.playerNbt.contains("telekinesis-enabled")) { return true; }

        blockBroken = true;

        // player.sendMessage(Text.literal(String.valueOf(player.getInventory().getEmptySlot())));
        // player.sendMessage(Text.literal(String.valueOf(player.getInventory().getOccupiedSlotWithRoomForStack(itemStacks.get(0)))));

        // TODO: This assumes that all items are the same, so we only need to pick the first item... do I need a workaround??
        int matchingSlot = player.getInventory().getOccupiedSlotWithRoomForStack(itemStacks.get(0));

        if (KeyInputHandler.telekinesisIsActive && matchingSlot != -1) {
            itemStacks.forEach(itemStack -> player.getInventory().insertStack(matchingSlot, itemStack));
        }
        else if (KeyInputHandler.telekinesisIsActive && player.getInventory().getEmptySlot() != -1) {
            itemStacks.forEach(itemStack -> player.getInventory().insertStack(itemStack));
        }
        else {
            shouldDropItem = true;

            /*List<ItemEntity> itemEntities = new ArrayList<>();
            itemStacks.forEach(itemStack -> {
                ItemEntity newItem = new ItemEntity(world, player.getPos().x, player.getPos().y, player.getPos().z,
                itemStack, 0.0, 0.0, 0.0);
                world.spawnEntity(newItem);

                itemEntities.add(newItem);
            });

            itemEntities.forEach(itemEntity -> {
                world.spawnEntity(itemEntity);
                player.sendMessage(Text.literal(itemEntity.toString()));
            });*/
        }

        return true;
    }
}
