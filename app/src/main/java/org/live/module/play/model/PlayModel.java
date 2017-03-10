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

    /**
     *  暂停
     */
    public void pause() ;

    /**
     *  恢复播放
     */
    public void resume() ;

    public void initPlayerView(TXCloudVideoView playView) ;

}
