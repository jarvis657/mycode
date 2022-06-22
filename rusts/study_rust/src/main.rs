//extern crate study_rust;

use std::io;
use std::io::Write;
use std::string::String;

use crate::base_concept::custom_impl_inherit::FullName;
use crate::base_concept::custom_impl_inherit::Player;

mod base_concept;
mod dyn_struct;
mod examples;
mod file_ops;
mod indexes;
mod main_test;
mod mem_ordering;
mod other_hard;
mod ownshiptest;
mod patterns;
mod thread_web;
mod threads;
mod tokio_test;
mod trait_concept;
mod unsafes;

#[allow(unused_variables)]
#[warn(unreachable_code)]
fn main() {
    ownshiptest::first_word("haha");
    base_concept::base::test_base();
    base_concept::base::test_scope();
    ownshiptest::copy_test();
    let x = 42;
    let aa = x;
    println!("aa:{:05}, x:{:07} ", aa, x);

    //    ⭐️ When assigning a variable binding to another variable binding or when passing it to a function(Without referencing), if its data type is a
    //
    //    Copy Type
    //    Bound resources are made a copy and assign or pass it to the function.
    //        The ownership state of the original bindings are set to “copied” state.
    //        Mostly Primitive types
    //    Move type
    //    Bound resources are moved to the new variable binding and we can not access the original variable binding anymore.
    //        The ownership state of the original bindings are set to “moved” state.
    //        Non-primitive types
    //    🔎 The functionality of a type is handled by the traits which have been implemented to it. By default, variable bindings have ‘move semantics.’ However, if a type implements core::marker::Copy trait , it has a ‘copy semantics’.
    //
    //    💡 So in the above second example, ownership of the Vec object moves to “b” and “a” doesn’t have any ownership to access the resource.

    //    let a = vec![1, 2, 3];
    //    let b = a;
    //    println!("{:?} {:?}", a, b);

    let aas = String::from("hello");
    let mut aas2 = aas;
    println!("aas2 : {}", aas2);
    //println!("aas: {}",aas)
    //let aas3 = my_fn_1(aas2);
    my_fn_1(&mut aas2);
    println!("myfn1 invoked aas3 :{}", aas2);
    let s = String::from("vv");
    let ss = test_fn2(s);
    println!("ss :{} ", ss);

    //    println!("s:{}", s);

    print!("hello ==========");
    io::stdout().flush().unwrap();
    print!("\rHELLO");
    io::stdout().flush().unwrap();
    io::stdout().flush().unwrap();
    print!("\rHELLO123123");
    io::stdout().flush().unwrap();
    io::stdout().flush().unwrap();
    print!("\rvvvv");
    io::stdout().flush().unwrap();
    println!("===========================================");

    let calcxx = calc(22);
    println!("x: {}", x);
    println!("calcxx: {}", calcxx);

    let x = true;
    let y: bool = false;
    let i_31: i32 = i32::min_value();
    let m_32: i32 = i32::max_value();

    println!("===========================================");
    let e = ["my value"; 3]; //["my value", "my value", "my value"];
    println!("{:?}", e);
    println!("{:#?}", e);

    let mut vs = String::with_capacity(100);
    vs.push_str("vss");
    println!("test vs: {}", vs);
    my_fn_1(&mut vs);

    println!("test2 vs: {}", vs);

    let b: (i32, f64) = (1, 1.5);
    println!("{:?}", b);

    //    slice
    let a: [i32; 4] = [1, 2, 3, 5]; //Parent Array

    let b: &[i32] = &a; //Slicing whole array
    let c = &a[0..4]; // From 0th position to 4th(excluding)
    let d = &a[..]; //Slicing whole array

    let e = &a[1..3]; //[2, 3]
    let f = &a[1..]; //[2, 3, 4]
    let g = &a[..3]; //[1, 2, 3]

    //    string
    let a = "Hello, world."; //a: &'static str
    let b: &str = "こんにちは, 世界!";

    let mut string_test = String::new();
    string_test.push_str("haha");

    let b: fn(i32) -> i32 = plus_one;

    let c = b(5); //6

    let i = plus_one(21);

    //arithmetric
    let a = 5;
    let b = a + 1; //6
    let c = a - 1; //4
    let d = a * 2; //10
    let e = a / 2; // ⭐️ 2 not 2.5
    let f = a % 2; //1

    let g = 5.0 / 2.0; //2.5

    //comparison
    let a = 1;
    let b = 2;

    let c = a == b; //false
    let d = a != b; //true
    let e = a < b; //true
    let f = a > b; //false
    let g = a <= a; //true
    let h = a >= a; //true
                    // 🔎
    let i = true > false; //true
    let j = 'a' > 'A'; //true

    //type casting
    let a = 15;
    let b = (a as f64) / 2.0; //7.5

    base_concept::custom_error::main();
    base_concept::custom_impl_inherit::read_ext_read();
    base_concept::custom_impl_inherit::read_ext_read();
    base_concept::custom_impl_inherit::read_ext_read();
    file_ops::file_ops_file::test_file_op();
    ownshiptest::copy_test();
    study_rust::hello();
    //    main_test::other_test_hello();
    study_rust::test_panic();
    //    & &mut *
    //    The & or &mut operators are used for borrowing and * operator for Dereferencing.

    //control ⭐️ Return data type should be same on each block, when using this as an expression.
    //optimistic code
    let team_size = 7;
    let team_size_in_text = if team_size < 5 {
        "Small"; //⭐️no ;
    } else if team_size < 10 {
        "Medium";
    } else {
        "Large";
    };
    println!("Current team size : {:#?}", team_size_in_text);
    let is_below_eighteen = if team_size < 18 {
        true;
    } else {
        false;
    };

    //match
    let tshirt_width = 28;
    let tshirt_size = match tshirt_width {
        16 => "S",      // check 16
        17 | 18 => "M", // check 17 and 18
        20 => "XL",
        19..=21 => "L", // check from 19 to 21 [19,20,21]
        _ => "Not Available",
    };
    println!("{}", tshirt_size); // L

    let is_allowed = false;
    let list_type = match is_allowed {
        true => "Full",
        false => "Restricted", // no default/ _ condition can be skipped
                               // Because data type of is_allowed is boolean and all possibilities checked on conditions
    };
    println!("{}", list_type); // Restricted

    let marks_paper_a: u8 = 25;
    let marks_paper_b: u8 = 30;
    let output = match (marks_paper_a, marks_paper_b) {
        (50, 50) => "Full marks for both papers",
        (50, _) => "Full marks for paper A",
        (_, 50) => "Full marks for paper B",
        (x, y) if x > 25 && y > 25 => "Good",
        (_, _) => "Work hard",
    };
    println!("{}", output); // Work hard

    //    loop
    // Outer break
    let mut b1 = 1;
    'outer_loop: loop {
        //set label outer_loop
        let mut b2 = 1;
        'inner_loop: loop {
            println!("Current Value : [{}][{}]", b1, b2);
            if b1 == 2 && b2 == 2 {
                break 'outer_loop; // kill outer_loop
            } else if b2 == 5 {
                break 'inner_loop;
            };
            b2 += 1;
        }
        b1 += 1;
    }

    // Outer break
    'outer_for: for c1 in 1..6 {
        //set label outer_for
        'inner_for: for c2 in 1..6 {
            println!("Current Value 2 : [{}][{}]", c1, c2);
            if c1 == 2 && c2 == 3 {
                break 'outer_for;
            }; //kill outer_for
        }
    }

    // Working with arrays/vectors
    let group: [&str; 4] = ["Mark", "Larry", "Bill", "Steve"];

    for n in 0..group.len() {
        //group.len() = 4 -> 0..4 👎 check group.len()on each iteration
        println!("Current Person : {}", group[n]);
    }

    for person in group.iter() {
        //👍 group.iter() turn the array into a simple iterator
        println!("Current Person : {}", person);
    }

    //vec!
    let mut a2: Vec<i32> = Vec::new();
    let mut b2: Vec<i32> = vec![];
    let mut b3 = vec![9i32, 2, 3]; //sufixing 1st value with data type

    let mut b4 = vec![1, 2, 3];
    let mut b5: Vec<i32> = vec![1, 2, 3];
    let mut b6 = vec![1i32, 2, 3];
    let mut b7 = vec![0; 10]; //ten zeroes

    //vec! iterator
    let mut v = vec![1, 2, 3, 4, 5];

    for i in &v {
        println!("A reference to {}", i);
    }

    for i in &mut v {
        *i = *i + 2;
        println!("A mutable reference to {}", i);
    }

    for i in v {
        println!("Take ownership of the vector and its element {}", i);
    }

    println!(" {} : =============== start ", "rust quiz 1");
    let (.., x, y) = (1, 2, ..);

    println!("result : {} ", b"666"[y][x]);

    println!(" {} : ===============  end ", "rust quiz 1");

    println!(" {} : struct usage ===============  end ", " ========== ");
    // creating an instance
    let black = Color {
        red: 0,
        green: 0,
        blue: 0,
    };

    // accessing its fields using dot notation
    println!(
        "Black = rgb({}, {}, {})",
        black.red, black.green, black.blue
    ); //Black = rgb(0, 0, 0)

    // structs are immutable by default, use `mut` to make it mutable but doesn't support field level mutability
    let mut link_color = Color {
        red: 0,
        green: 0,
        blue: 255,
    };
    link_color.blue = 38;
    println!(
        "Link Color = rgb({}, {}, {})",
        link_color.red, link_color.green, link_color.blue
    ); //Link Color = rgb(0, 0, 238)

    // copy elements from another instance
    let blue = Color {
        blue: 255,
        ..link_color
    };
    println!("Blue = rgb({}, {}, {})", blue.red, blue.green, blue.blue); //Blue = rgb(100, 0, 255)

    // destructure the instance using a `let` binding, this will not destruct blue instance
    let Color {
        red: r,
        green: g,
        blue: b,
    } = blue;
    println!("Blue = rgb({}, {}, {})", r, g, b); //Blue = rgb(100, 10, 255)

    // creating an instance via functions & accessing its fields
    let midnightblue = get_midnightblue_color();
    println!(
        "Midnight Blue = rgb({}, {}, {}) ",
        midnightblue.red, midnightblue.green, midnightblue.blue
    ); //Midnight Blue = rgb(25, 25, 112)

    // destructure the instance using a `let` binding
    let Color {
        red: r,
        green: g,
        blue: b,
    } = get_midnightblue_color();
    println!("Midnight Blue = rgb({}, {}, {}) ", r, g, b); //Midnight Blue = rgb(25, 25, 112)

    // creating an instance
    let black = Color2(0, 0, 0);

    // destructure the instance using a `let` binding, this will not destruct black instance
    let Color2(r, g, b) = black;
    println!("Black = rgb({}, {}, {})", r, g, b); //black = rgb(0, 0, 0);

    //newtype pattern
    let distance = Kilometers(20);
    // destructure the instance using a `let` binding
    let Kilometers(distance_in_km) = distance;
    println!("The distance: {} km", distance_in_km); //The distance: 20 km
    println!(" {} : struct usage ===============  end ", " ========== ");

    println!(" {} : enum usage ===============  start ", " ========== ");
    let mut form_status = FlashMessage::Success;
    print_flash_message(form_status);
    form_status = FlashMessage::Warning {
        category: 2,
        message: String::from("Field X is required"),
    };
    print_flash_message(form_status);
    form_status = FlashMessage::Error(String::from("Connection Error"));
    print_flash_message(form_status);
    println!(" {} : enum usage ===============  end ", " ========== ");

    println!(" {} : generic usage =============== start", " ========== ");
    let point_a = Point { x: 0, y: 1 };
    let point_b = Point {
        y: 0.0f32,
        x: 1.2f32,
    };
    let point_c = Point { x: 0i32, y: 2i32 };

    println!(" {} : generic usage =============== end", " ========== ");

    println!(
        " {} : impl for struct =============== start",
        " ========== "
    );
    let p = Player {
        first_name: String::from("liu"),
        last_name: String::from("muqi"),
    };
    let string = p.full_name();

    println!("{:#?} user ,string:{}", &p, string);
    let string2 = p.full_name2();

    let player_2 = Player {
        first_name: "Roger".to_string(),
        last_name: "Federer".to_string(),
    };

    println!("Player 02: {}", player_2.full_name());
    println!("Player 02: {}", player_2.full_name_trait());
    println!(" {} : impl for struct =============== end", " ========== ");

    let kitty = base_concept::custom_impl_inherit::Cat {
        sound: "Meow".to_string(),
    };
    let the_bell = base_concept::custom_impl_inherit::Bell {
        sound: "Ding Dong".to_string(),
    };
    base_concept::custom_impl_inherit::make_sound(&kitty); // Meow!
    base_concept::custom_impl_inherit::make_sound(&the_bell); // Ding Dong!

    phrases::greetings::hello();
}

