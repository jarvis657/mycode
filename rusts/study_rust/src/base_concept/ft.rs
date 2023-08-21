#[feature(const_fn)]
pub fn ft_test() -> () {
    use std::sync::atomic::AtomicBool;
    static FLAG: AtomicBool = AtomicBool::new(true);
    println!("static flag:{:#?}", FLAG);
}
