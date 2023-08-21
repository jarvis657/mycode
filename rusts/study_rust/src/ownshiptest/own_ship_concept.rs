use std::mem::size_of;

struct impportant<'a> {
    p: &'a str,
}

fn a_interface() {}

fn is_copy<T>(_val: T)
where
    T: Copy,
{
}

pub fn is_copy_main() {
    is_copy(true);
    is_copy('c');
    is_copy(42i8);
    is_copy(42i64);
    is_copy(42u64);
    is_copy(42isize);
    is_copy("hello");
    is_copy(a_interface);
    print!("test");
    println!("size of char            : {}", size_of::<char>());
    println!("size of u8            : {}", size_of::<u8>());
    println!("size of str            : {}", size_of::<&str>());
    println!("size of String            : {}", size_of::<String>());

    println!("size of isize            : {}", size_of::<isize>());
    println!("size of Option<isize>    : {}", size_of::<Option<isize>>());

    println!("size of &isize           : {}", size_of::<&isize>());
    println!("size of Box<isize>       : {}", size_of::<Box<isize>>());

    println!(
        "size of Option<&isize>     : {}",
        size_of::<Option<&isize>>()
    );
    println!(
        "size of Option<Box<isize>> : {}",
        size_of::<Option<Box<isize>>>()
    );

    println!("size of *const isize     : {}", size_of::<*const isize>());
    println!(
        "size of Option<*const isize> : {}",
        size_of::<Option<*const isize>>()
    );
}

pub fn base(x: i32) {
    let s: &'static str;
    {
        let r: i32 = 1;
        {
            let x = 5;
            //            r = &x;// open error
        }
        println!("r:{}", r);
        s = "i have a static lifetime!";
    }
    println!("print s:{}", s);
}

pub fn ship_fn_string(x: String, y: String) -> String {
    if x.len() > y.len() {
        x
    } else {
        y
    }
}

//本例中所有的指定生命周期的参数和返回值都至少存活同样时间
pub fn ship_fn<'a>(x: &'a str, y: &'a str) -> &'a str {
    let s;
    if x.len() > y.len() {
        s = x;
    } else {
        s = y;
    }
    let a = s;
    a
}

pub fn first_word(s: &str) -> &str {
    let bytes = s.as_bytes();
    for (i, &item) in bytes.iter().enumerate() {
        if item == b' ' {
            let x = &s[0..i];
            return x;
        }
    }
    &s[..]
}

#[cfg(test)]
mod own_ship_test {
    use crate::ownshiptest::own_ship_concept::{ship_fn, ship_fn_string};

    use super::super::own_ship_concept;

    //    use std::process::Termination;

    #[test]
    fn test_base() {}

    #[test]
    //    #[bench]
    //    fn test_ship_fn(b: &mut Bencher) -> impl Termination {
    fn test_ship_fn() {
        let s1 = String::from("abc");
        let s2 = "def";
        let s3 = String::from("vvv");
        let s4 = String::from("vvv");

        let ship_fn1 = ship_fn(&s1, s2);

        let ship_fn2 = ship_fn_string(s3, s4);
        println!("ship fn 1:{}", ship_fn1);

        println!("ship fn 2:{}", ship_fn2);
    }

    //    //复习生命期概念
    //    struct Context(&str);  //tuple struct, 单元素
    ////错误，结构中指针需要生命期，因为结构和引用的本源可能生命期不同
    //
    //    struct Parser {
    //        context: &Context,  //错误
    //    }
    struct Context<'a>(&'a str);
    //表示Context不能比&st活得长，即Conetx存在时&str必须合法，但context死掉&str可以存在

    //    struct Parser<'a> {
    //        context: &'a Context<'a>,  //parser, &Context, &str，同一生命期
    //    }
    struct Parser<'c, 's: 'c> {
        //'s >= 'c，这里有两个引用了，可以改
        context: &'c Context<'s>,
    }

    //
    //    //如果错误返回字符串
    //    impl Parser {
    //        fn parse(&self) -> Result<(), &str> {
    //            Err(&self.context.0[1..])  //Context中第一个元素，字符串去掉第一个字符
    //        }
    //    }
    impl<'a, 'b> Parser<'a, 'b> {
        fn parse(&self) -> Result<(), &str> {
            //lifetime elision, 只有一个参数，&self，省略生命期
            // fn parse<'a>(&'a self) -> Result<(), &'a str> {
            Err(&self.context.0[1..])
        }
    }

    //使用parse函数
    fn parse_context(context: Context) -> Result<(), &str> {
        //Context转移所有权,单一参数，省略生命期
        //    Parser { context: &context }.parse()  //出错
        Err("s")
    } //Parser, Context死掉
      //而parse函数返回&Parser.&Context.&str，而Parser, &Context, &str有共同生命期
      //所以编译器认为灭门惨案，&str是个孤儿，不科学
      //所以，唯一的办法就是告诉编译器，他们生命期不同，而且&str必须长于Context
      //这块the book讲的真不怎么样，又臭又长，能把明白讲成不明白

    #[test]
    fn test_advance_ship() {}
}
