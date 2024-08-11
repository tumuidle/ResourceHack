package arcnode.reshack.mod.forge;

import arcnode.reshack.mod.ConfigurationPayload;
import arcnode.reshack.mod.ConfigurationRequestPayload;
import arcnode.reshack.mod.IResourceHackPlatform;
import net.minecraft.client.player.LocalPlayer;

import arcnode.reshack.mod.ResourceHack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@Mod(ResourceHack.MOD_ID)
@OnlyIn(Dist.CLIENT)
public final class ResourceHackForge implements IResourceHackPlatform {
    public ResourceHackForge(IEventBus bus) {
        // Run our common setup.
        ResourceHack.init(this);
        bus.addListener(this::registerPayloads);

        NeoForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onEntityJoinWorld(EntityJoinLevelEvent event) {
        if (event.getEntity() instanceof LocalPlayer)
            ResourceHack.sendRequest();
    }

    private void registerPayloads(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar reg = event.registrar("1").optional();
        reg.playToServer(ConfigurationRequestPayload.TYPE, ConfigurationRequestPayload.CODEC, (payload, context) -> {});
        reg.playToClient(ConfigurationPayload.TYPE, ConfigurationPayload.CODEC, (payload, context) -> {
            ResourceHack.LOG.info("Configurations received from server");
            ResourceHack.configure(payload.config());
        });
    }

    @Override
    public void sendConfigRequest() {
        PacketDistributor.sendToServer(new ConfigurationRequestPayload());
    }
}
