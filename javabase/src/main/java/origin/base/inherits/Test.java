package origin.base.inherits;

import com.google.common.collect.ImmutableList;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author:lmq
 * @Date: 2020/4/10
 * @Desc:
 **/
public class Test extends XT {
    protected int pushDataCount = 10;
    static final AtomicInteger nextIndex = new AtomicInteger();
    Object[] indexedVariables;
    private List<String> NOTIFY_USER = ImmutableList.of("Test..........");

    public Test(Object[] indexedVariables) {
        this.indexedVariables = indexedVariables;
    }

    public List<String> getUser() {
        return this.NOTIFY_USER;
    }

    public Object getPushDataCount(Object o) {
        System.out.println("haha Test invoke" + NOTIFY_USER.toString());
        return pushDataCount;
    }

    public void setPushDataCount(int pushDataCount) {
        this.pushDataCount = pushDataCount;
    }

    public void p() {
        System.out.println("abstract root");
    }
}
