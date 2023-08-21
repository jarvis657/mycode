package origin.base;


import java.lang.reflect.Constructor;

/**
 * @Author:lmq
 * @Date: 2019/9/15
 * @Desc:
 **/
public class Singleton {
    private Singleton() {
        System.out.println("singleton##############inner con");
    }

    private static class LazyHolder {
        private static final Singleton INSTANCE = new Singleton();

        private LazyHolder() {
            System.out.println("lazy_holder##############inner con");
        }
    }

    public static Singleton getInstance() {
        return LazyHolder.INSTANCE;
    }

    private volatile static Singleton singleton = null;
//    public static Singleton getInstance() {
//        if (singleton == null) {
//            synchronized (Singleton.class) {
//                if (singleton == null) {
//                    singleton = new Singleton();
//                }
//            }
//        }
//        return singleton;
//    }

    private enum SingletonEnum {
        INSTANCE;

        SingletonEnum() {
            System.out.println("init singleton enum");
        }
    }

    public static void main(String[] args) {
        try {
//            //获得构造器
//            Constructor con = Singleton.class.getDeclaredConstructor();
//            //设置为可访问
//            con.setAccessible(true);
//            //构造两个不同的对象
//            Singleton singleton1 = (Singleton) con.newInstance();
//            Singleton singleton2 = (Singleton) con.newInstance();
//            //验证是否是不同对象
//            System.out.println(singleton1);
//            System.out.println(singleton2);
//            System.out.println(singleton1.equals(singleton2));
            System.out.println("............................");
            Singleton instance = Singleton.getInstance();
            instance = Singleton.getInstance();
            instance = Singleton.getInstance();
            instance = Singleton.getInstance();
            System.out.println(".........done.............");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
