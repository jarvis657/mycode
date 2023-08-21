package argrith.trees;

import io.netty.util.HashedWheelTimer;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/**
 * @Author:lmq
 * @Date: 2020/7/29
 * @Desc:
 **/
public class TravelTreeNode {
    public static void main(String[] args) {
        TreeNode head = createTree();
//        recurseFront(head);
//        recurseMid(head);
//        recurseEnd(head);
//        front(head);
//        front_test(head);
//        mid(head);
//        mid_test(head);
//        System.out.println("======");
//        mid_test(head);
//        endWith2Stack(head);
//        endWithOneStack(head);
//        List<List<TreeNode>> lay = lay(head);
//        System.out.println(lay.toString());
        flatten(head);
        front(head);
        System.out.println("==============");
        head = createTree();
        flatten2(head);
        front(head);
    }

    public static TreeNode convertBST2DoubleLinkList(TreeNode root) {
        TreeNode node = root;
        TreeNode parent = null;
        TreeNode newRoot = null;
        while (node != null) {
            //旋转左侧节点（就是右旋，让左子树升高）
            while (node.left != null) {
                node = rightRotateNode(node);
                //这两行是设置双向链表右链接
                if (parent != null) {
                    parent.right = node;
                }
            }
            if (newRoot == null) {
                newRoot = node;
            }
            //这几行是设置双向链表左链接
            node.left = parent;
            parent = node;
            node = node.right;
        }
        return newRoot;
    }

    private static TreeNode rightRotateNode(TreeNode node) {
        TreeNode left = node.left;
        node.left = node.left.right;
        left.right = node;
        return left;
    }

    /**
     * 非递归实现的二叉树后序遍历<br>
     * 借助于一个栈进行实现
     *
     * @param head
     */
    public static void endWithOneStack(TreeNode head) {
        System.out.println();
        if (head == null) {
            return;
        } else {
            Stack<TreeNode> stack = new Stack<TreeNode>();
            stack.push(head);
            // 该节点代表已经打印过的节点，待会会及时的进行更新
            TreeNode printedTreeNode = null;
            while (!stack.isEmpty()) {
                // 获取 栈顶的元素的值，而不是pop掉栈顶的值
                head = stack.peek();
                // 如果当前栈顶元素的左节点不为空，左右节点均未被打印过，说明该节点是全新的，所以压入栈中
                if (head.getLeft() != null && printedTreeNode != head.getLeft() && printedTreeNode != head.getRight()) {
                    stack.push(head.getLeft());
                } else if (head.getRight() != null && printedTreeNode != head.getRight()) {
                    // 第一层不满足，则说明该节点的左子树已经被打印过了。如果栈顶元素的右节点未被打印过，则将右节点压入栈中
                    stack.push(head.getRight());
                } else {
                    // 上面两种情况均不满足的时候则说明左右子树均被打印过，此时只需要弹出栈顶元素，打印该值即可
                    System.out.println("当前值为：" + stack.pop().getValue());
                    // 记得实时的更新打印过的节点的值
                    printedTreeNode = head;
                }
            }
        }
    }

    /**
     * 非递归实现的二叉树的后序遍历<br>
     * 借助于两个栈来实现
     *
     * @param head
     */
    public static void endWith2Stack(TreeNode head) {
        System.out.println();
        if (head == null) {
            return;
        } else {
            Stack<TreeNode> stack1 = new Stack<TreeNode>();
            Stack<TreeNode> stack2 = new Stack<TreeNode>();

            stack1.push(head);
            // 对每一个头结点进行判断，先将头结点放入栈2中，然后依次将该节点的子元素放入栈1.
            // 顺序为left-->right。便是因为后序遍历为“左右根”
            while (!stack1.isEmpty()) {
                head = stack1.pop();
                stack2.push(head);
                if (head.getLeft() != null) {
                    stack1.push(head.getLeft());
                }

                if (head.getRight() != null) {
                    stack1.push(head.getRight());
                }
            }

            // 直接遍历输出栈2，即可实现后序遍历的节点值的输出
            while (!stack2.isEmpty()) {
                System.out.println("当前节点的值：" + stack2.pop().getValue());
            }
        }
    }

    /**
     * 非递归实现的二叉树的中序遍历
     * <p>
     * 前序遍历-根左右
     * 中序遍历-左根右
     * 后序遍历-左右根
     *
     * @param head
     */
    public static void mid(TreeNode head) {
        System.out.println();
        if (head == null) {
            return;
        } else {
            Stack<TreeNode> TreeNodes = new Stack<TreeNode>();
            // 使用或的方式是因为 第一次的时候栈中元素为空，head的非null特性可以保证程序可以执行下去
            while (!TreeNodes.isEmpty() || head != null) {
                // 当前节点元素值不为空，则放入栈中，否则先打印出当前节点的值，然后将头结点变为当前节点的右子节点。
                if (head != null) {
                    TreeNodes.push(head);
                    head = head.getLeft();
                } else {
                    TreeNode temp = TreeNodes.pop();
                    System.out.println("mid当前节点的值：" + temp.getValue());
                    head = temp.getRight();
                }
            }
        }
    }

