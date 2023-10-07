package java21;

/**
 * @Author:jarvis
 * @Date: 2023/9/26
 * @Desc:
 **/
public class ScopedValueExample {

    final static ScopedValue<String> LoginUser = ScopedValue.newInstance();

    /**
     * 使用 ScopedValue.newInstance()声明了一个 ScopedValue，用 ScopedValue.where给 ScopedValue设置值，并且使用 run
     * 方法执行接下来要做的事儿，这样一来，ScopedValue就在 run() 的内部随时可获取了，在run方法中，模拟调用了一个service
     * 的login方法，不用传递LoginUser这个参数，就可以直接通过LoginUser.get方法获取当前登录用户的值了。
     *
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        ScopedValue.where(LoginUser, "张三").run(() -> {
            new Service().login();
        });

        Thread.sleep(2000);
    }

    static class Service {

        void login() {
            System.out.println("当前登录用户是：" + LoginUser.get());
        }
    }
}
