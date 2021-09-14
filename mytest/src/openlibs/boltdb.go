package main

import (
	"bytes"
	"fmt"
	"log"
	"strconv"
	"time"

	bolt "go.etcd.io/bbolt"
)

var world = []byte("greeting")

func main() {
	for i := 0; i < 10; i++ {
	}
	db, err := bolt.Open("bolt.db", 0644, &bolt.Options{Timeout: time.Duration(10)})
	if err != nil {
		log.Fatal(err)
	}
	//defer db.Close()

	key := []byte("hello")
	value := []byte("Hello World!")

	// retrieve the data
	err = db.View(func(tx *bolt.Tx) error {
		bucket := tx.Bucket(world)
		if bucket == nil {
			return fmt.Errorf("Bucket %s not found!", world)
		}
		bucket.ForEach(func(k, v []byte) error {
			//val := bucket.Get(k)
			fmt.Printf("k:%v,v:%v\n", string(k), string(v))
			return nil
		})
		return nil
	})
	if err != nil {
		log.Fatal(err)
	}

	stats := db.Stats()
	fmt.Printf("%+v\n", stats)
	// store some data
	err = db.Update(func(tx *bolt.Tx) error {
		bucket, err := tx.CreateBucketIfNotExists(world)
		if err != nil {
			return err
		}
		id, _ := bucket.NextSequence()
		get := bucket.Get(key)
		if string(get) == "" {
			value = []byte("vvv")
		}
		err = bucket.Put(key, []byte(string(value)+"_id:"+strconv.FormatUint(id, 10)))
		if err != nil {
			return err
		}
		return nil
	})
	if err != nil {
		log.Fatal(err)
	}
	// retrieve the data
	err = db.View(func(tx *bolt.Tx) error {
		bucket := tx.Bucket(world)
		if bucket == nil {
			return fmt.Errorf("Bucket %s not found!", world)
		}
		val := bucket.Get(key)
		fmt.Println(string(val))

		//前缀扫描
		c := bucket.Cursor()
		prefix := []byte("he")
		for k, v := c.Seek(prefix); bytes.HasPrefix(k, prefix); k, v = c.Next() {
			fmt.Printf("key=%s, value=%s\n", k, v)
		}
		//范围扫描
		// Our time range spans the 90's decade.
		min := []byte("1990-01-01T00:00:00Z")
		max := []byte("2000-01-01T00:00:00Z")
		// Iterate over the 90's.
		for k, v := c.Seek(min); k != nil && bytes.Compare(k, max) <= 0; k, v = c.Next() {
			fmt.Printf("%s: %s\n", k, v)
		}
		return nil
	})
	if err != nil {
		log.Fatal(err)
	}
}
