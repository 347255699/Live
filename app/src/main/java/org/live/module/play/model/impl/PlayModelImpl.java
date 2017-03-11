package org.live.module.play.model.impl;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.tencent.rtmp.ITXLivePlayListener;
import com.tencent.rtmp.TXLivePlayer;
import com.tencent.rtmp.ui.TXCloudVideoView;

import org.live.module.play.PlayConstants;
import org.live.module.play.listener.OnPlayListener;
import org.live.module.play.model.PlayModel;

/**
 * Created by Mr.wang on 2017/3/10.
 */
public class PlayModelImpl implements PlayModel {

    public static final String TAG = "PlayModelImpl" ;

    private TXLivePlayer livePlayer ;   //播放器

    private OnPlayListener listener ;

    private boolean firstStartPlayFlag = true ;     //第一次直播的标志

    public PlayModelImpl(Context context, final OnPlayListener listener) {
        this.livePlayer = new TXLivePlayer(context) ;
        this.listener = listener ;
        this.livePlayer.setPlayListener(new ITXLivePlayListener() { //绑定播放视频的回调
            @Override
            public void onPlayEvent(int i, Bundle bundle) {
                switch (i) {
                    case PlayConstants.PLAY_EVT_PLAY_BEGIN: {   //开始直播,或重新开始
                        if (firstStartPlayFlag) {
                            listener.onFirstStartingPlay() ;
                            firstStartPlayFlag = false ;
                        }
                        Log.d(TAG, "开始直播") ;
                        listener.onStartingPlay() ;
                        break ;
                    }
                    case PlayConstants.PLAY_EVT_PLAY_LOADING: { //正在加载
                        Log.d(TAG, "正在加载") ;
                        listener.onLoading();
                        break ;
                    }
                    case PlayConstants.PLAY_ERR_NET_DISCONNECT: {   //已经断开连接，需要重新进入
                        Log.d(TAG, "已经断开连接了") ;
                        listener.onDestroyPlayFromNetWorkFail() ;
                        break ;
                    }
                    case PlayConstants.PLAY_EVT_CONNECT_SUCC: {     //已经连接到服务器
                        Log.d(TAG, "已经连接到服务器") ;
                        break;
                    }
                    case PlayConstants.PLAY_EVT_RTMP_STREAM_BEGIN: {    //已经连接到服务器,并开始拉流
                        Log.d(TAG, "已经连接到服务器,并开始拉流") ;
                        break ;
                    }
                }   //switch
            }

            @Override
            public void onNetStatus(Bundle bundle) {

            }
        });
    }

    @Override
    public void play(String playIp) {
        Log.d(TAG, "play地址： "+ playIp) ;
        if(playIp.contains("http://") && playIp.contains(".flv")) {     //flv的直播协议
            livePlayer.startPlay(playIp, TXLivePlayer.PLAY_TYPE_LIVE_FLV) ;
        } else {
            livePlayer.startPlay(playIp, TXLivePlayer.PLAY_TYPE_LIVE_RTMP) ;    //rtmp的直播协议
        }
    }

    @Override
    public void stopPlay() {
        livePlayer.stopPlay(true) ; // true代表清除最后一帧画面
    }

    @Override
    public void pause() {
        livePlayer.pause() ;
    }

    @Override
    public void resume() {
        livePlayer.resume() ;
    }

    @Override
    public void initPlayerView(TXCloudVideoView playView) {
        this.livePlayer.setPlayerView(playView) ;
    }
}
