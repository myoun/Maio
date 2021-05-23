package kr.myoung2.maio.mixin;

import kr.myoung2.maio.Maio;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    /**
     * @author wincho
     * @reason
     */
    @Overwrite
    private String getWindowTitle() {
        return "MAIO " + Maio.maioVersion;
    }
}
