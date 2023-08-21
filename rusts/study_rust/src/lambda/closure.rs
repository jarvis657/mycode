pub fn c_test() {
    let mut x = 1_i32;
    //    fn inner_add() -> i32 {
    //        x + 1
    //    }
    let mut inner_add = move || {
        //加上move 原来的x还是1,不加就是3 move代表所有权转移
        x = x + 2;
        x + 1
    };
    let x2 = inner_add();
    println!("result {}", x2);
    println!("origin:x:{}", x);
}

struct Person {
    name: String,
    age: u8,
}

pub fn ref_closure() {
    let p = Person {
        name: "Hao Chen".to_string(),
        age: 44,
    };
    //可以运行，因为 `u8` 有 Copy Trait
    let age = |p: &Person| p.age;
    // String 没有Copy Trait，所以，这里所有权就 Move 走了
    // let name = |p: &Person| p.name;
    //下面这行报错:cannot infer an appropriate lifetime for borrow expression due to conflicting requirements
    // let name = |p: &Person| &p.name;
    //下面的声明可以正确译 给lamba 添加生命周期参数
    let name: for<'a> fn(&'a Person) -> &'a String = |p: &Person| &p.name;
    println!("#######name={}, age={}", name(&p), age(&p));
}
