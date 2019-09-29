package securitysm2;


import org.bouncycastle.util.encoders.Base64;
import org.bouncycastle.util.encoders.Hex;

import java.nio.charset.StandardCharsets;

public abstract class AbstractCipher extends AbstractEncrypt implements Encrypt, Decrypt{


	@Override
	public byte[] decrypt(String hexEncodedInfo) {
		return decrypt(hexEncodedInfo, Decrypt.HEX);
	}


	@Override
	public byte[] decrypt(String encodedInfo, int encodedType) {
		byte[] decode;
		if (encodedType == Decrypt.BASE64) {
			byte[] bytes = encodedInfo.getBytes(StandardCharsets.UTF_8);
			decode = Base64.decode(bytes);
		} else {
			decode = Hex.decode(encodedInfo);
		}
		return decrypt(decode);
	}


	@Override
	public String decrypt2UTF8(String hexEncodeInfo) {
		byte[] decrypted = decrypt(hexEncodeInfo);
		return new String(decrypted, StandardCharsets.UTF_8);
	}

	@Override
	public String decrypt2UTF8(String encodeInfo, int encodedType) {
		byte[] decrypted = decrypt(encodeInfo, encodedType);
		return new String(decrypted, StandardCharsets.UTF_8);
	}


	@Override
	public String decrypt2UTF8(byte[] bytes) {
		byte[] decrypted = decrypt(bytes);
		return new String(decrypted, StandardCharsets.UTF_8);
	}

}
