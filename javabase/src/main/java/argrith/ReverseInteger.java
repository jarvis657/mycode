package argrith;

/**
 * @Author:lmq
 * @Date: 2020/3/1
 * @Desc:
 **/
public class ReverseInteger {
    public static int reverse(int i) {
        int result = 0;
        while (i != 0) {
            result = (i % 10) + result * 10;
            i = i / 10;
        }
        return result;
    }

    public static void main(String[] args) {
//        String a = "1534236469";
//        String reverse = StringUtils.reverse(a);
//        System.out.println(reverse);
        int reverse = reverse(1534236469);
        if(reverse >= Integer.MIN_VALUE && reverse <= Integer.MAX_VALUE){
            System.out.println((int)reverse);
        }else {
            System.out.println("vvvvvvvvv");
        }
        System.out.println(reverse);
        int i = 2147483645;
        System.out.println(i++);
        System.out.println(i++);
        System.out.println(i++);
        System.out.println(i++);
        System.out.println(i++);
        System.out.println(i++);
        System.out.println(i++);
        System.out.println(i++);
        System.out.println(i++);
        System.out.println(i++);
        System.out.println(i++);
        System.out.println(i++);
    }
}
