use std::cell::{Cell, RefCell};
use std::mem::replace;
use std::rc::{Rc, Weak};
use std::thread;

#[derive(Debug)]
struct User<'a> {
    username: &'a str,
    email: &'a str,
    sign_in_count: u64,
    active: bool,
}

#[test]
fn main_test_own_ship() {
    let user1 = User {
        email: "someone@example.com",
        username: "someusername123",
        active: true,
        sign_in_count: 1,
    };
    // 编译通过，4、5两行交换就编译不过。
    let mut i = 0;
    let r = &mut i;
    let rf: &mut u32 = r; // 和 let rf = r; 行为不同。
    *rf = 3;
    *r = 2;
}

#[test]
fn ship_main() {
    let b = foo("world");
    println!("b:{}", b);
}

fn foo(x: &str) -> String {
    let a = "Hello, ".to_string() + x;
    a
}
// 闭包会根据闭包内使用需求, 采取最小化的动作, borrow, 然后是 mut borrow, 最后才是 move:
//
// foo_ref(&var) 是 borrow
// foo_mut(&mut var) 是 mut borrow
// var; 是 move, foo(var) 也是 move;
//
// 是否 Copy, 和上面的行为正交, 影响的是 move 本身的行为.
pub fn baisc_ow_test() {
    let mut num = 5;
    let mut num2 = 5;
    {
        let mut add_num = |x: i32| {
            println!("lambda addr num:{:p}", &num);
            num += x;
        };
        add_num(5);
    }
    {
        let mut add_num2 = move |x: i32| {
            println!("lambda addr num2: {:p}", &num2);
            num2 += x;
        };
        add_num2(5);
    }
    let t = thread::spawn(move || {
        println!("Hello, {}, thread addr:{:p}", num, &num); //新线程,使用外部变量必须move进来
    });
    println!("wait {:?}", t.join());
    //借用
    println!("top num={}, addr:{:p}", num, &num); //输出 10  打印可以看出来 地址一样 是借用
                                                  //因为 i32类型实现了copy
    println!("top num2={}, addr:{:p}", num2, &num2); //输出 5 打印可以看出来地址不一样 是 copy
}

//unique_ptr。独占内存，不共享。在Rust中是：std::boxed::Box
// shared_ptr。以引用计数的方式共享内存。在Rust中是：std::rc::Rc
// weak_ptr。不以引用计数的方式共享内存。在Rust中是：std::rc::Weak
//对于Rust的 Rc 来说，Rc指针内会有一个 strong_count 的引用持计数，一旦引用计数为0后，内存就自动释放了。
// 需要共享内存的时候，需要调用实例的 clone() 方法。如： let another = rc.clone() 克隆的时候，只会增加引用计数，不会作深度复制（个人觉得Clone的语义在这里被践踏了）
// 有这种共享的引用计数，就意味着有多线程的问题，所以，如果需要使用线程安全的智能指针，则需要使用std::sync::Arc
// 可以使用 Rc::downgrade(&rc) 后，会变成 Weak 指针，Weak指针增加的是 weak_count 的引用计数，内存释放时不会检查它是否为 0。
pub fn test_ai_pointer() {
    //声明两个未初始化的指针变量
    let weak: Weak<i32>;
    let mut strong: Rc<i32>;
    {
        let five = Rc::new(5); //局部变量
        strong = five.clone(); //进行强引用
        weak = Rc::downgrade(&five); //对局部变量进行弱引用
    }
    //此时，five已析构，所以 Rc::strong_count(&strong)=1， Rc::weak_count(&strong)=1
    //如果调用 drop(strong)，那个整个内存就释放了
    //drop(strong);
    //如果要访问弱引用的值，需要把弱引用 upgrade 成强引用，才能安全的使用

    //另外，如果你要修改 Rc 里的值，Rust 会给你两个方法，一个是 get_mut()，一个是 make_mut() ，这两个方法都有副作用或是限制。
    // get_mut() 需要做一个“唯一引用”的检查，也就是没有任何的共享才能修改
    //修改引用的变量 - get_mut 会返回一个Option对象
    //但是需要注意，仅当（只有一个强引用 && 没有弱引用）为真才能修改
    if let Some(val) = Rc::get_mut(&mut strong) {
        //需要做一个“唯一引用”的检查，也就是没有任何的共享才能修改  所以 如果要生效 需要将 weak注释掉
        *val = 555;
        println!(" in strong {}", strong);
    }
    match weak.upgrade() {
        Some(r) => println!(" weak {}", r),
        None => println!("None"),
    }
    //此处可以修改，但是是以 clone 的方式，也就是让strong这个指针独立出来了。
    *Rc::make_mut(&mut strong) = 999; //这样 strong的弱引用就失效了
    println!("strong:{}", strong);
    match weak.upgrade() {
        Some(r) => println!(" weak {}", r),
        None => println!("None"),
    }
}

struct Buffer {
    buffer: String,
}

struct Render {
    current_buffer: Buffer,
    next_buffer: Buffer,
}

impl Render {
    //更新buffer，把 next 更新到 current 中，再更新 next
    fn update_buffer(&mut self, buf: String) {
        self.current_buffer = replace(&mut self.next_buffer, Buffer { buffer: buf });
    }
}

pub fn test_cell() {
    let x = Cell::new(1);
    let y = &x; //引用（借用）
    let z = &x; //引用（借用）
    x.set(2); // 可以进行修改，x，y，z全都改了
    y.set(3);
    z.set(4);
    println!("x={} y={} z={}", x.get(), y.get(), z.get());
    let x = RefCell::new(vec![1, 2, 3, 4]);
    {
        println!("{:?}", *x.borrow())
    }
    {
        let mut my_ref = x.borrow_mut();
        my_ref.push(1);
    }
    println!("{:?}", *x.borrow());
}
