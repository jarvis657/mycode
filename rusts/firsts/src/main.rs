extern crate firsts;

use std::io;

fn main() {
    firsts::hello();
    println!("Hello, world!");
    let mut ss = String::new();
    let result = io::stdin().read_line(&mut ss);
    println!("result:{:?},your input:{}", result, ss);
    {
        let word = get_first_word(&mut ss);
        println!("first word:  {:?}", word);
    }
    ss.clear();
    println!("ss :  {:?}", ss);
}

fn get_first_word(s: &mut str) -> &str {
    &s[0..1]
}
// #[no_mangle]
// exten "C" fn fast_blank(buf: Buf) -> bool {
//     buf.as_slice().chars().all(|c| c.is_whitespace())
// }
