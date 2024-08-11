package arcnode.reshack.mod;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

public record ConfigurationRequestPayload() implements CustomPacketPayload {
    public static final Type<ConfigurationRequestPayload> TYPE = new Type<>(ResourceHack.getChannelConfigRequest());
    public static final StreamCodec<FriendlyByteBuf, ConfigurationRequestPayload> CODEC = CustomPacketPayload.codec(ConfigurationRequestPayload::write, ConfigurationRequestPayload::read);


    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
    private static ConfigurationRequestPayload read(FriendlyByteBuf fbb) {
        return new ConfigurationRequestPayload();
    }

    private void write(FriendlyByteBuf fbb) {
    }
}
