package com.yourname.activeragdoll;

import net.minecraft.client.KeyMapping;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import com.mojang.blaze3d.platform.InputConstants;
import org.lwjgl.glfw.GLFW;

@EventBusSubscriber(modid = ActiveRagdollAddon.MODID, value = Dist.CLIENT)
public class KeybindManager {
    
    public static final KeyMapping FALL_KEY = new KeyMapping(
        "key.activeragdoll.fall", 
        KeyConflictContext.IN_GAME, 
        InputConstants.Type.KEYSYM, 
        GLFW.GLFW_KEY_Z, 
        "key.categories.activeragdoll"
    );

    @SubscribeEvent
    public static void registerBindings(RegisterKeyMappingsEvent event) {
        event.register(FALL_KEY);
    }
}