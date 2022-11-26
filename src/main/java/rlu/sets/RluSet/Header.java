package rlu.sets.RluSet;

class Header<T> {
    long threadId;
    RluNode<T> actualNode;
    RluNode<T> copy;// to store modified copy of the node

    public Header(long threadId, RluNode<T> actualNode) {
        this.threadId = threadId;
        this.actualNode = actualNode;
    }

    public Header(long threadId, RluNode<T> actualNode, RluNode<T> copy) {
        this.threadId = threadId;
        this.actualNode = actualNode;
        this.copy = copy;
    }

    public Header(long threadId) {
        this.threadId = threadId;
        this.actualNode = null;
        this.copy = null;
    }
}