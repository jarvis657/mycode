package origin.aspect;

/**
 * @Author:qishan
 * @Date: 2019-08-20
 * @Desc:
 **/
public class Point {

    public Point() {
    }

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    private double x;
    private double y;
//    private String name;

//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
}
