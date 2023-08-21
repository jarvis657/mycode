use std::{fmt, panic};

// Custom error type; can be any type which defined in the current crate
// 💡 In here, we use a simple "unit struct" to simplify the example
struct AppError;

// Implement std::fmt::Display for AppError
impl fmt::Display for AppError {
    fn fmt(&self, f: &mut fmt::Formatter) -> fmt::Result {
        write!(f, "An Error Occurred, Please Try Again!") // user-facing output
    }
}

// Implement std::fmt::Debug for AppError
impl fmt::Debug for AppError {
    fn fmt(&self, f: &mut fmt::Formatter) -> fmt::Result {
        write!(f, "{{ file: {}, line: {} }}", file!(), line!()) // programmer-facing output
    }
}

// A sample function to produce an AppError Err
fn produce_error() -> Result<(), AppError> {
    Err(AppError)
}

pub fn main() {
    match produce_error() {
        Err(e) => eprintln!("{}", e), // An Error Occurred, Please Try Again!
        _ => println!("No error"),
    }

    eprintln!("{:?}", produce_error()); // Err({ file: src/main.rs, line: 17 })
}

pub fn test_catch_panic() {
    let result = panic::catch_unwind(|| { println!("hello!"); });
    assert!(result.is_ok());
    let result = panic::catch_unwind(|| { panic!("oh no!"); });
    assert!(result.is_err());
    println!("done test_catch_panic");
}

pub fn erase_panic() {
    let result = panic::catch_unwind(|| { println!("hello!"); });
    assert!(result.is_ok());
    panic::set_hook(Box::new(|panic_info| {
        if let Some(location) = panic_info.location() {
            println!("panic occurred '{}' at {}", location.file(), location.line()
            );
        } else {
            println!("can't get location information...");
        }
    }));
    let result = panic::catch_unwind(|| { panic!("oh no!"); });
    assert!(result.is_err());
    println!("done erase_panic");
}