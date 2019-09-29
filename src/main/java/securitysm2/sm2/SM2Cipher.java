package securitysm2.sm2;

import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.math.ec.ECPoint;
import securitysm2.AbstractCipher;
import securitysm2.Decrypt;
import securitysm2.Encrypt;
import securitysm2.utils.ByteUtils;
import securitysm2.utils.SMUtils;

import java.math.BigInteger;

public class SM2Cipher extends AbstractCipher implements Encrypt, Decrypt {
    private byte[] publicKey;
    private byte[] privateKey;

    public SM2Cipher(String pubKey, String priKey) {
        this.publicKey = ByteUtils.hexStringToBytes(pubKey);
        this.privateKey = ByteUtils.hexStringToBytes(priKey);
    }

    public static KeyModel generateKeyPair() {
        SM2 sm2 = SM2.instance();
        AsymmetricCipherKeyPair key = sm2.eccKeyPairGenerator.generateKeyPair();
        ECPrivateKeyParameters ecpriv = (ECPrivateKeyParameters)key.getPrivate();
        ECPublicKeyParameters ecpub = (ECPublicKeyParameters)key.getPublic();
        BigInteger privateKey = ecpriv.getD();
        ECPoint publicKey = ecpub.getQ();
        KeyModel keyModel = new KeyModel();
        keyModel.setPublicKey(ByteUtils.encodeHexString(publicKey.getEncoded()));
        keyModel.setPrivateKey(ByteUtils.encodeHexString(privateKey.toByteArray()));
        return keyModel;
    }

    @Override
    public byte[] encrypt(byte[] data) {
        byte[] source = new byte[data.length];
        System.arraycopy(data, 0, source, 0, data.length);
        SMUtils cipher = new SMUtils();
        SM2 sm2 = SM2.instance();
        ECPoint userKey = sm2.eccCurve.decodePoint(this.publicKey);
        ECPoint c1 = cipher.initEnc(sm2, userKey);
        cipher.encryptSM(source);
        byte[] c3 = new byte[32];
        cipher.doFinalSM(c3);
        String encryptStr = ByteUtils.encodeHexString(c1.getEncoded()) + ByteUtils.encodeHexString(source) + ByteUtils.encodeHexString(c3);
        return ByteUtils.hexStringToBytes(encryptStr);
    }

    @Override
    public byte[] decrypt(byte[] encryptedData) {
        String data = ByteUtils.encodeHexString(encryptedData);
        byte[] c1Bytes = ByteUtils.hexStringToBytes(data.substring(0, 130));
        int c2Len = encryptedData.length - 97;
        byte[] c2 = ByteUtils.hexStringToBytes(data.substring(130, 130 + 2 * c2Len));
        byte[] c3 = ByteUtils.hexStringToBytes(data.substring(130 + 2 * c2Len, 194 + 2 * c2Len));
        SM2 sm2 = SM2.instance();
        BigInteger userD = new BigInteger(1, this.privateKey);
        ECPoint c1 = sm2.eccCurve.decodePoint(c1Bytes);
        SMUtils cipher = new SMUtils();
        cipher.initDec(userD, c1);
        cipher.decryptSM(c2);
        cipher.doFinalSM(c3);
        return c2;
    }

    public String encrypt2HexForJavascript(String info) {
        String encrypted = this.encrypt2Hex(info);
        return encrypted.substring(2).toLowerCase();
    }

    public String decryptFromJavascript(String data) {
        return new String(this.decrypt("04" + data));
    }
}

