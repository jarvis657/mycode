use pin_project::pin_project;

#[pin_project]
#[derive(Debug)]
struct Test {
    #[pin]
    a: String,
    #[pin]
    b: *const String,
}

impl Test {
    fn new(txt: &str) -> Self {
        //栈对象
        Test {
            a: String::from(txt),
            b: std::ptr::null(),
        }
    }
    fn init(&mut self) {
        let self_ref: *const String = &self.a;
        self.b = self_ref;
    }
    fn a(&self) -> &str {
        &self.a
    }
    fn b(&self) -> &String {
        unsafe { &*(self.b) }
    }
}

// 这个代码例子充分说明了`self-referential structs` 引发的严重bug.其实在c/c++中也是一样的， 结构体中一个指针field却指向本结构体中的某个其他field，表面看没什么问题，但是一旦此结构体被move到其他内存位置，如下面代码中的swap, 这种按位copy的方式不会将结构体中的指针更新为新内存位置！这也就是为什么Cpp提供移动构造函数等语言特性！而Rust语言默认语义是Move, 而且是按位copy方式， 所以出现上面的问题。
//分析: 这个问题 是因为b属性 是因为 是指针数据, 按bit copy(std::mem:swap) 指针数据指向还是不会变
pub fn test_main() {
    let mut test1 = Test::new("test1");
    test1.init();
    let mut test2 = Test::new("test2");
    test2.init();
    let c = Box::new(['c'; 23]);//23个c
    let option = c.get(0);
    let option2 = c.get(1);
    let option3 = c.get(22);
    println!("c:={},0={},1={},22={}", c.len(), option.unwrap(), option2.unwrap(), option3.unwrap());

    println!("test1 a: {}, b: {}", test1.a(), test1.b());
    println!("test2 a: {}, b: {}", test2.a(), test2.b());
    std::mem::swap(&mut test1, &mut test2); //按bit 直接复制交换。
    println!("had swapd");
    println!("test1 a: {}, b: {}", test1.a(), test1.b());
    println!("test2 a: {}, b: {}", test2.a(), test2.b());
    test1.a = "I've totally changed now!".to_string();
    println!("test1 a: {}, b: {}", test1.a(), test1.b());
    println!("test2 a: {}, b: {}", test2.a(), test2.b()); //证明指针test2.b仍然指向test1.a位置，但是他应该指向test2.a才对的！！！
}
