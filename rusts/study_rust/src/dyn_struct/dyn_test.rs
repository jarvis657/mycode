pub trait Draw {
    fn draw(&self);
    //    fn draw(&self) -> Self;
}

#[derive(Default)]
pub struct Screen {
    pub components: Vec<Box<dyn Draw>>,
}

//特性对象中的特性不能返回Self类型,也不能使用反型参数
//pub trait Clone {
//    fn clone(&self) -> Self;
//}
//
//pub struct Cm {
//    pub compens: Vec<Box<dyn Clone>>,
//}
