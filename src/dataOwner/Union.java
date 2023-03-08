package dataOwner;

import java.util.Collection;

public class Union {
    private Collection<Integer> ids;
    private Collection<Integer> keywords;

    public Collection<Integer> getIds() {
        return ids;
    }

    public Collection<Integer> getKeywords() {
        return keywords;
    }

    public Union(Collection<Integer> ids, Collection<Integer> keywords) {
        this.ids = ids;
        this.keywords = keywords;
    }
    public int getScore(Union u) {
        int score = 0;
        for (int keyword : u.getKeywords()) {
            if(this.getKeywords().contains(keyword)) {
                score++;
            }
        }
        return score;
    }
    public void union(Union u) {
        for (int id : u.getIds()) {
            if(!this.ids.contains(id)) {
                this.ids.add(id);
            }
        }
        for (int keyword : u.getKeywords()) {
            if(this.keywords.contains(keyword)) {
                this.keywords.add(keyword);
            }
        }
    }
}
