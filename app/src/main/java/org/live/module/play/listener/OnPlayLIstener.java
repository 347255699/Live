package org.live.module.play.listener;

/**
 * Created by wangzhancheng on 2017/3/10.
 */

public interface OnPlayListener {

    /**
     *  当第一次开始直播的瞬间
     */
    public void onFirstStartingPlay() ;

    /**
     *  正在开始播放的瞬间
     */
    public void onStartingPlay() ;

    /**
     *  视频播放loading
     */
    public void onLoading() ;

    /**
     *  网络重连3次失败，需要摧毁当前的view，并重新进入直播间才行。
     */
    public void onDestroyPlayFromNetWorkFail() ;


}
