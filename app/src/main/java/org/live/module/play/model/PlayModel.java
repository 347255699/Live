package org.live.module.play.model;

import com.tencent.rtmp.ui.TXCloudVideoView;

/**
 * Created by Mr.wang on 2017/3/10.
 */
public interface PlayModel {

    /**
     *  播放直播 （拉流）
     * @param playIp 播放地址
     */
    public void play(String playIp) ;

    /**
     *  停止播放. 在退出的时候(摧毁view的时候)需要停止播放
     */
    public void stopPlay() ;

    public void initPlayerView(TXCloudVideoView playView) ;

    /**
     *
     * @param orientation 1. 竖屏   2.横屏
     */
    public void setPlayViewOrientation(int orientation) ;

}
