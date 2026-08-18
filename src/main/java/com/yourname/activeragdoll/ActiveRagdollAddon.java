package com.yourname.activeragdoll;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.chat.Component;

import java.lang.reflect.Method;

@Mod(ActiveRagdollAddon.MODID)
public class ActiveRagdollAddon {
    public static final String MODID = "activeragdoll";
    public static boolean isCollapsed = false;
    private static boolean loggedMethods = false;

    public ActiveRagdollAddon(IEventBus modEventBus) {
        NeoForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onPlayerTick(PlayerTickEvent.Post event) {
        if (event.getEntity() instanceof Player player) {
            if (player.level().isClientSide()) {
                if (!loggedMethods && player.tickCount > 60) {
                    logReactionLauncherMethods(player);
                    loggedMethods = true;
                }
            }
        }
    }

    private void logReactionLauncherMethods(Player player) {
        try {
            Class<?> clazz = Class.forName("dev.leo.ragdollreactions.physics.ReactionLauncher");
            player.displayClientMessage(Component.literal("§a--- ReactionLauncher Methods ---"), false);
            for (Method m : clazz.getDeclaredMethods()) {
                StringBuilder sb = new StringBuilder();
                sb.append(m.getName()).append("(");
                Class<?>[] pTypes = m.getParameterTypes();
                for (int i = 0; i < pTypes.length; i++) {
                    sb.append(pTypes[i].getSimpleName());
                    if (i < pTypes.length - 1) sb.append(", ");
                }
                sb.append(")");
                player.displayClientMessage(Component.literal("§e" + sb.toString()), false);
            }
            player.displayClientMessage(Component.literal("§a--------------------------------"), false);
        } catch (Exception e) {
            player.displayClientMessage(Component.literal("§cError inspecting ReactionLauncher: " + e.getMessage()), false);
        }
    }
}