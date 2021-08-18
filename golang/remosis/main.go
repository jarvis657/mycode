// Package main provides qishan@mogujie.com
package main

import (
	"encoding/json"
	"flag"
	"fmt"
	"io/ioutil"
	"log"
	"os"

	//_ "net/http/pprof"

	"time"

	"mogujie.com/remosis/actions/rdbtool"
	"mogujie.com/remosis/config"
	"mogujie.com/remosis/server"
	"mogujie.com/remosis/sys"
)

var (
	//获取配置信息的远程url
	confurl string
	//获取到server的配置信息
	content []byte
	//错误
	err error
	//解码后的配置 channel
	cfchan       = make(chan *config.Config, 1)
	errRedisChan = make(chan string)
	returnServer = make(map[string]interface{})
)

const (
	//SLEEP 执行后睡眠时间单位second 建议大于10秒以上
	SLEEP = 10
	//WAITTIMECURL 抓去配置间隔时间 单位second
	WAITTIMECURL = 120
)

func init() {
	flag.StringVar(&confurl, "conf", "http://remosis.mogujie.com/remosis?nodeName=", "get configuration from url")
	//flag.StringVar(&confurl, "conf", "http://recommender.mogujie.org/redis/machine/config/", "get configuration from url")
	//flag.StringVar(&confurl, "conf", "http://10.17.36.112/redis/config/", "get configuration from url")
	//flag.Set("log_dir", "/Users/lmq/Documents/mogujie_code/recommender/remosis/agent/src/mogujie.com/remosis/logs")
}

//getConfig 获取配置中心配置信息
func getConfig() {
	var perTime int64
	var diff int64 = WAITTIMECURL
	var cf *config.Config
	for {
		currTime := time.Now().Unix()
		if currTime-perTime > diff {
			nodeName, err := sys.GetNodeName()
			if err != nil {
				log.Println("获取本机名失败---->", err)
				continue
			}
			log.Println("当前本机名", nodeName)
			//TODO when online ok
			//n := url.QueryEscape(nodeName)
			//content, err = server.GetInfo(confurl + n)
			pwd, _ := os.Getwd()
			pwd = pwd + "/conf/template.json"
			fmt.Println("current file--->", pwd)
			txt, _ := ioutil.ReadFile(pwd)
			//txt, _ := ioutil.ReadFile("/Users/lmq/Documents/mogujie_code/recommender/remosis/agent/src/mogujie.com/remosis/deploy/conf/template.json")
			content = txt
			if err != nil {
				log.Println("getConfig -->从配置系统获取的配置错误:", err)
				perTime = currTime
			} else {
				cf, err = config.ParseConf(content)
				if err != nil {
					log.Println("getConfig -->从配置系统获取的配置 解析config 错误:", err)
					perTime = currTime
				} else {
					confb, _ := json.Marshal(cf)
					log.Println("getConfig -->从配置系统获取的配置:", string(confb))
					cfchan <- cf
					perTime = currTime
				}
			}
		} else if cf != nil {
			cfchan <- cf
		}
	}
}

//syncNodeInfo 同步机器节点信息
func syncNodeInfo(conf *config.Config) {
	// sync node base mem cpu and net etc infos
	go func(conf *config.Config) {
		sysStat, err := sys.GetSysStat(conf.Server)
		var curSysStat *sys.SysStat
		if err == nil {
			curSysStat = sys.CopyOne(sysStat)
		}
		returnServer["info"] = curSysStat
		returnServer["type"] = "nodeinfo"
		ssj, err := json.Marshal(returnServer)
		server.SendInfo(conf.Collector.CollectUrl, conf.Collector.AcceptType, conf.Collector.ConnTimeout, string(ssj))
	}(conf)
}

//syncRedisBaseInfo 同步redis性能信息
func syncRedisBaseInfo(res *sys.Redises, conf *config.Config) {
	// sync redis info
	go func(conf *config.Config) {
		rs := res.GetReidsInfo(conf)
		returnServer["info"] = rs
		returnServer["type"] = "redisinfo"
		info, err := json.Marshal(returnServer)
		if err != nil {
			log.Println("syncRedisBaseInfo 获取redis信息执行错误", err)
		} else {
			server.SendInfo(conf.Collector.CollectUrl, conf.Collector.AcceptType, conf.Collector.ConnTimeout, string(info))
		}
	}(conf)
}
func syncAlarmRedisInfo(errRedis chan string, conf *config.Config) {
	select {
	case err := <-errRedis:
		server.SendInfo(conf.Collector.AlarmUrl, conf.Collector.AcceptType, conf.Collector.ConnTimeout, err)
	case <-time.After(1 * time.Second):
	}
}
func topSlotSize(mp *map[int]*rdbtool.SlotInfo) {
}

//run
func main() {
	/*
		*s := "/Users/lmq/Documents/dump2222.rdb"
		*slotinfos := rdbtool.ParseRdb(s)
		*for k, i := range slotinfos {
			*    fmt.Println(k, "<>>>>>>>>>>>>>>>>>>,", i)
			*}
			*sy := sync.WaitGroup{}
			*sy.Add(1)
			*sy.Wait()
	*/
	flag.Parse()
	//go func() { log.Println(http.ListenAndServe("localhost:6060", nil)) }()
	//实时获取配置
	go getConfig()
	var cf *config.Config
	cf = <-cfchan
	mso := &server.SocketServer{Port: cf.Server.Socket, Message: make(chan string, 1)}
	//后台处理redis指令
	rs := &sys.Redises{}
	rs.DealCommandChan()
	//启动socket接受 control端的控制信息
	go mso.MSocketServer(rs)
	log.Println("开始监控数据")
	for {
		cf = <-cfchan
		if mso.Port != cf.Server.Socket {
			mso.Port = cf.Server.Socket
		}
		//检测ip与本node  ip 是否相同
		b, err := sys.CheckNodeIp(cf.Server.Ip)
		if err != nil || !b {
			log.Println("配置系统 ip 检测错误 -->", b, " error -->", err)
			time.Sleep(time.Second * time.Duration(SLEEP))
			continue
		}
		//同步redis info信息
		go syncRedisBaseInfo(rs, cf)
		//同步机器node 信息
		go syncNodeInfo(cf)
		//检查redis alive 存活
		go rs.RedisAliveCheck(cf, errRedisChan)
		//与webcontrol 同步 报警信息
		go syncAlarmRedisInfo(errRedisChan, cf)
		time.Sleep(time.Second * time.Duration(SLEEP))
	}
}
