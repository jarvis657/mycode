package origin.spring.infra.bean.processors;

/**
 * @Author:jarvmuqiliu
 * @Date: 2022/6/16
 * @Desc:
 **/
public class BeanTest {

    private String name;

    public BeanTest() {
        System.out.println("执行构造函数");
    }

    public BeanTest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}


