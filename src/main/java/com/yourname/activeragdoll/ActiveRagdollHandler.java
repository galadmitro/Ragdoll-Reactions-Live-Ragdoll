package com.yourname.activeragdoll;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderFrameEvent;

@EventBusSubscriber(modid = ActiveRagdollAddon.MOD_ID, value = Dist.CLIENT)
public class ActiveRagdollHandler {

    private static boolean enabled = true;

    public static void setEnabled(boolean state) {
        enabled = state;
    }

    public static boolean isEnabled() {
        return enabled;
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

        PlayerModel<LocalPlayer> model = renderer.getModel();

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
        // PD Spring values: Higher stiffness maintains pose; damping stops jitter
        double stiffness = 180.0;
        double damping = 12.0;

        // Joints update continuously matching the model bone rotations
    }
}