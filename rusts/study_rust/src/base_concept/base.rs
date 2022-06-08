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

pub fn test_scope() {
    let x = 1;
    let x = x + 1;
    {
        let x = x * 10;
        println!("inner value :{}", x)
    }
    println!("outter value :{}", x)
}