//=============lifetime on With Impls and Traits start=========================================
struct StructImpl<'a> {
    x: &'a str,
}

//impl<'b> StructImpl<'b> {
//    fn function<'b>(&self) -> &'b str {
//        self.x
//    }
//}

struct StructImplb<'a> {
    x: &'a str,
    y: &'a str,
}

mod phrases {
    fn private_fn() {
        println!("phrases mod private Hello, world!");
    }

    pub mod greetings {
        pub fn hello() {
            super::private_fn();
        }
    }
}

impl<'a> StructImplb<'a> {
    fn new(x: &'a str, y: &'a str) -> StructImplb<'a> {
        //no need to specify <'a> after new; impl already has it
        StructImplb { x: x, y: y }
    }
}
// 🔎
//fn function<F>(f: F) where for<'a> F: FnOnce(&'a String)
//struct Struct<F> where for<'a> F: FnOnce(&'a String) { x: F }
//enum Enum<F> where for<'a> F: FnOnce(&'a String) { Variant(F) }
//impl<F> Struct<F> where for<'a> F: FnOnce(&'a String) { fn x(&self) -> &F { &self.x } }
//impl<'a> Trait<'a> for Type
//impl<'a> Trait for Type<'a>
//=============lifetime on With Impls and Traits end=========================================

