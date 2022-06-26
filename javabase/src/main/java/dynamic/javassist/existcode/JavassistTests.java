package dynamic.javassist.existcode;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.AttributeInfo;
import javassist.bytecode.annotation.Annotation;
import org.junit.Test;
import origin.utils.Jacksons;

/**
 * @Author:lmq
 * @Date: 2022/4/25
 * @Desc: 当然javassist也是有一定的局限性的：
 *         1、泛型、枚举是不支持的，
 *         2、注解的修改也不支持，虽然可以通过底层的javassist类来解决
 *         3、不支持数组的初始化工作，除非数组的容量为1
 *         4、不支持内部类和匿名类
 *         5、不支持continue和break
 *         6、对于复杂的继承也不支持，支持简单的继承关系
 **/
public class JavassistTests {

    /**
     * javassist处理类的基本操作
     */
    @Test
    public void test01() throws Exception {
        Student student = new Student();
        System.out.println(student.getName());
        ClassPool pool = ClassPool.getDefault();
        CtClass cc = pool.get("dynamic.javassist.existcode.Student");
        byte[] bytes = cc.toBytecode(); //获取类的二进制字节码转成byte数组
        String allName = cc.getName();   //获取类的全限定名  dynamic.javassist.existcode.Student
        String name = cc.getSimpleName(); //获取类的简单名   Student
        CtClass superclass = cc.getSuperclass();    //获取类的父类
        CtClass[] interfaces = cc.getInterfaces();  //获取类的父接口
        System.out.println(allName);
        System.out.println(name);
        System.out.println(superclass.getName());
        System.out.println(interfaces[0].getName());
        System.out.println(Arrays.toString(bytes));
    }

    /**
     * 新增方法
     */
    @Test
    public void test02() throws Exception {
        ClassPool pool = ClassPool.getDefault();
        CtClass cc = pool.get("dynamic.javassist.existcode.Student");
        //CtMethod make = CtNewMethod.make("public int add(int a,int b){return a+b;}",cc);  创建方法可以使用这种方法 也可以使用下面的方法
//        public int length(int... args) { return args.length; }
        CtMethod m = CtMethod.make("public int length(int[] args) { return args.length; }", cc);
        m.setModifiers(m.getModifiers());// Modifier.VARARGS == 0x00000080
        cc.addMethod(m);

        CtMethod ctMethod = new CtMethod(CtClass.intType, "add", new CtClass[]{CtClass.intType, CtClass.intType}, cc);
        //new CtMethod 的第一个参数为返回类型，第二个参数是方法名，第三个参数为方法的参数列表（这里没有给参数命名），第四个参数为CtClass类
        ctMethod.setModifiers(Modifier.PUBLIC); //声明方法的访问权限
        ctMethod.setBody("{return $1+$2;}");    //设置方法体 上面没有给参数命名，这里需要使用$符号进行匹配
        /**
         * $0 表示this  $1表示第一个形参   $2 表示第二个形参 以此类推
         * $args 表示的是一个Object[] 将参数列表放入这个数组中  args[0] 对应的就是 $1 args[1] 对应的就是$2 以此类推
         * $$ 所有方法参数的简写，主要用在方法调用上
         * $cflow 一个方法调用的深度，主要用于递归调用上
         * $r 方法返回值的类型
         * $_  方法的返回值（修改方法体时不支持）
         * addCatch() 方法中加入try catch块
         * $e 表示异常对象
         * $class this的类型（Class） 也就是$0的类型
         * $sig 方法参数的类型（Class）数组，数组的顺序为参数的顺序
         *
         */
        cc.addMethod(ctMethod); //将方法添加到类中
        //下面使用java反射来进行调用验证
        Class clazz = cc.toClass();
        Object obj = clazz.newInstance();
        Method declaredMethod = clazz.getDeclaredMethod("add", int.class, int.class);
        Method mm = clazz.getDeclaredMethod("length", int[].class);
        Object invoke = declaredMethod.invoke(obj, 200, 300);
        System.out.println("result:" + (int) invoke);
        Object lg = mm.invoke(obj, new int[]{1, 2, 3, 4, 5, 6});
        System.out.println(lg);
    }

    /**
     * 修改已有的方法
     */
    @Test
    public void test03() throws Exception {
        ClassPool pool = ClassPool.getDefault();
        CtClass cc = pool.get("dynamic.javassist.existcode.Student");
        //根据方法名和参数列表来获取方法
//        CtMethod myTest = cc.getDeclaredMethod("myTest", new CtClass[]{CtClass.intType});
        CtMethod myTest = cc.getDeclaredMethod("myTest", new CtClass[]{CtClass.intType});
        myTest.insertBefore("System.out.println($1 + \" 准备打开书\");"); //在方法前进行插入代码
        myTest.insertAfter("System.out.println(\"已经学完了，出去玩\");"); //在方法后面进行插入代码
//        myTest.insertAt(10, "System.out.println(\"这是在某一行插入的代码！\");"); //在某一行插入代码
        CtMethod getString = cc.getDeclaredMethod("getString", new CtClass[]{pool.get("java.lang.String")});
        getString.insertBefore("System.out.println(\"长度是:\"+$1.length() + \" -------------准备打开书\");"); //在方法前进行插入代码
        getString.insertAfter("System.out.println(\"-------------已经学完了，出去玩\");"); //在方法后面进行插入代码

        //使用反射进行调用测试
        Class clazz = cc.toClass();
        Object obj = clazz.newInstance();
        Method declaredMethod = clazz.getDeclaredMethod("myTest", int.class);
        Object invoke = declaredMethod.invoke(obj, 1111);
        System.out.println("result:" + (int) invoke);

        Method declaredMethod2 = clazz.getDeclaredMethod("getString", String.class);
        Object invoke2 = declaredMethod2.invoke(obj, "zz");
        System.out.println("result:  ----- " + invoke2);
    }

