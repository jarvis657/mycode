package origin.base;

/**
 * @Author:lmq
 * @Date: 2020/8/10
 * @Desc:
 **/
public class CheckExceptions {
    public static void main(String[] args) {
        RuntimeException runtimeException = new RuntimeException();
        E1 e1 = new E1();
        EE1 ee1 = new EE1();
        System.out.println(E1.class.isInstance(ee1));
        System.out.println(e1.getClass().isInstance(ee1));
        System.out.println(e1.getClass().isAssignableFrom(ee1.getClass()));
    }

    public static class E1 extends RuntimeException {
        public E1() {
        }

        public E1(String message) {
            super(message);
        }
    }

    public static class EE1 extends E1 {
        public EE1() {
        }

        public EE1(String message) {
            super(message);
        }
    }
}
