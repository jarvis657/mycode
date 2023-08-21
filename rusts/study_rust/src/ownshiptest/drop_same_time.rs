struct T {
    dropped: bool,
}
struct R<'a> {
    inner: Option<&'a T>,
}
impl T {
    fn new() -> Self {
        T { dropped: false }
    }
}

impl Drop for T {
    fn drop(&mut self) {
        self.dropped = true;
    }
}
impl<'a> R<'a> {
    fn new() -> Self {
        R { inner: None }
    }
    fn set_ref<'b: 'a>(&mut self, ptr: &'b T) {
        self.inner = Some(ptr);
    }
}
#[feature(generic_param_attrs, dropck_eyepatch)]
impl<'a> Drop for R<'a> {
    // unsafe impl<#[may_dangle] 'a> Drop for R<'a> {
    fn drop(&mut self) {
        if let Some(inner) = self.inner {
            println!("drop R when T is {}", inner.dropped);
        }
    }
}

// #[cfg(test)]
// mod tests {
#[test]
fn test_drop_same_time() {
    {
        let (a, mut b): (i32, Option<&i32>) = (1, None);
        b = Some(&a)
    }
    {
        let (mut a, b): (Option<&i32>, i32) = (None, 1);
        a = Some(&b);
    }
    {
        let (a, mut b): (T, R) = (T::new(), R::new());
        b.set_ref(&a);
    }
    {
        let (mut a, b): (R, T) = (R::new(), T::new());
        // a.set_ref(&b);
    }
}
// }
