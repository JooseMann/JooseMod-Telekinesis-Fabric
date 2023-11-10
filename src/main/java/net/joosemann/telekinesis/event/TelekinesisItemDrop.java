package net.joosemann.telekinesis.event;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerEntityManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;

public class TelekinesisItemDrop implements ServerEntityEvents.Load {
    @Override
    public void onLoad(Entity entity, ServerWorld world) {

        if (entity instanceof ItemEntity itemEntity && TelekinesisBlockBreak.blockBroken) {

            ItemStack item = itemEntity.getStack();

            if (item != null && !TelekinesisBlockBreak.shouldDropItem) {
                itemEntity.remove(Entity.RemovalReason.KILLED);
            }
        }

        TelekinesisBlockBreak.blockBroken = false;
        TelekinesisBlockBreak.shouldDropItem = false;
    }
}
