package util;

import java.math.BigInteger;
import java.util.Random;

public class utils {

    public static String getRandBinaryStr() {
        String r = new BigInteger(256,new Random()).toString(2);
        if(r.charAt(0) == '-') {
            r = r.substring(1);
        }
        return r;
    }

    public static String XOR(String x, String y) {
        StringBuilder res = new StringBuilder(Math.max(x.length(), y.length()));
        int i = 0;
        if(x.charAt(i) == '-') {
            i++;
        }
        for (; i < x.length(); i++) {
            if(x.charAt(i) != '1' && x.charAt(i) != '0'){
                System.out.println("不是二进制字符串进行异或");
            }
            if(i >= y.length() && x.charAt(i) == '0' || i < y.length() && x.charAt(i) == y.charAt(i)) {
                res.append(0);
            }else {
                res.append(1);
            }
        }
        return res.toString();
    }
}
