package arcnode.reshack.mod.mixin;

import arcnode.reshack.mod.ResourceHack;
import net.minecraft.client.multiplayer.ClientCommonPacketListenerImpl;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.protocol.common.ClientboundResourcePackPushPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientCommonPacketListenerImpl.class)
public class MixinClientCommonPacketListenerImpl {
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
        if (ResourceHack.getLoadedUrls().contains(packet.url())) {
            ci.cancel();
        } else ResourceHack.getLoadedUrls().add(packet.url());
    }
}
