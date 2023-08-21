package origin.base.reflections;

import java.lang.invoke.*;

/**
 * @Author:lmq
 * @Date: 2020/4/16
 * @Desc:
 **/
public class MethodHandleTest {

    class GrandFather {
        void thinking() {
            System.out.println("I 'm grandFather!");
        }
    }

    class Father extends GrandFather {
        void thinking() {
            System.out.println("I 'm father!");
        }
    }

    class Son extends Father {
        void thinking() {
            //实现祖父类的thinking(),打印 I 'm grandFather
            MethodType mt = MethodType.methodType(void.class);
            try {
                MethodHandle md = MethodHandles.lookup().findSpecial(GrandFather.class, "thinking", mt, this.getClass());
                md.invoke(this);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * invokevirtual——对实例方法的标准分派
     * invokestatic——用于分派静态方法
     * invokeinterface——用于通过接口进行方法调用的分派
     * invokespecial——当需要进行非虚（也就是“精确”）分派时会用到
     *
     * @param args
     * @throws Throwable
     */
    public static void main(String[] args) throws Throwable {
//        MethodHandleTest.Son son = new MethodHandleTest().new Son();
//        son.thinking();
        constantCallSite();
        System.out.println("done constant-----");
        useMutableCallSite();
    }

    public static void constantCallSite() throws Throwable {
        MethodType type = MethodType.methodType(String.class, int.class, int.class);
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        MethodHandle handle = lookup.findVirtual(String.class, "substring", type);
        //这样也可以
//        Object hello = handle.invoke("Hello", 2, 3);
//        System.out.println(hello);
        ConstantCallSite callSite = new ConstantCallSite(handle);
        MethodHandle invoker = callSite.dynamicInvoker();
        String result = (String) invoker.invoke("Hello", 2, 3);
        System.out.println(result);
    }

    /**
     * MutableCallSite 允许对其所关联的目标方法句柄通过setTarget方法来进行修改。
     * 以下为 创建一个 MutableCallSite，指定了方法句柄的类型，则设置的其他方法也必须是这种类型。
     * MutableCallSite.syncAll() 提供了方法来强制要求各个线程中 MutableCallSite 的使用者立即获取最新的目标方法句柄。
     * 但这个时候也可以选择使用 VolatileCallSite。VolatileCallSite
     * 作用与 MutableCallSite 类似，不同的是它适用于多线程情况，用来保证对于目标方法句柄所做的修改能够被其他线程看到。
     */
    public static void useMutableCallSite() throws Throwable {
        MethodType type = MethodType.methodType(int.class, int.class, int.class);
        MutableCallSite callSite = new MutableCallSite(type);
        MethodHandle invoker = callSite.dynamicInvoker();

        MethodHandles.Lookup lookup = MethodHandles.lookup();
        MethodHandle maxHandle = lookup.findStatic(Math.class, "max", type);
        callSite.setTarget(maxHandle);
        //需要考虑的是多线程情况下的可见性问题。有可能在一个线程中对MutableCallSite的目标方法句柄做了修改，而在另外一个线程中不能及时看到这个变化。对于这种情况，MutableCallSite提供了一个静态方法syncAll来强制要求各个线程中MutableCallSite的使用者立即获取最新的目标方法句柄。该方法接收一个MutableCallSite类型的数组作为参数。
        //如果一个目标方法句柄可变的调用点被设计为在多线程的情况下使用，可以直接使用VolatileCallSite，而不使用MutableCallSite。当使用VolatileCallSite的时候，每当目标方法句柄发生变化的时候，其他线程会自动看到这个变化。这与Java中volatile关键词的语义是一样的。这比使用MutableCallSite再加上syncAll方法要简单得多。除了这一点之外，VolatileCallSite的作用与MutableCallSite完全相同。
        MutableCallSite.syncAll(new MutableCallSite[]{callSite});

        int result = (int) invoker.invoke(3, 5);
        System.out.println(result == 5);

        MethodHandle minHandle = lookup.findStatic(Math.class, "min", type);
        callSite.setTarget(minHandle);
        result = (int) invoker.invoke(3, 5);
        System.out.println(result == 3);
    }


    public static CallSite bootstrap(MethodHandles.Lookup lookup, String name, MethodType type, String value) throws Exception {
        MethodHandle mh = lookup.findVirtual(String.class, "toUpperCase", MethodType.methodType(String.class)).bindTo(value);
        return new ConstantCallSite(mh);
    }
}
