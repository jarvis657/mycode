use crate::base_concept::structs::*;

pub trait Summary {
    fn summarize(&self) -> String {
        String::from("read more")
    }
}

impl Summary for TraitStArticle {
    fn summarize(&self) -> String {
        return format!("impl summary for trait st article");
    }
}

impl Summary for TraitStUser {
    fn summarize(&self) -> String {
        return format!("impl summary for trait st user");
    }
}

//true for user false for article
pub fn returns_summarizable(switch: bool) -> Box<dyn Summary> where {
    if !switch {
        //error when open
        //        #[derive(Debug)]
        Box::new(TraitStArticle {
            headline: "headline".to_string(),
            location: "location".to_string(),
            author: "author".to_string(),
            content: "content".to_string(),
        })
    } else {
        Box::new(TraitStUser {
            username: "qishan".to_string(),
            age: 0,
            location: "location".to_string(),
        })
    }
}
