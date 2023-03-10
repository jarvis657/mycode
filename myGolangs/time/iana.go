package main

import (
	"errors"
	"fmt"
	"github.com/golang-module/carbon"
	"github.com/jeandeaual/go-locale"
	"time"
)

type MyCountry string

const (
	Germany      MyCountry = "Germany"
	UnitedStates MyCountry = "United States"
	China        MyCountry = "China"
	CN           MyCountry = "CN"
)

// timeZoneID 是国家=>IANA 标准时区标识符 的键值对字典
var timeZoneID = map[MyCountry]string{
	Germany:      "Europe/Berlin",
	UnitedStates: "America/Los_Angeles",
	China:        "Asia/Shanghai",
}

// 获取 IANA 时区标识符
func (c MyCountry) TimeZoneID() (string, error) {
	if id, ok := timeZoneID[c]; ok {
		return id, nil
	}
	return "", errors.New("timezone id not found for country")
}

// 获取tz时区标识符的格式化时间字符
func TimeIn(t time.Time, tz, format string) string {

	// https:/golang.org/pkg/time/#LoadLocation loads location on
	// 加载时区
	loc, err := time.LoadLocation(tz)
	if err != nil {
		//handle error
	}
	// 获取指定时区的格式化时间字符串
	return t.In(loc).Format(format)
}

func main() {
	//https://en.wikipedia.org/wiki/List_of_tz_database_time_zones
	userLocales, e := locale.GetLocales()
	fmt.Println(userLocales, "---", e)
	location, e2 := time.LoadLocation("en-US")
	fmt.Println(location, "===", e2)
	location, e2 = time.LoadLocation("en-US")
	fmt.Println(location, "===", e2)
	location, e2 = time.LoadLocation("Europe/Chisinau")
	fmt.Println(location, "===", e2)
	format := time.Now().In(location).Format("2006-01-02 15:04:05")
	fmt.Println(format)
	location, e2 = time.LoadLocation("Europe/Ljubljana")
	fmt.Println(location, "===", e2)
	format = time.Now().In(location).Format("2006-01-02 15:04:05")
	fmt.Println(format)
	location, e2 = time.LoadLocation("EST")
	fmt.Println(location, "===", e2)
	format = time.Now().In(location).Format("2006-01-02 15:04:05")
	fmt.Println(format)
	timeString := carbon.Parse("2020-07-05 13:14:15").SetLocale("en-SA").ToDateTimeString(carbon.Shanghai)
	//time.Now().In(time.LoadLocation("en-SA"))
	fmt.Println(timeString)
	fmt.Println("==================")
	// 获取美国的时区结构体
	tz, err := UnitedStates.TimeZoneID()
	if err != nil {
		//handle error
	}
	//格式化成美国的时区
	usTime := TimeIn(time.Now(), tz, time.RFC3339)

	fmt.Printf("Time in %s: %s", UnitedStates, usTime)
}
