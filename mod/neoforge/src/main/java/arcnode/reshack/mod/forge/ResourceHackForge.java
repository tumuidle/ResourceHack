package arcnode.reshack.mod.forge;

import net.minecraft.client.player.LocalPlayer;

import arcnode.reshack.mod.ResourceHack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;

@Mod(ResourceHack.MOD_ID)
@OnlyIn(Dist.CLIENT)
public final class ResourceHackForge {
    public ResourceHackForge() {
        // Run our common setup.
//        ResourceHack.init();
        NeoForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onEntityJoinWorld(EntityJoinLevelEvent event) {
        if (event.getEntity() instanceof LocalPlayer)
            ResourceHack.sendRequest();
    }
}