    public static void mid_test(TreeNode root) {
        Stack<TreeNode> stack = new Stack<>();
        while (!stack.isEmpty() || root != null) {
            while (root != null) {
                stack.add(root);
                root = root.left;
            }
            TreeNode left = stack.pop();
            System.out.println(left);
            root = left.right;
        }
    }

    /**
     * 非递归实现的二叉树的先序遍历
     * <p>
     * 前序遍历-根左右
     * 中序遍历-左根右
     * 后序遍历-左右根
     *
     * @param head
     */
    public static void front(TreeNode head) {
        System.out.println();
        // 如果头结点为空，则没有遍历的必要性，直接返回即可
        if (head == null) {
            return;
        } else {
            // 初始化用于存放节点顺序的栈结构
            Stack<TreeNode> TreeNodes = new Stack<TreeNode>();
            // 先把head节点放入栈中，便于接下来的循环放入节点操作
            TreeNodes.add(head);
            while (!TreeNodes.isEmpty()) {
                // 取出栈顶元素，判断其是否有子节点
                TreeNode temp = TreeNodes.pop();
                System.out.println("front当前节点的值：" + temp.getValue());
                // 先放入右边子节点的原因是先序遍历的话输出的时候左节点优先于右节点输出，而栈的特性决定了要先放入右边的节点
                if (temp.getRight() != null) {
                    TreeNodes.push(temp.getRight());
                }
                if (temp.getLeft() != null) {
                    TreeNodes.push(temp.getLeft());
                }
            }
        }
    }

    public static void front_test(TreeNode root) {
        Stack<TreeNode> stack = new Stack<>();
        while (!stack.isEmpty() || root != null) {
            while (root != null) {
                System.out.println(root.value);
                stack.add(root);
                root = root.left;
            }
            TreeNode left = stack.pop();
            root = left.right;
        }
    }


    /**
     * 递归实现的先序遍历
     *
     * @param head
     */
    public static void recurseFront(TreeNode head) {
        System.out.println();

        if (head == null) {
            return;
        }
        System.out.println("当前节点值：" + head.getValue());
        recurseFront(head.left);
        recurseFront(head.right);
    }

    /**
     * 递归实现的中序遍历
     *
     * @param head
     */
    public static void recurseMid(TreeNode head) {
        System.out.println();
        if (head == null)
            return;
        recurseMid(head.getLeft());
        System.out.println("当前节点的值：" + head.getValue());
        recurseMid(head.getRight());
    }

    /**
     * 递归实现的后序遍历递归实现
     *
     * @param head
     */
    public static void recurseEnd(TreeNode head) {
        System.out.println();
        if (head == null)
            return;
        recurseEnd(head.getLeft());
        recurseEnd(head.getRight());
        System.out.println("当前节点的值为：" + head.getValue());
    }

    public static TreeNode createTree() {
        // 初始化节点
        //     1
        //  2     3
        //4   5  6
        TreeNode head = new TreeNode(1);
        TreeNode headLeft = new TreeNode(2);
        TreeNode headRight = new TreeNode(3);
        TreeNode headLeftLeft = new TreeNode(4);
        TreeNode headLeftRigth = new TreeNode(5);
        TreeNode headRightLeft = new TreeNode(6);
        TreeNode headRightRight = new TreeNode(7);
        // 为head节点 赋予左右值
        head.setLeft(headLeft);
        head.setRight(headRight);

        headLeft.setLeft(headLeftLeft);
        headLeft.setRight(headLeftRigth);
        headRight.setLeft(headRightLeft);
        headRight.setRight(headRightRight);
        // 返回树根节点
        return head;
    }

    /**
     * 层序遍历
     *
     * @param root
     * @return
     */
    public static List<List<TreeNode>> lay(TreeNode root) {
        Queue<TreeNode> queue = new LinkedList<>();
        List<List<TreeNode>> results = new ArrayList<>();
        List<TreeNode> levels = new ArrayList<>();
        int level = 0;
        queue.add(root);
        while (queue.size() != 0) {
            //获取队列头
            int childSize = queue.size();
            while (childSize > 0) {
                TreeNode c = queue.poll();
                levels.add(c);
                if (c.left != null) {
                    queue.add(c.left);
                }
                if (c.right != null) {
                    queue.add(c.right);
                }
                childSize--;
            }
            results.add(levels);
            levels = new ArrayList<>();
            level++;
        }
        System.out.println("level" + level);
        return results;
    }

    /**
     * 1
     * / \
     * 2   5
     * / \   \
     * 3   4   6
     * =>
     * 1
     * \
     * 2
     * \
     * 3
     * \
     * 4
     * \
     * 5
     * \
     * 6
     *
     * @param root
     */
    private static TreeNode last = null;

    public static void flattenRec(TreeNode root) {
        if (root == null) {
            return;
        }
        flattenRec(root.right);
        flattenRec(root.left);
        root.right = last;
        root.left = null;
        last = root;
    }

