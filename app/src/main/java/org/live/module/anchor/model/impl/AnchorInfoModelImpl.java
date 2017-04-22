package org.live.module.anchor.model.impl;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.AsyncHttpPost;
import com.koushikdutta.async.http.AsyncHttpPut;
import com.koushikdutta.async.http.AsyncHttpResponse;
import com.koushikdutta.async.http.body.MultipartFormDataBody;

import org.live.common.constants.LiveConstants;
import org.live.common.util.JsonUtils;
import org.live.common.util.SimpleResponseModel;
import org.live.module.anchor.constants.AnchorConstants;
import org.live.module.anchor.model.AnchorInfoModel;
import org.live.module.home.listener.AnchorInfoModelListener;
import org.live.module.home.view.impl.HomeActivity;

import java.io.File;

/**
 * 主播信息逻辑处理层实现
 * Created by KAM on 2017/4/23.
 */

public class AnchorInfoModelImpl implements AnchorInfoModel {

    private Context context;
    private AnchorInfoModelListener modelListener;

    public AnchorInfoModelImpl(Context context, AnchorInfoModelListener modelListener) {
        this.context = context;
        this.modelListener = modelListener;
    }

    @Override
    public void postAnchorItemInfo() {

    }

    /**
     * 提交用户封面
     */
    @Override
    public void postAnchorCoverImg(String filePath) {
        String liveRoomId = HomeActivity.mobileUserVo.getLiveRoomVo().getRoomId();
        AsyncHttpPut put = new AsyncHttpPut(LiveConstants.REMOTE_SERVER_HTTP_IP + "/app/liveroom/cover/" + liveRoomId);
        MultipartFormDataBody body = new MultipartFormDataBody();
        body.addFilePart("file", new File(filePath)); // 添加文件
        put.setBody(body);
        AsyncHttpClient.getDefaultInstance().executeString(put, new AsyncHttpClient.StringCallback() {
            @Override
            public void onCompleted(Exception ex, AsyncHttpResponse source, String result) {
                if (ex != null) {
                    Log.e("Global", ex.getMessage());
                    return;
                }
                Message message = responseHandler.obtainMessage(AnchorConstants.HTTP_RESPONSE_RESULT_POST_LIVE_ROOM_COVER_CODE);
                message.obj = result;
                responseHandler.sendMessage(message);
            }
        }); // 提交表单
    }

    /**
     * 处理响应信息
     */
    Handler responseHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == AnchorConstants.HTTP_RESPONSE_RESULT_POST_LIVE_ROOM_COVER_CODE) {
                SimpleResponseModel model = JsonUtils.fromJson((String) msg.obj, SimpleResponseModel.class);
                if (model.getStatus() == 1) {
                    modelListener.showToast("提交成功");
                    modelListener.back();
                } else {
                    modelListener.showToast(model.getMessage());
                }
            }
        }
    };
}
