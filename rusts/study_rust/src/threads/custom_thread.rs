use core::borrow::{Borrow, BorrowMut};
use std::char;
use std::rc::Rc;
use std::sync::{mpsc, Arc, Mutex};
use std::thread;
use std::time::Duration;

pub fn test_thread() {
    let v = vec![1, 2, 3];
    let handle = thread::spawn(move || {
        println!("vector: {:?}", v);
    });
    handle.join();
}

pub fn test_ship() {
    let vc = vec![1, 2, 3];
    let vc2 = vec![4, 5, 6];
    let handle = thread::spawn(move || {
        println!("here's a vector: {:?}", vc);
        println!("here's a vector: {:?}", vc2);
    });
    handle.join().unwrap();
    //    println!("here's a vector: {:?}", vc);
    //    println!("here's a vector: {:?}", vc2);
}

#[test]
fn test_thread_ship() {
    test_ship();
    let (sender, reciver) = mpsc::channel();
    let thread_offset = Arc::new(0);
    let counter = Arc::new(Mutex::new(0));

    let mut thread_pool = vec![];
    for i in 0..5 {
        let t_o = Arc::clone(&thread_offset);
        let sender_tmp = mpsc::Sender::clone(&sender);

        let counter = Arc::clone(&counter);

        let handle = thread::spawn(move || {
            let mut guard = counter.lock().unwrap();
            let mut r;
            for i in 0..10 {
                *guard += 1;
                let string = char::from_digit(*t_o, 10).unwrap().to_string();
                let s = String::from("thread ");
                let f = char::from_digit(i, 10).unwrap().to_string();
                //                let g = char::from_digit(*guard, 10).unwrap().to_string();
                r = s + &f + ":"; // + &g;
                sender_tmp.send(r);
            }
        });
        thread_pool.push(handle);
    }
    println!("=============sss===========");
    for data in reciver {
        println!("data reciving:{}", data);
    }
    println!("=============eeeeee============");
    for handle in thread_pool {
        handle.join().unwrap();
    }
}
