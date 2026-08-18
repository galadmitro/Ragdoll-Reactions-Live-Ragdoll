package com.yourname.activeragdoll;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;

@Mod(ActiveRagdollAddon.MOD_ID)
public class ActiveRagdollAddon {
    public static final String MOD_ID = "activeragdoll";

    public ActiveRagdollAddon(IEventBus modEventBus) {
        if (FMLEnvironment.dist == Dist.CLIENT) {
            KeybindManager.register(modEventBus);
        }
    }
}