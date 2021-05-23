package kr.myoung2.maio.client;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;


public class MaioClient implements ClientModInitializer {

    private final String discordApplicationId = "839363250734891048";

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

    }

}

