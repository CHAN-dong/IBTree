package util;

import java.math.BigInteger;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.BitSet;
import java.util.List;


import static util.HASH.*;
import static util.utils.XOR;
import static util.utils.getRandBinaryStr;

public class IBF {
    private BitSet bits1;
    private BitSet bits2;
    private String r;
    int size;

    public BitSet getBits1() {
        return bits1;
    }

    public BitSet getBits2() {
        return bits2;
    }

    public String getR() {
        return r;
    }

    public IBF(String lKey, IBF left, IBF right) {
        r = getRandBinaryStr();
        if(left.size != right.size) {
            System.out.println("左右过滤器大小不同");
        }
        this.size = left.size;
        bits1 = new BitSet(size);
        bits2 = new BitSet(size);
        for (int i = 0; i < size; i++) {
            String hash = Integer.toString(l_h(i, lKey), 2);
            boolean l = false;
            boolean r = false;
            boolean left_tag = H(utils.XOR(hash, left.r));
            boolean right_tag = H(utils.XOR(hash, right.r));
            if(left_tag && left.bits1.get(i) || !left_tag && left.bits2.get(i)) {
                l = true;
            }
            if(right_tag && right.bits1.get(i) || !right_tag && right.bits2.get(i)) {
                r = true;
            }
            boolean tag = H(utils.XOR(hash, this.r));
            if(tag) {
                if(l || r) {
                    this.bits1.set(i, true);
                    this.bits2.set(i, false);
                }else {
                    this.bits1.set(i, false);
                    this.bits2.set(i, true);
                }
            }else {
                if(l || r) {
                    this.bits1.set(i, false);
                    this.bits2.set(i, true);
                } else {
                    this.bits1.set(i, true);
                    this.bits2.set(i, false);
                }
            }
        }

    }

    public IBF(int size, String lKey, String[] keys, List<Integer> keywords) {
        this.size = size;
        bits1 = new BitSet(size);
        bits2 = new BitSet(size);
        r = getRandBinaryStr();
        init(lKey);

        for (Integer keyword : keywords) {
            add(keyword, keys, lKey);
        }
    }

    public void init(String lKey) {
        for (int i = 0; i < size; i++) {
            boolean tag = H(utils.XOR(Integer.toString(l_h(i, lKey), 2), r));
            if(tag) {
                bits2.set(i, true);
                bits1.set(i, false);
            }else{
                bits1.set(i, true);
                bits2.set(i, false);
            }
        }
    }

    public void add(int keyword, String[] keys, String lKey) {
        for (String key : keys) {
            int l = h(keyword, key, this.size);
            //tag为true时，定位到bits1，false定位到bits2
            boolean tag = H(utils.XOR(Integer.toString(l_h(l, lKey), 2), r));
            if(tag) {
                bits1.set(l, true);
                bits2.set(l, false);
            }else{
                bits1.set(l, false);
                bits2.set(l, true);
            }
        }
    }

    public boolean contains(int l, boolean isBits1) {
        if(isBits1) {
            return bits1.get(l);
        } else {
            return bits2.get(l);
        }
    }

    public boolean contains(int l, int value) {
        boolean isBits1 = H(XOR(Integer.toString(value, 2), r));
        return contains(l, isBits1);
    }
}
