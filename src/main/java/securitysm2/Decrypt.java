package securitysm2;

public interface Decrypt {

    Integer HEX = 1;

    Integer BASE64 = 2;

    String SM4_ECB = "ECB";

    String SM4_CBC = "CBC";


    byte[] decrypt(final String hexEncodedInfo);

    byte[] decrypt(final String encodedInfo, int encodedType);

    byte[] decrypt(final byte[] bytes);

    String decrypt2UTF8(final String hexEncodedInfo);

    String decrypt2UTF8(final String encodedInfo, int encodedType);

    String decrypt2UTF8(final byte[] bytes);


}
