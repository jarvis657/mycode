use std::time;

fn main() {
    let begin = time::SystemTime::now();
    let x = fibonacci(50);
    let result = time::SystemTime::now().duration_since(begin);
    println!("result:{},{}", x, result.unwrap().as_nanos());
    let x1 = 0.7 + 0.2 + 0.1;
    println!("result:{}", x1 );
}

fn fibonacci(n: u32) -> u32 {
    match n {
        0 => 1,
        1 => 1,
        _ => fibonacci(n - 1) + fibonacci(n - 2),
    }
}


