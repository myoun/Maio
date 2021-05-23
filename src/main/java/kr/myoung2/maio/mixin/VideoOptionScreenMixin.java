package kr.myoung2.maio.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.DisconnectedScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.VideoOptionsScreen;
import net.minecraft.client.gui.screen.options.GameOptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.options.GameOptions;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(VideoOptionsScreen.class)
public class VideoOptionScreenMixin extends GameOptionsScreen {

    public VideoOptionScreenMixin(Screen parent, GameOptions gameOptions, Text title) {
        super(parent, gameOptions, title);
    }

    @Inject(at = @At("RETURN"),method = "init")
    private void addButton1(CallbackInfo ci) {
        addButton(new ButtonWidget(this.width /2 - 100 + 205, 10,100,20, new TranslatableText("maio.button.gamma").append(String.format(" : %s", MinecraftClient.getInstance().options.gamma >= 10 ? "true" : "false" )), (buttonWidget) -> {
            if (MinecraftClient.getInstance().options.gamma >= 10) {
                MinecraftClient.getInstance().options.gamma = 0;
            }
            else {
                MinecraftClient.getInstance().options.gamma = 1000.0;
            }
            Text text = new TranslatableText("maio.button.gamma").append(String.format(" : %s",MinecraftClient.getInstance().options.gamma >= 10 ? "true" : "false"));
            buttonWidget.setMessage(text);
        }));

    }

}
