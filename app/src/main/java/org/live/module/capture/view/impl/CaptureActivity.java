package org.live.module.capture.view.impl;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import org.live.R;
import org.live.common.constants.LiveConstants;
import org.live.common.constants.LiveKeyConstants;
import org.live.common.listener.BackHandledFragment;
import org.live.common.listener.BackHandledInterface;
import org.live.common.listener.ChatActivityEvent;
import org.live.common.provider.AnchorInfoProvider;
import org.live.module.capture.listener.OnCaptureActivityListener;
import org.live.module.chat.service.AnchorChatService;
import org.live.module.chat.view.impl.ChatFragment;
import org.live.module.home.view.impl.HomeActivity;
import org.live.module.login.domain.MobileUserVo;
import org.live.module.play.domain.LiveRoomInfo;
import org.live.module.publish.view.impl.LiveOverFragment;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * 录屏主活动窗口
 */
public class CaptureActivity extends FragmentActivity implements BackHandledInterface, AnchorInfoProvider, ChatActivityEvent, OnCaptureActivityListener {
    private BackHandledFragment mBackHandedFragment = null;
    private Window win = null;
    private String rtmpUrl = null; // 推流地址
    private TextView cCapturingStatusTextView = null; // 录屏直播提示信息
    private CaptureFragment captureFragment;
    private ChatFragment anchorChatFragment;
    private MobileUserVo mobileUserVo; // 用户信息引用
    private AnchorChatService.ChatReceiveServiceBinder anchorChatServiceBinder;
    private String wsUrl; // websocket链接

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        win = this.getWindow();
        win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); // 保持屏幕常亮
        this.rtmpUrl = getIntent().getStringExtra(LiveKeyConstants.Global_URL_KEY);
        // win.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); // 全屏显示
        requestWindowFeature(Window.FEATURE_NO_TITLE); // 隐藏标题
        setContentView(R.layout.activity_capture);
        captureFragment = new CaptureFragment();
        anchorChatFragment = new ChatFragment();
        this.mobileUserVo = HomeActivity.mobileUserVo; // 取得用户信息
        String nicknameEncoding = null;
        try {
            nicknameEncoding = URLEncoder.encode(mobileUserVo.getNickname(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
        }
        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append(LiveConstants.REMOTE_SERVER_WEB_SOCKET_IP)
                .append("/chat?account=")
                .append(mobileUserVo.getAccount())
                .append("&chatroom=")
                .append(mobileUserVo.getLiveRoomVo().getRoomNum())
                .append("&nickname=")
                .append(nicknameEncoding)
                .append("&anchor=1");
        this.wsUrl = urlBuilder.toString();
        Intent intent = new Intent(this, AnchorChatService.class);
        intent.putExtra(LiveKeyConstants.Global_URL_KEY, wsUrl);
        bindService(intent, conn, Context.BIND_AUTO_CREATE); // 绑定聊天服务
        initUIElements();
        setDefalultFragment(); // 设置默认fragment
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        win.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); // 取消屏幕常亮
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
    public void logoutService() {
        if (anchorChatServiceBinder != null) {
            unbindService(conn);
            anchorChatServiceBinder = null;
        }
    }

    /**
     * 设置默认fragment
     */
    private void setDefalultFragment() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.fl_capture, captureFragment, "capture");
        ft.add(R.id.fl_capture_chat, anchorChatFragment, "captureChat");
        ft.commit();
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

    @Override
    public LiveRoomInfo getLiveRoomInfo() {
        LiveRoomInfo liveRoomInfo = new LiveRoomInfo(mobileUserVo.getLiveRoomVo().getRoomId(), mobileUserVo.getUserId(), mobileUserVo.getLiveRoomVo().getRoomNum(), null, mobileUserVo.getHeadImgUrl(), mobileUserVo.getLiveRoomVo().getRoomName(), mobileUserVo.getLiveRoomVo().getLiveRoomUrl(), false);
        return liveRoomInfo;
    }

    @Override
    public void backLiving() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.remove(captureFragment); // 移除推流界面
        ft.remove(anchorChatFragment); // 移除主播端聊天界面
        ft.add(R.id.fl_capture, new LiveOverFragment(), "liveOver"); // 添加直播结束界面
        ft.commit();
    }

    @Override
    public AnchorChatService.ChatReceiveServiceBinder getChatReceiveServiceBinder() {
        return this.anchorChatServiceBinder;
    }
}
