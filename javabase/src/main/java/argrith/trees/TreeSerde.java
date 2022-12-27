package argrith.trees;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @Author:lmq
 * @Date: 2020/7/14
 * @Desc: 二叉树的序列化与反序列化, 使用 深度遍历 (DFS 快,内存大 vs BFS 慢,内存小 )
 **/
public class TreeSerde {
    private void ser(StringBuilder sb, TreeNode root) {
        if (root == null) {
            sb.append("nil,");
            return;
        }
        sb.append(root.getValue()).append(",");
        ser(sb, root.left);
        ser(sb, root.right);
    }

    // Encodes a tree to a single string.
    public String serialize(TreeNode root) {
        StringBuilder sb = new StringBuilder();
        ser(sb, root);
        return sb.substring(0, sb.length() - 1);
    }

    // Encodes a tree to a single string.
    public String serialize_norecursion(TreeNode root) {
        if (root == null) {
            return "nil";
        }
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);
        StringBuilder sb = new StringBuilder();
        while (!queue.isEmpty()) {
            TreeNode poll = queue.remove();
            if (poll == null) {
                sb.append("nil").append(",");
                continue;
            }
            sb.append(poll.getValue()).append(",");
            queue.add(poll.left);
            queue.add(poll.right);
        }
        return sb.substring(0, sb.length() - 1);
    }
}
