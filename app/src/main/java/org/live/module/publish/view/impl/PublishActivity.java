package org.live.module.publish.view.impl;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;
import android.view.WindowManager;

import com.live.live.R;

import org.live.module.publish.listener.BackHandledFragment;
import org.live.module.publish.listener.BackHandledInterface;

/**
 * 主播界面
 */
public class PublishActivity extends FragmentActivity implements BackHandledInterface {
    private static final String TAG = "PublishActivity";
    private BackHandledFragment mBackHandedFragment;
    private Window win = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        win = this.getWindow();
        win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); // 保持屏幕常亮
        // win.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); // 全屏显示
        requestWindowFeature(Window.FEATURE_NO_TITLE); // 隐藏标题
        setContentView(R.layout.activity_recorder);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        win.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); // 取消屏幕常亮
    }

    @Override
    public void setSelectedFragment(BackHandledFragment selectedFragment) {
        this.mBackHandedFragment = selectedFragment;
    }

    @Override
    public void onBackPressed() {
        if(mBackHandedFragment == null || !mBackHandedFragment.onBackPressed()){
            if(getSupportFragmentManager().getBackStackEntryCount() == 0){
                super.onBackPressed();
            }else{
                getSupportFragmentManager().popBackStack();
            }
        }
    }
}
