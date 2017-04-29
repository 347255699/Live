package org.live.module.play.view.impl;


import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;

import android.support.v4.app.FragmentActivity;
import android.view.Window;
import android.view.WindowManager;

import org.live.R;
import org.live.common.constants.LiveConstants;
import org.live.common.constants.LiveKeyConstants;
import org.live.common.listener.ChatActivityEvent;
import org.live.common.provider.AnchorInfoProvider;
import org.live.module.chat.service.AnchorChatService;
import org.live.module.chat.view.impl.ChatFragment;
import org.live.module.home.constants.HomeConstants;
import org.live.module.home.view.impl.HomeActivity;
import org.live.module.login.domain.MobileUserVo;
import org.live.module.play.domain.LiveRoomInfo;
import org.live.module.play.listener.OnPlayActivityEvent;
import org.live.module.publish.view.impl.LiveOverFragment;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


/**
 * 直播拉流的Activity
 * <p>
 * Created by Mr.wang on 2017/3/9.
 */
public class PlayActivity extends FragmentActivity implements AnchorInfoProvider, OnPlayActivityEvent, ChatActivityEvent {

    public static final String TAG = "PlayActivity";

    private PlayFragment playFragment;
    private ChatFragment chatFragment;
    private LiveRoomInfo liveRoomInfo; // 直播间相关信息
    private MobileUserVo mobileUserVo; // 当前用户信息
    private AnchorChatService.ChatReceiveServiceBinder anchorChatServiceBinder;

    private String wsUrl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); // 保持屏幕常亮
        requestWindowFeature(Window.FEATURE_NO_TITLE);     //隐藏标题
        setContentView(R.layout.activity_play);
        Intent intent = getIntent();
        liveRoomInfo = new LiveRoomInfo(intent.getStringExtra(HomeConstants.LIVE_ROOM_ID_KEY),
                intent.getStringExtra(HomeConstants.ANCHOR_ID_KEY),
                intent.getStringExtra(HomeConstants.LIVE_ROOM_NUM_KEY),
                intent.getIntExtra(HomeConstants.LIVE_ROOM_ONLINE_COUNT_KEY, 1) + "",
                intent.getStringExtra(HomeConstants.HEAD_IMG_URL_KEY),
                intent.getStringExtra(HomeConstants.LIVE_ROOM_NAME_KEY),
                intent.getStringExtra(HomeConstants.LIVE_ROOM_URL_KEY),
                intent.getIntExtra(HomeConstants.LIMIT_TYPE_KEY_FLAG, 0) == 1
        ); // 取得直播间信息

        mobileUserVo = HomeActivity.mobileUserVo; // 取得用户信息
        playFragment = new PlayFragment();
        chatFragment = new ChatFragment();
        String nicknameEncoding = null ;
        try {
            nicknameEncoding = URLEncoder.encode(mobileUserVo.getNickname(), "UTF-8") ;
        } catch (UnsupportedEncodingException e) {
        }
        StringBuilder urlBuilder = new StringBuilder(128);
        urlBuilder.append(LiveConstants.REMOTE_SERVER_WEB_SOCKET_IP)
                .append("/chat?account=")
                .append(mobileUserVo.getAccount())
                .append("&chatroom=")
                .append(liveRoomInfo.getLiveRoomNum())
                .append("&nickname=")
                .append(nicknameEncoding) ;
        this.wsUrl = urlBuilder.toString();
        Intent serviceIntent = new Intent(this, AnchorChatService.class);
        serviceIntent.putExtra(LiveKeyConstants.Global_URL_KEY, wsUrl);
        bindService(serviceIntent, conn, Context.BIND_AUTO_CREATE); // 绑定聊天服务*/
        setFragmentDefault(); // 设置默认fragment
    }

    /**
     * 设置默认的fragment
     */
    public void setFragmentDefault() {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fl_play_playerDump, playFragment, "playerDump");
        fragmentTransaction.add(R.id.fl_play_chat, chatFragment, "playChat");
        fragmentTransaction.commit();
    }

    /**
     * 重新加载当前的fragment
     */
    public void reloadCurrentFragment() {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fl_play_playerDump, playFragment, "playerDump");
       // fragmentTransaction.replace(R.id.fl_play_chat, chatFragment, "playChat");
        fragmentTransaction.commit();
    }

    /**
     * 主播离开，替换fragment为直播结束的fragement
     */
    public void replaceLiveOverFragment() {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.remove(chatFragment);
        fragmentTransaction.replace(R.id.fl_play_playerDump, new LiveOverFragment(), "playerDump");
        fragmentTransaction.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (anchorChatServiceBinder != null) {
            unbindService(conn);
            anchorChatServiceBinder = null;
        }
    }

    @Override
    public LiveRoomInfo getLiveRoomInfo() {
        return this.liveRoomInfo;
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

    @Override
    public void showChatInputView() {
        if (chatFragment != null) {
            chatFragment.showInput(); // 显示输入框
        }
    }

    @Override
    public void logoutService() {
        if (anchorChatServiceBinder != null) {
            unbindService(conn);
            anchorChatServiceBinder = null;
        }
    }

    @Override
    public AnchorChatService.ChatReceiveServiceBinder getChatReceiveServiceBinder() {
        return this.anchorChatServiceBinder;
    }

}
