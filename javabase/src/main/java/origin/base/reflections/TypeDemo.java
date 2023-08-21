package origin.base.reflections;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * @Author:lmq
 * @Date: 2020/6/1
 * @Desc:
 **/
public class TypeDemo<T> {

    // GenericArrayType：组件类型为类型变量的数组
    public T[] a;
    // GenericArrayType：组件类型为参数化类型的数组
    public List<?>[] b;
    // ParameterizedType：参数化类型
    // List<? extends Object>携带的"? extends Object"
    // 即通配符表达式，也就是WildcardType
    public List<? extends Object> c;
    // Class：普通类型
    public List d;
    // 类型变量
    public T e;


    public static void wildcardType() throws Exception {
        Field field = TypeDemo.class.getDeclaredField("c");
        Type type = field.getGenericType();
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            for (Type actualTypeArg : actualTypeArguments)
                System.out.println(actualTypeArg.getClass());
        }
    }
}
