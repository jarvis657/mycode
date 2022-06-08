package origin.json;

/**
 * @Author:jarvmuqiliu
 * @Date: 2022/5/15
 * @Desc:
 **/
public class Rectangle extends Shape {

    private int w;
    private int h;

    public static Rectangle of(int w, int h) {
        Rectangle r = new Rectangle();
        r.setW(w);
        r.setH(h);
        return r;
    }

    public int getW() {
        return w;
    }

    public void setW(int w) {
        this.w = w;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }

    @Override
    public String toString() {
        return "Rectangle{" +
                "w=" + w +
                ", h=" + h +
                '}';
    }
}
