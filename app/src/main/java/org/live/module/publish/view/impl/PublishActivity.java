package org.live.module.publish.view.impl;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.view.Window;
import android.view.WindowManager;


import org.live.R;
import org.live.common.constants.LiveConstants;
import org.live.common.constants.LiveKeyConstants;
import org.live.common.listener.BackHandledFragment;
import org.live.common.listener.BackHandledInterface;
import org.live.module.chat.service.AnchorChatService;
import org.live.module.home.view.impl.HomeActivity;
import org.live.module.login.domain.MobileUserVo;
import org.live.module.publish.listener.OnPublishActivityListener;

/**
 * 推流主活动窗口
 */
public class PublishActivity extends FragmentActivity implements BackHandledInterface, OnPublishActivityListener {
    private static final String TAG = "PublishActivity";
    private BackHandledFragment mBackHandedFragment;
    private Window win = null;
    private String rtmpUrl = null;
    private MobileUserVo mobileUserVo;
    private String wsUrl;
    private AnchorChatService.ChatReceiveServiceBinder anchorChatServiceBinder;

    public static PublishActivity instanace ;

    private boolean logoutServiceFlag = false ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instanace = this ;
        win = this.getWindow();
        win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); // 保持屏幕常亮
        // win.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); // 全屏显示
        requestWindowFeature(Window.FEATURE_NO_TITLE); // 隐藏标题
        this.rtmpUrl = getIntent().getStringExtra(LiveKeyConstants.Global_URL_KEY); // 初始化参数
        setContentView(R.layout.activity_recorder);
        this.mobileUserVo = HomeActivity.mobileUserVo;
        this.wsUrl = LiveConstants.REMOTE_SERVER_WEB_SOCKET_IP + "/chat?account=" + mobileUserVo.getAccount() + "&chatroom=" + mobileUserVo.getLiveRoomVo().getRoomNum()
                + "&nickname=" + mobileUserVo.getNickname() + "&anchor=" + 1; // websocket链接
        Intent intent = new Intent(this, AnchorChatService.class);
        intent.putExtra(LiveKeyConstants.Global_URL_KEY, wsUrl);
        bindService(intent, conn, Context.BIND_AUTO_CREATE); // 绑定聊天服务
    }

    @Override
    public void finish() {
        super.finish();
        win.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); // 取消屏幕常亮
        logoutService(); // 注销服务
        logoutServiceFlag = true ;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(!logoutServiceFlag) {
            win.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); // 取消屏幕常亮
            logoutService(); // 注销服务
        }
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

    @Override
    public AnchorChatService.ChatReceiveServiceBinder getChatReceiveServiceBinder() {
        return anchorChatServiceBinder;
    }

    /**
     * 服务连接实体
     */
    private ServiceConnection conn = new ServiceConnection() {
        /**
         * 服务断开连接时回调
         * @param name
         */
        @Override
        public void onServiceDisconnected(ComponentName name) {
        }

        /**
         * 服务建立连接时回调
         * @param name
         * @param service 当前服务实体
         */
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            anchorChatServiceBinder = (AnchorChatService.ChatReceiveServiceBinder) service; // 取得服务实体
        }
    };

    /**
     * 注销服务
     */
    private void logoutService() {
        if (anchorChatServiceBinder != null) {
            unbindService(conn);
        }
    }

    /**
     * 取得websocket链接
     *
     * @return
     */
    @Override
    public String getWsUrl() {
        return this.wsUrl;
    }

}
