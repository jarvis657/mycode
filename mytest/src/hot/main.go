package main

/*
   #cgo linux LDFLAGS: -lrt
   #include <fcntl.h>
   #include <unistd.h>
   #include <sys/mman.h>

   #define FILE_MODE (S_IRUSR | S_IWUSR | S_IRGRP | S_IROTH)

   int my_shm_new(char *name) {
       //shm_unlink(name);
       return shm_open(name, O_RDWR|O_CREAT, FILE_MODE);
   }
*/
import "C"
import (
	"context"
	"errors"
	"flag"
	"fmt"
	"log"
	"net"
	"net/http"
	"os"
	"os/exec"
	"os/signal"
	"syscall"
	"time"
	"unsafe"
)

const SHM_NAME = "my_shm"
const SHM_SIZE = 8

type MyData struct {
	Count int
}

var (
	server   *http.Server
	listener net.Listener = nil
	data     *MyData      = nil

	graceful = flag.Bool("graceful", false, "listen on fd open 3 (internal use only)")
)

func handler(w http.ResponseWriter, r *http.Request) {
	data.Count++
	hostName, _ := os.Hostname()
	f, _ := w.(http.Flusher)
	fmt.Fprintf(w, "%s v1 count:%d ", hostName, data.Count)
	for i := 0; i < 20; i++ {
		fmt.Fprintf(w, "%s v1-%d\n", hostName, i)
		f.Flush()
		time.Sleep(1 * time.Second)
	}
	fmt.Fprintf(w, "\n")
}

func init() {
	fd, err := C.my_shm_new(C.CString(SHM_NAME))
	if err != nil {
		fmt.Println(err)
		return
	}

	C.ftruncate(fd, SHM_SIZE)

	ptr, err := C.mmap(nil, SHM_SIZE, C.PROT_READ|C.PROT_WRITE, C.MAP_SHARED, fd, 0)
	if err != nil {
		fmt.Println(err)
		return
	}
	C.close(fd)

	data = (*MyData)(unsafe.Pointer(ptr))
}

func main() {
	var err error

	flag.Parse()

	http.HandleFunc("/test", handler)
	server = &http.Server{Addr: ":8089"}

	if *graceful {
		log.Println("listening on the existing file descriptor 3")
		f := os.NewFile(3, "")
		listener, err = net.FileListener(f)
	} else {
		log.Println("listening on a new file descriptor")
		listener, err = net.Listen("tcp", server.Addr)
	}
	if err != nil {
		log.Fatalf("listener error: %v", err)
	}

	go func() {
		err = server.Serve(listener)
		log.Printf("server.Serve err: %v\n", err)
	}()

	handleSignal()
}

func handleSignal() {
	ch := make(chan os.Signal, 1)

	signal.Notify(ch, syscall.SIGINT, syscall.SIGTERM, syscall.SIGUSR2)
	for {
		sig := <-ch
		log.Printf("signal receive: %v\n", sig)
		ctx, _ := context.WithTimeout(context.Background(), 20*time.Second)
		switch sig {
		case syscall.SIGINT, syscall.SIGTERM:
			log.Println("shutdown")
			signal.Stop(ch)
			server.Shutdown(ctx)
			log.Println("graceful shutdown")
			return
		case syscall.SIGUSR2:
			log.Println("reload")
			err := reload()
			if err != nil {
				log.Fatalf("graceful reload error: %v", err)
			}
			server.Shutdown(ctx)
			log.Println("graceful reload")
			return
		}
	}
}

func reload() error {
	tl, ok := listener.(*net.TCPListener)
	if !ok {
		return errors.New("listener is not tcp listener")
	}
	f, err := tl.File()
	if err != nil {
		return err
	}
	args := []string{"-graceful"}
	cmd := exec.Command(os.Args[0], args...)
	cmd.Stdout = os.Stdout
	cmd.Stderr = os.Stderr
	cmd.ExtraFiles = []*os.File{f}

	return cmd.Start()
}

// 其它和main1.go一样，只是handler 返回的版本是v2
func handler2(w http.ResponseWriter, r *http.Request) {
	data.Count++
	hostName, _ := os.Hostname()
	f, _ := w.(http.Flusher)
	fmt.Fprintf(w, "%s v2 count:%d ", hostName, data.Count)
	for i := 0; i < 20; i++ {
		fmt.Fprintf(w, "%s v2-%d\n", hostName, i)
		f.Flush()
		time.Sleep(1 * time.Second)
	}
	fmt.Fprintf(w, "\n")
}
