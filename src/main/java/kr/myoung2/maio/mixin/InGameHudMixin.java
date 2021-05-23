package kr.myoung2.maio.mixin;

import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {

    @Shadow @Final private MinecraftClient client;

    @Shadow public abstract TextRenderer getFontRenderer();

    @Shadow private int scaledWidth;

    @Inject(method = "render",at=@At("RETURN"))
    public void render(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        this.client.getProfiler().push("test");

        Text text = new TranslatableText(client.fpsDebugString.split(" ")[0]+" FPS");
        int i = this.getFontRenderer().getWidth((StringVisitable)text);
        this.getFontRenderer().drawWithShadow(matrices, text, 10f, 5.0F, 16777215);
        this.client.getProfiler().pop();
    }
}
