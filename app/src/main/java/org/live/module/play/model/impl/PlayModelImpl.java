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

    public PlayModelImpl(Context context, final OnPlayListener listener) {
        this.livePlayer = new TXLivePlayer(context) ;
        this.listener = listener ;
        this.livePlayer.setPlayListener(new ITXLivePlayListener() { //绑定播放视频的回调
            @Override
            public void onPlayEvent(int i, Bundle bundle) {
                switch (i) {
                    case PlayConstants.PLAY_EVT_PLAY_BEGIN: {   //开始直播,或重新开始
                        Log.d(TAG, "开始直播") ;
                        listener.onStartingPlay() ;
                    }
                    case PlayConstants.PLAY_EVT_PLAY_LOADING: { //正在加载
                        Log.d(TAG, "正在加载") ;
                        listener.onLoading();
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
