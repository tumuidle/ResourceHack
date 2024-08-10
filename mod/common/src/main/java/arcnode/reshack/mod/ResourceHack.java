package arcnode.reshack.mod;

import arcnode.reshack.common.ConfigData;
import arcnode.reshack.common.Networking;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.network.protocol.common.*;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public final class ResourceHack {
    public static final String MOD_ID = "resourcehack";
    public static final Logger LOG = LoggerFactory.getLogger("ResourceHack");

    @Getter
    private static final ResourceLocation channelConfig = ResourceLocation.fromNamespaceAndPath("reshack", Networking.CHANNEL_CONFIG);
    @Getter
    private static final ResourceLocation channelReset = ResourceLocation.fromNamespaceAndPath("reshack", Networking.CHANNEL_RESET);

    @Getter
    @Setter
    private static ConfigData config = null;

    @Getter
    private static List<String> loadedUrls = new ArrayList<>();

    public static void init() {
        LOG.info("ResourceHack powered by ArcNode");
    }

    public static void sendRequest() {
        LOG.info("Sending configuration request");

        // TODO: refactor
    }

    public static boolean configure(ClientboundCustomPayloadPacket packet) {
        /*
        if (packet.getIdentifier().equals(ResourceHack.getChannelConfig())) {
            setConfig(Networking.read(packet.getData()));
            return true;
        } else if (packet.getIdentifier().equals(ResourceHack.getChannelReset())) {
            ResourceHack.getLoadedUrls().clear();
            return true;
        }
         */

        // TODO: refactor
        return false;
    }

    public static String getKey() {
        if (config == null) {
            return null;
        }
        return config.getKey();
    }
}
