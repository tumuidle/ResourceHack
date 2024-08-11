package arcnode.reshack.mod;

import arcnode.reshack.common.ConfigData;
import arcnode.reshack.common.Networking;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.network.protocol.common.*;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.*;

public final class ResourceHack {
    public static final String MOD_ID = "resourcehack";
    public static final Logger LOG = LoggerFactory.getLogger("ResourceHack");


    @Getter
    private static final ResourceLocation channelConfig = ResourceLocation.fromNamespaceAndPath(Networking.NAMESPACE, Networking.CHANNEL_CONFIG);
    @Getter
    private static final ResourceLocation channelConfigRequest = ResourceLocation.fromNamespaceAndPath(Networking.NAMESPACE, Networking.CHANNEL_CONFIG_REQUEST);
    @Getter
    private static final ResourceLocation channelReset = ResourceLocation.fromNamespaceAndPath(Networking.NAMESPACE, Networking.CHANNEL_RESET);

    @Setter
    private static ConfigData config = null;
    @Getter
    private static IResourceHackPlatform platform;

    @Getter
    private static final Map<String, File> decryptedPackCache = new HashMap<>();
    @Getter
    private static List<String> loadedUrls = new ArrayList<>();
    @Getter
    private static Map<UUID, String> packUrls = new HashMap<>();

    public static void init(IResourceHackPlatform platform) {
        LOG.info("ResourceHack powered by ArcNode");
        ResourceHack.platform = platform;
    }

    public static void sendRequest() {
        LOG.info("Sending configuration request");

        platform.sendConfigRequest();
    }

    public static void configure(ConfigData config) {
        /*
        if (packet.getIdentifier().equals(ResourceHack.getChannelConfig())) {
            setConfig(Networking.read(packet.getData()));
            return true;
        } else if (packet.getIdentifier().equals(ResourceHack.getChannelReset())) {
            ResourceHack.getLoadedUrls().clear();
            return true;
        }
         */
        if (config.getKey().length() != 16)
            throw new IllegalArgumentException("(ResHack) Invalid key length " + config.getKey().length());
        ResourceHack.config = config;
    }

    public static String getKey() {
        if (config == null) {
            return null;
        }
        return config.getKey();
    }
}
