package dataOwner;

import util.IBF;

public class BFTreeNode {
    public IBF bf;
    public BFTreeNode left;
    public BFTreeNode right;

    public BFTreeNode(IBF bf, BFTreeNode left, BFTreeNode right) {
        this.bf = bf;
        this.left = left;
        this.right = right;
    }
}
