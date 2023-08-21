#![crate_type = "staticlib"]
#[test]
fn test_main_unsafe() {
    let mut num = 5i64;
    //let r1 = &num;
    //let r2 = &mut num; 不可以
    let r1 = &num as *const i64; //不可变生指针，产生生指针是安全的
    let r2 = &mut num as *mut i64; //可变生指针，指向同一地址

    unsafe {
        //只能再unsafe block之内deref
        println!("r1 is: {}", *r1); //deref生指针，不安全
        println!("r2 is: {}", *r2);
        println!("num is: {}", num);
    }
    unsafe {
        //必须在unsafe block调用
        dangerous(); //我知道我在干啥
    }

    let mut v = vec![1, 2, 3, 4, 5, 6];
    let r = &mut v[..]; //可变片段
    let (a, b) = r.split_at_mut(3); //在索引3处分开
    assert_eq!(a, &mut [1, 2, 3]);
    assert_eq!(b, &mut [4, 5, 6]);

    extern "C" {
        //c语言接口
        fn abs(input: i32) -> i32; //c函数
    }

    unsafe {
        //其他语言没有安全特性
        println!("Absolute value of -3 according to C: {}", abs(-13));
    }
    call_from_c();
}

fn split_at_mut(slice: &mut [i32], mid: usize) -> (&mut [i32], &mut [i32]) {
    //安全
    use std::slice;
    let len = slice.len();
    let ptr = slice.as_mut_ptr(); //生指针*mut i32，基于合法数据

    assert!(mid <= len); //确认索引合法
    unsafe {
        (
            slice::from_raw_parts_mut(ptr, mid), //deref
            slice::from_raw_parts_mut(ptr.offset(mid as isize), len - mid),
        ) //offset is unsafe
    }
}

unsafe fn dangerous() { //unsafe声明
                        //some unsafe code
}

//让其他语言调用rust
#[no_mangle]
pub extern "C" fn call_from_c() {
    println!("Just called a Rust function from C!");
}
