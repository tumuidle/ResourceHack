package arcnode.reshack.mod.fabric;

import arcnode.reshack.common.ConfigData;
import arcnode.reshack.common.Networking;
import arcnode.reshack.mod.ResourceHack;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

public record ConfigurationPayload(ConfigData config) implements CustomPacketPayload {
    public static final Type<ConfigurationPayload> TYPE = new Type<>(ResourceHack.getChannelConfig());
    public static final StreamCodec<FriendlyByteBuf, ConfigurationPayload> CODEC = CustomPacketPayload.codec(ConfigurationPayload::write, ConfigurationPayload::read);

    private static ConfigurationPayload read(FriendlyByteBuf fbb) {
        var result = new ConfigurationPayload(Networking.read(fbb));
        fbb.readBytes(fbb.readableBytes());
        return result;
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    private void write(FriendlyByteBuf fbb) {
        ResourceHack.LOG.info("ENCODE");
        Networking.write(this.config, fbb);
    }
}
