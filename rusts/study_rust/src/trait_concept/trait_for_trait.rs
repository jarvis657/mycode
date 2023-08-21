// #![feature(specialization)]

use std::fmt::Display;
use std::future::Future;

extern crate http;

trait Shape {
    fn area(&self) -> f64;
}

trait Round {
    fn get_radius(&self) -> f64;
    //    fn default() -> Self;
}

trait MyDefault {
    fn default() -> Self;
}

#[derive(Clone, Copy)]
struct Circle {
    radius: f64,
}

#[derive(Clone, Copy, Debug)]
struct MCircle {}

impl Circle {
    fn st(this: &Self) -> f64 {
        this.radius
    }
}

impl Round for Circle {
    fn get_radius(&self) -> f64 {
        self.radius
    }

    //    fn default() -> Self {
    //        Self
    //    }
}

impl Round for MCircle {
    fn get_radius(&self) -> f64 {
        unimplemented!()
    }
}

//impl Shape for dyn Round {
//    fn area(&self) -> f64 {
//        self.get_radius() * self.get_radius() * std::f64::consts::PI
//    }
//}

//pub trait Service {
//    type Request;
//    type Response;
//    type Error;
//    type Future: Future<Item=Self::Response, Error=Self::Error>;
//    fn call(&self, req: Self::Request) -> Self::Future;
//}

//trait HttpService = Service<Request=http::Request, Response=http::Response, Error=http::Error>;
pub trait Example {
    fn call_e(&self);
}

impl<T> Example for T {
    default fn call_e(&self) {
        println!("most T call !!!!!!!!!!");
    }
}

impl<T> Example for T
where
    T: Display,
{
    default fn call_e(&self) {
        println!("generic T call !!!!!!!!!!");
    }
}

impl Example for str {
    fn call_e(&self) {
        println!("str  call !!!!!!!!!!");
    }
}

trait Foo {
    fn default_impl(&self) {
        println!("corrent impl!")
    }
}

impl Foo {
    fn trait_object() {
        println!("trait object impl")
    }
}

struct Bar {}

impl Foo for Bar {}

pub fn  test_trait_object(){
    let b = Bar {};
    b.default_impl();
    Foo::trait_object();
}

#[allow(unused_variables)]
#[cfg(test)]
mod tests {
    use crate::trait_concept::trait_for_trait::{Circle, Round, test_trait_object};
    use std::ops::Deref;

    #[test]
    fn it_works() {
        test_trait_object();
        let circle = Circle { radius: 2f64 };
        let c = circle;
        // c.area() not work
        let b = Box::new(circle); // circle 指针
        println!("1 data:{}, st:{}", b.get_radius(), Circle::st(&circle));
        let b = Box::new(Circle { radius: 3.2f64 }) as Box<dyn Round>;
        b.get_radius();

        println!("2 data:{}", b.get_radius());
    }
}
