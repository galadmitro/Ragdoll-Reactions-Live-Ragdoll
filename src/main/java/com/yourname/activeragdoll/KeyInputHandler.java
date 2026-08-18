package com.yourname.activeragdoll;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;

@EventBusSubscriber(modid = ActiveRagdollAddon.MOD_ID, value = Dist.CLIENT)
public class KeyInputHandler {

    private static boolean active = true;

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        while (KeybindManager.TOGGLE_RAGDOLL_KEY.consumeClick()) {
            active = !active;
            ActiveRagdollHandler.setEnabled(active);
            
            mc.player.displayClientMessage(
                Component.literal("Active Ragdoll: " + (active ? "§aENABLED" : "§cDISABLED")), 
                true
            );
        }
    }

    public static boolean isActive() {
        return active;
    }
}