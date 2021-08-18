pub fn test_base() {
    let ref a: i32;
    //a = 1;  // expected &i32，consider borrowing here: `&1`
    a = &1;

    let ref a1 = 2;
    let a2 = &2;
}