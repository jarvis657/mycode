package origin.base.inherits.annotations;

import org.springframework.boot.context.properties.source.ConfigurationPropertyName;

import java.util.ArrayList;
import java.util.List;

public class MyInheritedClass extends InheritedBase {
    public static void main(String[] args) {
        String name = String.format("management.endpoint.%s.cache.time-to-live", "refresh");
        ConfigurationPropertyName of = ConfigurationPropertyName.of(name);
        List<String> as  = new ArrayList<>();
        System.out.println(of);
    }
}
