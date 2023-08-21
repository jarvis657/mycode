package origin.spring.ioc.factorys;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author:lmq
 * @Date: 2020/2/26
 * @Desc:
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Car {
    private   int maxSpeed ;
    private  String brand ;
    private   double price ;
}
