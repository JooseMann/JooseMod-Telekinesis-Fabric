package net.joosemann.telekinesis.mixin;

import net.joosemann.telekinesis.util.IEntityDataSaver;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class ModEntityDataSaverMixin implements IEntityDataSaver {
    private NbtCompound persistentData;

    @Inject(method = "writeNbt", at = @At("HEAD"))
    protected void injectWriteMethod(NbtCompound nbt, CallbackInfoReturnable info) {
        // Write custom nbt data into persistentData
        if (persistentData != null) {
            // nbt.put("telekinesis.joosemann_data", persistentData);
            if (persistentData.getBoolean("telekinesis.joosemann_data")) {
                persistentData.remove("telekinesis.joosemann_data");
            }
            else {
                persistentData.putBoolean("telekinesis.joosemann_data", true);
            }
        }
    }

    @Inject(method = "readNbt", at = @At("HEAD"))
    protected void injectReadMethod(NbtCompound nbt, CallbackInfo info) {
        // Retrieve custom nbt data from persistentData
        if (nbt.contains("telekinesis.joosemann_data", 10)) {
            persistentData = nbt.getCompound("telekinesis.joosemann_data");
        }
    }
}
