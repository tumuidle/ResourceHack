package arcnode.reshack.mod.fabric;

import arcnode.reshack.mod.ConfigurationPayload;
import arcnode.reshack.mod.ConfigurationRequestPayload;
import arcnode.reshack.mod.IResourceHackPlatform;
import arcnode.reshack.mod.ResourceHack;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientEntityEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.minecraft.world.entity.Entity;

public final class ResourceHackFabric implements ClientModInitializer, IResourceHackPlatform {
    @Override
    public void onInitializeClient() {
        // This entrypoint is suitable for setting up client-specific logic, such as rendering.
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER)
            throw new UnsupportedOperationException("Client only mod");

        ResourceHack.init(this);

        ClientEntityEvents.ENTITY_LOAD.register((Entity entity, ClientLevel world) -> {
            if (entity instanceof LocalPlayer)
                ResourceHack.sendRequest();
        });

        // Register networking
        PayloadTypeRegistry.playC2S().register(ConfigurationRequestPayload.TYPE, ConfigurationRequestPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(ConfigurationPayload.TYPE, ConfigurationPayload.CODEC);
        ClientPlayNetworking.registerGlobalReceiver(ConfigurationPayload.TYPE, ((payload, context) -> {
            ResourceHack.LOG.info("Configurations received from server");
            ResourceHack.configure(payload.config());
        }));
    }

    @Override
    public void sendConfigRequest() {
        Minecraft.getInstance().getConnection().send(new ServerboundCustomPayloadPacket(new ConfigurationRequestPayload()));
    }
}
