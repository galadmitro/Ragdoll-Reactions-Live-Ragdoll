package com.yourname.activeragdoll;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.lang.reflect.Method;

@Mod(ActiveRagdollAddon.MODID)
public class ActiveRagdollAddon {
    public static final String MODID = "activeragdoll";
    public static boolean isCollapsed = false;
    private static boolean physicsActive = false;

    public ActiveRagdollAddon(IEventBus modEventBus) {
        NeoForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onPlayerTick(PlayerTickEvent.Post event) {
        if (event.getEntity() instanceof Player player) {
            if (player.level().isClientSide()) {
                if (isCollapsed) {
                    if (!physicsActive) {
                        triggerRagdoll(player);
                        physicsActive = true;
                    }
                } else {
                    physicsActive = false;
                    enforceStandingRagdoll(player);
                }
            }
        }
    }

    private void triggerRagdoll(Player player) {
        try {
            Class<?> launcherClass = Class.forName("dev.leo.ragdollreactions.physics.ReactionLauncher");
            Method launchMethod = launcherClass.getMethod("launchReaction", Player.class, Vec3.class, float.class);
            launchMethod.invoke(null, player, player.getDeltaMovement(), 1.0f);
        } catch (Exception ignored) {
        }
    }

    private void enforceStandingRagdoll(Player player) {
        Vec3 velocity = player.getDeltaMovement();
        player.setDeltaMovement(velocity.x, Math.max(velocity.y, -0.05), velocity.z);
        player.setOnGround(true);
        
        player.yBodyRotO = player.yBodyRot;
        player.yBodyRot = player.getYRot();
        player.yHeadRotO = player.yHeadRot;
        player.yHeadRot = player.getYRot();
    }
}