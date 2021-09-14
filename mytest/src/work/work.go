package main

import (
	"hash/fnv"
	"log"
	"math/rand"
	"time"
)

// Workable means a workable work must have a Do method
type Workable interface {
	// Do runs the specified work with argument of worker id
	Do(int)
}
type Work struct {
	ID  int
	Job string
}

var letters = []rune("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789")

func randomString(length int) string {
	rs := make([]rune, length)
	for i := range rs {
		rs[i] = letters[rand.Intn(len(letters))]
	}
	return string(rs)
}

// MockSomeWorks mocks some sample works implemented Workable interface
func MockSomeWorks(amount int) []Work {
	works := make([]Work, amount)
	for i := range works {
		works[i] = Work{i, randomString(8)}
	}
	return works
}

// Do implements Workable interface sample
func (w *Work) Do(workerId int) {
	hash := fnv.New32a()
	hash.Write([]byte(w.Job))
	//if os.Getenv("DEBUG") == "true" {
	log.Printf("Worker[%d]: Doing Work[%d] hash word[\"%s\"] to [\"%d\"]\n", workerId, w.ID, w.Job, hash.Sum32())
	//}
	if workerId == 1 {
		//TODO 有问题。仔细看 这个会导致1的workid不执行
		time.Sleep(time.Second / 1)
	}
}

//
//type DefaultWork struct {
//	hash       string
//	createdAt  time.Time
//	startedAt  time.Time
//	finishedAt time.Time
//	f          func(int, *DefaultWork)
//}
//
//func (w *DefaultWork) Do(workerID int) {
//	w.startedAt = time.Now()
//	w.f(workerID, w)
//	w.finishedAt = time.Now()
//}
//
//func (w *DefaultWork) Hash() string {
//	return w.hash
//}
//
//func (w *DefaultWork) CreatedAt() time.Time {
//	return w.createdAt
//}
//
//func (w *DefaultWork) StartedAt() time.Time {
//	return w.startedAt
//}
//
//func (w *DefaultWork) FinishedAt() time.Time {
//	return w.finishedAt
//}
//
//func HandleFunc(f func(workerID int, work *DefaultWork)) *DefaultWork {
//	now := time.Now()
//	h := sha1.New()
//	io.WriteString(h, now.String())
//	hash := fmt.Sprintf("----------------%x", h.Sum(nil))
//
//	return &DefaultWork{
//		hash:      hash,
//		createdAt: time.Now(),
//		f:         f,
//	}
//}
