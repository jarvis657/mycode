package origin.base.inherits;

import com.google.common.collect.ImmutableList;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author:lmq
 * @Date: 2020/4/10
 * @Desc:
 **/
public class TestB extends Test{
//    AtomicInteger pushDataCount = new AtomicInteger(20);
    private List<String> NOTIFY_USER = ImmutableList.of("TestBBBBB..........");
//    static final AtomicInteger nextIndex = new AtomicInteger();

    public static final Object UNSET = new Object();

    public TestB() {
        super(newIndexedVariableTable());
    }


    private static Object[] newIndexedVariableTable() {
        Object[] array = new Object[32];
        Arrays.fill(array, UNSET);
        return array;
    }


    public List<String> getUser(){
        return this.NOTIFY_USER;
    }
    @Override
    public Object getPushDataCount(Object o) {
        System.out.println(o.toString()+ "  haha TestB invoke"+getUser().toString());
        return pushDataCount;
    }
    public void setPd(){
        pushDataCount = 111;
    }

    @Override
    public void p() {
        System.out.println(indexedVariables.hashCode()+"-second pb ......."+nextIndex.getAndIncrement());
    }
}
