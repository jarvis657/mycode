pub mod multi_thread_mod {
    use std::net::{TcpListener, TcpStream};
    use std::sync::{mpsc, Arc, Mutex};
    use std::thread;

    type Job = Box<dyn FnOnce() + Send + 'static>;

    //type定义别名，简化语法
    //特性对象绑定FnOnce(), Send, 生命期
    //这些东西和spawn closure的绑定一样，下面的类型只有一个，这个对象实际就是指向了closure
    //再此肯定clousre当作类型

    pub struct ThreadPool {
        //不直接保存线程
        workers: Vec<Worker>,
        sender: mpsc::Sender<Job>, //作为发送端
    }

    //    pub fn spawn<F, T>(f: F) -> JoinHandle<T>  //spawn返回JoinHandle, 但我们不用
    //        where
    //            F: FnOnce() -> T + Send + 'static,  //条件绑定，我们照抄。以后可以传递给spawn
    //        //Fn, FnMute, FnOnce, 表示closuer如何使用环境变量。FnOnce, 消费，夺取所有权
    //        //Send，将closure发送给其他线程，'static永久生命期
    //
    //        //f: F是一个closuer，FnOnce()是个trati, 说明closure可以绑定特性，那么也可以再加个send
    //        //closure的特性绑定是作为泛型的绑定，而不是函数的绑定（参数或返回值）
    //        //就是把closuer当作泛型一样使用
    //        //这样的大事，不记得明确讲过呀
    //
    //            T: Send + 'static  //返回值，不关心
    struct Worker {
        //私有即可
        id: usize,
        //用来识别
        thread: thread::JoinHandle<()>,
    }

    impl ThreadPool {
        // --snip--
        pub fn new(size: usize) -> ThreadPool {
            assert!(size > 0);
            let mut workers = Vec::with_capacity(size);
            let (sender, receiver) = mpsc::channel(); //建立通道
            let receiver = Arc::new(Mutex::new(receiver)); //共享，夺权
                                                           //            通道是mspc, 可以有多个发送者，只能一个接收者，所以上面的做法行不通。需要共享所有权，Rc<T>，多线程，Arc<T>, receiver需要可变又不能冲突，Mutex<T>, 所以需要Arc<Mutex<T>>
            for id in 0..size {
                workers.push(Worker::new(id, Arc::clone(&receiver)));
            }
            ThreadPool { workers, sender }
        }
        pub fn execute<F>(&self, f: F)
        where
            F: FnOnce() + Send + 'static,
        {
            let job = Box::new(f); //产生特性对象的实例，Box<closure>
            self.sender.send(job).unwrap(); //closure发送入通道
        }
    }

    impl Worker {
        fn new(id: usize, receiver: Arc<Mutex<mpsc::Receiver<Job>>>) -> Worker {
            let thread = thread::spawn(move || {
                //明确夺权。产生线程，并执行循环
                loop {
                    let job = receiver.lock().unwrap().recv().unwrap();
                    // Mutex lock, Mutex unwrap, Receiver recv, Receiver unwrap，lock释放
                    // mutex保证只有一个worker接收消息，recv()阻塞，直到收到消息
                    // job is Job: Box<FnOnce() + Send + 'static>
                    println!("Worker {} got a job; executing.", id);
                    job();
                    //                    (*job)();//说没问题,以后的版本会解决
                    // *  deref Box<closure> -> closure || {handle_connection(stream)} -> run closure
                    // 即worker一经建立，就开始等待请求处理请求，然后再等，每个如此
                }
            });
            Worker { id, thread }
        }
    }

    pub fn listen_port() {
        let listener = TcpListener::bind("127.0.0.1:7879").unwrap();
        let pool = ThreadPool::new(4); //使用类似thread::spawn的方式
        for stream in listener.incoming() {
            let stream = stream.unwrap();
            pool.execute(|| {
                handle_connection(stream);
            }); //这里要全面复习一下closure, 否则绝对懵逼
        }
    }

    fn handle_connection(mut stream: TcpStream) {}

    #[cfg(test)]
    mod test_listen {
        use crate::thread_web::multi_thread_web::multi_thread_mod::listen_port;

        #[test]
        fn test_m_start() {
            listen_port();
        }
    }
}