    /**
     * 新增属性并且添加set和get方法
     */
    @Test
    public void test04() throws Exception {
        ClassPool pool = ClassPool.getDefault();
        CtClass cc = pool.get("dynamic.javassist.existcode.Student");
        //CtField make = CtField.make("private String phone;", cc);  可以通过这种方式添加属性，也可以使用下面的方式
        CtField ctField = new CtField(pool.get("java.lang.String"), "phone", cc);
        //new CtField 第一个参数为属性的类型，第二个参数为属性名，第三个参数为CtClass
        ctField.setModifiers(Modifier.PRIVATE);  //设置属性的访问权限
        cc.addField(ctField);                    //将属性添加到方法中

        //CtField phone = cc.getDeclaredField("phone");  这里可以根据属性名来获取属性
        //添加set和get方法 第一个参数为方法名 第二个参数为属性
        cc.addMethod(CtNewMethod.getter("getPhone", ctField));
        cc.addMethod(CtNewMethod.setter("setPhone", ctField));

        //通过反射进行调用测试
        Class clazz = cc.toClass();
        Object obj = clazz.newInstance();
        Method setPhone = clazz.getDeclaredMethod("setPhone", String.class);
        Method getPhone = clazz.getDeclaredMethod("getPhone", null);
        System.out.println(Jacksons.transObjectToString(obj));

        setPhone.invoke(obj, "13088888888");
        Object phone = getPhone.invoke(obj);

        System.out.println(Jacksons.transObjectToString(obj));
        System.out.println(String.valueOf(phone));
    }

    /**
     * 构造方法
     *
     * @throws Exception
     */
    @Test
    public void test05() throws Exception {
        ClassPool pool = ClassPool.getDefault();
        CtClass cc = pool.get("dynamic.javassist.existcode.Student");

        CtConstructor[] constructors = cc.getConstructors();
        for (CtConstructor c : constructors) {
            System.out.println(c.getLongName());//打印构造方法的全类限定名
            c.insertBefore("System.out.println(\"我是构造方法\");"); //当然我们也可以对构造方法进行操作
        }
        Class clazz = cc.toClass();
        Object obj = clazz.newInstance();
    }

    /*
    注解
     */
    @Test
    public void test06() throws Exception {
        ClassPool pool = ClassPool.getDefault();
        CtClass cc = pool.get("dynamic.javassist.existcode.Student");
        Object[] all = cc.getAnnotations();  //获取标注在类上的所有注解
        for (Object a : all) {
            if (a instanceof Auto) {
                Auto b = (Auto) a;
                System.out.println("name : " + b.name() + " year : " + b.year());
            }
        }
    }

    @Test
    public void testAnnotation2() throws Exception {
        // TODO Auto-generated method stub
        ClassPool classPool = ClassPool.getDefault();
        CtClass ctClass = classPool.get("dynamic.javassist.existcode.PersonService");
        //字段添加注解
        CtField ctField = ctClass.getDeclaredField("name");
        List<AttributeInfo> attributeInfos = ctField.getFieldInfo().getAttributes();
        AnnotationsAttribute annotationsAttribute =
                !attributeInfos.isEmpty() ? (AnnotationsAttribute) attributeInfos.get(0)
                        : new AnnotationsAttribute(ctField.getFieldInfo().getConstPool(),
                                AnnotationsAttribute.visibleTag);
        Annotation annotation = new Annotation("com.fasterxml.jackson.annotation.JsonIgnore",
                ctField.getFieldInfo().getConstPool());
        annotationsAttribute.addAnnotation(annotation);
        ctField.getFieldInfo().addAttribute(annotationsAttribute);
        ctClass.writeFile("D:\\javassist\\");
        //class
        System.out.println("==================TEST=================");
        Class<?> afterClass = ctClass.toClass();
        Field field = afterClass.getDeclaredField("name");
        java.lang.annotation.Annotation[] annotation2 = field.getAnnotations();
        for (java.lang.annotation.Annotation annotation3 : annotation2) {
            System.out.println(annotation3);
        }
        ctClass.detach();
    }

    @Test
    public void testInsert() throws Exception {
        ClassPool pool = ClassPool.getDefault();
//        pool.insertClassPath(new ClassClassPath(DynamicCode.class));
        CtClass cc = pool.get("dynamic.javassist.existcode.Point");
        CtMethod m = cc.getDeclaredMethod("move");
//        CtMethod m = cc.getDeclaredMethod("move", new CtClass[]{CtClass.intType,CtClass.intType});
        m.insertBefore("{ System.out.println($1); System.out.println($2); }");
        Class cz = cc.toClass();
        Object o = cz.newInstance();
        Method move = cz.getDeclaredMethod("move", int.class, int.class);
        move.invoke(o, 1, 2);
    }
}
