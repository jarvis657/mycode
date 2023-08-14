package origin.jdk8.funcs;

/**
 * @Author:jarvis
 * @Date: 2023/8/2
 * @Desc:
 **/
interface MyInterface {

    // 抽象方法
    void abstractMethod();

    // 默认方法
    default void defaultMethod() {
        System.out.println("===>>>"+this.getClass().getName());
        System.out.println("This is a default method.");
    }
}

class MyClass implements MyInterface {

    @Override
    public void abstractMethod() {
        System.out.println("Implementing abstractMethod() in MyClass.");
    }
}

class MyClass2 implements MyInterface {

    @Override
    public void abstractMethod() {
        System.out.println("Implementing abstractMethod() in MyClass.");
    }
}

/**
 * @description:
 * @author: shu
 * @createDate: 2023/7/1 9:29
 * @version: 1.0
 */
public class Interfaces {

    public static void main(String[] args) {
        MyClass obj = new MyClass();
        obj.abstractMethod();
        obj.defaultMethod();
        MyClass2 myClass2 = new MyClass2();
        myClass2.defaultMethod();
    }

}
