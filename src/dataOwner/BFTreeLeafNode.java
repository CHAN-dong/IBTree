package dataOwner;

import util.IBF;

public class BFTreeLeafNode extends BFTreeNode{
    public int id;
    public BFTreeLeafNode next = null;


    public BFTreeLeafNode(IBF bf, BFTreeNode left, BFTreeNode right, int id, BFTreeLeafNode prior) {
        super(bf, left, right);
        this.id = id;
        if(prior != null) {
            prior.next = this;
        }
    }
}
