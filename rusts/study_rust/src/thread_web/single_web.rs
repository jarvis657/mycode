#[allow(unused_variables)]
pub mod single_web_mod {
    use std::fs;
    use std::io::{Read, Write};
    use std::net::{TcpListener, TcpStream};

    pub fn listen_port() {
        let listener = TcpListener::bind("127.0.0.1:7878").unwrap();
        for stream in listener.incoming() {
            let stream = stream.unwrap();
            handle_connection(stream); //交权
        }
    }

    fn handle_connection(mut stream: TcpStream) {
        //TcpStream内部状态可能改变
        let mut buffer = [0; 512]; //array
        stream.read(&mut buffer).unwrap(); //读取TcpStream写入buffer
        println!("request: {}", String::from_utf8_lossy(&buffer[..])); //切片
                                                                       //from_utf8_lossy(), 用?替非法字符
                                                                       //        let response = "HTTP/1.1 200 OK\r\n\r\n";  //手动回复
                                                                       //        let contents = fs::read_to_string("thread_web/hello.html").unwrap();
                                                                       //        let response = format!("HTTP/1.1 200 OK\r\n\r\n{}", contents);
                                                                       //        stream.write(response.as_bytes()).unwrap();  //&[u8] -> bytes
                                                                       //        stream.flush().unwrap();  //阻塞知道写完
        route(Box::new(&buffer), stream);
        //        println!("{:?}", dbg!(stream));
    }

    fn route(buffer: Box<&[u8]>, mut stream: TcpStream) {
        let get = b"GET / HTTP/1.1\r\n"; //b

        if buffer.starts_with(get) {
            let contents = fs::read_to_string("hello.html").unwrap();
            let response = format!("HTTP/1.1 200 OK\r\n\r\n{}", contents);

            stream.write(response.as_bytes()).unwrap();
            stream.flush().unwrap();
        } else {
            //重复逻辑
            let status_line = "HTTP/1.1 404 NOT FOUND\r\n\r\n";
            let contents = fs::read_to_string("404.html").unwrap();
            let response = format!("{} {}", status_line, contents);

            stream.write(response.as_bytes()).unwrap();
            stream.flush().unwrap();
        }
    }
}

#[cfg(test)]
mod test_listen {
    use crate::thread_web::single_web::single_web_mod::listen_port;

    #[test]
    fn test_start() {
        listen_port();
    }
}
