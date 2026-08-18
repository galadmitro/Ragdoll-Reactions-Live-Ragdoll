package com.yourname.activeragdoll;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.minecraft.world.entity.player.Player;

import java.lang.reflect.Method;

@Mod(ActiveRagdollAddon.MODID)
public class ActiveRagdollAddon {
    public static final String MODID = "activeragdoll";
    public static boolean isCollapsed = false;

    public ActiveRagdollAddon(IEventBus modEventBus) {
        NeoForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onPlayerTick(PlayerTickEvent.Post event) {
        if (event.getEntity() instanceof Player player) {
            if (player.level().isClientSide()) {
                updateRagdollState(player);
            }
        }
    }

    private void updateRagdollState(Player player) {
        try {
            Class<?> ragdollClass = Class.forName("dev.leo.ragdollreactions.ragdoll.Ragdoll");
            Method getMethod = ragdollClass.getMethod("get", Player.class);
            Object ragdollInstance = getMethod.invoke(null, player);
            
            if (ragdollInstance != null) {
                Method setPhysics = ragdollInstance.getClass().getMethod("setPhysicsEnabled", boolean.class);
                setPhysics.invoke(ragdollInstance, true);
                
                Method setActive = ragdollInstance.getClass().getMethod("setActiveRagdoll", boolean.class);
                setActive.invoke(ragdollInstance, !isCollapsed);
            }
        } catch (Exception ignored) {
            player.yBodyRotO = player.yBodyRot;
            player.yBodyRot = player.getYRot();
            player.yHeadRotO = player.yHeadRot;
            player.yHeadRot = player.getYRot();
        }
    }
}