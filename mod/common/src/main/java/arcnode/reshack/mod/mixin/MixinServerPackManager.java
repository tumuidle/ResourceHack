package arcnode.reshack.mod.mixin;

import net.minecraft.client.resources.server.ServerPackManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerPackManager.class)
public class MixinServerPackManager {
    @Redirect(
            method = "pushNewPack",
            at = @At(value = "FIELD", target = "Lnet/minecraft/client/resources/server/ServerPackManager;packPromptStatus:Lnet/minecraft/client/resources/server/ServerPackManager$PackPromptStatus;")
    )
    public ServerPackManager.PackPromptStatus redirectPackPromptStatus(ServerPackManager instance) {
        return ServerPackManager.PackPromptStatus.ALLOWED;
    }
}
