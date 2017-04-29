package org.live.module.publish.view.impl;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import org.live.R;
import org.live.common.constants.LiveConstants;
import org.live.common.constants.LiveKeyConstants;
import org.live.common.listener.BackHandledFragment;
import org.live.common.listener.BackHandledInterface;
import org.live.common.listener.ChatActivityEvent;
import org.live.common.provider.AnchorInfoProvider;
import org.live.module.chat.service.AnchorChatService;
import org.live.module.chat.view.impl.ChatFragment;
import org.live.module.home.view.impl.HomeActivity;
import org.live.module.login.domain.MobileUserVo;
import org.live.module.play.domain.LiveRoomInfo;
import org.live.module.publish.listener.OnPublishActivityListener;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * 推流主活动窗口
 */
public class PublishActivity extends FragmentActivity implements BackHandledInterface, OnPublishActivityListener, AnchorInfoProvider, ChatActivityEvent {
    private static final String TAG = "PublishActivity";
    private BackHandledFragment mBackHandedFragment;
    private Window win = null;
    private String rtmpUrl = null;
    private MobileUserVo mobileUserVo;
    private String wsUrl;
    private AnchorChatService.ChatReceiveServiceBinder anchorChatServiceBinder;

    private PublishFragment publishFragment;
    private ChatFragment anchorChatFragment;

    private String anchorId; // 主播
    private String roomId; // 房间Id

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        win = this.getWindow();
        win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); // 保持屏幕常亮
        // win.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); // 全屏显示
        requestWindowFeature(Window.FEATURE_NO_TITLE); // 隐藏标题
        this.rtmpUrl = getIntent().getStringExtra(LiveKeyConstants.Global_URL_KEY); // 初始化参数
        setContentView(R.layout.activity_recorder);
        this.mobileUserVo = HomeActivity.mobileUserVo;

        String nicknameEncoding = null ;
        try {
            nicknameEncoding = URLEncoder.encode(mobileUserVo.getNickname(), "UTF-8") ;
        } catch (UnsupportedEncodingException e) {
        }

        StringBuilder urlStringBuilder = new StringBuilder(128) ;
        urlStringBuilder.append(LiveConstants.REMOTE_SERVER_WEB_SOCKET_IP)
                        .append("/chat?account=")
                        .append(mobileUserVo.getAccount())
                        .append("&chatroom=")
                        .append(mobileUserVo.getLiveRoomVo().getRoomNum())
                        .append("&nickname=")
                        .append(nicknameEncoding)
                        .append("&anchor=1") ;

        this.wsUrl = urlStringBuilder.toString() ; // websocket链接
        Intent intent = new Intent(this, AnchorChatService.class);
        intent.putExtra(LiveKeyConstants.Global_URL_KEY, wsUrl);
        bindService(intent, conn, Context.BIND_AUTO_CREATE); // 绑定聊天服务
        setDefaultFragment(); // 设置默认fragment
        this.anchorId = mobileUserVo.getUserId(); // 取得主播id
        this.roomId = mobileUserVo.getLiveRoomVo().getRoomId(); // 取得当前房间id

    }

    /**
     * 设置默认fragment
     */
    public void setDefaultFragment() {
        this.publishFragment = new PublishFragment();
        this.anchorChatFragment = new ChatFragment();
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.fl_icon_group, publishFragment, "publish"); // 设置推流界面
        ft.add(R.id.fl_chat, anchorChatFragment, "anchorChat"); // 设置主播端的聊天界面
        ft.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        win.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); // 取消屏幕常亮
        logoutService(); // 注销服务
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
    public void logoutService() {
        if (anchorChatServiceBinder != null) {
            unbindService(conn);
            anchorChatServiceBinder = null;
        }
    }

    @Override
    public LiveRoomInfo getLiveRoomInfo() {

        LiveRoomInfo liveRoomInfo = new LiveRoomInfo(mobileUserVo.getLiveRoomVo().getRoomId(), mobileUserVo.getUserId(), mobileUserVo.getLiveRoomVo().getRoomNum(), null, mobileUserVo.getHeadImgUrl(), mobileUserVo.getLiveRoomVo().getRoomName(), mobileUserVo.getLiveRoomVo().getLiveRoomUrl(), false);
        return liveRoomInfo;
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

    /**
     * 退出直播
     */
    @Override
    public void backLiving() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.remove(publishFragment); // 移除推流界面
        ft.remove(anchorChatFragment); // 移除主播端聊天界面
        ft.add(R.id.fl_icon_group, new LiveOverFragment(), "liveOver"); // 添加直播结束界面
        ft.commit();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // 自动旋转打开，Activity随手机方向旋转之后，只需要改变推流方向
        int mobileRotation = getWindowManager().getDefaultDisplay().getRotation();
        publishFragment.onDisplayRotationChanged(mobileRotation);
        Log.i(TAG, "-------------------------------------------------------》》》 Configuration onChange");
    }


}
