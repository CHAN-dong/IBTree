package util;

public class queryAndVerifyRes {
    public long queryTime;

    public queryAndVerifyRes(long queryTime) {
        this.queryTime = queryTime;
    }

    @Override
    public String toString() {
        return "queryAndVerifyRes{" +
                "queryTime=" + queryTime + "ns" +
                '}';
    }
}
