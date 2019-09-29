package securitysm2;

import org.bouncycastle.util.encoders.Base64;
import securitysm2.utils.ByteUtils;

import java.nio.charset.StandardCharsets;

public abstract class AbstractEncrypt implements Encrypt {
    @Override
    public byte[] encrypt(String info) {
        byte[] bytes = info.getBytes(StandardCharsets.UTF_8);
        return encrypt(bytes);

    }

    @Override
    public String encrypt2Hex(String info) {
        byte[] encrypted = encrypt(info);
        return ByteUtils.getHexString(encrypted);
    }

    @Override
    public String encrypt2Hex(byte[] bytes) {
        byte[] encrypted = encrypt(bytes);
        return ByteUtils.getHexString(encrypted);
    }

    @Override
    public String encrypt2B64(String info) {
        byte[] encrypted = encrypt(info);
        return new String(Base64.encode(encrypted), StandardCharsets.UTF_8);
    }

    @Override
    public String encrypt2B64(byte[] bytes) {
        byte[] encrypted = encrypt(bytes);
        return new String(Base64.encode(encrypted), StandardCharsets.UTF_8);
    }

}
