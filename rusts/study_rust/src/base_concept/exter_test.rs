//!
//! This crate was created to support Hands on Algorithms and Data Structures With Rust!
//!
//! Chapter 1
//!

use std::cell::RefCell;
use std::collections::{HashMap, VecDeque};
use std::error::Error;
use std::fmt::Debug;
use std::num::ParseIntError;
use std::ops::Deref;
use std::rc::Rc;
use std::string::ParseError;
use std::sync::mpsc::{channel, Receiver, Sender};
use std::sync::{Arc, Mutex};
use std::thread;
use std::time::Duration;
//use std::borrow::BorrowMut;
use crate::base_concept::exter_test::List::{Cons, Nil};

fn shared_state() {
    let v = Arc::new(Mutex::new(vec![]));
    let handles = (0..10).map(|i| {
        let numbers = v.clone();
        println!(
            "v clone size:{},num size :{},{:?}",
            Arc::strong_count(&v),
            Arc::strong_count(&numbers),
            *v.lock().unwrap()
        );
        thread::spawn(move || {
            let mut vector = numbers.lock().unwrap();
            (*vector).push(i);
        })
    });
    for handle in handles {
        handle.join().unwrap();
    }
    println!(
        "final v clone size:{},{:?}",
        Arc::strong_count(&v),
        *v.lock().unwrap()
    );

    let xxx = String::from("fdsafs");
    let xx = "a";
    let result1 = xx.parse::<i32>();

    let ve = vec![1, 23];

    let mut vv = vec![];
    for i in 0..4 {
        let numbers = v.clone();
        println!(
            "######v clone size:{},num size :{},{:?}",
            Arc::strong_count(&v),
            Arc::strong_count(&numbers),
            *v.lock().unwrap()
        );
        let join_handle = thread::spawn(move || {
            let mut vector = numbers.lock().unwrap();
            (*vector).push(i);
        });
        vv.push(join_handle);
    }
    for handle in vv {
        handle.join().unwrap();
    }
}

fn threading() {
    let c = "xx";
    let cc = &c[0..1];
    let x = 10;
    let handle = thread::spawn(move || {
        println!("Hello from a thread, the number is {}", x);
        "haha"
    });
    let result = handle.join();
    let x1 = result.unwrap();
    println!("Hello from a thread, return is {}", x1);
}

fn channels() {
    const N: i32 = 10;
    let (tx, rx): (Sender<i32>, Receiver<i32>) = channel();

    let handles = (0..N).map(|i| {
        let _tx = tx.clone();
        thread::spawn(move || {
            // don't use the result
            let _ = _tx.send(i).unwrap();
        })
    });
    // close all threads
    for h in handles {
        h.join().unwrap();
    }

    // receive N times
    let numbers: Vec<i32> = (0..N).map(|_| rx.recv().unwrap()).collect();

    println!("{:?}", numbers);
}

#[derive(Debug, Clone)]
struct FileName {
    name: Rc<String>,
    ext: Rc<String>,
}

fn ref_counter() {
    let name = Rc::new(String::from("main"));
    let ext = Rc::new(String::from("rs"));

    for _ in 0..3 {
        let f = FileName {
            name: name.clone(),
            ext: ext.clone(),
        };
        println!("{:?}", f);
    }
}

//fn mutex_test(){
//    let status = Arc::new(Mutex::new(JobStatus { jobs_completed: 0 }));
//    let status_shared = status.clone();
//    let j = thread::spawn(move || {
//        for _ in 0..10 {
//            thread::sleep(Duration::from_millis(50));
//            status_shared.lock().unwrap().jobs_completed += 1;
//        }
//    });
//    j.join();
//}

pub fn capitalize_first(input: &str) -> String {
    let mut c = input.chars();
    match c.next() {
        None => String::new(),
        Some(first) => first.to_uppercase().to_string() + c.as_str(),
    }
}

pub fn total_cost(item_quantity: &str) -> Result<i32, ParseIntError> {
    let chars = item_quantity.chars();
    let processing_fee = 1;
    let cost_per_item = 5;
    let qty = item_quantity.parse::<i32>()?;

    Ok(qty * cost_per_item + processing_fee)
}

fn item_quantity_is_an_invalid_number() {
    let x = total_cost("beep boop").unwrap_err().to_string();
    let result = x.trim().parse::<i64>();
    println!("datax:{}", x);
}

#[derive(Debug, PartialEq, Eq)]
pub enum DivisionError {
    NotDivisible(NotDivisibleError),
    DivideByZero,
}

#[derive(Debug, PartialEq, Eq)]
pub struct NotDivisibleError {
    dividend: i32,
    divisor: i32,
}

pub fn divide(a: i32, b: i32) -> Result<i32, DivisionError> {
    if b != 0 && a % b == 0 {
        Ok(a / b)
    } else if a == 0 {
        Ok(0)
    } else if b == 0 {
        Err(DivisionError::DivideByZero)
    } else {
        Err(DivisionError::NotDivisible(NotDivisibleError {
            dividend: a,
            divisor: b,
        }))
    }
}

