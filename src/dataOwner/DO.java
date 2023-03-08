package dataOwner;

import util.IBF;
import util.ReadDataToIdKeywordsPair;

import java.math.BigInteger;
import java.util.*;

import static util.HASH.h;
import static util.HASH.l_h;

public class DO {
    //用于哈希函数的密钥
    private String[] hashKeys = new String[]{"1", "2", "3", "4"};
    private String lKey = "12345";
    int keywordSize;
    int BFSize;
    private BFTreeLeafNode head = null;
    private BFTreeLeafNode tail = null;
    private BFTreeNode BFTreeRoot;

    public BFTreeLeafNode getHead() {
        return head;
    }

    public BFTreeNode getBFTreeRoot() {
        return BFTreeRoot;
    }

    public DO(String dataPath, int keywordSize) {
        long start = System.currentTimeMillis();
        List<List<Integer>> idKeywordsParis = ReadDataToIdKeywordsPair.readData(dataPath);
        this.keywordSize = keywordSize;
        this.BFSize = keywordSize * 5;
        ListTreeNode ListTreRoot = buildListTree(idKeywordsParis);
        this.BFTreeRoot = buildBFTree(ListTreRoot);
        long end = System.currentTimeMillis();
        System.out.println("索引构造时间：" + (end - start) + "ms");
    }

    public Trapdoor getTrapdoor(int[] keywords) {
        List<Integer> posList = new ArrayList<>();
        HashMap<Integer, Integer> posValueMap = new HashMap<>();
        int n = hashKeys.length;
        for (int i = 0; i < keywords.length; i++) {
            for (int j = 0; j < n; j++) {
                int pos = h(keywords[i], hashKeys[j], BFSize);
                if(!posList.contains(pos)) {
                    int value = l_h(pos, lKey);
                    posList.add(pos);
                    posValueMap.put(pos, value);
                }
            }
        }
        return new Trapdoor(posList, posValueMap);
    }
    private BFTreeNode buildBFTree(ListTreeNode root) {
        if(root.left == null && root.right == null) {//叶结点
            long l1 = System.currentTimeMillis();
            int id = root.getIds().get(0);
            IBF ibf = new IBF(BFSize, lKey, hashKeys, root.getKeywordList(id));
            BFTreeLeafNode leafNode = new BFTreeLeafNode(ibf, null, null, id, tail);
            tail = leafNode;
            if(head == null) {
                head = leafNode;
            }
            long l2 = System.currentTimeMillis();
//            System.out.println("一个节点叶完成，时间为：" + (l2 - l1));
            return leafNode;
        }

        //非叶节点
        BFTreeNode left_tree = buildBFTree(root.left);
        BFTreeNode right_tree = buildBFTree(root.right);
        long l1 = System.currentTimeMillis();
//        通过子节点获取bf
        IBF ibf = new IBF(lKey, left_tree.bf, right_tree.bf);
        BFTreeNode resNode = new BFTreeNode(ibf, left_tree, right_tree);
        long l2 = System.currentTimeMillis();
//        System.out.println("一个中间节点完成" + (l2 - l1));

        return resNode;
    }
    private ListTreeNode buildListTree(List<List<Integer>> idKeywordsParis) {
        List<Integer> ids = new ArrayList<>();
        HashMap<Integer, List<Integer>> idKeywordsMap = new HashMap<>();
        for (int i = 0; i < idKeywordsParis.size(); i++) {
            ids.add(i);
            idKeywordsMap.put(i, idKeywordsParis.get(i));
        }
        ListTreeNode root = new ListTreeNode(idKeywordsMap, ids);
        splitToGetChildNode(root);
        //改进方法建树
//        promoteSplitToGetChildNode(root);
        return root;
    }

