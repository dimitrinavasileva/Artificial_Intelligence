public class LeafNode extends TreeNode {
    private final String response;

    LeafNode(String response) {
        this.response = response;
    }

    @Override
    public String toString() {
        return response;
    }
}
