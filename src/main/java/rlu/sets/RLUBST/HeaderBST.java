package rlu.sets.RLUBST;


class HeaderBST<T extends Comparable<T>> {
    long threadId;
    RLUBSTNode<T> actualNode;
    RLUBSTNode<T> copy;// to store modified copy of the node

    public HeaderBST(long threadId, RLUBSTNode<T> actualNode, RLUBSTNode<T> copy) {
        this.threadId = threadId;
        this.actualNode = actualNode;
        this.copy = copy;
    }

    public HeaderBST(long threadId) {
        this.threadId = threadId;
        this.actualNode = null;
        this.copy = null;
    }

}