//=============lifetime on struct start=========================================
//Elements with references should attach lifetimes after & sign.
//After the name of the struct or enum, we should mention that the given lifetimes are generic types.
// single element
// data of x should live at least as long as Struct exists
struct Structa<'a> {
    x: &'a str,
}

// multiple elements
// data of x and y should live at least as long as Struct exists
struct Structb<'b> {
    x: &'b str,
    y: &'b str,
}

// variant with single element
// data of the variant should live at least as long as Enum exists
enum Enum<'a> {
    Variant(&'a String),
}
//=============lifetime on struct end=========================================

//=============lifetime on funcion start=========================================
//Lifetimes are denoted with an apostrophe. By convention, a lowercase letter is used for naming. Usually starts with 'a and follows alphabetic order when we need to add multiple lifetime annotations.
// no inputs, return a reference
fn function1<'t>() -> &'t str {
    "1".trim_end()
}

// single input
fn function2<'a>(x: &'a str) -> &'a str {
    "b".trim_end()
}

// single input and output, both has same lifetime
// output should live at least as long as input exists
fn function3<'a>(x: &'a str) -> &'a str {
    "c".trim_end()
}

// multiple inputs, only one input and the output share same lifetime
// output should live at least as long as y exists
fn function4<'a>(x: i32, y: &'a str) -> &'a str {
    "d".trim_end()
}

