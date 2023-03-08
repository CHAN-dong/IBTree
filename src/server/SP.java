package server;

import dataOwner.BFTreeLeafNode;
import dataOwner.BFTreeNode;
import dataOwner.Trapdoor;
import util.WriteVO;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SP {
    private BFTreeNode BFTreeRoot;

    private BFTreeLeafNode head;

    private void treeToStr(BFTreeNode node) {
        if (node == null) {
            return;
        } else {
            StringBuffer ans = new StringBuffer();
            ans.append(node.bf.getR().toString() + " ");
            ans.append(node.bf.getBits1().toString() + " ");
            ans.append(node.bf.getBits2().toString() + " ");
            WriteVO.writeVOToLocal(ans.toString());
            treeToStr(node.left);
            treeToStr(node.right);
        }
    }

    private void getIndexSize() {
        treeToStr(BFTreeRoot);
    }

    public SP(BFTreeNode BFTreeRoot, BFTreeLeafNode head) {
        this.BFTreeRoot = BFTreeRoot;
        this.head = head;
        getIndexSize();
    }

    public List<Integer> search(Trapdoor trapdoor) {
        List<Integer> posList = trapdoor.getPosList();
        HashMap<Integer, Integer> posValueMap = trapdoor.getPosValueMap();
        return searchTree(this.BFTreeRoot, posList, posValueMap);
    }
    private List<Integer> searchTree(BFTreeNode node, List<Integer> posList, HashMap<Integer, Integer> posValueMap) {
        boolean isContains = true;
        for (int i = 0; i < posList.size(); i++) {
            int pos = posList.get(i);
            if(!node.bf.contains(pos, posValueMap.get(pos))) {
                isContains = false;
                break;
            }
        }
        List<Integer> res = null;
        if(isContains) {
            if(node instanceof BFTreeLeafNode) {//为叶节点
                BFTreeLeafNode leafNode = (BFTreeLeafNode) node;
                res = new ArrayList<Integer>(){{add(leafNode.id);}};
            }else {//不为叶节点
                List<Integer> left = searchTree(node.left, posList, posValueMap);
                List<Integer> right = searchTree(node.right, posList, posValueMap);
                if(left != null || right != null) {
                    res = new ArrayList<>();
                }else {
                    return null;
                }
                if(left != null) {
                    res.addAll(left);
                }
                if(right != null) {
                    res.addAll(right);
                }
            }
        }
        return res;
    }
}
