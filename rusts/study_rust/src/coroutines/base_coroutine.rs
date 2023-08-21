use std::ops::{Generator, GeneratorState};

pub fn test_coroutine() {
    let mut g = || {
        let mut curr = 1u64;
        let mut next = 1u64;
        loop {
            let new_next = curr.checked_add(next);
            if let Some(nw_next) = new_next {
                curr = next;
                next = nw_next;
                yield curr;
            } else {
                return;
            }
        }
    };
    // loop {
    //     // unsafe {
    //     match g.resume() {
    //         GeneratorState::Yielded(v) => println!("{}", v),
    //         GeneratorState::Complete(v) => return,
    //     }
    //     // }
    // }
}
