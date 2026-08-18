package com.yourname.activeragdoll;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import org.lwjgl.glfw.GLFW;

public class KeyInputHandler {
    public static final KeyMapping RAGDOLL_KEY = new KeyMapping(
            "key.activeragdoll.toggle",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_R,
            "category.activeragdoll"
    );

    public static void register(RegisterKeyMappingsEvent event) {
        event.register(RAGDOLL_KEY);
    }

    public static void handleClientTicks(ClientTickEvent.Post event) {
        while (RAGDOLL_KEY.consumeClick()) {
            ActiveRagdollAddon.isCollapsed = !ActiveRagdollAddon.isCollapsed;
        }
    }
}