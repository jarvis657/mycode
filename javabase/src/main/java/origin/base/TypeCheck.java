package origin.base;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TypeCheck {

    public static void main(String[] args) {
        // Number "extends" Number (in this context)

        List<? extends Number> foo3 = new ArrayList<Number>();
        // Integer extends Number
        List<? extends Number> foo4 = new ArrayList<Integer>();

        // Double extends Number
        List<? extends Number> foo5 = new ArrayList<Double>();

        System.out.println(
                ((ParameterizedType) MyStringSubClass.class.getGenericSuperclass()).getActualTypeArguments()[0]);
        System.out.println(MyExtend.class.getGenericSuperclass());
        List<String> aa = new ArrayList<>();
        aa.add("avs");
        System.out.println(aa.getClass().getTypeParameters()[0]);
    }

    class MyGenericClass<T> {

    }

    class MyStringSubClass extends MyGenericClass<String> {

    }

    static class MyClass {

    }

    class MyExtend extends MyClass {

    }
}
