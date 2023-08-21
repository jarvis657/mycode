use std::any::Any;
use std::borrow::Borrow;
use std::f64::consts::PI;

struct Rectangle {
    width: u32,
    height: u32,
}

struct Circle {
    x: u32,
    y: u32,
    radius: u32,
}

//如果要向下转型 需要实现Any 可把'a替换成'static 实现类就不用写lifetime了
trait IShape<'a>: Any + 'a {
    fn as_any(&self) -> &dyn Any;
    fn area(&self) -> f32;
    fn to_string(&self) -> String;
}

impl Rectangle {
    fn r(&self) {
        println!("Rectangle private fn");
    }
}

impl Circle {
    fn c() {
        println!("Circle private fn");
    }
}

impl<'a> IShape<'a> for Rectangle {
    fn as_any(&self) -> &dyn Any {
        self
    }
    fn area(&self) -> f32 {
        (self.height * self.width) as f32
    }
    fn to_string(&self) -> String {
        format!(
            "Rectangle -> width={} height={} area={}",
            self.width,
            self.height,
            self.area()
        )
    }
}

impl<'a> IShape<'a> for Circle {
    fn as_any(&self) -> &dyn Any {
        self
    }
    fn area(&self) -> f32 {
        (self.radius * self.radius) as f32 * PI as f32
    }
    fn to_string(&self) -> String {
        format!("Circle -> x={}, y={}, area={}", self.x, self.y, self.area())
    }
}


pub fn test_inherit() {
    use std::vec::Vec;

    let rect = Box::new(Rectangle {
        width: 4,
        height: 6,
    });
    let circle = Box::new(Circle {
        x: 0,
        y: 0,
        radius: 5,
    });
    let mut v: Vec<Box<dyn IShape>> = Vec::new();
    v.push(rect);
    v.push(circle);

    for i in v.iter() {
        println!("area={}", i.area());
        println!("{}", i.to_string());

        if let Some(s) = i.as_any().downcast_ref::<Rectangle>() {
            s.r();
            println!("downcast - Rectangle w={}, h={}", s.width, s.height);
        } else if let Some(s) = i.as_any().downcast_ref::<Circle>() {
            println!("downcast - Circle x={}, y={}, r={}", s.x, s.y, s.radius);
        } else {
            println!("invaild type");
        }
    }
}
