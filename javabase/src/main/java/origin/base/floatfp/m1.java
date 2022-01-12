package origin.base.floatfp;

/**
 * @Author:lmq
 * @Date: 2021/11/8
 * @Desc:
 **/
// Example of precision control with strictfp
public strictfp class m1 {

    public strictfp static void main(String[] args) {
        testStrictfp();
    }

    public strictfp static void testStrictfp() {
        float aFloat = 0.6666666666666666666f;
        double aDouble = 0.88888888888888888d;
        double sum = aFloat + aDouble;
        System.out.println("sum: " + sum);
    }
//    public static void main(String[] args) {
//        float aFloat = 0.6710339f;
//        double aDouble = 0.04150553411984792d;
//        double sum = aFloat + aDouble;
//        float quotient = (float) (aFloat / aDouble);
//        System.out.println("float: " + aFloat);
//        System.out.println("double: " + aDouble);
//        System.out.println("sum: " + sum);
//        System.out.println("quotient: " + quotient);
//    }
}


