package origin.aspect;

import org.junit.Test;

/**
 * @Author:qishan
 * @Date: 2019-08-20
 * @Desc:
 **/
public class TestPointAspect {

    @Test
    public void test() {
        Point point = new Point(1,1);
//        point.setName("test");
        point.setX(12);
        point.setY(123);
        System.out.println(point);
    }
}

