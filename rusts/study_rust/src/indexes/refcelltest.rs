pub trait Messager {
    fn send(&self, msg: &str);
}

//pub struct LimitTracker<'a, T: 'a + Messager> {
pub struct LimitTracker<'a, T: 'a>
where
    T: Messager,
{
    messenger: &'a T,
    value: usize,
    max: usize,
}

impl<'a, T> LimitTracker<'a, T>
where
    T: Messager,
{
    pub fn new(messenger: &'a T, max: usize) -> LimitTracker<T> {
        LimitTracker {
            messenger,
            value: 0,
            max,
        }
    }
    pub fn set_value(&mut self, value: usize) {
        self.value = value;
        let per_of_max = self.value as f64 / self.max as f64;
        if per_of_max >= 0.75 && per_of_max < 0.9 {
            self.messenger.send("over 0.75 ");
        } else if per_of_max >= 0.9 && per_of_max < 1.0 {
            self.messenger.send("over 0.9 ");
        } else if per_of_max >= 1.0 {
            self.messenger.send("over 1.0 ");
        }
    }
}

#[cfg(test)]
mod tests {
    use std::cell::RefCell;

    use crate::indexes::refcelltest::Messager;

    struct MockMessager {
        sent_message: RefCell<Vec<String>>,
    }

    impl MockMessager {
        fn new() -> MockMessager {
            MockMessager {
                sent_message: RefCell::new(vec![]),
            }
        }
    }

    impl Messager for MockMessager {
        fn send(&self, msg: &str) {
            self.sent_message.borrow_mut().push(String::from(msg))
        }
    }

    #[test]
    fn test_mock() {
        let messager = MockMessager::new();
        messager.send("haha");
        let vec = messager.sent_message.into_inner();
        assert_eq!(vec[0], "haha")
    }
}
