//====================
use std::io::Cursor;
use std::io::{Error, ErrorKind, Read, Result};

//⭐️ As you can see methods take a special first parameter, the type itself. It can be either self, &self, or &mut self; self if it’s a value on the stack (taking ownership), &self if it’s a reference, and &mut self if it’s a mutable reference.
pub trait FullName {
    fn full_name_trait(&self) -> String;
}

impl FullName for Player {
    fn full_name_trait(&self) -> String {
        format!("{} {}", self.first_name, self.last_name)
    }
}

//====== impl for struct
#[derive(Debug)]
pub struct Player {
    pub first_name: String,
    pub last_name: String,
}

impl Player {
    //Impls with Associated functions
    //Some other languages support static methods. At such times, we call a function directly through the class without creating an object. In Rust, we call them Associated Functions. we use :: instead of . when calling them from struct.
    //ex. Person::new(“Elon Musk Jr”);
    //    / we have used :: notation for `new()` and . notation for `full_name()`
    // 🔎 Also in here we have used `Method Chaining`. Instead of using two statements for new() and full_name()
    // calls, we can use a single statement with Method Chaining.
    // ex. player.add_points(2).get_point_count();
    pub fn new(first_name: String, last_name: String) -> Player {
        Player {
            first_name: first_name,
            last_name: last_name,
        }
    }
    pub fn full_name(&self) -> String {
        format!("{} {}", self.first_name, self.last_name)
    }
    pub fn full_name2(&self) -> String {
        format!("haha2 : {}", self.first_name)
    }
}

//====== impl for struct end
//==============================================
//Trait objects
//🔎 While Rust favors static dispatch, it also supports dynamic dispatch through a mechanism called ‘trait objects.’
pub trait GetSound {
    fn get_sound(&self) -> String;
}

pub struct Cat {
    pub sound: String,
}

impl GetSound for Cat {
    fn get_sound(&self) -> String {
        self.sound.clone()
    }
}

pub struct Bell {
    pub sound: String,
}

impl GetSound for Bell {
    fn get_sound(&self) -> String {
        self.sound.clone()
    }
}

pub fn make_sound<T: GetSound>(t: &T) {
    println!("{}!", t.get_sound())
}

//==============================================
//Traits with generics
pub trait From<T> {
    fn from(t: T) -> Self;
}

impl From<u8> for u16 {
    fn from(_: u8) -> Self {
        unimplemented!()
    }
}

//=========trait inherits===========
pub trait PersonI {
    fn full_name(&self) -> String;
}

//Employee inherit from person trait
pub trait Employee: PersonI {
    fn job_title(&self) -> String;
    fn job_test2(&self) -> String {
        let mut string = String::new();
        string.push_str("jobtest2");
        return string;
    }
}

//ExpatEmployee inherit from Employee and Expat traits
pub trait ExpatEmployee: Employee + FullName {
    fn additional_tax(&self) -> f64;
}

trait ReadExt: Read {
    fn read_u8(&mut self) -> Result<u8> {
        let mut bytes = [0u8; 1];
        let size = self.read(&mut bytes[..])?;
        if size != 1 {
            return Err(Error::new(ErrorKind::UnexpectedEof, "read not one bytes"));
        }
        println!("read_u8 invoke onece :{:#?} ", bytes);
        Ok(bytes[0])
    }
}

impl<T: Read> ReadExt for T {}

pub fn read_ext_read() {
    let mut cursor = Cursor::new(vec![0u8, 1, 2]);
    let mut value = cursor.read_u8().unwrap();
    println!("read u8: {}", value);
    value = cursor.read_u8().unwrap();
    println!("read u8: {}", value);
}
