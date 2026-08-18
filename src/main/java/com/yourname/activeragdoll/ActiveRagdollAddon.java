package com.yourname.activeragdoll;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.minecraft.world.entity.player.Player;

import java.lang.reflect.Method;

@Mod(ActiveRagdollAddon.MODID)
public class ActiveRagdollAddon {
    public static final String MODID = "activeragdoll";
    public static boolean isCollapsed = false;

    public ActiveRagdollAddon(IEventBus modEventBus) {
        modEventBus.addListener(this::onClientSetup);
        modEventBus.addListener(KeyInputHandler::register);
        NeoForge.EVENT_BUS.register(this);
        NeoForge.EVENT_BUS.addListener(KeyInputHandler::handleClientTicks);
    }

    private void onClientSetup(FMLClientSetupEvent event) {
    }

    @SubscribeEvent
    public void onPlayerTick(PlayerTickEvent.Post event) {
        if (event.getEntity() instanceof Player player) {
            if (player.level().isClientSide()) {
                try {
                    Class<?> clazz = Class.forName("dev.leo.ragdollreactions.physics.ReactionLauncher");
                    Method resetState = clazz.getMethod("resetState");
                    if (isCollapsed) {
                        resetState.invoke(null);
                    }
                } catch (Exception ignored) {
                }
            }
        }
    }
}