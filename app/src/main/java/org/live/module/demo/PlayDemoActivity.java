package org.live.module.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.tencent.rtmp.MyLivePlayer;
import com.tencent.rtmp.TXLivePlayer;
import com.tencent.rtmp.ui.TXCloudVideoView;

import org.live.R;

/**
 * Created by wang on 2017/4/28.
 */

public class PlayDemoActivity extends Activity {

    private TXCloudVideoView mPlayerView = null;   //播放器的view

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_demo) ;
        mPlayerView = (TXCloudVideoView) findViewById(R.id.vv_demo_player) ;
        MyLivePlayer livePlayer = new MyLivePlayer(this) ;   //播放器
        Intent intent = getIntent() ;
        String rtmpPlay = intent.getStringExtra("rtmpPlay");
        livePlayer.setPlayerView(mPlayerView) ;
        livePlayer.startPlay(rtmpPlay, TXLivePlayer.PLAY_TYPE_LIVE_RTMP) ;
    }
}
