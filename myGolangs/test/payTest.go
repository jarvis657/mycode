package main

import (
	"crypto/md5"
	"fmt"
	"io"
	"net/http"
	"net/url"
	"sort"
	"strconv"
	"strings"
	"time"
)

const URL = "https://pay.ipaynow.cn/"
const AppId = "156886024666838"
const AppKey = "3VzjWmoiu9jKHAhSYMgAk7ZkSRS2vNJx"

// 公众号对接
func main() {
	var s = 1
	switch s {
	case 1:
		fmt.Println("1111111111")
	case 2:
		fmt.Println("2222222222")
	//以下两个仅仅是为了数据指标查看
	case 3:
		fmt.Println("3333333333")
	}
	t := time.Now().UnixNano() % 100000
	fmt.Println(t)
	//下单
	//pay()

	//查询
	//query()
}

// 下单
func pay() {
	//请求参数
	var paramMap map[string]any
	paramMap = make(map[string]any)
	//功能码
	paramMap["funcode"] = "WP001"
	//接口版本号
	paramMap["version"] = "1.0.4"
	//商户应用唯一标识
	paramMap["appId"] = AppId
	//商户订单号 自行生成
	paramMap["mhtOrderNo"] = strconv.FormatInt(time.Now().UnixNano(), 10)
	//商户商品名称
	paramMap["mhtOrderName"] = "测试AA"
	//商户交易类型
	paramMap["mhtOrderType"] = "05"
	//商户订单币种类型
	paramMap["mhtCurrencyType"] = "156"
	//商户订单交易金额 单位分
	paramMap["mhtOrderAmt"] = 1
	//商户订单详情
	paramMap["mhtOrderDetail"] = "商品订单详情"
	//商户订单超时时间 单位秒
	paramMap["mhtOrderTimeOut"] = 3600
	//商户订单开始时间
	paramMap["mhtOrderStartTime"] = time.Now().Format("20060102150405")
	//商户后台通知 URL
	paramMap["notifyUrl"] = "http://api.openchat.zy.dev/merchantNotify"
	//商户前台通知 URL
	//paramMap["frontNotifyUrl"] = "https://www.baidu.com"
	//商户字符编码
	paramMap["mhtCharset"] = "UTF-8"
	//设备类型
	paramMap["deviceType"] = "20"
	//用户所选渠道类型
	//paramMap["payChannelType"] = "13"
	//商户保留域
	//paramMap["mhtReserved"] = "保留域"
	//是否支持信用卡支付
	//paramMap["mhtLimitPay"] = "0"
	//输出格式
	paramMap["outputType"] = "1"
	//商户公众号appid,需要自行申请
	//paramMap["mhtSubAppId"] = "wx0b5cc697d69ab732"
	//消费者id
	//paramMap["consumerId"] = "ofJA_wXmFr4Ad8SYRyNAmIIlTW0A"
	//商户签名方法
	paramMap["mhtSignType"] = "MD5"

	var originalStr = map2KeyValue(paramMap) + "&" + md5Encryption(AppKey)
	fmt.Println("生成签名明文:", originalStr)
	var sign = md5Encryption(originalStr)
	fmt.Println("生成签名:", sign)

	//签名
	paramMap["mhtSignature"] = sign

	//请求接口
	fmt.Println("请求现在支付下单接口参数：", paramMap)

	urlValues := url.Values{}
	for key := range paramMap {
		urlValues.Add(key, fmt.Sprintf("%v", paramMap[key]))
	}
	resp, _ := http.PostForm(URL, urlValues)
	defer resp.Body.Close()
	body, _ := io.ReadAll(resp.Body)
	var respStr = string(body)
	fmt.Println("请求现在支付下单接口响应：", respStr)

	//验签
	var respMap = keyValue2Map(respStr)
	var respSignature = respMap["signature"]
	delete(respMap, "signature")
	var respOriginalStr = map2KeyValue(respMap) + "&" + md5Encryption(AppKey)
	fmt.Println("接口响应生成签名明文:", respOriginalStr)
	var generateSign = md5Encryption(respOriginalStr)
	if strings.Compare(generateSign, fmt.Sprintf("%v", respSignature)) == 0 {
		fmt.Println("签名验证成功")
		//do something
	} else {
		fmt.Println("签名验证失败")
	}
}

// 查询
func query() {
	//请求参数
	var paramMap map[string]any
	paramMap = make(map[string]any)
	//功能码
	paramMap["funcode"] = "MQ002"
	//接口版本号
	paramMap["version"] = "1.0.0"
	//设备类型
	paramMap["deviceType"] = "0600"
	//商户应用唯一标识
	paramMap["appId"] = AppId
	//商户订单号
	paramMap["mhtOrderNo"] = "1668759263383998300"
	//商户字符编码
	paramMap["mhtCharset"] = "UTF-8"
	//商户签名方法
	paramMap["mhtSignType"] = "MD5"

	var originalStr = map2KeyValue(paramMap) + "&" + md5Encryption(AppKey)
	fmt.Println("生成签名明文:", originalStr)
	var sign = md5Encryption(originalStr)
	fmt.Println("生成签名:", sign)

	//签名
	paramMap["mhtSignature"] = sign

	//请求接口
	fmt.Println("请求现在支付查询接口参数：", paramMap)

	urlValues := url.Values{}
	for key := range paramMap {
		urlValues.Add(key, fmt.Sprintf("%v", paramMap[key]))
	}
	resp, _ := http.PostForm(URL, urlValues)
	defer resp.Body.Close()
	body, _ := io.ReadAll(resp.Body)
	var respStr = string(body)
	fmt.Println("请求现在支付查询接口响应：", respStr)

	//验签
	var respMap = keyValue2Map(respStr)
	var respSignature = respMap["signature"]
	delete(respMap, "signature")
	var respOriginalStr = map2KeyValue(respMap) + "&" + md5Encryption(AppKey)
	fmt.Println("接口响应生成签名明文:", respOriginalStr)
	var generateSign = md5Encryption(respOriginalStr)
	if strings.Compare(generateSign, fmt.Sprintf("%v", respSignature)) == 0 {
		fmt.Println("签名验证成功")
		//do something
	} else {
		fmt.Println("签名验证失败")
	}
}

// map转keyValue字符串
func map2KeyValue(paramMap map[string]any) string {
	var keys []string
	var build strings.Builder
	for key := range paramMap {
		keys = append(keys, key)
	}
	sort.Sort(sort.StringSlice(keys))
	for index := range keys {
		var value = paramMap[keys[index]]
		if value != "" {
			build.WriteString(keys[index])
			build.WriteString("=")
			build.WriteString(fmt.Sprintf("%v", value))
			build.WriteString("&")
		}
	}
	var str = build.String()
	if len(str) > 0 {
		str = str[:len(str)-1]
	}
	return str
}

// keyValue字符串转map
func keyValue2Map(keyValueStr string) map[string]any {
	var targetMap map[string]any
	targetMap = make(map[string]any)
	if keyValueStr == "" {
		return targetMap
	}
	var array = strings.Split(keyValueStr, "&")
	if array != nil && len(array) > 0 {
		for index := range array {
			var tempStr = array[index]
			var tempArray = strings.Split(tempStr, "=")
			if len(tempArray) == 2 {
				var value, _ = url.QueryUnescape(tempArray[1])
				targetMap[tempArray[0]] = value
			}
		}
	}
	return targetMap
}

// md5加密
func md5Encryption(str string) string {
	data := []byte(str)
	has := md5.Sum(data)
	md5str := fmt.Sprintf("%x", has) //将[]byte转成16进制
	return strings.ToLower(md5str)
}
