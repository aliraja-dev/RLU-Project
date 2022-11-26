package rlu.sets.RluSet;

class Header<T> {
    long threadId;
    RluNode<T> actualNode;
    private final boolean isCopy = true; // to indicate this is a copy

    public Header(long threadId, RluNode<T> actualNode) {
        this.threadId = threadId;
        this.actualNode = actualNode;
    }

    public Header(long threadId) {
        this.threadId = threadId;
    }

    public boolean isCopy() {
        return isCopy;
    }
}