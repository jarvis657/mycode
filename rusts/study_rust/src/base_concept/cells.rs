use std::cell::{Cell, RefCell};

struct ThingWithCell {
    counter: Cell<u8>,
}

impl ThingWithCell {
    fn new() -> Self {
        ThingWithCell {
            counter: Cell::new(0),
        }
    }
    fn increment(&self) {
        let i = self.counter.get() + 1;
        self.counter.set(i);
    }
    fn count(&self) -> u8 {
        self.counter.get()
    }
}

struct ThingWithRefCell {
    counter: RefCell<u8>,
}

impl ThingWithRefCell {
    fn new() -> Self {
        ThingWithRefCell {
            counter: RefCell::new(0),
        }
    }
    fn increment(&self) {
        let mut counter = self.counter.borrow_mut();
        *counter = *counter + 1;
    }
    fn count(&self) -> u8 {
        *self.counter.borrow_mut()
    }
}

//cell 数据是Copy类型, RefCell无要求,测试下数据是否Copy了
//cell 与refCell的区别:1. cell 提供值,refCell提供引用. 2.cell不异常,refCell能panic
pub fn test_cell() {
    let cell = ThingWithCell::new();
    println!("cell counter pointer:{:p}", cell.counter.as_ptr());
    cell.increment();
    println!("cell counter pointer:{:p}", cell.counter.as_ptr());
    let with_ref_cell = ThingWithRefCell::new();
    println!(
        "ref cell counter pointer:{:p}",
        with_ref_cell.counter.as_ptr()
    );
    with_ref_cell.increment();
    println!(
        "ref cell counter pointer:{:p}",
        with_ref_cell.counter.as_ptr()
    );
}
