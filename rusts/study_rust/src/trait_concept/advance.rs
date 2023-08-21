use std::fmt::Arguments;
use std::ops::Add;

use crate::base_concept::iters::Counter;

pub trait CustomIterator {
    type Item;
    //占坑类型，由具体实施指定
    fn next(&mut self) -> Option<Self::Item>; //关联类型，占坑类型放在特性方法里
}

impl CustomIterator for Counter {
    type Item = u32;

    fn next(&mut self) -> Option<Self::Item> {
        self.count += 1;
        if self.count < 6 {
            Some(self.count)
        } else {
            None
        }
    }
}

#[derive(Debug, PartialEq)] //assert_eq
struct Point {
    x: i32,
    y: i32,
}

//add定义
//trait Add<RHS=Self> {  //缺省类型参数，RHS代表加号右边类型
//    type Output; //占坑类型
//    fn add(self, rhs: RHS) -> Self::Output;  //关联类型
//}
impl Add for Point {
    //Add<Self>
    type Output = Point;
    fn add(self, other: Point) -> Point {
        //仍需指定类型
        Point {
            x: self.x + other.x,
            y: self.y + other.y,
        }
    }
}

struct Millimeters(u32);

//tuple struct
struct Meters(u32);

impl Add<Meters> for Millimeters {
    //rhs = Meters，就少写一个<>?
    type Output = Millimeters;

    fn add(self, other: Meters) -> Millimeters {
        Millimeters(self.0 + (other.0 * 1000))
    }
}

#[test]
fn test_ad() {
    trait Pilot {
        fn fly(&self); //self, 方法
    }
    trait Wizard {
        fn fly(&self);
    }

    struct Human;

    impl Pilot for Human {
        fn fly(&self) {
            println!("pilot This is your captain speaking.");
        }
    }

    impl Wizard for Human {
        fn fly(&self) {
            println!("wizard Up!");
        }
    }

    impl Human {
        fn fly(&self) {
            println!("*human waving arms furiously*");
        }
    }

    let person = Human;
    person.fly(); //缺省调用结构自己的方法
    Human::fly(&person); //等价
    Pilot::fly(&person);
    Wizard::fly(&person);

    trait Animal {
        fn baby_name() -> String; //no self, associated function
    }
    struct Dog;
    impl Dog {
        fn baby_name() -> String {
            // no self, associated function
            String::from("Spot")
        }
    }
    impl Animal for Dog {
        fn baby_name() -> String {
            String::from("puppy")
        }
    }
    println!("A baby dog is called a {}", Dog::baby_name());
    //调用结构自己的关联函数，spot
    //    println!("A baby dog is called a {}", Animal::baby_name());
    //编译错误，不知道使用哪个具体实施
    println!("A baby dog is called a {}", <Dog as Animal>::baby_name());
    // FQSD, 正确

    //    /想如下打印Point, 使用Display
    //**********
    //*        *
    //* (1, 3) *
    //*        *
    //**********
    use std::fmt;

    trait OutlinePrint: fmt::Display {
        //特性的特性绑定，实施OutlinePrint的类型必须也实行了Display
        fn outline_print(&self) {
            let output = self.to_string(); //绑定Display的都实施to_string
            let len = output.len();
            println!("{}", "*".repeat(len + 4));
            println!("*{}*", " ".repeat(len + 2));
            println!("* {} *", output);
            println!("*{}*", " ".repeat(len + 2));
            println!("{}", "*".repeat(len + 4));
        }
    }
    impl OutlinePrint for Point {}
    impl fmt::Display for Point {
        //Point必须实施Display
        fn fmt(&self, f: &mut fmt::Formatter) -> fmt::Result {
            write!(f, "({}, {})", self.x, self.y)
        }
    }

    //使用tuple struct包装
    struct Wrapper(Vec<String>); //newtype
    impl fmt::Display for Wrapper {
        //可以，Wrapper是本地
        fn fmt(&self, f: &mut fmt::Formatter) -> fmt::Result {
            write!(f, "[{}]", self.0.join(", ")) //SliceConcatExt::join, nightly?
        }
    }
    let w = Wrapper(vec![String::from("hello"), String::from("world")]);
    println!("w = {}", w); //hello, world
                           //newtype Wapper<Vec<T>>不能使用Vec的方法，可以实施deref或单个方法

    //长类型简称
    type Thunk = Box<dyn Fn() + Send + 'static>; //closure特性对象

    let f: Thunk = Box::new(|| println!("hi"));
    //let f: Box<dyn Fn() + Send + 'static> = Box::new(|| println!("hi"));

    fn takes_long_type(f: Thunk) {
        //fn takes_long_type(f: Box<dyn Fn() + Send + 'static>) {
        // --snip--
    }

    fn returns_long_type() -> Thunk {
        //fn returns_long_type() -> Box<dyn Fn() + Send + 'static> {
        // --snip--
        Box::new(|| ())
    }

    fn bar(flag: bool) {
        //返回never, diverging function, 岔路函数？
        if flag {
            println!("nve")
        } else {
            println!("nve")
        }
    }
    //every trait is DST，特性是动态尺寸类型
    //所以特性对象必须用指针包装，&dyn Trait，Box<dyn Trait>, Rc<dyn, Trait>, etc.

    //Sized特性
    //所有编译时已知的类型都自动实施Sized
    //所有泛型函数都自动绑定Sized
    fn genericone<T>(t: T) {
        //fn generic<T: Sized>(t: T) { //rust变成这样
        // --snip--
    }
    //很重要的概念，所以rust编译时把泛型具体化，early binding, static dispatch
    //object safety也和DST密切相关

    //使用不确定大小参数
    fn generic<T: ?Sized>(t: &T) { //Sized专用。因为可能是DST, T必须是指针
                                   // --snip--
    }
}
