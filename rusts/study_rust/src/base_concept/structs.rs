use std::fmt::Display;

//use crate::trait_concept::basic::Summary;

#[derive(PartialEq, PartialOrd)] //衍生属性
pub struct TraitStArticle {
    pub headline: String,
    pub location: String,
    pub author: String,
    pub content: String,
}

impl std::fmt::Debug for TraitStArticle {
    fn fmt(&self, f: &mut std::fmt::Formatter) -> std::fmt::Result {
        write!(
            f,
            "headline:{},location:{},author:{},content:{}",
            self.headline, self.author, self.location, self.content
        )
    }
}

impl std::fmt::Display for TraitStArticle {
    fn fmt(&self, f: &mut std::fmt::Formatter) -> std::fmt::Result {
        write!(
            f,
            "headline:{},location:{},author:{},content:{}",
            self.headline, self.author, self.location, self.content
        )
    }
}

#[derive(PartialEq, PartialOrd, Debug)] //衍生属性
pub struct TraitStUser {
    pub username: String,
    pub age: u8,
    pub location: String,
}

//impl std::fmt::Debug for TraitStUser {
//    fn fmt(&self, f: &mut std::fmt::Formatter) -> std::fmt::Result {
//        write!(f, "username:{},age:{},location:{}", self.username, self.age, self.location)
//    }
//}

impl std::fmt::Display for TraitStUser {
    fn fmt(&self, f: &mut std::fmt::Formatter) -> std::fmt::Result {
        write!(
            f,
            "username:{},age:{},location:{}",
            self.username, self.age, self.location
        )
    }
}

struct Pair<T> {
    x: T,
    y: T,
}

impl<T> Pair<T> {
    fn new(x: T, y: T) -> Self {
        Self { x, y }
    }
}

impl<T: Display + PartialOrd> Pair<T> {
    fn cmp_display(&self) {
        if self.x > self.y {
            println!("largest x> y:{}", self.x)
        } else {
            println!("largest x< y:{}", self.y)
        }
    }
}
