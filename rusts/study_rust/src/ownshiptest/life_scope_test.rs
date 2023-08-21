use std::fmt::Debug;
use std::marker::PhantomData;
use std::ptr::null;

#[derive(Debug)]
struct T {
    member: i32,
}

fn return_life<'a: 'b, 'b>(arg: &'a T) -> &'a i32 {
    &arg.member
}

fn test_life() {
    let mut t = T { member: 2 };
    let x = return_life(&t);
    println!("{:?}", x)
}

#[derive(Clone, Debug)]
struct S;

//裸指针 中如何添加生命周期
struct R<'a, T: Debug + 'a> {
    x: *const T,
    _marker: PhantomData<&'a T>,
}

//R的析构函数
impl<'a, T: Debug + 'a> Drop for R<'a, T> {
    fn drop(&mut self) {
        unsafe {
            println!("dropping R while S{:?}", *self.x);
        }
    }
}

impl<'a, T: Debug + 'a> R<'a, T> {
    pub fn ref_to<'b: 'a>(&mut self, obj: &'b T) {
        self.x = obj;
    }
}

fn test_r() -> () {
    let local = S {};
    {
        let mut r = R {
            x: null(),
            _marker: PhantomData,
        };
        r.ref_to(&local);
    }
}

fn test_a<'a>(x: &'a str, y: &'a str) -> &'a str {
    if x.len() > y.len() {
        x
    } else {
        y
    }
}

//fn test_unsafe() {
//    let x = 1_i32;
//    let mut x2 = 1;
////    let raw_mut = &mut x2 as *mut u32 as *mut i32 as *mut i64;
//    unsafe { *raw_mut = -1; }
//    println!("x:{:X},x2:{:X}", x, x2);
//}

//#[test]
pub fn test() {
    test_r();
    test_life();
}
