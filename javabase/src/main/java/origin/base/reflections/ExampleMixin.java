package origin.base.reflections;

import static java.lang.System.out;

/**
 * @Author:lmq
 * @Date: 2020/6/28
 * @Desc:
 **/
public interface ExampleMixin {

    String getText();

    default void printInfo() {
        out.println("default interface ############### :" + getText());
    }
}
