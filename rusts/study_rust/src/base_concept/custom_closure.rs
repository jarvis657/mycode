use std::collections::HashMap;
use std::fmt::Error;

use crate::calc;

struct Cacher<T>
    where
        T: Fn(u32, &str) -> u32,
{
    calculation: T,
    value: HashMap<u32, u32>,
}

impl<T> Cacher<T>
    where
        T: Fn(u32, &str) -> u32,
{
    pub fn new(cal: T) -> Cacher<T> {
        Cacher {
            calculation: cal,
            value: HashMap::new(),
        }
    }
    pub fn value(&mut self, arg: u32, desc: &str) -> u32 {
        //when value is Option
        //        match self.value {
        //            None => {
        //                let v = (self.calculation)(arg, desc);
        //                self.value = Some(v);
        //                v
        //            }
        //            Some(v) => v,
        //        }
        //when value is hashmap
        *self
            .value
            .entry(arg)
            .or_insert((self.calculation)(arg, desc))
    }
}

//#[cfg(test)]
mod tests {
    use std::error::Error;
    use std::thread;
    use std::time::Duration;

    use crate::base_concept::custom_closure::Cacher;

    struct Foo {
        a: i32,
        b: i32,
    }

    pub fn test_clo<T>(abc: T) -> T
        where
            T: Copy,
    {
        let c1 = |x| x;
        return c1(abc);
    }

    //    use std::error::Error;
    //    use crate::base_concept::custom_closure::test_clo;
    //
    #[test]
    fn test_test_clo() {
        let clo = test_clo("abc");
        println!("haha test_test_clo :{}", clo);
    }

    #[test]
    fn test_closure() {
        let mut cacher = Cacher::new(move |x, desc| {
            println!("calc doing,x is :{} desc:{}", x, desc);
            thread::sleep(Duration::from_secs(2));
            x
        });
    }

    #[test]
    fn test_closure2() {
        let mut f = Foo { a: 5, b: 6 };
        let a = &mut f.a;
        let baz = || &mut f.b;
        let b = baz();
    }
}
