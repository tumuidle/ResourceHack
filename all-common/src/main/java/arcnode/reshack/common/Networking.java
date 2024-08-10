package arcnode.reshack.common;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.StandardCharsets;

public class Networking {
    public static final String NAMESPACE = "reshack";
    public static final String CHANNEL_CONFIG = "conf/send";
    public static final String CHANNEL_CONFIG_REQUEST = "conf/request";
    public static final String CHANNEL_RESET = "reset";

    public static ConfigData read(ByteBuf data) {
        byte[] keyBytes = new byte[data.readIntLE()];
        data.readBytes(keyBytes);
        return new ConfigData(
                new String(keyBytes, StandardCharsets.UTF_8)
        );
    }

    public static ByteBuf write(ConfigData data) {
        ByteBuf bb = Unpooled.buffer();
        write(data, bb);
        return bb;
    }

    public static void write(ConfigData data, ByteBuf bb) {
        byte[] keyBytes = data.getKey().getBytes(StandardCharsets.UTF_8);
        bb.writeIntLE(keyBytes.length);
        bb.writeBytes(keyBytes);
    }
}
