package org.live.common.constants;

/**
 * 全局的常量定义
 * <p>
 * Created by Mr.wang on 2017/3/9.
 */
public class LiveConstants {

    /**
     * 远程服务器地址
     */
    public static String REMOTE_SERVER_IP = "119.29.80.59";

    /**
     * http协议开头
     */
    public static final String HTTP_PREFIX = "http://";

    /**
     * websocket协议开头
     */
    public static final String WEB_SOCKET_PREFIX = "ws://";

    /**
     * http远程服务器的端口
     */
    public static final String REMOTE_PORT = "80";

    /**
     * 远程服务器的地址
     */
    public static String REMOTE_SERVER_HTTP_IP =
            HTTP_PREFIX + REMOTE_SERVER_IP + ":" + REMOTE_PORT;

    /**
     * rtmp协议开头
     */
    public static final String REMP_PREFIX = "rtmp://";

    /**
     * hls直播协议地址的后缀
     */
    public static final String HLS_TYPE_SUFFIX = ".m3u8";

    /**
     * flv直播协议地址的后缀
     */
    public static final String FLV_TYPE_SUFFIX = ".flv";

    /**
     * 用于HttpRequestUtils工具类的，通用响应结果标识
     */
    public static final int HTTP_RESPONSE_RESULT_CODE = 1002;

    /**
     * 远程服务器的地址，websocket
     */
    public static final String REMOTE_SERVER_WEB_SOCKET_IP = WEB_SOCKET_PREFIX + REMOTE_SERVER_IP  + ":" + REMOTE_PORT;

    /**
     * rtmp拉流地址的前缀
     */
    public static final String RTMP_PLAY_IP_PREFIX  = "rtmp://119.29.80.59/live/" ;

}
