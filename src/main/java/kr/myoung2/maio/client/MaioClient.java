package kr.myoung2.maio.client;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;


public class MaioClient implements ClientModInitializer {

    private final String discordApplicationId = "839363250734891048";
    private static Boolean currentlyZoomed;
    private static KeyBinding keyBinding;
    private static Boolean originalSmoothCameraEnabled;
    private static final MinecraftClient mc = MinecraftClient.getInstance();

    public static final double zoomLevel = 19.0;

    @Override
    public void onInitializeClient() {

        DiscordRPC lib = DiscordRPC.INSTANCE;
        DiscordEventHandlers handlers = new DiscordEventHandlers();
        handlers.ready = (user -> System.out.println("Ready for Discord RPC"));
        lib.Discord_Initialize(discordApplicationId,handlers,true,"");
        DiscordRichPresence presence = new DiscordRichPresence();
        presence.startTimestamp = System.currentTimeMillis() / 1000;
        presence.details = "Minecraft "+MinecraftClient.getInstance().getGame().getVersion().getName();
        new Thread(() -> {
            System.out.println("thread started");
           while (!Thread.currentThread().isInterrupted()) {
               lib.Discord_RunCallbacks();
               presence.state = "Account : "+ (MinecraftClient.getInstance().getSession().getUsername());
               lib.Discord_UpdatePresence(presence);
               }
               try {
                   Thread.sleep(2000);
               } catch (InterruptedException ignored) {}
        },"Discord RPC").start();


        keyBinding = new KeyBinding("maio.key.zoom", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_C, "category.maio");

        currentlyZoomed = false;
        originalSmoothCameraEnabled = false;

        KeyBindingHelper.registerKeyBinding(keyBinding);
    }

    public static Boolean isZooming() {
        return keyBinding.isPressed();
    }

    public static void manageSmoothCamera() {
        if (zoomStarting()) {
            zoomStarted();
            enableSmoothCamera();
        }

        if (zoomStopping()) {
            zoomStopped();
            resetSmoothCamera();
        }
    }
    private static Boolean isSmoothCamera() {
        return mc.options.smoothCameraEnabled;
    }

    private static void enableSmoothCamera() {
        mc.options.smoothCameraEnabled = true;
    }

    private static void disableSmoothCamera() {
        mc.options.smoothCameraEnabled = false;
    }

    private static boolean zoomStarting() {
        return isZooming() && !currentlyZoomed;
    }

    private static boolean zoomStopping() {
        return !isZooming() && currentlyZoomed;
    }

    private static void zoomStarted() {
        originalSmoothCameraEnabled = isSmoothCamera();
        currentlyZoomed = true;
    }

    private static void zoomStopped() {
        currentlyZoomed = false;
    }

    private static void resetSmoothCamera() {
        if (originalSmoothCameraEnabled) {
            enableSmoothCamera();
        } else {
            disableSmoothCamera();
        }
    }

}

