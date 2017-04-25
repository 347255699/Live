package org.live.module.home.view.impl;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.live.R;
import org.live.module.home.constants.HomeConstants;

/**
 * 直播结束的activity
 * Created by wang on 2017/4/25.
 */
public class LiveOverActivity extends Activity {

    private ImageView headImgVIew ;  //头像的view

    private TextView nicknameView ; //昵称的veiw

    private TextView accountView ;  //账号的view

    private TextView attentionCountView ;   //关注数的view

    private TextView descriptionView ;      //个性签名的view

    private Button returnBtn ;      //返回按钮


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_over) ;
        Intent intent = getIntent() ;
        String liveRoomId = intent.getStringExtra(HomeConstants.LIVE_ROOM_ID_KEY) ; //直播间id
    }

    /**
     * 初始化ui
     */
    private void initial() {

        headImgVIew = (ImageView) findViewById(R.id.iv_live_over_headImg) ;
        accountView = (TextView) findViewById(R.id.tv_live_over_account) ;
        nicknameView = (TextView) findViewById(R.id.tv_live_over_nickname);

    }
}
