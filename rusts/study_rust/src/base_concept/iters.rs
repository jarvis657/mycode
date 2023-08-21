use std::path::Iter;

use crate::base_concept::custom_impl_inherit::read_ext_read;
use crate::base_concept::structs::TraitStArticle;
use core::fmt;
use std::fmt::Display;

pub fn test_iter(articles: Vec<TraitStArticle>, sz: u32) -> Vec<TraitStArticle> {
    let vec = articles
        .into_iter()
        .filter(|a| {
            let i = a.author.len();
            sz == (i as u32)
        })
        .collect();
    vec
}

pub struct Counter {
    pub count: u32,
}

impl Counter {
    pub fn new() -> Counter {
        Counter { count: 0 }
    }
}

impl Iterator for Counter {
    type Item = u32;

    fn next(&mut self) -> Option<Self::Item> {
        self.count += 1;
        if self.count < 6 {
            Some(self.count)
        } else {
            None
        }
    }
}

pub fn use_iter<ITEM, ITER>(mut iter: ITER)
where
    ITER: Iterator<Item = ITEM>,
    ITEM: fmt::Debug,
{
    while let Some(i) = iter.next() {
        println!("{:?}", i);
    }
}

pub fn foo_iter(n: i32) -> impl Iterator<Item = i32> {
    (0..n).map(|x| x * 100)
}

fn consume_iter_static<I: Iterator<Item = u8>>(iter: I) {
    //fn consume_iter_static<I: Iterator>(iter: I) {
    println!("static")
}

fn consume_iter_dynamic(iter: Box<dyn Iterator<Item = u8>>) {
    println!("dynamic")
}

fn multiply(m: i32) -> Box<dyn Fn(i32) -> i32> {
    Box::new(|m| m * 20)
}

fn multiply2<T>(m: T) -> impl Fn(T) -> T {
    |m| m
}

//错误写法
//fn multiply3<T: Fn(i32) -> i32>(m: i32) -> T {}

#[cfg(test)]
mod test {
    use crate::base_concept::iters::{use_iter, Counter};

    #[test]
    fn test_iter() {
        let mut counter = Counter::new();
        assert_eq!(counter.next(), Some(1));
        assert_eq!(counter.next(), Some(2));
        assert_eq!(counter.next(), Some(3));
        assert_eq!(counter.next(), Some(4));
        assert_eq!(counter.next(), Some(5));
        assert_eq!(counter.next(), None);

        let sum :u32 = Counter::new()
            .zip(Counter::new().skip(2))
            .map(|(a, b)| a * b)
            .filter(|x| x % 3 == 0)
            .sum();
        assert_eq!(18u32, sum);
        let v = vec![1, 2, 3, 4, 5, 6, 7];
        use_iter(v.iter());
    }

    #[test]
    fn test_match() {
        let x = 4;
        let y = false;
        match x {
            4 | 5 | 6 if y => println!("4,5,6"),
            7..=10 => println!("7...10"),
            _ => println!("none"),
        };
    }
}
