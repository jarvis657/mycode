pub fn hello() {
    println!("Hello, world!");
    println!("change vec list element");
    let mut a = vec![1, 2, 3];
    let b = change_and_get_first_element(&mut a);
    println!("{:?} {}", a, b); // [4, 2, 3] 4
    println!("Hello, world! 你好,我这是中文");
    let mut s = String::with_capacity(100);
    s.push_str("中文");
    println!("{}", s);
}

fn change_and_get_first_element(a: &mut Vec<i32>) -> i32 {
    a[0] = 4;
    a[0]
}

pub fn test_panic() {
    if complex_function().is_none() {
        println!(" x not exits!!!!!!!!!!!!!!!!!!");
    }
}

pub fn complex_function() -> Option<&'static str> {
    let x = get_an_optional_value()?; // if None, returns immidiately; if Some("abc"), set x to "abc"
                                      // some other code, ex
    println!("{}", x); // "abc" ; if you change line 19 `false` to `true`
    Some("")
}

fn get_an_optional_value() -> Option<&'static str> {
    //if the optional value is not empty
    if false {
        return Some("abc");
    }

    //else
    None
}

pub fn test_std_macos() {
    // Standard Macros also defined by both compiler and std
    // print, println, eprint, eprintln
    // format, format_args
    // write, writeln

    // concat, concat_idents, stringify //concat_idents : nightly-only experimental API

    // include, include_bytes, include_str

    // assert, assert_eq, assert_ne
    // debug_assert, debug_assert_eq, debug_assert_ne

    // try, panic, compile_error, unreachable, unimplemented

    // file, line, column, module_path
    // env, option_env
    // cfg

    // select, thread_local //select : nightly-only experimental API

    // vec

    // std modules
    // char, str

    // i8, i16, i32, i64, i128, isize
    // u8, u16, u32 ,u64, u128, usize
    // f32, f64
    // num

    // vec, slice, hash, heap, collections // heap : nightly-only experimental API

    // string, ascii, fmt

    // default

    // marker, clone, convert, cmp, iter

    // ops, ffi

    // option, result, panic, error

    // io
    // fs, path
    // mem, thread, sync
    // process, env
    // net
    // time
    // os

    // ptr, boxed, borrow, cell, any, rc

    // prelude

    // intrinsics // intrinsics : nightly-only experimental API
    // raw // raw : nightly-only experimental API
}
