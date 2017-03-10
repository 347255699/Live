package org.live.module.play.view.impl;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.Window;

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
        requestWindowFeature(Window.FEATURE_NO_TITLE) ;     //隐藏标题
        setContentView(R.layout.activity_play) ;
        setFragmentDefault() ;

    }

    /**
     *  设置默认的fragment
     */
    public void setFragmentDefault() {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction() ;
        fragmentTransaction.replace(R.id.rl_play_playerDump, new PlayFragment(), PlayFragment.TAG) ;
        fragmentTransaction.commit() ;
    }

}
