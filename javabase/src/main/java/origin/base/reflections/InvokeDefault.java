package origin.base.reflections;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

import static java.lang.System.out;


/**
 * 会造成 父类 与子类交叉调用!!!注意
 * 特别注意:
 * out.println("invoker begin =======printinfo");
 * //print info should by default implementation
 * dynamic.printInfo();
 * out.println("=========invoke default==done====");
 * 这里 结果是 default interface ############### :innerclass
 */
public class InvokeDefault {

    public static void main(String... args) throws Throwable {
//        Object target = new Object();
        ExampleMixin target = new ExampleMixin() {
            @Override
            public String getText() {
                return "innerclass";
            }

            @Override
            public void printInfo() {
                out.println(" @@@overide printinfo!!!!!!!!!!!!!!!" + getText());
            }
        };
        Map<String, BiFunction<Object, Object[], Object>> behavior = new HashMap<>();
        ExampleMixin dynamic = (ExampleMixin) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                new Class[]{ExampleMixin.class},
                (Object proxy, Method method, Object[] arguments) -> {

                    //custom mixin behavior
                    if (behavior.containsKey(method.getName())) {
                        return behavior.get(method.getName()).apply(target, arguments);
                        //default mixin behavior
                    } else if (method.isDefault()) {
                        //this block throws java.lang.IllegalAccessException: no private access for invokespecial
                        //这个调用的子类
//                        return MethodHandles.lookup()
//                                .in(method.getDeclaringClass())
////                                .in(method.getDeclaringClass())
//                                .unreflect(method)
//                                .bindTo(target)
//                                .invokeWithArguments();
                        //invoke default
                        Constructor<MethodHandles.Lookup> constructor = MethodHandles.Lookup.class
                                .getDeclaredConstructor(Class.class, int.class);
                        constructor.setAccessible(true);
                        Class<?> declaringClass = method.getDeclaringClass();
                        int allModes = MethodHandles.Lookup.PUBLIC | MethodHandles.Lookup.PRIVATE | MethodHandles.Lookup.PROTECTED | MethodHandles.Lookup.PACKAGE;
                        return constructor.newInstance(declaringClass, allModes)
                                .unreflectSpecial(method, declaringClass)
                                .bindTo(proxy)//影响: 对应的方法里面有其他方法调用 则找 这个bindto对应的
                                .invokeWithArguments(args);
                        //no mixin behavior
                    } else if (ExampleMixin.class == method.getDeclaringClass()) {
                        throw new UnsupportedOperationException(method.getName() + " is not supported");
                        //base class behavior
                    } else {
                        return method.invoke(target, arguments);
                    }
                });

        //define behavior for abstract method getText()
        behavior.put("getText", (o, a) -> o.toString() + " -myText");
        out.println(dynamic.getClass());
        out.println(dynamic.toString());
        out.println(dynamic.getText());
        out.println("invoker begin =======printinfo");
        //print info should by default implementation
        dynamic.printInfo();
        out.println("=========invoke default==done====");

        MethodType type = MethodType.methodType(void.class);
        MethodHandle sayHelloHandle = MethodHandles.lookup().findVirtual(ExampleMixin.class, "printInfo", type);
//        sayHelloHandle.bindTo((ExampleMixin) () -> "vv").invoke();
        sayHelloHandle.bindTo(target).invoke();//这样的话 default不会调用,调用的是override的

        out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        Constructor<MethodHandles.Lookup> constructor = MethodHandles.Lookup.class.getDeclaredConstructor(Class.class, int.class);
        constructor.setAccessible(true);
        int allModes = MethodHandles.Lookup.PUBLIC | MethodHandles.Lookup.PRIVATE | MethodHandles.Lookup.PROTECTED | MethodHandles.Lookup.PACKAGE;
//        Method printInfo = target.getClass().getMethod("printInfo", null);
        Method printInfo = ExampleMixin.class.getMethod("printInfo", null);
        constructor.newInstance(target.getClass(), allModes)
                .unreflectSpecial(printInfo, target.getClass())
                .bindTo(target)
                .invokeWithArguments(args);
        out.println("done+++++++++++++++=");
        out.println("========proxy invoke=================");
        ExampleMixin o = (ExampleMixin) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[]{ExampleMixin.class}, (proxy, method, argas) -> null);
        String text = o.getText();
        out.println("o::::" + text);
        o.printInfo();
    }
}
