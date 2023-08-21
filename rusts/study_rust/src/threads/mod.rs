pub use custom_thread::test_thread;
pub use share_data::share_convard;

mod custom_thread;
mod other_thread;
mod share_data;

pub fn cus_th() {
    custom_thread::test_thread();
}
