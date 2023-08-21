use std::fs::File;
use std::io::{BufRead, BufReader, ErrorKind};

pub fn test_file_op() {
    let file = File::open("src/lib1.rs").expect("file not found");
    let buf_reader = BufReader::new(file);

    for line in buf_reader.lines() {
        println!("{}", line.unwrap());
    }
    let f = File::open("hello.txt");

    let f = match f {
        Ok(file) => file,
        Err(error) => match error.kind() {
            ErrorKind::NotFound => match File::create("hello.txt") {
                Ok(fc) => fc,
                Err(e) => panic!("Problem creating the file: {:?}", e),
            },
            other_error => panic!("Problem opening the file: {:?}", other_error),
        },
    };
    let ff = File::open("hello2.txt").unwrap_or_else(|error| {
        if error.kind() == ErrorKind::NotFound {
            File::create("hello.txt").unwrap_or_else(|error| {
                panic!("Problem creating the file: {:?}", error);
            })
        } else {
            panic!("Problem opening the file: {:?}", error);
        }
    });
    // let x1 = file!("src/main_test.rs");

    //    fs::create_dir("some_dir").expect("Can not create the directry!");
    //    File::create("some_dir/empty.txt").expect("Can not create the file!");
}
