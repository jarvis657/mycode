use std::sync::{Arc, Condvar, Mutex};
use std::thread;
use std::time::Duration;

pub fn share_convard() {
    let pair = Arc::new((Mutex::new(false), Condvar::new()));
    let pair2 = pair.clone();
    thread::spawn(move || {
        thread::sleep(Duration::from_secs(2));
        let &(ref lock, ref cvar) = &*pair2;
        let mut guard = lock.lock().unwrap();
        *guard = true;
        cvar.notify_one();
        println!("child {}", *guard);
    });
    let (ref lock, ref cvar) = &*pair;
    let mut guard = lock.lock().unwrap();
    println!("before wait {}", guard);
    while !*guard {
        guard = cvar.wait(guard).unwrap();
    }
    println!("after wait {}", *guard);
}
