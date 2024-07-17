package net.joosemann.telekinesis.util;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;

public class AttackEntityItemCallback {
    public static final Event<IAttackEntityItemCallback> EVENT = EventFactory.createArrayBacked(IAttackEntityItemCallback.class,
            (listeners) -> (player, entity) -> {
                for (IAttackEntityItemCallback event : listeners) {
                    ActionResult result = event.interact(player, entity);

                    if (result != ActionResult.PASS) {
                        return result;
                    }
                }

                return ActionResult.PASS;
            }
    );

    public interface IAttackEntityItemCallback {
        ActionResult interact(PlayerEntity player, LivingEntity entity);
    }
}