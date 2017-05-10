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
import org.live.common.util.JsonUtils;

import java.util.concurrent.ExecutionException;


/**
 * 主播聊天消息接受服务
 * Created by KAM on 2017/4/24.
 */

public class AnchorChatService extends Service {
    private static final String TAG = "Global";
    private String wsUrl;

    public static final String ACTION = "org.live.module.chat.service.RECEIVER"; // 广播意图
    //duplexing

    private Intent intent = new Intent(ACTION); // 设置广播意图
    private WebSocket websocket;

    Future<WebSocket> future ;  //未来获取到websocket

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        try {
            wsUrl = intent.getStringExtra(LiveKeyConstants.Global_URL_KEY); // 取得websocket链接
            Log.i(TAG, "---------------------》》开始建立websocket链接");
            buildWebSocketConnecting(); // 建立链接
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return new ChatReceiveServiceBinder();
    }


    @Override
    public void onDestroy() {
        Log.i(TAG, "---------------------》》断开websocket链接");
        //if (websocket != null) {
        getWebsocket().close(); // 关闭websocket链接
        //}
        super.onDestroy();
    }

    /**
     * 建立websocket链接，注意此链接建立在非UI线程内
     */
    private void buildWebSocketConnecting() throws Exception {
        future = AsyncHttpClient.getDefaultInstance().websocket(wsUrl + "", "my-protocol", new AsyncHttpClient.WebSocketConnectCallback() {
            @Override
            public void onCompleted(Exception ex, WebSocket webSocket) {
                if (ex != null) {
                    Log.e("Global", ex.getMessage());
                    return;
                }
                webSocket.setStringCallback(new WebSocket.StringCallback() {
                    public void onStringAvailable(String result) {
                        Log.i("Global", "-------->>" + result);
                        intent.putExtra("msg", result);
                        sendBroadcast(intent); //发送广播给Fragment

                    }
                });
            }
        });
        //this.websocket = future.get();
    }

    /**
     * 聊天服务binder
     */
    public class ChatReceiveServiceBinder extends Binder {

        public void sendMsg(Message message) {
            getWebsocket().send(JsonUtils.toJson(message)); // 发送消息
        } // 发送消息
    }

    /**
     * 延迟加载获取webssocket
     * @return
     */
    private WebSocket getWebsocket() {
        if(websocket == null) {
            try {
                websocket = future.get() ;
            } catch (Exception e) {
                Log.d(TAG, e != null ? e.getMessage() : "unknown Exception") ;
            }
        }
        return websocket ;
    }
}
