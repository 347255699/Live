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
    public static final String REMOTE_SERVER_IP = "10.20.197.154";

    /**
     * http协议开头
     */
    public static final String HTTP_PREFIX = "http://";

    /**
     * http远程服务器的端口
     */
    public static final String REMOTE_PORT = "8080";

    /**
     * 远程服务器的地址
     */
    public static final String REMOTE_SERVER_HTTP_IP =
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

}
