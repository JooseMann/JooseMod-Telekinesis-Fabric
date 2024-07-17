package net.joosemann.telekinesis.mixin;

import net.joosemann.telekinesis.util.IItemData;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class ModEntityDataSaverMixin implements IItemData {

    @Unique
    private NbtCompound nbtData;

    @Override
    public NbtCompound getNbtData() {
        return nbtData;
    }

    @Inject(method = "getNbt", at = @At("RETURN"))
    public void injectGetNbt(CallbackInfoReturnable<NbtCompound> cir) {
        nbtData = cir.getReturnValue();
    }

}
