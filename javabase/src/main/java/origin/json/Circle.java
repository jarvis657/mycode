package origin.json;

/**
 * @Author:jarvmuqiliu
 * @Date: 2022/5/15
 * @Desc:
 **/
public class Circle extends Shape {

    int radius;

    public static Circle of(int radius) {
        Circle c = new Circle();
        c.setRadius(radius);
        return c;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    @Override
    public String toString() {
        return "Circle{" +
                "radius=" + radius +
                '}';
    }
}

