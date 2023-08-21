package origin.base.inherits;

import com.google.common.collect.ImmutableList;

import java.util.List;

/**
 * @Author:lmq
 * @Date: 2020/8/25
 * @Desc:
 **/
public class T extends TestB {
    List<String> NOTIFY_USER = ImmutableList.of("TTTTTTTTTTTTTTTTTT  Test..........");

//    public List<String> getUser() {
//        getPushDataCount(T.this);
//        getPushDataCount(this);
//        return this.NOTIFY_USER;
//    }

//    @Override
//    public Object getPushDataCount(Object o) {
//        System.out.println(o.toString() + " haha TTTT invoke" + getUser());
//        return pushDataCount;
//    }
}
