package kr.myoung2.maio.util;

import com.mojang.authlib.Agent;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilMinecraftSessionService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import com.mojang.util.UUIDTypeAdapter;
import com.sun.org.apache.xerces.internal.util.Status;

import kr.myoung2.maio.mixin.MinecraftClientAccessorMixin;
import kr.myoung2.maio.mixin.RealmsMainScreenMixin;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Session;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class AuthUtils {
    private static final YggdrasilAuthenticationService yas = new YggdrasilAuthenticationService(MinecraftClient.getInstance().getNetworkProxy(),
            UUID.randomUUID().toString());
    private static final YggdrasilUserAuthentication yua = (YggdrasilUserAuthentication) yas.createUserAuthentication(Agent.MINECRAFT);
    private static final YggdrasilMinecraftSessionService ymss = (YggdrasilMinecraftSessionService) yas.createMinecraftSessionService();
    private static Status lastStatus = Status.UNKNOWN;
    private static long lastStatusCheck;

    public static void login(String email, String password) throws AuthenticationException {
        yua.setUsername(email);
        yua.setPassword(password);
        yua.logIn();

        final String name = yua.getSelectedProfile().getName();
        final String uuid = UUIDTypeAdapter.fromUUID(yua.getSelectedProfile().getId());
        final String token = yua.getAuthenticatedToken();
        final String type = yua.getUserType().getName();

        yua.logOut();

        final Session session = new Session(name, uuid, token, type);
        setSession(session);
    }

    public static void offlineLogin(String username) {
        Session offlineSession = new Session(username,UUID.nameUUIDFromBytes(("Offline Player : "+username).getBytes(StandardCharsets.UTF_8)).toString(),"invalid","legacy");
        setSession(offlineSession);
    }

    private static void setSession(Session session) {
        ((MinecraftClientAccessorMixin) MinecraftClient.getInstance()).setSession(session);

        RealmsMainScreenMixin.setCheckedClientCompatability(false);
        RealmsMainScreenMixin.setRealmsGenericErrorScreen(null);
    }
}