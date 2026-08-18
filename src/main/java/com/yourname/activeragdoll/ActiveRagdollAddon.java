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
    private static Object activeRagdollInstance = null;

    public ActiveRagdollAddon(IEventBus modEventBus) {
        NeoForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onPlayerTick(PlayerTickEvent.Post event) {
        if (event.getEntity() instanceof Player player) {
            if (player.level().isClientSide()) {
                updateContinuousActiveRagdoll(player);
            }
        }
    }

    private void updateContinuousActiveRagdoll(Player player) {
        try {
            if (activeRagdollInstance == null) {
                Class<?> managerClass = Class.forName("dev.leo.ragdollreactions.ragdoll.RagdollManager");
                Method getOrCreate = managerClass.getMethod("getOrCreateRagdoll", Player.class);
                activeRagdollInstance = getOrCreate.invoke(null, player);
            }

            if (activeRagdollInstance != null) {
                Method setKinematic = activeRagdollInstance.getClass().getMethod("setKinematic", boolean.class);
                Method setSpringTarget = activeRagdollInstance.getClass().getMethod("setSpringTargetPose", Player.class);
                Method setSpringStrength = activeRagdollInstance.getClass().getMethod("setSpringStrength", float.class);

                if (isCollapsed) {
                    setKinematic.invoke(activeRagdollInstance, false);
                    setSpringStrength.invoke(activeRagdollInstance, 0.0f);
                } else {
                    setKinematic.invoke(activeRagdollInstance, false);
                    setSpringStrength.invoke(activeRagdollInstance, 0.85f);
                    setSpringTarget.invoke(activeRagdollInstance, player);
                }
            }
        } catch (Exception e) {
            fallbackSpringPhysics(player);
        }
    }

    private void fallbackSpringPhysics(Player player) {
        try {
            Class<?> launcherClass = Class.forName("dev.leo.ragdollreactions.physics.ReactionLauncher");
            if (isCollapsed) {
                Method launchMethod = launcherClass.getMethod("launchReaction", Player.class, Vec3.class, float.class);
                launchMethod.invoke(null, player, player.getDeltaMovement(), 1.0f);
            } else {
                Method updateMethod = launcherClass.getMethod("keepAlive", Player.class, float.class);
                updateMethod.invoke(null, player, 0.85f);
            }
        } catch (Exception ignored) {
            applyProceduralRagdollJoints(player);
        }
    }

    private void applyProceduralRagdollJoints(Player player) {
        if (!isCollapsed) {
            Vec3 vel = player.getDeltaMovement();
            double speed = Math.sqrt(vel.x * vel.x + vel.z * vel.z);
            float wobble = (float) Math.sin(player.tickCount * 0.5f) * (float) speed * 0.15f;

            player.yBodyRotO = player.yBodyRot;
            player.yBodyRot = player.getYRot() + wobble * 12.0f;
            player.yHeadRotO = player.yHeadRot;
            player.yHeadRot = player.getYRot();
        }
    }
}