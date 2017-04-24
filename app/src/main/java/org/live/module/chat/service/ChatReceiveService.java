package org.live.module.chat.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.koushikdutta.async.future.Future;
import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.WebSocket;

import org.live.common.constants.LiveKeyConstants;
import org.live.common.domain.Message;
import org.live.common.domain.MessageType;
import org.live.common.util.JsonUtils;
import org.live.module.home.view.impl.HomeActivity;
import org.live.module.login.domain.MobileUserVo;

import java.util.concurrent.ExecutionException;


/**
 * 主播聊天消息接受服务
 * Created by KAM on 2017/4/24.
 */

public class ChatReceiveService extends Service {
    private static final String TAG = "Global";
    private String wsUrl;
    //duplexing
    public static final String ACTION = "org.live.module.chat.service.RECEIVER"; // 广播意图
    private Intent intent = new Intent(ACTION); // 设置广播意图
    private WebSocket websocket;
    private MobileUserVo mobileUserVo;

    // 此websocket连接，必选3个参数 chatroom（直播间号），account（用户账号）, nickname(昵称),  1个可选参数 anchor（是否主播开启直播间的flag）
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        try {
            wsUrl = intent.getStringExtra(LiveKeyConstants.Global_URL_KEY); // 取得websocket链接
            this.mobileUserVo = HomeActivity.mobileUserVo; // 取得用户信息引用（当前用户为主播）
            buildWebSocketConnecting(); // 建立链接
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return null;
    }

    /**
     * 建立websocket链接，注意此链接建立在非UI线程内
     */
    private void buildWebSocketConnecting() throws Exception {
        Future<WebSocket> future = AsyncHttpClient.getDefaultInstance().websocket(wsUrl + "", "my-protocol", new AsyncHttpClient.WebSocketConnectCallback() {
            @Override
            public void onCompleted(Exception ex, WebSocket webSocket) {
                if (ex != null) {
                    Log.e("Global", ex.getMessage());
                    return;
                }
                webSocket.setStringCallback(new WebSocket.StringCallback() {
                    public void onStringAvailable(String result) {
                        Message message = JsonUtils.fromJson(result, Message.class);
                        switch (message.getMessageType()) {
                            case MessageType.SEND_TO_CHATROOM_MESSAGE_TYPE:
                                intent.putExtra("msg", result);
                                sendBroadcast(intent); //发送广播给Fragment
                                break;
                            default:
                                break;
                        }
                    }
                });
            }
        });
        this.websocket = future.get();
    }

    /**
     * 聊天服务binder
     */
    public class ChatReceiveServiceBinder extends Binder {

        public void sendMsg(Message message) {
            // TODO 发送消息

        }
    }
}
