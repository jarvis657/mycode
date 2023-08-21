package origin.base.reflections;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

/**
 * 你会发现每一个类型参数都被保留了，而且在运行期可以通过反射机制获取到。那么到底什么是 “类型擦除”？至少某些东西被擦除了吧？是的。事实上，除了结构化信息外的所有东西都被擦除了 —— 这里结构化信息是指与类结构相关的信息，而不是与程序执行流程有关的。换言之，与类及其字段和方法的类型参数相关的元数据都会被保留下来，可以通过反射获取到
 *
 * @Author:lmq
 * @Date: 2020/5/29
 * @Desc:
 **/
public class GetGenerics<T> {
    private byte value;
    private T t;

    public int compareTo(Byte that) {
        return this.value;
    }

//    //这是泛型方法 生成的桥方法
//    public int compareTo(Object that) {
//        return this.compareTo((Byte) that);
//    }

    public int compareTo(T t){
        return 1;
    }

    public static void main(String[] args) throws Exception {
        GetGenerics<List> listGetGenerics = new GetGenerics<List>();
        listGetGenerics.test();
    }

    public void test() throws Exception {
        ParameterizedType type = (ParameterizedType) Bar.class.getGenericSuperclass();
        System.out.println(type.getActualTypeArguments()[0]);

        ParameterizedType fieldType = (ParameterizedType) Foo.class.getField("children").getGenericType();
        System.out.println(fieldType.getActualTypeArguments()[0]);

        ParameterizedType paramType = (ParameterizedType) Foo.class.getMethod("foo", List.class).getGenericParameterTypes()[0];
        System.out.println(paramType.getActualTypeArguments()[0]);

        System.out.println(Foo.class.getTypeParameters()[0].getBounds()[0]);
    }

    class Foo<E extends CharSequence> {
        public List<Bar> children = new ArrayList<Bar>();

        public List<StringBuilder> foo(List<String> foo) {
            return null;
        }

        public void bar(List<? extends String> param) {
        }
    }

    class Bar extends Foo<String> {
    }
}

