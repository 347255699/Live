package org.live.module.play.presenter;

/**
 * Created by Mr.wang on 2017/3/10.
 */
public interface PlayPresenter {

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

}
