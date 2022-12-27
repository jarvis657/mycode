use std::env;
use std::fs::File;
use std::io::Write;
use std::path::Path;
use std::process::Command;

fn main() {
    let path = env::var("OUT_DIR").unwrap_or("/home/lmq/".parse().unwrap());
    let path_buf = Path::new(&path).join("commit_id.rs");
    println!("path:=====================:{}", path_buf.to_str().unwrap());
    let mut file = File::create(path_buf.as_path()).unwrap();
    let output = Command::new("git")
        .arg("rev-parse")
        .arg("HEAD")
        .output()
        .expect("Failed git execed");
    let commit = String::from_utf8(output.stdout);
    let ci = format!(
        r#"pub const CURRENT_COMMIT_ID : &'static str = "{}";"#,
        commit.unwrap_or("invalid!!!".parse().unwrap())
    );
    file.write_all(ci.as_bytes());
}
