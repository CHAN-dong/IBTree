package util;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HASH {
    //返回为true则定位到BitSet1，false定位到BitSet2
    public static boolean H(String i) {
        return creatHash(i) % 2 == 1;
    }

    public static int h(int keyword, String key, int size) {
        return creatHash(keyword + key) % size;
    }

    public static int l_h(int l, String lKey) {
        return creatHash(l + lKey);
    }

    public static int creatHash(String data) {
        return creatHash((data.getBytes(StandardCharsets.UTF_8)));
    }

    public static int creatHash(byte[] data) {
        int result = 0;
        MessageDigest digestFunction;
        try {
            digestFunction = MessageDigest.getInstance("MD5");
        }catch (NoSuchAlgorithmException e) {
            digestFunction = null;
        }
        byte salt = 0;
        byte[] digest;
        synchronized (digestFunction) {
            digestFunction.update(salt);
            digest = digestFunction.digest(data);
        }
        for (int i = 0; i < digest.length/4; i++) {
            int h = 0;
            for (int j = (i*4); j < (i*4)+4; j++) {
                h <<= 8;
                h |= ((int) digest[j]) & 0xFF;
            }
            result = h;
        }
        return Math.abs(result);
    }
}
