package arcnode.reshack;

import arcnode.reshack.common.ConfigData;
import arcnode.reshack.common.Networking;
import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPluginMessage;
import io.netty.buffer.ByteBuf;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.Messenger;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

public class ResourceHack extends JavaPlugin implements PluginMessageListener {
    private static final String CONFIG_CHANNEL = "%s:%s".formatted(Networking.NAMESPACE, Networking.CHANNEL_CONFIG);
    private static final String CONFIG_REQUEST_CHANNEL = "%s:%s".formatted(Networking.NAMESPACE, Networking.CHANNEL_CONFIG_REQUEST);
    private static final String RESET_CHANNEL = "%s:%s".formatted(Networking.NAMESPACE, Networking.CHANNEL_RESET);
    @Getter
    private static ResourceHack instance;

    private Metrics metrics;
    private byte[] clientConfigData = new byte[0];

    @Override
    public void onLoad() {
        instance = this;

        saveDefaultConfig();
    }

    @Override
    public void onEnable() {
        // Load and encode configuration
        FileConfiguration conf = getConfig();
        ByteBuf confBuf = Networking.write(new ConfigData(
                conf.getString("cryptic_key", "NULL_KEY")
        ));
        clientConfigData = confBuf.array();
        getSLF4JLogger().info("Encoded configuration to {} bytes", clientConfigData.length);

        // Register packet listener
        Messenger msg = Bukkit.getMessenger();
        msg.registerIncomingPluginChannel(this, CONFIG_REQUEST_CHANNEL, this);
        msg.registerOutgoingPluginChannel(this, CONFIG_CHANNEL);

        // Register command
        Bukkit.getCommandMap().register(getName(), new EncryptCommand());

        // Setup metrics
        getSLF4JLogger().info("Setting up metrics");
        metrics = new Metrics(this, 22807);
    }

    @Override
    public void onDisable() {
    }

    @Override
    public void onPluginMessageReceived(@NotNull String channel, @NotNull Player player, @NotNull byte[] bytes) {
        if (channel.equals(CONFIG_REQUEST_CHANNEL)) {
            getSLF4JLogger().info("Sending configurations to %s".formatted(player.getName()));
//            player.sendPluginMessage(this, CONFIG_CHANNEL, this.clientConfigData);
            PacketEvents.getAPI().getPlayerManager().getUser(player).sendPacket(new WrapperPlayServerPluginMessage(CONFIG_CHANNEL, this.clientConfigData));
        }
    }
}
