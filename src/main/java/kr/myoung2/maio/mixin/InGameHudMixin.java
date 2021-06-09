package kr.myoung2.maio.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Text;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
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

        Text fps = new LiteralText(client.fpsDebugString.split(" ")[0] + " FPS");
        LiteralText block = new LiteralText("");

        HitResult hitResult = MinecraftClient.getInstance().player.raycast(5.0, 0.0F, false);
        HitResult.Type type = hitResult.getType();
        if (type.equals(HitResult.Type.BLOCK)) {
            BlockPos blockPos = ((BlockHitResult) hitResult).getBlockPos();
            BlockState blockState = MinecraftClient.getInstance().world.getBlockState(blockPos);

            block = new LiteralText(block.getString() + "Block: " + Registry.BLOCK.getId(blockState.getBlock()) + ", " + blockPos.getX() + ", " + blockPos.getY() + ", " + blockPos.getZ() + "\n");

            int i = this.getFontRenderer().getWidth((StringVisitable) block);

            this.getFontRenderer().drawWithShadow(matrices, block, (float) (this.scaledWidth - i - 10), 5.0f, 16777215);

        }
        this.getFontRenderer().drawWithShadow(matrices, fps, 10f, 5.0F, 16777215);
        this.client.getProfiler().pop();

    }
}
