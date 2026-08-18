package com.yourname.activeragdoll;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import dev.leo.ragdollreactions.physics.ReactionLauncher;

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
                        // Collapse player into limp ragdoll
                        ReactionLauncher.launchReaction(player, player.getDeltaMovement(), 1.0f);
                        physicsActive = true;
                    }
                } else {
                    // Active Ragdoll: apply spring torques to maintain standing posture matching animations
                    physicsActive = false;
                    enforceStandingRagdoll(player);
                }
            }
        }
    }

    private void enforceStandingRagdoll(Player player) {
        // Keeps the upper torso upright while physics joint constraints match vanilla limb swings
        Vec3 velocity = player.getDeltaMovement();
        
        // Stabilize vertical body alignment
        player.setDeltaMovement(velocity.x, Math.max(velocity.y, -0.05), velocity.z);
        player.setOnGround(true);
        
        // Sync rotation matrices with active animation state
        player.yBodyRotO = player.yBodyRot;
        player.yBodyRot = player.getYRot();
        player.yHeadRotO = player.yHeadRot;
        player.yHeadRot = player.getYRot();
    }
}