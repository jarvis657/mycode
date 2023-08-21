package origin.base.inherits;

import java.util.Arrays;

/**
 * @Author:lmq
 * @Date: 2020/4/10
 * @Desc:
 **/
public class TestA extends Test {
    public static final Object UNSET = new Object();

    public TestA() {
        super(newIndexedVariableTable());
    }

    private static Object[] newIndexedVariableTable() {
        Object[] array = new Object[32];
        Arrays.fill(array, UNSET);
        return array;
    }

    public void setPd() {
        pushDataCount = 333;
    }

    @Override
    public void p() {
        System.out.println(indexedVariables.hashCode()+" second p......"+nextIndex.getAndIncrement());
    }
}
