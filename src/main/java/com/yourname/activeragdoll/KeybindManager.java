package com.yourname.activeragdoll;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public class KeybindManager {
    public static final KeyMapping TOGGLE_RAGDOLL_KEY = new KeyMapping(
        "key.activeragdoll.toggle",
        KeyConflictContext.IN_GAME,
        InputConstants.Type.KEYSYM,
        GLFW.GLFW_KEY_G,
        "key.categories.activeragdoll"
    );

    public static void register(IEventBus modEventBus) {
        modEventBus.addListener((RegisterKeyMappingsEvent event) -> {
            event.register(TOGGLE_RAGDOLL_KEY);
        });
    }
}