// multiple inputs. both inputs and the output share same lifetime
// output should live at least as long as x and y exist
fn function5<'a>(x: &'a str, y: &'a str) -> &'a str {
    "e".trim_end()
}

// multiple inputs. inputs can have diffent lifetimes 🔎
// output should live at least as long as x exists
fn function6<'a, 'b>(x: &'a str, y: &'b str) -> &'a str {
    "f".trim_end()
}

//=============lifetime on funcion end=========================================

// 01 - - - - - - - - - - - - - - - - - - - - - -
fn get_id_by_username(username: &str) -> Option<usize> {
    //if username can be found in the system, set userId
    return Some(123);
    //else
    None
}

//💭 So on above function, instead of setting return type as usize
// set return type as Option<usize>
//Instead of return userId, return Some(userId)
// else None (💡remember? last return statement no need return keyword and ending ;)

// 02 - - - - - - - - - - - - - - - - - - - - - -
struct Task {
    title: String,
    assignee: Option<usize>,
}

//💭 Instead of assignee: Person, we use Option<Person>
//Because the task has not been assigned to a specific person

// - - - - - - - - - - - - - - - - - - - - - - -
//when using Option types as return types on functions
// we can use pattern matching to catch the relevant return type(Some/None) when calling them

struct Point<T> {
    x: T,
    y: T,
}

struct PointCamel<B> {
    xa: B,
}

fn print_flash_message(m: FlashMessage) {
    // pattern matching with enum
    match m {
        FlashMessage::Success => println!("Form Submitted correctly : {:?}", FlashMessage::Success),
        FlashMessage::Warning { category, message } =>
        //Destructure, should use same field names
        {
            println!("Warning : {} - {}", category, message)
        }
        FlashMessage::Error(msg) => println!("Error : {}", msg),
    }
}

#[derive(Debug)]
enum FlashMessage {
    Success,
    //a unit variant
    Warning { category: i32, message: String },
    //a struct variant
    Error(String), //a tuple variant
}

struct Color2(u8, u8, u8);

struct Kilometers(i32);

// c like Struct Declaration
/* ⭐️ When regarding OOP in Rust, attributes and methods are placed separately on structs and traits. Structs contain only attributes, traits contain only methods. They are getting connected via impls.*/
struct Color {
    red: u8,
    green: u8,
    blue: u8,
}

fn get_midnightblue_color() -> Color {
    Color {
        red: 25,
        green: 25,
        blue: 112,
    }
}

fn plus_one(a: i32) -> i32 {
    a + 1
}

fn my_fn_1(x: &mut String) {
    println!("test2 &mut:{} ", x);
}

fn calc(i: i32) -> i32 {
    return i + 15;
}

fn test_fn2(_arg_name: String) -> String {
    //    if arg_name.is_empty(&arg_name) {
    //    } else {
    //        unimplemented!();
    //    }
    let mut s = String::with_capacity(100);
    s.push_str("中文222");
    return s;
}
