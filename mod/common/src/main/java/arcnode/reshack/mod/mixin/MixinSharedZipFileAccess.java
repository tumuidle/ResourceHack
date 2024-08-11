package arcnode.reshack.mod.mixin;

import arcnode.reshack.mod.ResourceHack;
import arcnode.reshack.mod.access.ForgeAccessHack;
import com.google.common.hash.Hashing;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.File;
import java.nio.file.Files;
import java.util.UUID;
import java.util.zip.ZipFile;

@Mixin(targets = "net.minecraft.server.packs.FilePackResources$SharedZipFileAccess")
public class MixinSharedZipFileAccess {
    @Shadow @Final File file;

    @Inject(
            method = "getOrCreateZipFile",
            at = @At(value = "NEW", target = "(Ljava/io/File;)Ljava/util/zip/ZipFile;"),
            cancellable = true
    )
    public void injectGetOrCreateZipFile(CallbackInfoReturnable<ZipFile> cir) {
        try {
            String key = ResourceHack.getKey();
            if (key == null) {
                ResourceHack.LOG.info("Resource not decrypted (Not configured yet)");
                return;
            }
            if (key.equals("NULL_KEY")) {
                ResourceHack.LOG.warn("Null key received from server");
                return;
            }

            byte[] original = Files.readAllBytes(this.file.toPath());

            File cached = ResourceHack.getDecryptedPackCache().get(this.file.getAbsolutePath());
            if (cached != null) {
                cir.setReturnValue(new ZipFile(cached));
            } else if (ForgeAccessHack.accessValidate(original)) {
                // Generate random file name
                String yee = UUID.randomUUID().toString();
                File tmp = File.createTempFile(yee.substring(0, 8), null);

                // Decrypt
                byte[] dec = ForgeAccessHack.accessDecrypt(key, original);
                Files.write(tmp.toPath(), dec);

                // Complete
                cir.setReturnValue(new ZipFile(tmp));
                ResourceHack.LOG.info("Resource decryption completed");
                tmp.deleteOnExit();

                // Put cache
                ResourceHack.getDecryptedPackCache().put(this.file.getAbsolutePath(), tmp);
                ResourceHack.LOG.info("Put cache for {}", this.file.getAbsolutePath());
            }
        } catch (Throwable t) {
            throw new RuntimeException("(ResHack) Error processing resources", t);
        }
    }
}
