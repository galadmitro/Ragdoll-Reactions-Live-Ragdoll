package com.yourname.activeragdoll;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;

@EventBusSubscriber(modid = ActiveRagdollAddon.MODID, value = Dist.CLIENT)
public class KeyInputHandler {
    
    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        while (KeybindManager.FALL_KEY.consumeClick()) {
            ActiveRagdollAddon.isCollapsed = !ActiveRagdollAddon.isCollapsed;
        }
    }
}