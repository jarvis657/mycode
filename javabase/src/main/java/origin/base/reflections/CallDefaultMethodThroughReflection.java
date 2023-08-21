package origin.base.reflections;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @Author:lmq
 * @Date: 2020/6/28
 * @Desc:
 **/
interface PrivateInaccessible {
    default void quack() {
        System.out.println("################# -> PrivateInaccessible.quack()");
    }
}

public class CallDefaultMethodThroughReflection {
    interface PrivateAccessible {
        default void quack() {
            System.out.println(" ->@@@@@@@@@@ PrivateAccessible.quack()");
        }
    }

    public static void main(String[] args) {
        System.out.println("PrivateAccessible");
        System.out.println("-----------------");
        System.out.println();
        proxy(PrivateAccessible.class).quack();

        System.out.println();
        System.out.println("PrivateInaccessible");
        System.out.println("-------------------");
        System.out.println();
        proxy(PrivateInaccessible.class).quack();
    }

    private static void quack(MethodHandles.Lookup lookup, Class<?> type, Object proxy) {
        System.out.println("Lookup.in(type).unreflectSpecial(...)");

        try {
            lookup.in(type)
                    .unreflectSpecial(type.getMethod("quack"), type)
                    .bindTo(proxy)
                    .invokeWithArguments();
        } catch (Throwable e) {
            System.err.println(" -> " + e.getClass() + ": " + e.getMessage());
        }

        System.out.println("Lookup.findSpecial(...)");
        try {
            lookup.findSpecial(type, "quack", MethodType.methodType(void.class, new Class[0]), type)
                    .bindTo(proxy)
                    .invokeWithArguments();
        } catch (Throwable e) {
            System.err.println(" -> " + e.getClass() + ": " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> T proxy(Class<T> type) {
        return (T) Proxy.newProxyInstance(
                Thread.currentThread().getContextClassLoader(),
                new Class[]{type},
                (Object proxy, Method method, Object[] arguments) -> {
                    System.out.println("MethodHandles.lookup()");
                    quack(MethodHandles.lookup(), type, proxy);

                    try {
                        System.out.println();
                        System.out.println("Lookup(Class)");
                        Constructor<MethodHandles.Lookup> constructor = MethodHandles.Lookup.class.getDeclaredConstructor(Class.class);
                        constructor.setAccessible(true);
                        constructor.newInstance(type);
                        quack(constructor.newInstance(type), type, proxy);
                    } catch (Exception e) {
                        System.err.println(" -> " + e.getClass() + ": " + e.getMessage());
                    }
//                    try {
//                        System.out.println();
//                        System.out.println("MethodHandles.privateLookupIn()");
//                        quack(MethodHandles.privateLookupIn(type, MethodHandles.lookup()), type, proxy);
//                    }
//                    catch (Error e) {
//                        System.out.println(" -> " + e.getClass() + ": " + e.getMessage());
//                    }

                    return null;
                }
        );
    }
}
