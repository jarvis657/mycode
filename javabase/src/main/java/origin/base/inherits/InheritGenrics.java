package origin.base.inherits;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author:lmq
 * @Date: 2020/6/11
 * @Desc:
 **/
public class InheritGenrics {
    static class Fruit {
    }

    static class Apple extends Fruit {
    }

    static class RedApple extends Apple {
    }

    public static void main(String[] args) {
        // 上界通配符（Upper Bounds Wildcards）上界<? extends T>不能往里存，只能往外取 compile error
        List<? extends Fruit> appList2 = new ArrayList();
//        appList2.add(new Fruit());
//        appList2.add(new Apple());
//        appList2.add(new RedApple());

        //下届通配符
        List<? super Fruit> appList = new ArrayList();
        appList.add(new Fruit());
        appList.add(new Apple());
        appList.add(new RedApple());
    }
}
