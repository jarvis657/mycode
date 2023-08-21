package origin.base.inherits;

/**
 * @Author:lmq
 * @Date: 2020/8/3
 * @Desc:
 **/
public class TestBridgeMethod {

    public static class A<T> {
        public T getT() {
            return null;
        }
    }

    public static class B extends A<String> {
        public String getT() {
            return null;
        }
    }
}
