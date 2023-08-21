use rayon::iter::IntoParallelRefMutIterator;
use std::cell::Cell;
use std::sync::Mutex;

fn run_thread() {
    let m = Mutex::new(Cell::new(0));
    rayon::join(
        || {
            let mutex_guard = m.lock().unwrap();
            mutex_guard.set(mutex_guard.get() + 1);
            println!("Thread 1: {:?}", mutex_guard.get())
        },
        || {
            let mutex_guard = m.lock().unwrap();
            mutex_guard.set(mutex_guard.get() + 1);
            println!("Thread 2: {:?}", mutex_guard.get())
        },
    );
    rayon::join(
        || {
            let mutex_guard = m.lock().unwrap();
            mutex_guard.set(mutex_guard.get() + 1);
            println!("Thread 3: {:?}", mutex_guard.get())
        },
        || {
            let mutex_guard = m.lock().unwrap();
            mutex_guard.set(mutex_guard.get() + 1);
            println!("Thread 4: {:?}", mutex_guard.get())
        },
    );
}

#[test]
fn test_run_thread() {
    run_thread();
}

#[test]
///Mutex是Rust中的一种锁。
///Condvar需要和Mutex一同使用，因为有Mutex保护，Condvar并发才是安全的。
///Mutex::lock方法返回的是一个MutexGuard，在离开作用域的时候，自动销毁，从而自动释放锁，从而避免锁没有释放的问题。
///Condvar在等待时，时会释放锁的，被通知唤醒时，会重新获得锁，从而保证并发安全。
fn test_condvar() {
    use std::sync::Arc;
    use std::sync::{Condvar, Mutex};
    use std::thread;
    let pair = Arc::new((Mutex::new(false), Condvar::new()));
    let pair2 = pair.clone();
    // 创建一个新线程
    thread::spawn(move || {
        let &(ref lock, ref cvar) = &*pair2;
        let mut started = lock.lock().unwrap();
        *started = true;
        cvar.notify_one();
        println!("notify main thread");
    });
    // 等待新线程先运行
    let &(ref lock, ref cvar) = &*pair;
    let mut started = lock.lock().unwrap();
    while !*started {
        println!("before wait");
        started = cvar.wait(started).unwrap();
        println!("after wait");
    }
}

///原子类型是最简单的控制共享资源访问的一种机制，相比较于后面将介绍的锁而言，原子类型不需要开发者处理加锁和释放锁的问题，同时支持修改，读取等操作，还具备较高的并发性能，从硬件到操作系统，到各个语言，基本都支持。在标准库std::sync::atomic中，你将在里面看到Rust现有的原子类型，包括AtomicBool，AtomicIsize，AtomicPtr，AtomicUsize。这4个原子类型基本能满足百分之九十的共享资源安全访问的需要。下面我们就用原子类型，结合共享内存的知识，来展示一下一个线程修改，一个线程读取的情况：
#[test]
fn test_atomic() {
    use std::sync::atomic::{AtomicUsize, Ordering};
    use std::sync::Arc;
    use std::thread;
    let var: Arc<AtomicUsize> = Arc::new(AtomicUsize::new(5));
    let share_var = var.clone();
    // 创建一个新线程
    let new_thread = thread::spawn(move || {
        println!(
            "share value in new thread: {}",
            share_var.load(Ordering::SeqCst)
        );
        // 修改值
        share_var.store(9, Ordering::SeqCst);
    });
    // 等待新建线程先执行
    new_thread.join().unwrap();
    println!("share value in main thread: {}", var.load(Ordering::SeqCst));
}

#[test]
fn test_parallel() {
    extern crate rayon;
    use rayon::prelude::*;

    let a: Box<i32> = Box::new(10);
    // 我们需要先解引用a，再隐式把 & 转换成 *
    let b: *const i32 = &*a;
    // 使用 into_raw 方法
    let c: *const i32 = Box::into_raw(a);

    let mut colors = vec![
        -20.0f32, 0.0, 20.0, 40.0, 80.0, 100.0, 150.0, 180.0, 200.0, 250.0, 300.0,
    ];
    println!("original: {:?}", &colors);
    colors.par_iter_mut().for_each(|color| {
        let c: f32 = if *color < 0.0 {
            0.0
        } else if dbg!(dbg!(*color) > 255.0) {
            255.0
        } else {
            *color
        };
        *color = c / 255.0;
    });
    println!("transformed: {:?}", &colors);
}
