package com.yourname.activeragdoll;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderFrameEvent;
import net.neoforged.neoforge.client.event.RenderPlayerEvent;

@EventBusSubscriber(modid = ActiveRagdollAddon.MOD_ID, value = Dist.CLIENT)
public class ActiveRagdollHandler {

    private static boolean enabled = true;

    public static void setEnabled(boolean state) {
        enabled = state;
    }

    public static boolean isEnabled() {
        return enabled;
    }

    // 1. THIS HIDES YOUR VANILLA PLAYER MODEL WHEN ENABLED
    @SubscribeEvent
    public static void onPlayerRender(RenderPlayerEvent.Pre event) {
        if (enabled && event.getEntity() == Minecraft.getInstance().player) {
            // Cancels the vanilla rendering so you only see the physics ragdoll
            event.setCanceled(true); 
        }
    }

    @SubscribeEvent
    public static void onRenderFrame(RenderFrameEvent.Pre event) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;

        if (player == null || !enabled || mc.isPaused()) {
            return;
        }

        if (!(mc.getEntityRenderDispatcher().getRenderer(player) instanceof PlayerRenderer renderer)) {
            return;
        }

        PlayerModel<AbstractClientPlayer> model = renderer.getModel();

        // Extract active joint rotations from vanilla player model frame
        float headX = model.head.xRot;
        float headY = model.head.yRot;

        float rArmX = model.rightArm.xRot;
        float rArmY = model.rightArm.yRot;
        float rArmZ = model.rightArm.zRot;

        float lArmX = model.leftArm.xRot;
        float lArmY = model.leftArm.yRot;
        float lArmZ = model.leftArm.zRot;

        float rLegX = model.rightLeg.xRot;
        float lLegX = model.leftLeg.xRot;

        syncPhysicsJoints(
            player, 
            headX, headY, 
            rArmX, rArmY, rArmZ, 
            lArmX, lArmY, lArmZ, 
            rLegX, lLegX
        );
    }

    private static void syncPhysicsJoints(
        LocalPlayer player,
        float hX, float hY,
        float raX, float raY, float raZ,
        float laX, float laY, float laZ,
        float rlX, float llX
    ) {
        double stiffness = 180.0;
        double damping = 12.0;

        // 2. THE MISSING LINK: YOU MUST CALL YOUR JAR'S SPECIFIC API HERE
        // 
        // Example of what you need to look for in the Ragdoll Reactions API:
        // RagdollManager.getRagdoll(player).setRightArmTarget(raX, raY, raZ, stiffness, damping);
        // RagdollManager.getRagdoll(player).setLeftLegTarget(llX, 0, 0, stiffness, damping);
        
        // Optional: Uncomment this line temporarily to verify in your IDE console
        // that the animation angles are successfully being captured while you walk:
        // System.out.println("Captured Right Arm X Angle: " + raX);
    }
}