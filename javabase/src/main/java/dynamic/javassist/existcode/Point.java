package dynamic.javassist.existcode;

/**
 * @Author:lmq
 * @Date: 2022/4/25
 * @Desc:
 **/
public class Point {

    int x, y;

    void move(int dx, int dy) {
        x += dx;
        y += dy;
    }
}
