fn add_one(x: i32) -> i32 {
    x + 1
}

fn do_twice(f: fn(i32) -> i32, arg: i32) -> i32 {
    //不就是定义重写一遍吗
    f(arg) + f(arg)
}

//closure绑定Fn/FnMut/FnOnce + 'static
//closure不是具体类型，大小未知
//fn returns_closure_my() -> Fn(i32) -> i32 {  //编译错误，DST
//    |x| x + 1
//}

//DST必须指针
fn returns_closure_cc() -> Box<dyn Fn(i32) -> i32> {
    //使用trait object包装
    Box::new(|x| x + 1) //指针，大小确定
}

#[test]
fn test_main() {
    let answer = do_twice(add_one, 5);
    println!("The answer is: {}", answer);

    let list_of_numbers = vec![1, 2, 3];
    let iter = list_of_numbers.iter();
    let list_of_strings: Vec<String> = iter
        //        .map(|i| i.to_string())  // map提取iterator为参数，应用closure
        .map(ToString::to_string) // map提取iterator为参数，应用closure
        .collect();
}