fn result_with_list() {
    let numbers = vec![27, 297, 38502, 81];
    let division_results = numbers.into_iter().map(|n| divide(n, 27));
    let x: Result<Vec<i32>, DivisionError> = division_results.collect(); //... Fill in here!
    let string = format!("{:?}", x);
    println!("{}", string)
    //    assert_eq!(string, "Ok([1, 11, 1426, 3])");
}

fn test1() {
    let input = vec![1, 2, 3];
    let fc = || std::mem::drop(input.as_ptr());
    fc();
    println!("===={:?}", input);
    //    let iterator = input.iter();
    //    let mapped = iterator.map(|&x| {
    //        return x * 2;
    //    });
    //    // Gather the result in a RingBuf.
    //    let output = mapped.collect::<VecDeque<_>>();
    //    println!("{:?}", output);
}

fn use_names_for_something_else(_names: Vec<&str>) {}

fn countSize() {
    let names = vec!["Jane", "Jill", "Jack", "John", "中"];
    let total_bytes = names
        .iter()
        .map(|name| name.len())
        .fold(0, |acc, len| acc + len);
    println!("total size:{}", total_bytes);
    use_names_for_something_else(names);
}

fn destructure() {
    let player_scores = [("Jack", 20), ("Jane", 23), ("Jill", 18), ("John", 19)];
    let players = player_scores
        .iter()
        .map(|&(player, _)| player)
        .collect::<Vec<_>>();
    assert_eq!(players, ["Jack", "Jane", "Jill", "John"]);
}

fn iter_mut_test() {
    let mut teams = [
        [
            ("Jack", 20),
            ("Jane", 23),
            ("Jill", 18),
            ("John", 19),
            ("jarvis", 33),
        ],
        [
            ("Bill", 17),
            ("Brenda", 16),
            ("Brad", 18),
            ("Barbara", 17),
            ("tt", 111),
        ],
    ];
    let teams_in_score_order = teams
        .iter_mut()
        .map(|team| {
            team.sort_by(|&a, &b| a.1.cmp(&b.1).reverse());
            team
        })
        .collect::<Vec<_>>();
    println!("teams_in_score_order: {:?}", teams_in_score_order);
    for i in &teams {
        println!(" for in Teams: {:?}", i);
    }
    println!("Teams: {:?}", teams);
}

fn iter_mut2_test() {
    let mut teams = [
        ("Jack", 20),
        ("Jane", 23),
        ("Jill", 18),
        ("John", 19),
        ("jarvis", 33),
    ];
    let teams_in_score_order = teams.sort_by(|&a, &b| a.1.cmp(&b.1).reverse());
    println!("teams_in_score_order: {:?}", teams_in_score_order);
    println!("Teams: {:?}", teams);
}

enum Message {
    Quit,
    Move { x: i32, y: i32 },
    Write(String),
    ChangeColor(i32, i32, i32),
    T(i32),
}

struct MyBox<T>(T)
where
    T: Debug;

impl<T> MyBox<T>
where
    T: Debug,
{
    fn new(x: T) -> MyBox<T> {
        MyBox(x)
    }
}

impl<T> Deref for MyBox<T>
where
    T: Debug,
{
    type Target = T;

    fn deref(&self) -> &T {
        &self.0
    }
}

impl<T> Drop for MyBox<T>
where
    T: Debug,
{
    fn drop(&mut self) {
        println!("Dropping MyBox with data `{:?}`!", self.0);
    }
}

fn hello(name: &str) {
    println!("Hello, {}!", name);
}

pub trait Messenger {
    fn send(&mut self, msg: &str);
}

pub struct LimitTracker<'a, T: Messenger> {
    messenger: &'a mut T,
    value: usize,
    max: usize,
}

impl<'a, T> LimitTracker<'a, T>
where
    T: Messenger,
{
    pub fn new(messenger: &mut T, max: usize) -> LimitTracker<T> {
        LimitTracker {
            messenger,
            value: 0,
            max,
        }
    }
    pub fn set_value(&mut self, value: usize) {
        self.value = value;
        let percentage_of_max = self.value as f64 / self.max as f64;
        if percentage_of_max >= 1.0 {
            self.messenger.send("Error: You are over your quota!");
        } else if percentage_of_max >= 0.9 {
            self.messenger
                .send("Urgent warning: You've used up over 90% of your quota!");
        } else if percentage_of_max >= 0.75 {
            self.messenger
                .send("Warning: You've used up over 75% of your quota!");
        }
    }
}

#[derive(Debug)]
enum List {
    Cons(Rc<RefCell<i32>>, RefCell<Rc<List>>),
    Nil,
}

impl List {
    fn tail(&self) -> Option<&RefCell<Rc<List>>> {
        match self {
            Cons(_, item) => Some(item),
            Nil => None,
        }
    }
}

