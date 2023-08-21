package argrith;

import java.util.StringJoiner;

/**
 * @Author:lmq
 * @Date: 2020/12/5
 * @Desc:
 **/
public class ListNode {
    private Object values;
    private ListNode next;

    public ListNode(Object values) {
        this.values = values;
    }

    public Object getValues() {
        return values;
    }

    public void setValues(Object values) {
        this.values = values;
    }

    public ListNode getNext() {
        return next;
    }

    public void setNext(ListNode next) {
        this.next = next;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ListNode.class.getSimpleName() + "[", "]")
                .add("values=" + values)
                .add("next=" + next)
                .toString();
    }
}
