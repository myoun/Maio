package kr.myoung2.maio.mixin;


import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameMenuScreen.class)
public class GameMenuScreenMixin extends Screen {

    protected GameMenuScreenMixin(Text title) {
        super(title);
    }
    @Inject(at=@At("RETURN"),method = "init")
    private void addButton(CallbackInfo ci) {
        addButton(new ButtonWidget(this.width/2-100+205,90,100,20, new TranslatableText("maio.button.killgame"),(button -> {
            MinecraftClient.getInstance().stop();
        }
        )));
    }
}
