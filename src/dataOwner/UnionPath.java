package dataOwner;

public class UnionPath {
    private Union A;
    private Union B;
    private int score;

    public UnionPath(Union a, Union b) {
        A = a;
        B = b;
        this.score = getScore(a, b);
    }

    private int getScore(Union u1, Union u2) {
        int score = 0;
        for (int keyword : u2.getKeywords()) {
            if(u1.getKeywords().contains(keyword)) {
                score++;
            }
        }
        return score;
    }

    public Union getA() {
        return A;
    }

    public Union getB() {
        return B;
    }

    public int getScore() {
        return score;
    }
}
