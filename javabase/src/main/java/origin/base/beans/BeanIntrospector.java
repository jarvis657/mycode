package origin.base.beans;

import static org.junit.Assert.assertTrue;

import com.sun.beans.editors.IntegerEditor;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.beans.PropertyEditor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.DirectFieldAccessor;

/**
 * @Author:jarvmuqiliu
 * @Date: 2022/6/16
 * @Desc:
 **/
public class BeanIntrospector {

    @Test
    public void go_getDescriptor() throws IntrospectionException {
        BeanInfo beanInfo = Introspector.getBeanInfo(User.class);
        System.out.println(beanInfo.getBeanDescriptor());
        System.out.println(Arrays.toString(beanInfo.getMethodDescriptors()));
        System.out.println(Arrays.toString(beanInfo.getPropertyDescriptors()));
    }

    @Test
    public void go_setAge() throws IntrospectionException, InvocationTargetException, IllegalAccessException {
        User userInfo = new User("a", 1);
        String age = "age";
        Object ageValue = 19;
        BeanInfo beanInfo = Introspector.getBeanInfo(User.class);
        PropertyDescriptor[] proDescrtptors = beanInfo.getPropertyDescriptors();
        if (proDescrtptors != null && proDescrtptors.length > 0) {
            for (PropertyDescriptor propDesc : proDescrtptors) {
                if (propDesc.getName().equals(age)) {
                    Method methodSetUserName = propDesc.getWriteMethod();//很重要的原则
                    methodSetUserName.invoke(userInfo, ageValue);
                    Method methodGetUserName = propDesc.getReadMethod();
                    System.out.println(methodGetUserName.invoke(userInfo)); //output:19
                    break;
                }
            }
        }
    }

    @Test
    public void go_setAgeToListener() throws IntrospectionException {
        User userInfo = new User("a", 1);
        String age = "age";
        String ageValue = "19";
        BeanInfo beanInfo = Introspector.getBeanInfo(User.class);
        PropertyDescriptor[] proDescrtptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor propDesc : proDescrtptors) {
            if (propDesc.getName().equals(age)) {
                propDesc.setPropertyEditorClass(IntegerEditor.class);//很重要，也可以自定义
//                PropertyEditor propertyEditor = propDesc.createPropertyEditor(userInfo);
                PropertyEditor propertyEditor = propDesc.createPropertyEditor(null);
                propertyEditor.addPropertyChangeListener(x -> {
                    PropertyEditor source = (PropertyEditor) x.getSource();
                    Method methodSetUserName = propDesc.getWriteMethod();
                    try {
                        System.out.println("ChangeListener --> newValue:" + source.getValue());
                        methodSetUserName.invoke(userInfo, source.getValue());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                propertyEditor.setAsText(ageValue);
                break;
            }
        }
        System.out.println(userInfo);
    }

    //BeanWrapperImpl
    @Test
    public void setterDoesNotCallGetter() {
        GetterBean target = new GetterBean();
        BeanWrapper accessor = new BeanWrapperImpl(target);
        accessor.setPropertyValue("name", "tom");
        assertTrue("Set name to tom", target.getName().equals("tom"));
    }

    //DirectFieldAccessor
    @Test
    public void setterDoesNotCallGetter2() {
        TestBean bean = new TestBean() {
            @SuppressWarnings("unused")
            String name = "alex";
        };
        //嵌套设置/访问对象字段数据
        DirectFieldAccessor accessor = new DirectFieldAccessor(bean);
        //如果嵌套对象为null，字段创建
        accessor.setAutoGrowNestedPaths(true);
        //设置字段值
        accessor.setPropertyValue("name", "zhangsan");
        //读取字段值
        System.out.println(accessor.getPropertyValue("name"));
    }

    private static class TestBean {

        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    private static class GetterBean {

        private String name;

        public String getName() {
            if (this.name == null) {
                throw new RuntimeException("name property must be set");
            }
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
