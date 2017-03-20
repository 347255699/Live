package org.live.module.chat.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.koushikdutta.async.future.Future;
import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.WebSocket;

import org.live.module.chat.listener.ChatMsgCallBack;

import java.util.concurrent.ExecutionException;

/**
 * 聊天服务
 * Created by KAM on 2017/3/20.
 */

public class ChatService extends Service {
    private static final String TAG = "Global";
    private String wsUrl = "ws://192.168.253.2:8080/websocket"; // websocket请求地址
    private ChatMsgCallBack chatMsgCallBack; // 消息回调
    private Handler cHandler;
    private static final int MSG_SUCCESS = 0;//获取消息成功的标识
    private static final int MSG_FAILURE = 1;//获取消息失败的标识
    private WebSocket webSocket;
    public static final String ACTION = "org.live.module.chat.service.RECEIVER"; // 广播意图
    private Intent intent = new Intent(ACTION); // 设置广播意图

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        initChatMsgHandler();
        buildWebSocketConnecting(); // 建立连接
        Log.i(TAG, "onBind");
        return new ChatBinder();
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy");
        super.onDestroy();
    }

    /**
     * 设置聊天消息回调，与view进行通讯
     *
     * @param chatMsgCallBack
     */
    public void setChatMsgCallBack(ChatMsgCallBack chatMsgCallBack) {
        this.chatMsgCallBack = chatMsgCallBack;
    }

    /**
     * 聊天初始化消息处理
     */
    private void initChatMsgHandler() {
        cHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MSG_SUCCESS:
                        chatMsgCallBack.onMsgAvailable((String) msg.obj); // 向ui发送消息
                        break;
                    case MSG_FAILURE:
                        showToastMsg("请检查网络是否断开...");
                        break;
                }
            }
        }; // 消息处理
    }

    /**
     * 显示提示消息
     *
     * @param msg
     */
    private void showToastMsg(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show(); // 显示

    }

    /**
     * 建立websocket链接，注意此链接建立在非UI线程内
     */
    private void buildWebSocketConnecting() {
        Future<WebSocket> future = AsyncHttpClient.getDefaultInstance().websocket(wsUrl, "my-protocol", new AsyncHttpClient.WebSocketConnectCallback() {
            @Override
            public void onCompleted(Exception ex, WebSocket webSocket) {

                if (ex != null) {
                    //ex.printStackTrace();
                    cHandler.obtainMessage(MSG_FAILURE);
                    return;
                }
                webSocket.setStringCallback(new WebSocket.StringCallback() {
                    public void onStringAvailable(String msg) {
                        // 字符串消息回调
                        //cHandler.obtainMessage(MSG_SUCCESS, msg).sendToTarget(); // 向UI线程发送消息
                        intent.putExtra("msg", msg);
                        sendBroadcast(intent); //发送广播给Fragment
                    }
                });
            }
        });

        try {
            webSocket = future.get(); // 取得websocket实例，用于发送消息
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送消息
     *
     * @param msg
     */
    public void sendMsg(String msg) {
        webSocket.send(msg); // 发送
    }

    /**
     * 聊天服务binder
     */
    public class ChatBinder extends Binder {
        /**
         * 获取当前Service的实例
         *
         * @return
         */
        public ChatService getChatService() {
            return ChatService.this;
        }
    }


}
