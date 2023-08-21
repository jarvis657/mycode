package origin.spring.ioc.factorys;

import org.junit.Test;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @Author:lmq
 * @Date: 2020/2/26
 * @Desc:
 **/
@Component
public class CarBean implements FactoryBean<Car> {
    private String carInfo;

    public CarBean() {
        this.carInfo = "car factory自身";
    }

    public Car getObject() throws Exception {
        System.out.println("=============init================");
        Car car = new Car();
        String[] infos = carInfo.split(",");
        car.setBrand(infos[0]);
        car.setMaxSpeed(Integer.valueOf(infos[1]));
        car.setPrice(Double.valueOf(infos[2]));
        return car;
    }

    public Class<Car> getObjectType() {
        return Car.class;
    }

    public boolean isSingleton() {
        return false;
    }

    public String getCarInfo() {
        return this.carInfo;
    }

    // 接受逗号分割符设置属性信息
    public void setCarInfo(String carInfo) {
        this.carInfo = carInfo;
    }
    @Autowired
    private ApplicationContext context;
    @Test
    public void test() {
        CarBean myBean1 = (CarBean) context.getBean("carBean");
        CarBean myBean2 = (CarBean) context.getBean("&carBean");//car factory自身
    }
}
