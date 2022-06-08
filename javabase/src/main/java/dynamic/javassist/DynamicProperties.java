package dynamic.javassist;


import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import javassist.CannotCompileException;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.NotFoundException;
import org.springframework.beans.BeanUtils;
import origin.utils.Jacksons;

/**
 * @Author:lmq
 * @Date: 2022/3/9
 * @Desc:
 **/
public class DynamicProperties {

    private static Map<String, Class> classMap = new ConcurrentHashMap<>();

    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 10; i++) {
            MyClass<Integer> mc = new MyClass<>();
            mc.setTotal(100);
            mc.setData(Arrays.asList(1, 2, 3));
////        List<Integer> mc = new ArrayList<>();
////        mc.add(1);
////        mc.add(2);
////        mc.add(3);
            Map<String, Class<?>> ps = new HashMap<>();
            ps.put("name", String.class);
            ps.put("age", Integer.class);
            ps.put("elementData", Object.class);
            Map<String, Object> vm = new HashMap<>();
            vm.put("name", "lmq" + i);
            vm.put("age", i);
            vm.put("elementData", null);
//        try {
            Object extendData = DynamicProperties.extendData(mc, ps, vm);
            System.out.println(Jacksons.transObjectToString(extendData));

        }
//            String s = Jacksons.transObjectToString(o);
//            System.out.println(s);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    public static Object extendData(Object data, Map<String, Class<?>> properties, Map<String, Object> values)
            throws Exception {
        Class<?> extendClass = null;
        String className = data.getClass().getName() + "Extend";
        if (classMap.get(className) != null) {
            extendClass = classMap.get(className);
        } else {
            extendClass = innerGen(className, data.getClass(), properties);
        }
        Object o = extendClass.newInstance();
        BeanUtils.copyProperties(data, o);
        Class<?> finalExtend = extendClass;
        properties.forEach((property, type) -> {
            try {
                finalExtend.getMethod(propertyMethodName("set", property), type).invoke(o, values.get(property));
            } catch (Exception e) {
                throw new RuntimeException("aa", e);
            }
        });
        return o;
    }

    private static Class<?> innerGen(String className, Class<?> superClass, Map<String, Class<?>> properties) {
        return (Class<?>) classMap.computeIfAbsent(className, d -> {
            try {
                return generate(className, superClass, properties);
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        });
    }

    /**
     * 生成Calss
     *
     * @param className class名
     * @param properties 要动态扩展的属性
     * @return 返回生成的扩展类
     * @throws Exception 生成中出现异常
     */
    private static Class<?> generate(String className, Class<?> superClass, Map<String, Class<?>> properties)
            throws Exception {
        ClassPool pool = ClassPool.getDefault();
        pool.insertClassPath(new ClassClassPath(superClass));
        CtClass cc = pool.makeClass(className);
        cc.setSuperclass(resolveCtClass(pool, superClass));
        // add this to define an interface to implement
        cc.addInterface(resolveCtClass(pool, Serializable.class));

        for (Entry<String, Class<?>> entry : properties.entrySet()) {
            cc.addField(new CtField(resolveCtClass(pool, entry.getValue()), entry.getKey(), cc));
            // add getter
            cc.addMethod(generateGetter(cc, entry.getKey(), entry.getValue()));
            // add setter
            cc.addMethod(generateSetter(cc, entry.getKey(), entry.getValue()));
        }
        return cc.toClass();
    }

    /**
     * 生成get方法
     *
     * @param declaringClass 声明类
     * @param fieldName 属性名
     * @param fieldClass 属性类别
     * @return 属性setter方法
     * @throws CannotCompileException 编译异常
     */
    private static CtMethod generateGetter(CtClass declaringClass, String fieldName, Class fieldClass)
            throws CannotCompileException {
        String getterName = propertyMethodName("get", fieldName);
        String method = String.format("public %s %s(){return this.%s;}}", fieldClass.getName(), getterName, fieldName);
        return CtMethod.make(method, declaringClass);
    }

    /**
     * 生成set方法
     *
     * @param declaringClass 声明类
     * @param fieldName 属性名
     * @param fieldClass 属性类别
     * @return 属性setter方法
     * @throws CannotCompileException 编译异常
     */
    private static CtMethod generateSetter(CtClass declaringClass, String fieldName, Class<?> fieldClass)
            throws CannotCompileException {
        String setterName = propertyMethodName("set", fieldName);
        String method = String.format("public void %s(%s %s){this.%s=%s;}", setterName, fieldClass.getName(), fieldName,
                fieldName, fieldName);
        return CtMethod.make(method, declaringClass);
    }

    /**
     * 属性方法的getter or setter
     *
     * @param getSet get or set
     * @param fieldName 属性名
     * @return 返回geterr or setter
     */
    private static String propertyMethodName(String getSet, String fieldName) {
        return getSet + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
    }

    private static CtClass resolveCtClass(ClassPool pool, Class<?> clazz) throws NotFoundException {
        return pool.get(clazz.getName());
    }
}

