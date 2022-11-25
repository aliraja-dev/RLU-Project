package rlu.sets;

class Header<U> {
    long threadId;
    RluNode<U> actualNode;
    private final boolean isCopy = true; // to indicate this is a copy

    public Header(long threadId, RluNode<U> actualNode) {
        this.threadId = threadId;
        this.actualNode = actualNode;
    }

    public boolean isCopy() {
        return isCopy;
    }
}