    /**
     * 待看
     *
     * @param root
     */
    public static void flatten(TreeNode root) {
        Stack<TreeNode> stack = new Stack();
        while (root != null || !stack.isEmpty()) {
            while (root != null) {
                stack.push(root);
                root = root.left;
            }
            if (!stack.isEmpty()) {
                TreeNode node = stack.pop();
                TreeNode tmp = node.right;
                node.right = node.left;
                node.left = null;

                while (node.right != null) node = node.right;
                node.right = tmp;
                root = tmp;
            }
        }
    }

    /**
     * 待看
     *
     * @param root
     */
    public static void flatten2(TreeNode root) {
        if (root == null) return;
        Stack<TreeNode> stack = new Stack<TreeNode>();
        stack.push(root);
        while (!stack.isEmpty()) {
            TreeNode current = stack.pop();
            if (current.right != null) stack.push(current.right);
            if (current.left != null) stack.push(current.left);
            if (!stack.isEmpty()) current.right = stack.peek();
            current.left = null;
        }
    }

    /**
     * 寻找两个节点的公共父节点
     *
     * @param root
     * @param p
     * @param q
     * @return
     */
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        //发现目标节点则通过返回值标记该子树发现了某个目标结点
        if (root == null || root == p || root == q) return root;
        //查看左子树中是否有目标结点，没有为null
        TreeNode left = lowestCommonAncestor(root.left, p, q);
        //查看右子树是否有目标节点，没有为null
        TreeNode right = lowestCommonAncestor(root.right, p, q);
        //都不为空，说明做右子树都有目标结点，则公共祖先就是本身
        if (left != null && right != null) return root;
        //如果发现了目标节点，则继续向上标记为该目标节点
        return left == null ? right : left;
    }

    public static <V> void dfsNotRecursive(TreeNode<V> tree) {
        if (tree != null) {
            //次数之所以用 Map 只是为了保存节点的深度，
            //如果没有这个需求可以改为 Stack<TreeNode<V>>
            Stack<Map<TreeNode<V>, Integer>> stack = new Stack<>();
            Map<TreeNode<V>, Integer> root = new HashMap<>();
            root.put(tree, 0);
            stack.push(root);
            while (!stack.isEmpty()) {
                Map<TreeNode<V>, Integer> item = stack.pop();
                TreeNode<V> node = item.keySet().iterator().next();
                int depth = item.get(node);
                //打印节点值以及深度
                System.out.println(tree.getValue().toString() + ",   " + depth);
//                if (node.getChildList() != null && !node.getChildList().isEmpty()) {
//                    for (TreeNode<V> treeNode : node.getChildList()) {
//                        Map<TreeNode<V>, Integer> map = new HashMap<>();
//                        map.put(treeNode, depth + 1);
//                        stack.push(map);
//                    }
//                }
            }
        }
    }

    public static <V> void bfsNotRecursive(TreeNode<V> tree) {
        if (tree != null) {
            //跟上面一样，使用 Map 也只是为了保存树的深度，没这个需要可以不用 Map
            Queue<Map<TreeNode<V>, Integer>> queue = new ArrayDeque<>();
            Map<TreeNode<V>, Integer> root = new HashMap<>();
            root.put(tree, 0);
            queue.offer(root);
            while (!queue.isEmpty()) {
                Map<TreeNode<V>, Integer> itemMap = queue.poll();
                TreeNode<V> itemTreeNode = itemMap.keySet().iterator().next();
                int depth = itemMap.get(itemTreeNode);
                //打印节点值以及深度
                System.out.println(itemTreeNode.getValue().toString() + ",   " + depth);
//                if (itemTreeNode.getChildList() != null && !itemTreeNode.getChildList().isEmpty()) {
//                    for (TreeNode<V> child : itemTreeNode.getChildList()) {
//                        Map<TreeNode<V>, Integer> map = new HashMap<>();
//                        map.put(child, depth + 1);
//                        queue.offer(map);
//                    }
//                }
            }
        }
    }

    /**
     * 使用栈来实现 dfs
     *
     * @param root
     */
    public static void dfsWithStack(TreeNode root) {
        if (root == null) {
            return;
        }

        Stack<TreeNode> stack = new Stack<>();
        // 先把根节点压栈
        stack.push(root);
        while (!stack.isEmpty()) {
            TreeNode treeNode = stack.pop();
            // 遍历节点
//            process(treeNode)

            // 先压右节点
            if (treeNode.right != null) {
                stack.push(treeNode.right);
            }

            // 再压左节点
            if (treeNode.left != null) {
                stack.push(treeNode.left);
            }
        }
    }

    /**
     * 使用队列实现 bfs
     *
     * @param root
     */
    private static void bfs(TreeNode root) {
        if (root == null) {
            return;
        }
        Queue<TreeNode> stack = new LinkedList<>();
        stack.add(root);

        while (!stack.isEmpty()) {
            TreeNode node = stack.poll();
            System.out.println("value = " + node.value);
            TreeNode left = node.left;
            if (left != null) {
                stack.add(left);
            }
            TreeNode right = node.right;
            if (right != null) {
                stack.add(right);
            }
        }
    }
}
