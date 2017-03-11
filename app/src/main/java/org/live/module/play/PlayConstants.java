package org.live.module.play;

/**
 *  关于播放模块相关的模块
 *
 * Created by Mr.wang on 2017/3/10.
 */

public class PlayConstants {

    /**
     * 	视频播放开始，如果有转菊花什么的这个时候该停了
     */
    public static final int PLAY_EVT_PLAY_BEGIN = 2004 ;

    /**
     * 视频播放进度，会通知当前进度和总体进度，仅在点播时有效
     */
    public static final int PLAY_EVT_PLAY_PROGRESS = 2005 ;

    /**
     *  视频播放loading，如果能够恢复，之后会有BEGIN事件
     */
    public static final int PLAY_EVT_PLAY_LOADING = 2007 ;

    /**
     *  视频播放结束
     */
    public static final int PLAY_EVT_PLAY_END = 2006 ;

    /**
     * 网络断连,且经多次重连抢救无效,可以放弃治疗,更多重试请自行重启播放
     */
    public static final int PLAY_ERR_NET_DISCONNECT = -2301 ;

    /**
     *  已经连接服务器
     */
    public static final int PLAY_EVT_CONNECT_SUCC = 2001 ;

    /**
     * 已经连接服务器，开始拉流（仅播放RTMP地址时会抛送，即直播的一种）
     */
    public static final int PLAY_EVT_RTMP_STREAM_BEGIN = 2002 ;


}
