use std::ops::Deref;
use std::rc::Rc;

use crate::indexes::base::ListEnum::{Consm, Nilm};

pub fn box_test() {
    let x = Box::new(5);
    println!("b={}", x);
}

enum Message {
    Quit,
    Move { x: i32, y: i32 },
    Write(String),
    ChangeColor(i32, i32, i32),
}

//递归数据大小无法确定.使用Box
pub enum ListEnum {
    Consm(i32, Box<ListEnum>),
    Nilm,
}

pub fn testListEnum() -> ListEnum {
    Cons(
        1,
        Box::new(Cons(2, Box::new(Cons(3, Box::new(Cons(4, Box::new(Nil))))))),
    )
}
pub fn test_list_enum() -> ListEnum {
    Consm(
        1,
        Box::new(Consm(
            2,
            Box::new(Consm(3, Box::new(Consm(4, Box::new(Nilm))))),
        )),
    )
}

#[derive(Debug)]
struct BoxColor(i32, i32, i32);

#[derive(Debug)]
pub struct MyBox<T, B>(T, B);

impl<T, B> MyBox<T, B> {
    pub fn new(t: T, b: B) -> MyBox<T, B> {
        MyBox(t, b)
    }
}

impl<T, B> Deref for MyBox<T, B> {
    type Target = B;

    fn deref(&self) -> &Self::Target {
        //MyBox返回B的引用,不转移所有权
        &self.1
    }
}

impl<T, B> Drop for MyBox<T, B> {
    fn drop(&mut self) {
        //        println!("dropping with data:`{},{}`!", self.0, self.1);
        println!("dropping with data:!");
    }
}

struct Owner {
    name: String,
}

struct Gadget {
    id: i32,
    owner: Rc<Owner>,
}

#[test]
fn test_rc() {
    let foo = Rc::new(vec![1.0, 2.0, 3.0]);
    let a = foo.clone(); // clone that is deep copy
    let b = Rc::clone(&foo); // Rc<T>.clone == Rc::clone(&T)
    println!("============rc================");
    let rc_owner = Rc::new(Owner {
        name: "lmq".to_string(),
    });
    let gadget_1 = Gadget {
        id: 1,
        owner: rc_owner.clone(),
    };
    let gadget_2 = Gadget {
        id: 2,
        owner: rc_owner.clone(),
    };
    drop(rc_owner);
    println!(" gadget{} owned by {}", gadget_1.id, gadget_1.owner.name);
    println!(" gadget{} owned by {}", gadget_2.id, gadget_2.owner.name);
}

#[test]
fn test_mybox() {
    let x = 5;
    let my_box = MyBox::new(1i32, 2i64);
    assert_eq!(5, x);
    assert_eq!(2i64, *my_box);
}
