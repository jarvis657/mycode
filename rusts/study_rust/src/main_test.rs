#[cfg(test)]
mod main_test {
    use super::super::*;
    use crate::base_concept::structs::{TraitStArticle, TraitStUser};

    pub fn other_test_hello() {
        println!("Hello, world!");
    }

    #[test]
    //    #[should_panic(expected = "guess value must be less than or equal to 100")]
    fn main_test_works() -> Result<(), String> {
        {
            let summarizable = trait_concept::basic::returns_summarizable(false);
            let string = summarizable.summarize();
            println!("summarizable {}", string)
        }
        Ok(())
    }

    #[test]
    fn largest_test() {
        let number_list = vec![34, 26, 100, 1, 3];
        let x1: Vec<_> = number_list.iter().map(|x| x + 1).collect();

        let result = largest(&number_list);
        println!("the largest num:{}", result);
        let char_list = vec!['y', 'a', 'c', 'b', 'm', 'z'];

        //
        //        let x2: Vec<_> = char_list.into_iter().map(|x| {
        //            let uppercase = x.to_uppercase();
        //            uppercase as char
        //        }).collect();
        //        let char_up_list = vec!['Y', 'A', 'C', 'B', 'M', 'Z'];
        //        assert_eq!(char_up_list, x2);
        //        println!("the char_list upper char:{:?}", x2);

        let result_char = largest(&char_list);
        println!("the largest char:{}", result_char);
    }

    //    fn largest<T: PartialOrd + Copy>(list: &[T]) -> T {
    fn largest<T: PartialOrd + Copy>(list: &[T]) -> T {
        let mut largest = list[0];
        for &item in list.iter() {
            if item > largest {
                largest = item
            }
        }
        largest
    }
}
