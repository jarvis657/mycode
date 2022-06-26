package dynamic.javassist.existcode;

@Auto(name = "KO", year = 1999)
public class Student extends People implements Earth {

    private int age;
    private String name;

//    public int length(int... args) {
//        return args.length;
//    }

    public Student() {
    }

    public Student(int age, String name) {
        this.age = age;
        this.name = name;
    }

    public int myTest(int a) {
        System.out.println("我在学习。。。。" + a);
        return 6666;
    }

    public String getString(String aa) {
        return "hhahhhhhhhhhhhhhh" + aa;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

