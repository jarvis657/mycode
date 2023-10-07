package origin.utils;

import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * @Author:jarvis
 * @Date: 2023/9/25
 * @Desc:
 **/
@Slf4j
public class IpUtils {

    private static final String UNKNOWN_VALUE = "unknown";
    private static final String LOCALHOST_V4 = "127.0.0.1";
    private static final String LOCALHOST_V6 = "0:0:0:0:0:0:0:1";
    private static final String X_FORWARDED_FOR = "X-Forwarded-For";
    private static final String X_REAL_IP = "X-Real-IP";
    private static final String PROXY_CLIENT_IP = "Proxy-Client-IP";
    private static final String WL_PROXY_CLIENT_IP = "WL-Proxy-Client-IP";
    private static final String HTTP_CLIENT_IP = "HTTP_CLIENT_IP";
    private static final String IP_DATA_PATH = "/Users/shepherd/Desktop/ip2region.xdb";
    private static byte[] contentBuff;

    /**
     * 获取客户端ip地址
     *
     * 通过ip 转成地址:
     *
     *
     * @param request
     * @return
     */
    public static String getRemoteHost(HttpServletRequest request) {
        String ip = request.getHeader(X_FORWARDED_FOR);
        if (StringUtils.isNotEmpty(ip) && !UNKNOWN_VALUE.equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = ip.indexOf(",");
            if (index != -1) {
                return ip.substring(0, index);
            } else {
                return ip;
            }
        }
        ip = request.getHeader(X_REAL_IP);
        if (StringUtils.isNotEmpty(ip) && !UNKNOWN_VALUE.equalsIgnoreCase(ip)) {
            return ip;
        }
        if (StringUtils.isBlank(ip) || UNKNOWN_VALUE.equalsIgnoreCase(ip)) {
            ip = request.getHeader(PROXY_CLIENT_IP);
        }
        if (StringUtils.isBlank(ip) || UNKNOWN_VALUE.equalsIgnoreCase(ip)) {
            ip = request.getHeader(WL_PROXY_CLIENT_IP);
        }
        if (StringUtils.isBlank(ip) || UNKNOWN_VALUE.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (StringUtils.isBlank(ip) || UNKNOWN_VALUE.equalsIgnoreCase(ip)) {
            ip = request.getHeader(HTTP_CLIENT_IP);
        }
        if (StringUtils.isBlank(ip) || UNKNOWN_VALUE.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip.equals(LOCALHOST_V6) ? LOCALHOST_V4 : ip;
    }
}