fn cycle_test() {
    let value = Rc::new(RefCell::new(5));

    let a = Rc::new(Cons(value, RefCell::new(Rc::new(Nil))));

    println!("a initial rc count = {}", Rc::strong_count(&a));
    println!("a next item = {:?}", a.tail());

    let value2 = Rc::new(RefCell::new(5));
    let b = Rc::new(Cons(value2, RefCell::new(Rc::clone(&a))));

    println!("a rc count after b creation = {}", Rc::strong_count(&a));
    println!("b initial rc count = {}", Rc::strong_count(&b));
    println!("b next item = {:?}", b.tail());

    if let Some(link) = a.tail() {
        *link.borrow_mut() = Rc::clone(&b);
    }

    println!("b rc count after changing a = {}", Rc::strong_count(&b));
    println!("a rc count after changing a = {}", Rc::strong_count(&a));

    // Uncomment the next line to see that we have a cycle;
    // it will overflow the stack
    //     println!("a next item = {:?}", a.tail());
}

fn rc_test() {
    let value = Rc::new(RefCell::new(5));
    let a = Rc::new(Cons(Rc::clone(&value), RefCell::new(Rc::new(Nil))));
    let b = Cons(Rc::new(RefCell::new(6)), RefCell::new(Rc::clone(&a)));
    let c = Cons(Rc::new(RefCell::new(10)), RefCell::new(Rc::clone(&a)));
    *value.borrow_mut() += 10;
    println!("a after = {:?}", a);
    println!("b after = {:?}", b);
    println!("c after = {:?}", c);
}

pub fn exter_main() {
    cycle_test();
    rc_test();
    let string = String::from("data");
    let rct = Rc::new(RefCell::new(string));
    (*rct.borrow_mut()).push_str("11");
    println!("rct  = {:?}", rct);
    let v = vec!["1", "2", "3"];
    for x in v.clone() {
        println!("{}", x);
    }
    println!("v_:{:?}", v);

    let x = vec!["Jill", "Jack", "Jane", "John"];
    let x_ = x.clone().into_iter().collect::<Vec<_>>();
    println!("x_:{:?}", x_);
    iter_mut2_test();
    iter_mut_test();
    destructure();
    countSize();
    test1();
    let input = [1, 2, 3];
    println!("haha  input pointer:{:p}, :{:?}", input.as_ptr(), input);
    let iterator = input.iter();
    let mapped = iterator
        .inspect(|&x| println!("Pre map:\t{}", x))
        .map(|&x| x * 10) // This gets fed into...
        .inspect(|x| println!("First map:\t{}", x))
        .map(|x| x + 5) // ... This.
        .inspect(|x| println!("Second map:\t{}", x));
    mapped.collect::<Vec<usize>>();
    println!("haha  input pointer:{:p}, :{:?}", input.as_ptr(), input);

    // First, we get a set of values.
    let input = [1, 2, 3];
    // Create an iterator over them.
    let iterator = input.iter();
    // Specify things to do along the chain.
    let mapped = iterator.map(|&x| x * 2);
    println!("{:?}", input);
    // Do something with the output.
    let output = mapped.collect::<Vec<usize>>();
    println!("{:?}", output);

    result_with_list();
    let cc = vec!["123", "fd"];
    let capitalized_words: Vec<String> = cc.into_iter().map(|c| capitalize_first(c)).collect();
    threading();
    shared_state();
    channels();
    ref_counter();
    item_quantity_is_an_invalid_number();
    let msize = std::mem::size_of::<Message>();
    println!("message size:{}", msize);
    let my_box = MyBox::new("rust");
    let xx = *my_box;
    hello(xx);
    hello(&my_box);
    hello(&(*my_box)[..]);
    std::mem::drop(my_box);
    println!("done-----------");
}

#[cfg(test)]
mod tests {
    use super::*;

    struct MockMessenger {
        sent_messages: Vec<String>,
    }

    impl MockMessenger {
        fn new() -> MockMessenger {
            MockMessenger {
                sent_messages: vec![],
            }
        }
    }

    impl Messenger for MockMessenger {
        fn send(&mut self, message: &str) {
            self.sent_messages.push(String::from(message));
        }
    }

    #[test]
    fn it_sends_an_over_75_percent_warning_message() {
        let mut mock_messenger = MockMessenger::new();
        let mut limit_tracker = LimitTracker::new(&mut mock_messenger, 100);

        limit_tracker.set_value(80);

        assert_eq!(mock_messenger.sent_messages.len(), 1);
    }

    use super::*;

    // Tests that verify your `divide` function implementation
    #[test]
    fn test_success() {
        assert_eq!(divide(81, 9), Ok(9));
    }

    #[test]
    fn test_not_divisible() {
        assert_eq!(
            divide(81, 6),
            Err(DivisionError::NotDivisible(NotDivisibleError {
                dividend: 81,
                divisor: 6
            }))
        );
    }

    #[test]
    fn test_divide_by_0() {
        assert_eq!(divide(81, 0), Err(DivisionError::DivideByZero));
    }

    #[test]
    fn test_divide_0_by_something() {
        assert_eq!(divide(0, 81), Ok(0));
    }

    // Iterator exercises using your `divide` function
    //    #[test]
}
