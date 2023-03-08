package dataOwner;

import java.util.HashMap;
import java.util.List;

public class ListTreeNode {
    private HashMap<Integer, List<Integer>> idKeywordsMap;
    private List<Integer> ids;
    public ListTreeNode left;
    public ListTreeNode right;

    public HashMap<Integer, List<Integer>> getIdKeywordsMap() {
        return idKeywordsMap;
    }

    public ListTreeNode(HashMap<Integer, List<Integer>> idKeywordsMap, List<Integer> ids) {
        this.idKeywordsMap = idKeywordsMap;
        this.ids = ids;
    }

    public ListTreeNode(HashMap<Integer, List<Integer>> idKeywordsMap, List<Integer> ids, ListTreeNode left, ListTreeNode right) {
        this.idKeywordsMap = idKeywordsMap;
        this.ids = ids;
        this.left = left;
        this.right = right;
    }
    public List<Integer> getKeywordList(int id) {
        return idKeywordsMap.get(id);
    }

    public List<Integer> getIds() {
        return ids;
    }
}
