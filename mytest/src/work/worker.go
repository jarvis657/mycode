package main

import (
	"log"
	"sync"
)

// a worker represents a goroutine
type worker struct {
	// id is a worker's unique attribute
	id int
	// channel is used to receive new works
	channel chan Workable
	// workerChannel holds all available worker's channel
	workerChannel chan chan Workable
	// end is used to receive end signal
	end chan bool
}

// start spawn a new goroutine which represents a worker.
// a worker is waiting for 2 channels
// 1. end signal
// 2. works to be done
// a worker sends its channel to workerChannel when having nothing to do
func (w *worker) start() {
	go func() {
		for {
			w.workerChannel <- w.channel
			select {
			case <-w.end:
				return
			case work1 := <-w.channel:
				work1.Do(w.id)
			}
		}
	}()
}

// stop sends end signal to the specified worker
func (w *worker) stop() {
	log.Printf("stopping worker[%d]\n", w.id)
	w.end <- true
}

// Collector is the bridge to communicate with workers
// which have
// 1. a work channel to receive new work
// and then send them to available workers.
// 2. an end channel to receive end signal
// and then send them to all workers.
type Collector struct {
	work chan Workable
	end  chan bool
	wg   sync.WaitGroup
}

// Send sends work to the specified worker pool
func (c *Collector) Send(work Workable) {
	c.work <- work
}

// End sends end signal to the specified worker pool
// and wait until all workers done
func (c *Collector) End() {
	c.end <- true
	c.wg.Wait()
}

// StartDispatcher starts specified numbered workers
// and starts a goroutine to schedule all workers
// returns a Collector pointer, through which we can send new
// work to workers or stop all.
func StartDispatcher(workerAmount int) *Collector {
	workerChannel := make(chan chan Workable, workerAmount)
	workers := make([]worker, workerAmount)

	input := make(chan Workable)
	end := make(chan bool)
	collector := Collector{
		work: input,
		end:  end,
	}
	collector.wg.Add(workerAmount)

	// init all workers
	// every worker has a unique channel, end
	// all workers share one workerChannel
	for i := range workers {
		workers[i] = worker{i, make(chan Workable), workerChannel, make(chan bool)}
		log.Printf("worker[%d] starting\n", i)
		workers[i].start()
	}

	// schedule Workers
	go func() {
		for {
			select {
			case <-end:
				for i := range workers {
					workers[i].stop()
					collector.wg.Done()
				}
				return
			case work1 := <-input:
				worker := <-workerChannel
				worker <- work1
			}
		}
	}()
	return &collector
}
