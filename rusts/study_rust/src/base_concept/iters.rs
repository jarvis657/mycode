use std::path::Iter;

use crate::base_concept::custom_impl_inherit::read_ext_read;
use crate::base_concept::structs::TraitStArticle;

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

#[cfg(test)]
mod test {
    use crate::base_concept::iters::Counter;

    #[test]
    fn test_iter() {
        let mut counter = Counter::new();
        assert_eq!(counter.next(), Some(1));
        assert_eq!(counter.next(), Some(2));
        assert_eq!(counter.next(), Some(3));
        assert_eq!(counter.next(), Some(4));
        assert_eq!(counter.next(), Some(5));
        assert_eq!(counter.next(), None);

        let sum = Counter::new()
            .zip(Counter::new().skip(2))
            .map(|(a, b)| a * b)
            .filter(|x| x % 3 == 0)
            .sum();
        assert_eq!(18u32, sum);
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
