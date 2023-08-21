extern crate num_bigint;
extern crate num_traits;

use num_bigint::{BigUint, ToBigUint};
use num_traits::One;

fn factorial(num: u64) -> BigUint {
    let current: BigUint = num.to_biguint().unwrap();
    if num <= 1 {
        return One::one();
    }
    return current * factorial(num - 1);
}

fn fun_test() {
    let num: u64 = 100000;
    println!("Factorial {}! = {}", num, factorial(num))
}
//fn main() {
//    println!("Hello, world!");
////    loopTest();
//    let x;
////    while true{x =1;break;} //compiler error
//    loop {
//        x = 1;
//        break;
//    }
//    println!("x:{}", x);
//    let i = 42;
//    //TODO how to get p value
////    let p = &i as *const i32 as *mut i32;
////    unsafe {
////        *p = 44;
////    }
////    println!("p:{}, pr: {:p}",&p, p);
//    let num = Numer::Float(32f32);
//    read_num(&num);
////    println!("num :{:#?}", &num);
//}
//
//enum Numer {
//    Int(i32),
//    Float(f32),
//}
//fn read_num(num: &Numer){
//    match num {
//        &Numer::Int(value) => println!("Integer :{}", value),
//        &Numer::Float(value) => println!("Float :{}", value),
//        _ => println!("other value")
//    };
//}
//
//fn loopTest() -> () {
//    let mut m = 1;
//    let n = 1;
//    'a: loop {
//        if m < 100 {
//            m += 1;
//            println!("final m :{}", m)
//        } else {
//            'b: loop {
//                if m + n > 50 {
//                    println!("m:{},n:{}", m, n);
//                    println!("break");
//                    break 'a;
//                } else {
////                    n += 1;
//                    continue 'a;
//                }
////                n += 1;
//            }
//        }
//    }
//}
