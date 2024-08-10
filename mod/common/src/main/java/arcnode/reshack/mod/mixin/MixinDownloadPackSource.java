package arcnode.reshack.mod.mixin;

import arcnode.reshack.mod.ResourceHack;
import net.minecraft.client.resources.server.PackLoadFeedback;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(targets = "net.minecraft.client.resources.server.DownloadedPackSource$6")
public class MixinDownloadPackSource {
    @Inject(
            method = "reportFinalResult",
            at = @At(value = "FIELD", target = "Lnet/minecraft/network/protocol/common/ServerboundResourcePackPacket$Action;SUCCESSFULLY_LOADED:Lnet/minecraft/network/protocol/common/ServerboundResourcePackPacket$Action;")
    )
    public void injectReportFinalResult(UUID uUID, PackLoadFeedback.FinalResult finalResult, CallbackInfo ci) {
        String url = ResourceHack.getPackUrls().get(uUID);
        if (url == null) {
            ResourceHack.LOG.warn("Resource {} has no recorded URL", uUID);
            return;
        }
        ResourceHack.getLoadedUrls().add(ResourceHack.getPackUrls().get(uUID));
        ResourceHack.LOG.info("Added loaded resource pack {}", uUID);
    }
}
