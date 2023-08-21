package origin.spring.ioc.test.beans;

import org.springframework.stereotype.Component;

/**
 * @Author:lmq
 * @Date: 2020/5/29
 * @Desc:
 **/
@Component
public class User {
   private String name;

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }
}
