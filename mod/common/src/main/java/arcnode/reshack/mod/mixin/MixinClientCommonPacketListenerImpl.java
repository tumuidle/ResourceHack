package arcnode.reshack.mod.mixin;

import arcnode.reshack.mod.ResourceHack;
import net.minecraft.client.multiplayer.ClientCommonPacketListenerImpl;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.ClientboundResourcePackPushPacket;
import net.minecraft.network.protocol.common.ServerboundResourcePackPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientCommonPacketListenerImpl.class)
public abstract class MixinClientCommonPacketListenerImpl {
    @Shadow public abstract void send(Packet<?> packet);

    @Redirect(
            method = "handleResourcePackPush",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ServerData;getResourcePackStatus()Lnet/minecraft/client/multiplayer/ServerData$ServerPackStatus;")
    )
    // Forced enable server resource pack
    public ServerData.ServerPackStatus redirectGetServerPackStatus(ServerData instance) {
        return ServerData.ServerPackStatus.ENABLED;
    }

    @Inject(
            method = "handleResourcePackPush",
            at = @At("HEAD"),
            cancellable = true)
    // Prevent resource reloading between servers when connecting through a proxy
    public void injectHandleResourcePack(ClientboundResourcePackPushPacket packet, CallbackInfo ci) {
        // TODO: Fix, not working in 1.21.1
//        if (ResourceHack.getLoadedUrls().contains(packet.url())) {
//            ResourceHack.LOG.info("Skipping loaded pack {}", packet.id());
//            ci.cancel();
//
//            // Notify server
//            this.send(new ServerboundResourcePackPacket(packet.id(), ServerboundResourcePackPacket.Action.ACCEPTED));
//            this.send(new ServerboundResourcePackPacket(packet.id(), ServerboundResourcePackPacket.Action.SUCCESSFULLY_LOADED));
//            System.out.println(ResourceHack.getLoadedUrls());
//        } else ResourceHack.getPackUrls().put(packet.id(), packet.url());
    }
}
