package origin.json;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * @Author:jarvmuqiliu
 * @Date: 2022/5/15
 * @Desc:
 **/
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@className")
public abstract class Shape {

}
