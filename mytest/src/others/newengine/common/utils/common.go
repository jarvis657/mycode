package utils

import (
	"github.com/google/uuid"
	"net"
	"strings"
)

func GetOptionalIp() string {
	addrs, err := net.InterfaceAddrs()
	if err != nil {
		return "noIp"
	}
	for _, address := range addrs {
		// 检查ip地址判断是否回环地址
		if ipnet, ok := address.(*net.IPNet); ok && !ipnet.IP.IsLoopback() {
			if ipnet.IP.To4() != nil {
				return ipnet.IP.String()
			}
		}
	}
	return "noIp"
}

func GetUUID() string {
	uuid := uuid.New().String()
	return strings.Replace(uuid, "-", "", -1)
}
