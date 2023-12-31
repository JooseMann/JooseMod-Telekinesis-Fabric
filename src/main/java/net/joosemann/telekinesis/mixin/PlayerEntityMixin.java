package net.joosemann.telekinesis.mixin;

import net.joosemann.telekinesis.util.IEntityDataSaver;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin implements IEntityDataSaver {
    // The telekinesis data. This is not accessible all the time, so see JooseModTelekinesisFabric.telekinesisData for the global version.
    public boolean telekinesisData = false;

    // Get the telekinesis data
    public boolean getTelekinesisData() {
        return telekinesisData;
    }

    // Write the nbt data
    @Inject(method = "writeCustomDataToNbt", at = @At("RETURN"))
    public void onWriteCustomDataToNbt(NbtCompound nbt, CallbackInfo ci) {
        nbt.putBoolean("telekinesis.jooseman_data", telekinesisData);
    }

    // Read the nbt data
    @Inject(method = "readCustomDataFromNbt", at = @At("RETURN"))
    public void onReadCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci) {
        telekinesisData = nbt.getBoolean("telekinesis.jooseman_data");
    }

    // Flips telekinesisData
    public void flipTelekinesisData() {
        telekinesisData = !telekinesisData;
    }
}
