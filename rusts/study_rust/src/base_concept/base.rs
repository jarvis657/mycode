#![feature(exclusive_range_pattern)]
pub fn test_base() {
    let ref a: i32;
    //a = 1;  // expected &i32，consider borrowing here: `&1`
    a = &1;

    let ref a1 = 2;
    let a2 = &2;
    println!("ref a ={:#?}", a);
    println!("ref a1 ={}", a1);
    println!("a2 ={}", a2);
}

//#[non_exhaustive]
pub enum Error {
    NotFound,
    PermissionDenied,
    ConnectionRefued,
}

fn deep_match(v: Option<Option<i32>>) -> Option<i32> {
    match v {
        Some(r @ Some(1..=10)) | Some(r @ Some(90..=100)) => r,
        _ => None,
    }
}

#[derive(Debug)]
struct P(f32, f32, f32);

// #[feature(non_exhaustive)]
// #[test]
pub fn mytesttest() {
    fn calc(P(x, _, y): &P) -> f32 {
        x * x + y * y
    }
    fn test_param(p: impl AsRef<str>) {
        println!("p:{}", p.as_ref());
    }
    test_param("vv--------".to_string());
    test_param("vdsavda");


    let t = P(1.0, 2.0, 3.0);
    println!("{}", calc(&t));
    println!(" p {:?}", t);

    let x = Error::ConnectionRefued;
    match x {
        Error::ConnectionRefued => {}
        Error::PermissionDenied => {}
        Error::ConnectionRefued => {}
        _ => {}
    };

    #[feature(non_exhaustive)]
    enum OptionalInt {
        Value(i32),
        Missing,
    }
    // guard方式
    let xif = OptionalInt::Value(15);
    match xif {
        OptionalInt::Value(i) if i > 5 => println!("value:{}", i),
        OptionalInt::Value(..) => println!("got int"),
        OptionalInt::Missing => println!("miss"),
        _ => {}
    }
    let xi = 5;
    //变量绑定
    match xi {
        e @ 1..=5 => println!("e:{}", e),
        _ => {}
    };

    match xi {
        ref r => println!("got ref:{}", r), // r type : &i32
    }

    let mut vv = vec![1, 2, 3];
    vv.push(2);

    let mut mx = Some("hello".into());

    match &mut mx {
        Some(i) => *i = "vvv",
        _ => {}
    };
    println!("mx: {:?}", mx);

    let int_vec = [1, 2, 3];
    //    //数组切片
    for i in int_vec[1..2].iter() {
        println!("i : {}", i);
    }

    for i in &int_vec {
        println!("{:?}", i);
    }

    for i in int_vec.iter() {
        println!("{:?}", i);
    }

    let option = int_vec.iter().max();
    let f_v = [1.0_f32, 2.0, 3.0];
    //    let option1 = f_v.iter().max();
}


pub fn test_scope() {
    let x = 1;
    let x = x + 1;
    {
        let x = x * 10;
        println!("inner value :{}", x)
    }
    println!("outter value :{}", x)
}
