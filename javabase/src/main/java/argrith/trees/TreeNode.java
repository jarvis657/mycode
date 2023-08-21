package argrith.trees;

import lombok.val;

/**
 * @Author:lmq
 * @Date: 2020/7/14
 * @Desc:
 **/
public class TreeNode<T> {
    public T value;
    public TreeNode left;
    public TreeNode right;

    public TreeNode(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public TreeNode getLeft() {
        return left;
    }

    public void setLeft(TreeNode left) {
        this.left = left;
    }

    public TreeNode getRight() {
        return right;
    }

    public void setRight(TreeNode right) {
        this.right = right;
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TreeNode{");
        sb.append("value=").append(value);
        sb.append('}');
        return sb.toString();
    }
}
