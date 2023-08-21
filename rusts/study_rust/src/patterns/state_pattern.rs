pub struct Post {
    state: Option<Box<dyn State>>,
    //枚举包含特性对象，绑定状态
    content: String,
}

impl Post {
    pub fn new() -> Post {
        //新帖
        Post {
            state: Some(Box::new(Draft {})), //特性对象，状态设为草稿，草稿需绑定状态
            content: String::new(),
        }
    }

    pub fn add_text(&mut self, text: &str) {
        //可变
        self.content.push_str(text); //写字
    }

    pub fn content(&self) -> &str {
        //        self.state.as_ref().unwrap().content(&self)  //根据状态返回内容
        self.state.as_ref().unwrap().content(self) //根据状态返回内容
                                                   //Option<T>.as_ref() -> Option<&T>
                                                   //unwrap()因为永远Some -> &Box<dyn State>特性对象
                                                   //deref coercion, &Box<dyn State> -> State （Box是指针，实施了deref)
                                                   //调用状态特性content(), 草稿，待审为空白，发表后返回内容

        //第一个self, 其实是&self, 但作为参数传进来，rust知道
        //应该是自动加参解参的一部分，automatic referencing and dereferencing
        //所以调用方法等不需要(*&self).state
        //第二个&self, 需要明确指明，因为rust不知道你要self, 还是&self（从试验看也许知道）
        //即便&(&self)也问题不大吧，也能自动解参，猜
        //这个问题在stackoverflow上看目前还在完善。感觉rust有很多黑语法
        //原则就是先按简单的写，然后cargo check
        //改成，&self.state.as_ref().unwrap().content(self)，也不出错
    }
    pub fn request_review(&mut self) {
        if let Some(s) = self.state.take() {
            //Option.take取出Some，夺权
            //得到特性绑定，调用方法
            self.state = Some(s.request_review()) //Some(Box<dyn State>)
        }
        //检查帖子状态，如果Some, 取出特性对象, 调用该对象request_review
        //如果草稿，返回pending_review特性对象。如果待审查，返回自身
    }

    pub fn approve(&mut self) {
        if let Some(s) = self.state.take() {
            self.state = Some(s.approve())
        } //类似审查，待审返回发表特性对象；草稿，发表返回自身
    }
}

trait State {
    fn request_review(self: Box<Self>) -> Box<dyn State>;
    //没有缺省，必须实施
    fn approve(self: Box<Self>) -> Box<dyn State>;
    fn content<'a>(&self, post: &'a Post) -> &'a str {
        "" //草稿，待审
    }
}

struct Draft {}

impl State for Draft {
    // --snip--
    fn request_review(self: Box<Self>) -> Box<dyn State> {
        //Box<self>,
        Box::new(PendingReview {})
    }

    fn approve(self: Box<Self>) -> Box<dyn State> {
        self //无权批准
    }
}

struct PendingReview {}

impl State for PendingReview {
    // --snip--
    fn approve(self: Box<Self>) -> Box<dyn State> {
        Box::new(Published {})
    }

    fn request_review(self: Box<Self>) -> Box<dyn State> {
        self
    }
}

struct Published {}

impl State for Published {
    fn request_review(self: Box<Self>) -> Box<dyn State> {
        self
    }

    fn approve(self: Box<Self>) -> Box<dyn State> {
        self
    }

    fn content<'a>(&self, post: &'a Post) -> &'a str {
        //指针 -> 指针，生命期
        &post.content //字符串
    }
}

//post无需关心各种操作的区别，通过state管理。不需要match
//比较复杂，有重复逻辑等
