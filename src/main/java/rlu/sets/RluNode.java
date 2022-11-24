package rlu.sets;

public class RluNode<U> {

    int key;
    RluNode<U> next;
    // to store header information
    Header<U> header;

    public RluNode(U item, RluNode<U> next) {
        this.key = item.hashCode();
        this.next = next;
        // Initialize the header
        header = new Header<>(Thread.currentThread().getId(), this);
    }

    public RluNode(int key) {
        this.key = key;
        next = null;
    }

    class Header<U> {
        long threadId;
        RluNode<U> actualNode;
        private final boolean isCopy = true; // to indicate this is a copy

        public Header(long threadId, RluNode<U> actualNode) {
            this.threadId = threadId;
            this.actualNode = actualNode;
        }
    }
}
