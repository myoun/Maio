package kr.myoung2.maio.mixin;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.realms.gui.screen.RealmsMainScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(RealmsMainScreen.class)
public interface RealmsMainScreenMixin {
    @Accessor
    @Mutable
    static void setCheckedClientCompatability(boolean checked) {
    }

    @Accessor
    @Mutable
    static void setRealmsGenericErrorScreen(Screen screen) {
    }
}