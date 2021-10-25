package main

import (
	"errors"
	"fmt"
	"regexp"
)

// Work is a sample work type which implemented Workable interface

var (
	ErrRecordNotExist   = errors.New("record not exist")
	ErrConnectionClosed = errors.New("connection closed")
	fe                  = &fileError{}
)

type fileError struct {
}

func (fe *fileError) Error() string {
	return "文件错误"
}

func main() {
	//c := StartDispatcher(4)
	//works := MockSomeWorks(30)
	//
	//for i := range works {
	//	c.Send(&works[i])
	//}
	//c.End()
	m := make(map[string]int32)
	m["a"] = 1
	fmt.Println(m["a"])
	fmt.Println(m["b"])

	ss := `;task type:ssh;shell task id:20210617_b0200156cf2a11ebadec525400154767;[2021-06-17 13:15:08,938] INFO: acquire pkg lock 'pkgadmin.tdb_freq_agent'<br />[2021-06-17 13:15:08,938] INFO: stopApp all<br />[2021-06-17 13:15:08,953] INFO: kill tdb_freq_agent with 15<br />[2021-06-17 13:15:09,047] INFO: tdb_freq_agent already dead<br />[2021-06-17 13:15:09,048] INFO: stop all successfully<br />[2021-06-17 13:15:09,048] INFO: delete pkg cron<br />[2021-06-17 13:15:09,145] INFO: acquire pkg lock 'pkgadmin.tdb_freq_agent'<br />[2021-06-17 13:15:09,145] INFO: stopApp all<br />[2021-06-17 13:15:09,159] INFO: tdb_freq_agent already dead<br />[2021-06-17 13:15:09,159] INFO: stop all successfully<br />[2021-06-17 13:15:09,160] INFO: delete pkg cron<br />[2021-06-17 13:15:09,169] INFO: crontab already deleted<br />[2021-06-17 13:15:09,466] INFO: acquire pkg lock 'pkgadmin.tdb_freq_agent'<br />[2021-06-17 13:15:09,466] INFO: restarting all<br />[2021-06-17 13:15:09,466] INFO: stopApp all<br />[2021-06-17 13:15:09,480] INFO: tdb_freq_agent already dead<br />[2021-06-17 13:15:09,480] INFO: stop all successfully<br />[2021-06-17 13:15:09,480] INFO: startApp all<br />[2021-06-17 13:15:09,481] INFO: add pkg cron<br />[2021-06-17 13:15:09,497] INFO: runConfigCode <start><br />[2021-06-17 13:15:12,506] INFO: run script succeeded<br />[2021-06-17 13:15:12,506] INFO: output:<br />argc = 2, conf path is [/home/oicq/tdb_freq_agent/conf/tdb_freq_agent.conf]
<br />sEthName:eth1 ip:1134586980
<br /><br />[2021-06-17 13:15:12,506] INFO: sleep 2 seconds and check<br />[2021-06-17 13:15:14,523] INFO: start all successfully<br />[2021-06-17 13:15:14,524] INFO: restart all successfully<br /><br />check_port.py<br />clear.sh<br />common.sh<br />common2.sh<br />data<br />md5sum.sh<br />monitor.sh<br />pkgadmin.py<br />pkgv2_helper.sh<br />proc_monitor.sh<br />procutil.py<br />procutil.pyc<br />reload.sh<br />restart.sh<br />start.sh<br />stop.sh<br />uninstall.sh<br />util.py<br />util.pyc<br />version.txt<br />[2021-06-17 13:15:09] need stop pack.... <br />[2021-06-17 13:15:09] ret:0<br />md5sum build successful!<br />[2021-06-17 13:15:09] Rollback need restart...<br />[2021-06-17 13:15:09] new admin<br />[2021-06-17 13:15:14] Rollback sucess<br /><br /><br />update%%100.108.160.67%%tdb_freq_agent%%/home/oicq/tdb_freq_agent%%rollback%%success%%%%1.0.18%%1.0.17%%<br />update%%100.108.160.67%%tdb_freq_agent%%/home/oicq/tdb_freq_agent%%rollback%%success%%%%1.0.18%%1.0.17%%<br /><br />[2021-06-17 13:15:14] Rollback 1.0.18 - 1.0.17<br />result%%success%%%%success<br />`
	ipRe := regexp.MustCompile(`update%%([\d\.]+)%%.*`)
	ipFinds := ipRe.FindStringSubmatch(ss)
	fmt.Printf("%+v,len:%v,ip:%v\n", ipFinds, len(ipFinds), ipFinds[1])

	switch genErr(3) {
	case ErrConnectionClosed:
		fmt.Println("1:ErrConnectionClosed")
	case ErrRecordNotExist:
		fmt.Println("0:ErrRecordNotExist")
	case fe:
		fmt.Println("fe:fe")
	default:
		fmt.Println("unknown")
	}
}

func genErr(i int) error {
	if i == 0 {
		return ErrRecordNotExist
	} else if i == 1 {
		return ErrConnectionClosed
	} else if i == 2 {
		return errors.New("unknown")
	}
	//return &fileError{}  这样switch case 找不到
	return fe
}
