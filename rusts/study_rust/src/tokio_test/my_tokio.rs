// use exitfailure::ExitFailure;
// use reqwest::Url;
// use serde_derive::{Deserialize, Serialize};
// use structopt::StructOpt;

// #[derive(Debug, StructOpt)]
// #[structopt(name = "input", about = "An example of StructOpt usage.")]
// struct Input {
//     city: String,
// }

// #[derive(Serialize, Deserialize, Debug)]
// struct Coord {
//     lon: f64,
//     lat: f64,
// }

// #[derive(Serialize, Deserialize, Debug)]
// struct Weather {
//     details: Details,
// }

// #[derive(Serialize, Deserialize, Debug)]
// struct Details {
//     id: i32,
//     main: String,
//     desc: String,
//     icon: String,
// }

// #[derive(Serialize, Deserialize, Debug)]
// struct Main {
//     temp: f64,
//     feels_like: f64,
//     temp_max: f64,
//     pressure: i32,
//     humidity: i32,
// }

// #[derive(Serialize, Deserialize, Debug)]
// struct W {
//     coord: Coord,
//     weather: Weather,
//     base: String,
//     main: Main,
// }

// #[derive(Serialize, Deserialize, Debug)]
// struct Ip {
//     origin: String,
// }

// impl Ip {
//     async fn get(city: &String) -> Result<Self, ExitFailure> {
//         // let url = format!("http://httpbin.org/ip");
//         // let url = Url::parse(&*url)?;
//         let resp = reqwest::get("http://httpbin.org/ip").await?.json::<Ip>().await?;
//         println!("{:?}", resp);
//         Ok(resp)
//     }
// }

use std::sync::Arc;
// #[tokio::main]
// async fn main() -> Result<(), ExitFailure> {
//     for i in [1, 2, 3] {
//         println!("i:{}", i)
//     }
//     Input::
//     let op = Input::from_args();
//     let get1 = Ip::get(&op.city);
//     println!("{:?}", get1.await?);
//     Result::Ok(())
// }
use std::time::Duration;

use tokio::sync::mpsc::unbounded_channel;
use tokio::sync::Mutex;
use tokio::time::sleep;

#[derive(Debug)]
enum Message {
    SendWelcomeEmail { to: String, name: String },
    DownloadVideo { id: usize },
    GenerateReport,
    Terminate,
}

// #[tokio::main]
pub async fn main() {
    // Here, we’re using an unbounded channel, which is an async alternative to the MPSC channel in the standard library.
    // In production, I’d strongly recommend using tokio::sync::mpsc::channel,
    // a limited-size channel that provides back pressure when your application is under load to prevent it from being overwhelmed.
    // The receiver is also wrapped in an Arc and a Tokio Mutex because it will be shared between multiple workers.
    // This won’t compile yet because it can’t infer the type of values we’re going to send through the channel.
    // let (sender, receiver) = unbounded_channel();
    // let receiver = Arc::new(Mutex::new(receiver));
    // let size = 5;
    // let mut workersAc = Arc::new(Vec::with_capacity(size));
    // for id in 0..size {
    //     let receiver = Arc::clone(&receiver);
    //     let workers = Arc::clone(&workersAc);
    //     // let worker = tokio::spawn(async move { /* ... */ });
    //     let worker = tokio::spawn(async move {
    //         //The reason you don’t use while let Some(message) = receiver.lock()... is that it wouldn't drop the mutex guard until after the content of the while loop executes.
    //         // That means the mutex would be locked while you process the message and only one worker could work at a time.
    //         // while let Some(message) = receiver.lock() {
    //         //
    //         // }
    //         loop {
    //             let message = receiver
    //                 .lock()
    //                 .await
    //                 .recv()
    //                 .await
    //                 .unwrap_or_else(|| Message::Terminate);
    //             println!("Worker {}: {:?}", id, message);
    //             match message {
    //                 Message::Terminate => break,
    //                 _ => sleep(Duration::from_secs(1 + id as u64)).await,
    //             }
    //         }
    //     });
    //     workers.push(worker);
    //     for _ in *workers {
    //         let _ = sender.send(Message::Terminate);
    //     }
    //     for worker in *workers {
    //         let _ = worker.await;
    //     }
    // }
}

// #[tokio::main]
pub async fn test_my_tokio_main() {
    // tokio::runtime::Runtime::new().unwrap().block_on(async {
    //     println!("Hello world");
    // })
    let (v1, v2, v3) = tokio::join!(
        async {
            sleep(Duration::from_millis(1000 * 4)).await;
            println!("Value 1 ready");
            "Value 1"
        },
        async {
            tokio::task::spawn_blocking(|| {
                std::thread::sleep(Duration::from_millis(1000 * 5));
                println!("Value 2 sleep done");
            });
            println!("Value 2 ready");
            "Value 2"
        },
        // async {
        //     sleep(Duration::from_millis(2800)).await;
        //     // std::thread::sleep(Duration::from_millis(3000))
        //     println!("Value 2 ready");
        //     "Value 2"
        // },
        async {
            sleep(Duration::from_millis(1000 * 2)).await;
            println!("Value 3 ready");
            "Value 3"
        },
    );

    assert_eq!(v1, "Value 1");
    assert_eq!(v2, "Value 2");
    assert_eq!(v3, "Value 3");
}
