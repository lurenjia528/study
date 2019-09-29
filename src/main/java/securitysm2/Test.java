package securitysm2;


import securitysm2.sm2.KeyModel;
import securitysm2.sm2.SM2Cipher;

import java.nio.charset.StandardCharsets;

public class Test {

    private static String priKey = "03ace564b89ae0f2ebf11a7b227c5fd960bf4a04d5d3ceeb964cc87f257460b6";
    private static String pubKey = "04f2aaa7c70b5b77caddfe0d61991a0497c7a3f2408ad8ea100b3d91173b04def11e247471dcb0348a1daea9ddccaa30d0cb2822da13ec11810bc94c0ec078cf75";


    private static SM2Cipher sm2Cipher;

    static {
        sm2Cipher = new SM2Cipher(pubKey, priKey);
    }

    //生成公私钥对
    public static void getKeyPair() {
        KeyModel keyModel = SM2Cipher.generateKeyPair();
        System.out.println("私钥：" + keyModel.getPrivateKey());
        System.out.println("公钥：" + keyModel.getPublicKey());
    }

    //后端加密（加密串在后端解密）
    public static String encrypt(String data) {
        return sm2Cipher.encrypt2Hex(data);
    }

    //后端解密（解后端加密过的串）
    public static String decrypt(String data) {
        return new String(sm2Cipher.decrypt(data), StandardCharsets.UTF_8);
    }

    //后端加密（前端解密）
    public static String encryptForJavaScript(String data) {
        return sm2Cipher.encrypt2HexForJavascript(data);
    }

    //后端解密（前端加密）
    public static String decryptFromJavaScript(String data) {
        return sm2Cipher.decryptFromJavascript(data);
    }

    public static void main(String[] args) {
        //后端java加密,java解密   ok
        String miwen = encrypt("asdasd");
        System.out.println("密文：" + miwen);
        String mingwen = decrypt(miwen);
        System.out.println("解密后明文：" + mingwen);

        //sample-sm2_crypt.html  js加密　　java解密
//        String s = decryptFromJavaScript("2ff5aa6a0086a6780b96a497222d8d1549a8b178afe867479ade57411dc9f0ff1ea68d88a53d38b49b037e98eca5810b6b754d850f2de07a82b11ca8045ab7f6e6c1897aa5c40064fe438b27c5e7abb355f2d10156bdefb012074066e89f7bbe2ec3dc89f63268d773");
//        System.out.println(s);

        // java加密　js解密
//        String jgjasods = encryptForJavaScript("asdfasdfasdfadsf");
//        System.out.println(jgjasods);

//        getKeyPair();

        //后端加密传值到前端
//        System.out.println(encryptForJavaScript("abcdefghijklmnopq"));
//        String name = "304502201c1183a88f08b7d40db52f42552d1e2f3932c9f764b00b8cc4fe580e2c4438ef022100d1008a3a5202dd544b249baa3971ed16797eeeed15650460d80ccee115b683b3";
//        //String encrypted = encryptForJavaScript(name);
//        String decrypt = decryptFromJavaScript(name);
//        System.out.println("对前端加密数据进行解密解密：" + decrypt);


//        //SM3进行信息摘要
//        String a = "0ff66e023637c9e2bb214f5a22010db4780ce1d58d74655f12e90ce97b2a1393ae6bec3cdb7edff297e157ee0378cc666ff522da6be0b402c9c54c2c86cd334453cb94995ba1a6322edbe5a93bb381962d50c224cada01f8268c098d866603c216cb61a3d46e78d150f715c315c4e4c1ec";
//        SM3Cipher sm3 = new SM3Cipher();
//        System.out.println(ByteUtils.getHexString(sm3.encrypt(a)));
//
//


    }
}
