package dynamic.javassist;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.List;
import origin.json.JacksonSerializers;
import origin.utils.Jacksons;

/**
 * @Author:lmq
 * @Date: 2022/3/9
 * @Desc:
 **/
public class MyClass<T> {

    private Integer total;

//    @JsonSerialize(using = JacksonSerializers.AdcodeSerializer.class)
    private List<T> data;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
