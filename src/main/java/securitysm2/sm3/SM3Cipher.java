package securitysm2.sm3;

import securitysm2.AbstractEncrypt;

public class SM3Cipher extends AbstractEncrypt {

    private static final int BYTE_LENGTH = 32;

    private static final int BLOCK_LENGTH = 64;

    private static final int BUFFER_LENGTH = BLOCK_LENGTH * 1;

    private byte[] xBuf = new byte[BUFFER_LENGTH];

    private int xBufOff;

    private byte[] vector = SM3.iv.clone();

    private int cntBlock = 0;

    public SM3Cipher() {
    }

    public SM3Cipher(SM3Cipher t) {
        System.arraycopy(t.xBuf, 0, this.xBuf, 0, t.xBuf.length);
        this.xBufOff = t.xBufOff;
        System.arraycopy(t.vector, 0, this.vector, 0, t.vector.length);
    }

    public int doFinal(byte[] out, int outOff) {
        byte[] tmp = doFinal();
        System.arraycopy(tmp, 0, out, outOff, tmp.length);
        return BYTE_LENGTH;
    }

    public void reset() {
        xBufOff = 0;
        cntBlock = 0;
        vector = SM3.iv.clone();
    }

    public void update(byte[] in, int inOff, int len) {
        int partLen = BUFFER_LENGTH - xBufOff;
        int inputLen = len;
        int dPos = inOff;
        if (partLen < inputLen) {
            System.arraycopy(in, dPos, xBuf, xBufOff, partLen);
            inputLen -= partLen;
            dPos += partLen;
            doUpdate();
            while (inputLen > BUFFER_LENGTH) {
                System.arraycopy(in, dPos, xBuf, 0, BUFFER_LENGTH);
                inputLen -= BUFFER_LENGTH;
                dPos += BUFFER_LENGTH;
                doUpdate();
            }
        }

        System.arraycopy(in, dPos, xBuf, xBufOff, inputLen);
        xBufOff += inputLen;
    }

    private void doUpdate() {
        byte[] b = new byte[BLOCK_LENGTH];
        for (int i = 0; i < BUFFER_LENGTH; i += BLOCK_LENGTH) {
            System.arraycopy(xBuf, i, b, 0, b.length);
            doHash(b);
        }
        xBufOff = 0;
    }

    private void doHash(byte[] n) {
        byte[] tmp = SM3.cf(vector, n);
        System.arraycopy(tmp, 0, vector, 0, vector.length);
        cntBlock++;
    }

    private byte[] doFinal() {
        byte[] b = new byte[BLOCK_LENGTH];
        byte[] buffer = new byte[xBufOff];
        System.arraycopy(xBuf, 0, buffer, 0, buffer.length);
        byte[] tmp = SM3.padding(buffer, cntBlock);
        for (int i = 0; i < tmp.length; i += BLOCK_LENGTH) {
            System.arraycopy(tmp, i, b, 0, b.length);
            doHash(b);
        }
        return vector;
    }

    public void update(byte in) {
        byte[] buffer = new byte[]{in};
        update(buffer, 0, 1);
    }

    public int getDigestSize() {
        return BYTE_LENGTH;
    }

    @Override
    public byte[] encrypt(byte[] bytes) {
        byte[] md = new byte[32];
        SM3Cipher sm3 = new SM3Cipher();
        sm3.update(bytes, 0, bytes.length);
        sm3.doFinal(md, 0);
        return md;
    }
}
