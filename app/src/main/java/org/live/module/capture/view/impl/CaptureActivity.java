package org.live.module.capture.view.impl;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import org.live.R;
import org.live.common.constants.LiveKeyConstants;
import org.live.common.listener.BackHandledFragment;
import org.live.common.listener.BackHandledInterface;

/**
 * 录屏主活动窗口
 */
public class CaptureActivity extends FragmentActivity implements BackHandledInterface {
    private BackHandledFragment mBackHandedFragment = null;
    private Window win = null;
    private String rtmpUrl = null; // 推流地址
    private TextView cCapturingStatusTextView = null; // 录屏直播提示信息

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        win = this.getWindow();
        win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); // 保持屏幕常亮
        this.rtmpUrl = getIntent().getStringExtra(LiveKeyConstants.Global_URL_KEY);
        // win.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); // 全屏显示
        requestWindowFeature(Window.FEATURE_NO_TITLE); // 隐藏标题
        setContentView(R.layout.activity_capture);
        initUIElements();
    }

    /**
     * 初始化控件
     */
    private void initUIElements() {
        this.cCapturingStatusTextView = (TextView) findViewById(R.id.tv_capture_status);
    }


    @Override
    public void setSelectedFragment(BackHandledFragment selectedFragment) {
        this.mBackHandedFragment = selectedFragment;
    }

    @Override
    public void onBackPressed() {
        if (mBackHandedFragment == null || !mBackHandedFragment.onBackPressed()) {
            if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                super.onBackPressed();
            } else {
                getSupportFragmentManager().popBackStack();
            }
        }
    }

    public String getRtmpUrl() {
        return this.rtmpUrl;
    }

    public TextView getcCapturingStatusTextView() {
        return this.cCapturingStatusTextView;
    }

}
