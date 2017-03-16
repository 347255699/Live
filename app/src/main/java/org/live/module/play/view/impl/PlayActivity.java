package org.live.module.play.view.impl;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;
import android.view.WindowManager;

import org.live.R;


/**
 *  直播拉流的Activity
 *
 * Created by Mr.wang on 2017/3/9.
 */
public class PlayActivity extends FragmentActivity {

    public static final String TAG = "PlayActivity" ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON) ; // 保持屏幕常亮
        requestWindowFeature(Window.FEATURE_NO_TITLE) ;     //隐藏标题
        setContentView(R.layout.activity_play) ;
        setFragmentDefault() ;

    }

    /**
     *  设置默认的fragment
     */
    public void setFragmentDefault() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction() ;
        fragmentTransaction.replace(R.id.rl_play_playerDump, new PlayFragment(), PlayFragment.TAG) ;
        fragmentTransaction.commit() ;
    }

    /**
     *  重新加载当前的fragment
     */
    public void reLoadCurrentFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction() ;
        fragmentTransaction.replace(R.id.rl_play_playerDump, new PlayFragment(), PlayFragment.TAG) ;
        fragmentTransaction.commit() ;
    }


}
