package com.yourname.activeragdoll;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.chat.Component;

import java.io.File;
import java.util.jar.JarFile;

@Mod(ActiveRagdollAddon.MODID)
public class ActiveRagdollAddon {
    public static final String MODID = "activeragdoll";
    public static boolean isCollapsed = false;
    private static boolean scanned = false;

    public ActiveRagdollAddon(IEventBus modEventBus) {
        NeoForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onPlayerTick(PlayerTickEvent.Post event) {
        if (event.getEntity() instanceof Player player) {
            if (player.level().isClientSide()) {
                if (!scanned && player.tickCount > 60) {
                    scanRagdollModToChat(player);
                    scanned = true;
                }
            }
        }
    }

    private void scanRagdollModToChat(Player player) {
        File modsDir = new File("mods");
        boolean foundClass = false;
        File[] files = null;
        
        if (modsDir.exists() && modsDir.isDirectory()) {
            files = modsDir.listFiles((dir, name) -> name.endsWith(".jar") && name.toLowerCase().contains("ragdoll_reactions"));
            
            if (files != null && files.length > 0) {
                try (JarFile jar = new JarFile(files[0])) {
                    player.displayClientMessage(Component.literal("§a--- RAGDOLL CLASSES FOUND ---"), false);
                    for (var entry : jar.stream().toList()) {
                        String name = entry.getName();
                        // Filter out Mixins and common bloat to fit in chat
                        if (name.endsWith(".class") && name.contains("dev/leo/ragdollreactions") && !name.contains("mixin")) {
                            String cleanName = name.replace("/", ".").replace(".class", "");
                            player.displayClientMessage(Component.literal("§e" + cleanName), false);
                            foundClass = true;
                        }
                    }
                    player.displayClientMessage(Component.literal("§a-----------------------------"), false);
                } catch (Exception e) {
                    player.displayClientMessage(Component.literal("§cError reading JAR: " + e.getMessage()), false);
                }
            } else {
                player.displayClientMessage(Component.literal("§cCould not find ragdoll_reactions JAR in the mods folder!"), false);
            }
        }
        
        if (!foundClass && files != null && files.length > 0) {
            player.displayClientMessage(Component.literal("§cFound the mod file, but failed to find 'dev.leo' classes inside it."), false);
        }
    }
}