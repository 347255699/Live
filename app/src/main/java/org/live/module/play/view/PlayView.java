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
     *  获取播放器的view
     * @return
     */
    public TXCloudVideoView getPlayerView() ;

    /**
     * 　显示正在加载
     */
    public void showLoading() ;

    /**
     *  隐藏正在加载
     */
    public void hideLoading() ;

    /**
     *  由于网络中断重连3次不行， 所以必要地摧毁当前的view
     */
    public void destroyPlayView() ;

    /**
     *  第一次开始直播
     */
    public void firstStartPlay() ;


}
