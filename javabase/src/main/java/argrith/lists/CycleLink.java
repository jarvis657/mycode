package argrith.lists;

import argrith.ListNode;

/**
 * @Author:lmq
 * @Date: 2020/12/5
 * @Desc:
 **/
public class CycleLink {

    /**
     * 假定起点到环入口点的距离为 a，p1 和 p2 的相交点M与环入口点的距离为b，环路的周长为L，当 p1 和 p2 第一次相遇的时候，假定 p1 走了 n 步.那么有：
     * p1走的路径： a+b ＝ n；
     * p2走的路径： a+b+kL = 2n； p2 比 p1 多走了k圈环路，总路程是p1的2倍
     * 根据上述公式可以得到 k*L=a+b=n显然，如果从相遇点M开始，p1 再走 n 步的话，还可以再回到相遇点，同时p2从头开始走的话，经过n步，也会达到相遇点M。
     * 显然在这个步骤当中 p1 和 p2 只有前 a 步走的路径不同，所以当 p1 和 p2 再次重合的时候，必然是在链表的环路入口点上
     *
     * @param head
     * @return
     */
    public static ListNode findLoopPort(ListNode head) {
        //如果head为空，或者为单结点，则不存在环
        if (head == null || head.getNext() == null) {
            return null;
        }
        ListNode slow, fast;
        slow = fast = head;
        //先判断是否存在环
        while (fast != null && fast.getNext() != null) {
            fast = fast.getNext().getNext();
            slow = slow.getNext();
            if (fast == slow)
                break;
        }
        if (fast != slow) {
            return null;    //不存在环
        }
        fast = head;                //快指针从头开始走，步长变为1
        while (fast != slow) {            //两者相遇即为入口点
            fast = fast.getNext();
            slow = slow.getNext();
        }
        return fast;
    }

    //单链表是否是回文链表
    public boolean isPalindrome(ListNode head) {
        if (head == null) {
            return true;
        }
        ListNode slow = head;
        ListNode fast = head;
        ListNode next = slow.getNext();
        ListNode pre = slow;
        //find mid pointer, and reverse head half part
        while (fast.getNext() != null && fast.getNext().getNext() != null) {
            fast = fast.getNext().getNext();
            pre = slow;
            slow = next;
            next = next.getNext();
            slow.setNext(pre);
        }

        //odd number of elements, need left move slow one step
        //如果是奇数个点，快指针才能指到末尾， 原因 是 每次跳的是偶数边，从头节点开始算 就是 奇数个点
        if (fast.getNext() == null) {
            slow = slow.getNext();
        } else {   //even number of elements, do nothing
        }
        //compare from mid to head/tail
        while (next != null) {
            if (slow.getValues() != next.getValues()) {
                return false;
            }
            slow = slow.getNext();
            next = next.getNext();
        }
        return true;
    }


    public static ListNode reverse(ListNode head) {
        if (head.getNext() == null) {
            return head;
        }
        ListNode next = head.getNext();
        ListNode last = reverse(next);
        head.getNext().setNext(head);
        head.setNext(null);
        return last;
    }

    private static ListNode successor = null; // 后驱节点
    //反转链表前 N 个节点

    /**
     *   head  =>  (reverseN 递归段)
     *
     * @param head
     * @param n
     * @return
     */
    public static ListNode reverseN(ListNode head,int n){
        if (n==1) {
            successor =  head.getNext();
            return head;
        }
        ListNode last =  reverseN(head.getNext(),n-1);
        //-----此段是递归逻辑
        ListNode next = head.getNext();
        next.setNext(head);
        head.setNext(successor);
        System.out.println("head:"+head.getValues() + " next:"+ next.getValues()+ "   succ:"+successor.getValues());
        //-----此段是递归逻辑
        return last;
    }
    //// 反转以 head 为起点的 n 个节点，返回新的头结点
    //ListNode reverseN(ListNode head, int n) {
    //    if (n == 1) {
    //        // 记录第 n + 1 个节点
    //        successor = head.next;
    //        return head;
    //    }
    //    // 以 head.next 为起点，需要反转前 n - 1 个节点
    //    ListNode last = reverseN(head.next, n - 1);
    //
    //    head.next.next = head;
    //    // 让反转之后的 head 节点和后面的节点连起来
    //    head.next = successor;
    //    return last;
    //}


    public static void main(String[] args) {
        ListNode listNode1 = new ListNode(1L);
        ListNode listNode2 = new ListNode(2L);
        ListNode listNode3 = new ListNode(3L);
        ListNode listNode4 = new ListNode(4L);
        ListNode listNode5 = new ListNode(5L);
        listNode1.setNext(listNode2);
        listNode2.setNext(listNode3);
        listNode3.setNext(listNode4);
        listNode4.setNext(listNode5);
        printList(listNode1);
//        ListNode trans = reverse(listNode1);
//        printList(trans);
        ListNode listNode = reverseN(listNode1, 3);
        printList(listNode);

    }

    public static void printList(ListNode listNode) {
        while (listNode != null) {
            System.out.print(listNode.getValues() + "=>");
            listNode = listNode.getNext();
        }
        System.out.println();
    }
}
