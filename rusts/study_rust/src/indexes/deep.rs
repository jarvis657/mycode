use std::cell::RefCell;
use std::rc::Rc;

use crate::indexes::deep::List::{Cons, Nil};

#[derive(Debug)]
enum List {
    Cons(i32, RefCell<Rc<List>>),
    Nil,
}

impl List {
    fn tail(&self) -> Option<&RefCell<Rc<List>>> {
        match self {
            Cons(_, item) => Some(item), //Some(&RefCell)
            Nil => None,
        }
    }
}

#[test]
fn test_main() {
    let a = Rc::new(Cons(5, RefCell::new(Rc::new(Nil))));

    println!("a initial rc count = {}", Rc::strong_count(&a)); //1
    println!("a next item = {:?}", a.tail()); //Some(RefCell<Rc<Nil>>)

    let b = Rc::new(Cons(10, RefCell::new(Rc::clone(&a)))); //指向a, Rc<Cons<10, RefCell<a>>

    println!("a rc count after b creation = {}", Rc::strong_count(&a)); //2
    println!("b initial rc count = {}", Rc::strong_count(&b)); //1
    println!("b next item = {:?}", b.tail()); //Some(RefCell<Rc<a>>)

    if let Some(link) = a.tail() {
        //匹配，if a.tail == Some(x) let link = x
        *link.borrow_mut() = Rc::clone(&b); //指向b
                                            //RefCell不可变引用，borrow_mut需要*
                                            //RefCell<Rc<Nil>>.borrow_mut() -> Rc<Nil> <= Rc<b>, a的空值变成b
    } //a, b互相指向

    println!("b rc count after changing a = {}", Rc::strong_count(&b)); //2
    println!("a rc count after changing a = {}", Rc::strong_count(&a)); //2

    // println!("a next item = {:?}", a.tail());  a-> b -> a -> b, 溢出
}
//drop b, rc count b - 1, rc count b - 1, b is gone, both Rc<List> 存在堆
//drop a, rc count a - 1, b is gone so rc count b doesn't change, Rc<list> 释放
//Rc<list> in b 还在
