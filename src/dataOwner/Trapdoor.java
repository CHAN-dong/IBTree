package dataOwner;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Trapdoor {
    private List<Integer> posList;
    private HashMap<Integer, Integer> posValueMap;

    public Trapdoor(List<Integer> posList, HashMap<Integer, Integer> posValueMap) {
        this.posList = posList;
        this.posValueMap = posValueMap;
    }

    public List<Integer> getPosList() {
        return posList;
    }

    public HashMap<Integer, Integer> getPosValueMap() {
        return posValueMap;
    }
}