    private void splitToGetChildNode(ListTreeNode node) {
        List<Integer> nodeList = node.getIds();
        if(nodeList.size() == 1) {
            return;
        }
        int mid = nodeList.size() / 2;
        List<Integer> leftIds = nodeList.subList(0, mid);
        List<Integer> rightIds = nodeList.subList(mid, nodeList.size());
        HashMap<Integer, List<Integer>> leftMap = new HashMap<>();
        HashMap<Integer, List<Integer>> rightMap = new HashMap<>();
        for (int i = 0; i < leftIds.size(); i++) {
            int id = leftIds.get(i);
            leftMap.put(id, node.getKeywordList(id));
        }
        for (int i = 0; i < rightIds.size(); i++) {
            int id = rightIds.get(i);
            rightMap.put(id, node.getKeywordList(id));
        }
        node.left = new ListTreeNode(leftMap, leftIds);
        node.right = new ListTreeNode(rightMap, rightIds);
        splitToGetChildNode(node.left);
        splitToGetChildNode(node.right);
    }
    private void promoteSplitToGetChildNode(ListTreeNode node) {
        List<Integer> idList = node.getIds();
        List<Union> unionList = new ArrayList<>();
        int n = idList.size();
        if(n == 1) {
            return;
        }
        //为树中包含的每个文档建立一个Union
        for (int i = 0; i < idList.size(); i++) {
            ArrayList<Integer> unionIds = new ArrayList<>();
            unionIds.add(idList.get(i));
            Collection<Integer> unionCollection = new ArrayList<>();
            for (int keyword : node.getKeywordList(idList.get(i))) {
                unionCollection.add(keyword);
            }
            unionList.add(new Union(unionIds, unionCollection));
        }
        //合并unionList，最终只剩两个Union
        while(unionList.size() >= 3) {
            List<UnionPath> pathList = new ArrayList<>();
            for (int i = 0; i < unionList.size(); i++) {
                for (int j = i + 1; j < unionList.size(); j++) {
                    pathList.add(new UnionPath(unionList.get(i), unionList.get(j)));
                }
            }
            pathList.sort(new Comparator<UnionPath>() {
                @Override
                public int compare(UnionPath o1, UnionPath o2) {
                    return o2.getScore() - o1.getScore();
                }
            });
            boolean tag = true;
            for (int i = 0; i < pathList.size(); i++) {
                Union a = pathList.get(i).getA();
                Union b = pathList.get(i).getB();
                if(a.getIds().size() + b.getIds().size() <= Math.ceil(n / 2.0)) {
                    a.union(b);
                    unionList.remove(b);
                    tag = false;
                    break;
                }
            }
            //当无法再合并时
            if(tag) {
                Union a = pathList.get(0).getA();
                Union b = pathList.get(0).getB();
                Union u = pathList.get(1).getA();//u为权值最小的集合，需要将其中所有id并到a，b中去
                if(u.equals(a) || u.equals(b)) {
                    u = pathList.get(0).getB();
                }
                unionList.remove(u);
                Iterator<Integer> iter = u.getIds().iterator();
                while(iter.hasNext()) {
                    Integer next = iter.next();
                    ArrayList<Integer> unionIds = new ArrayList<>();
                    unionIds.add(next);
                    Collection<Integer> unionCollection = new ArrayList<>();
                    for (int keyword : node.getKeywordList(next)) {
                        unionCollection.add(keyword);
                    }
                    Union union = new Union(unionIds, unionCollection);
                    if(a.getScore(union) > b.getScore(union) && a.getIds().size() + 1 <= Math.ceil(n / 2.0)) {
                        a.union(union);
                    }else {
                        b.union(union);
                    }
                }
            }
        }

        Union l;
        Union r;
        if(unionList.get(0).getIds().size() > unionList.get(1).getIds().size()) {
            l = unionList.get(0);
            r = unionList.get(1);
        } else {
            l = unionList.get(1);
            r = unionList.get(0);
        }
        HashMap<Integer, List<Integer>> idKeywordsMap_l = new HashMap<>();
        List<Integer> ids_l = new ArrayList<>(l.getIds());
        for (int i = 0; i < ids_l.size(); i++) {
            idKeywordsMap_l.put(ids_l.get(i), node.getKeywordList(ids_l.get(i)));
        }
        node.left = new ListTreeNode(idKeywordsMap_l, ids_l);

        HashMap<Integer, List<Integer>> idKeywordsMap_r = new HashMap<>();
        List<Integer> ids_r = new ArrayList<>(r.getIds());
        for (int i = 0; i < ids_r.size(); i++) {
            idKeywordsMap_r.put(ids_r.get(i), node.getKeywordList(ids_r.get(i)));
        }
        node.right = new ListTreeNode(idKeywordsMap_r, ids_r);

        promoteSplitToGetChildNode(node.left);
        promoteSplitToGetChildNode(node.right);
    }

}
