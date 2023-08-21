pub struct Post {
    content: String,
}

pub struct DraftPost {
    //状态编码为类型
    content: String,
}
impl Post {
    pub fn new() -> DraftPost {
        DraftPost {
            content: String::new(),
        }
    }

    pub fn content(&self) -> &str {
        &self.content
    }
}

impl DraftPost {
    pub fn add_text(&mut self, text: &str) {
        self.content.push_str(text);
    }

    pub fn request_review(self) -> PendingReviewPost {
        //self, 自杀，转移给待审
        PendingReviewPost {
            content: self.content,
        }
    }
}

pub struct PendingReviewPost {
    content: String,
}

impl PendingReviewPost {
    pub fn approve(self) -> Post {
        //自杀
        Post {
            content: self.content,
        }
    }
}

#[test]
fn test_pattern_main() {
    let mut post = Post::new(); //生成DraftPost

    post.add_text("I ate a salad for lunch today"); //DraftPost，利用类型给状态编码

    let post = post.request_review(); //生成PendingReview, let, 手动转换状态

    let post = post.approve(); //手动

    assert_eq!("I ate a salad for lunch today", post.content()); //前面不能assert conent
}

//not OOP, 但看起来简洁得多
//优点，简单，减少了一些中间步骤，直接消除了出错的机会
//缺点，状态转换不是完全封装的
