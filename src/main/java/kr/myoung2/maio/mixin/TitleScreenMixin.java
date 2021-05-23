package kr.myoung2.maio.mixin;


import kr.myoung2.maio.screen.LoginScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(TitleScreen.class)
public class TitleScreenMixin extends Screen {

    protected TitleScreenMixin(Text title) {
        super(title);
    }

    @Inject(at = @At("RETURN"), method = "initWidgetsNormal")
    private void addServerButton(int y, int spacingY, CallbackInfo ci) {
        addButton(new ButtonWidget(this.width / 2 - 100 + 205, y, 100, 20, new TranslatableText("maio.button.reloadsession"), (buttonWidget) -> MinecraftClient.getInstance().openScreen(new LoginScreen(this))));
    }
}
