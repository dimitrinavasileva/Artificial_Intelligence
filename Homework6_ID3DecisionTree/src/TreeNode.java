import java.util.ArrayList;
import java.util.List;

public class TreeNode {
    private final List<TreeNode> children = new ArrayList<>();
    private int value = -1; //Index of the attribute
    private String edge = null; //Value of the edge between parent node and this node

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    List<TreeNode> getChildren() {
        return children;
    }

    TreeNode addChild(TreeNode node) {
        children.add(node);
        return node;
    }

    String getEdge() {
        return edge;
    }

    void setEdge(String edge) {
        this.edge = edge;
    }

    int getValue() {
        return value;
    }

    void setValue(int value) {
        this.value = value;
    }
}
