package org.live.module.play.view;

import com.tencent.rtmp.ui.TXCloudVideoView;

/**
 *
 *
 * Created by Mr.wang on 2017/3/9.
 */

public interface PlayView {

    /**
     *  播放
     */
    public void play(String playIp) ;

    /**
     *  停止播放，用于摧毁组件
     */
    public void stopPlay() ;

    /**
     *  暂停
     */
    public void pause() ;

    /**
     *  继续播放
     */
    public void resume() ;

    /**
     *  获取播放器的view
     * @return
     */
    public TXCloudVideoView getPlayerView() ;


}
