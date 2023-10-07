/**
 * @author muqi.lmq
 * @date 2018/7/29.
 */
module gui {
    requires java.util.concurrent;
    //指令用于指定一个模块中哪些包下的public对外是可访问的，包括直接引入和反射使用
    exports com.jdt.a.person;
    // 只能被反射调用，用于指定某个包下所有的 public 类都只能在运行时可被别的模块进行反射，并且该包下的所有的类及其乘员都可以通过反射进行访问。
    opens com.jdt.a.refect;
}
