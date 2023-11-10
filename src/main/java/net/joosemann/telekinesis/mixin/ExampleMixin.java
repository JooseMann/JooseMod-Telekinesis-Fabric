package net.joosemann.telekinesis.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public class ExampleMixin {
	/*@Inject(at = @At("HEAD"), method = "loadWorld")
	private void init(CallbackInfo info) {
		// This code is injected into the start of MinecraftServer.loadWorld()V
	}*/

	/*@Inject(at = @At("HEAD"), method = "onBreak", cancellable = true)
	private void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player, CallbackInfo info) {
		player.sendMessage(Text.literal("Block Broken!"));
	}*/
}