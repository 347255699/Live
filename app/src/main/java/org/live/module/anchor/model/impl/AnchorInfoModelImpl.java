package org.live.module.anchor.model.impl;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.AsyncHttpPut;
import com.koushikdutta.async.http.AsyncHttpResponse;
import com.koushikdutta.async.http.body.MultipartFormDataBody;

import org.json.JSONObject;
import org.live.common.constants.LiveConstants;
import org.live.common.constants.RequestMethod;
import org.live.common.util.HttpRequestUtils;
import org.live.common.util.JsonUtils;
import org.live.common.util.SimpleResponseModel;
import org.live.module.anchor.constants.AnchorConstants;
import org.live.module.anchor.model.AnchorInfoModel;
import org.live.module.anchor.listener.AnchorInfoModelListener;
import org.live.module.home.constants.HomeConstants;
import org.live.module.home.view.impl.HomeActivity;
import org.live.module.login.domain.MobileUserVo;
import org.live.module.login.util.Validator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 主播信息逻辑处理层实现
 * Created by KAM on 2017/4/23.
 */

public class AnchorInfoModelImpl implements AnchorInfoModel {

    private Context context;
    private AnchorInfoModelListener modelListener;
    private String path = "/sdcard/myRoomCover/"; // sd路径

    public AnchorInfoModelImpl(Context context, AnchorInfoModelListener modelListener) {
        this.context = context;
        this.modelListener = modelListener;
    }

    /**
     * 上传主播单项信息
     *
     * @param key
     * @param val
     */
    @Override
    public void postAnchorItemInfo(String key, String val) {
        Map<String, Object> param = new HashMap<>();
        param.put(key, val);

        try {
            HttpRequestUtils.requestHttp(LiveConstants.REMOTE_SERVER_HTTP_IP + "/app/liveroom/" + HomeActivity.mobileUserVo.getLiveRoomVo().getRoomId(), RequestMethod.PUT, param, responseHandler, LiveConstants.HTTP_RESPONSE_RESULT_CODE);
        } catch (Exception e) {
            Log.e("Global", e.getMessage());
        }

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
     * 切割封面
     *
     * @param uri
     */
    @Override
    public void cropRoomCover(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 4);
        intent.putExtra("aspectY", 3);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 130);
        intent.putExtra("return-data", true);
        modelListener.cropRoomCover(intent, HomeConstants.CROP_RESULT_CODE + HomeConstants.LIVE_ROOM_COVER);
    }

    /**
     * 保存图片至sd卡
     *
     * @param mBitmap
     * @return
     */
    @Override
    public String setPicToSd(Bitmap mBitmap) {
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
            return null;
        }
        FileOutputStream b = null;
        File file = new File(path);
        file.mkdirs();// 创建文件夹
        String fileName = path + "cover.jpg";// 图片名字
        try {
            b = new FileOutputStream(fileName);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
        } catch (FileNotFoundException e) {
            Log.e("Global", e.getMessage());
        } finally {
            try {
                // 关闭流
                if (b != null) {
                    b.flush();
                    b.close();
                }
            } catch (IOException e) {
                Log.e("Global", e.getMessage());
            }
        }
        return fileName;
    }

    /**
     * 校验输入项
     *
     * @param key
     * @param val
     */
    @Override
    public boolean validateInputItem(String key, String val) {
        Map<String, Map<String, Object>> rules = new HashMap<>();
        Map<String, Object> rule = new LinkedHashMap<>();
        if (key.equals("description")) {
            rule.put("required", true);
            rule.put("maxLength", 15);
        }
        if (key.equals("roomName")) {
            rule.put("required", true);
            rule.put("maxLength", 10);
        }
        rules.put(key, rule);
        Map<String, String> vals = new HashMap<>();
        vals.put(key, val);
        Map<String, String> labels = new HashMap<>();
        if (key.equals("roomName")) {
            labels.put(key, "房间名");
        }
        if (key.equals("description")) {
            labels.put(key, "个性签名");
        }
        Validator validator = new Validator(context);
        return validator.validate(vals, labels, rules); // 开启校验
    }

    @Override
    public void checkLiveRoomIsBan() {
        try {
            HttpRequestUtils.requestHttp(LiveConstants.REMOTE_SERVER_HTTP_IP + "/app/liveroom/" + HomeActivity.mobileUserVo.getLiveRoomVo().getRoomId(), RequestMethod.GET, null, responseHandler, HomeConstants.HTTP_RESPONSE_IS_BAN_RESULT_CODE);
        } catch (Exception e) {
            Log.e("Global", e.getMessage());
        }
    }

    /**
     * 处理响应信息
     */
    Handler responseHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == AnchorConstants.HTTP_RESPONSE_RESULT_POST_LIVE_ROOM_COVER_CODE) {
                SimpleResponseModel model = JsonUtils.fromJson((String) msg.obj, SimpleResponseModel.class);
                if (model != null) {
                    if (model.getStatus() == 1) {
                        modelListener.showToast("提交成功");
                        MobileUserVo.LiveRoomInUserVo liveRoomVo = HomeActivity.mobileUserVo.getLiveRoomVo();
                        liveRoomVo.setRoomCoverUrl((String) model.getData());
                        modelListener.setRoomCover(); // 设置封面
                    } else {
                        modelListener.showToast(model.getMessage());
                    }
                }
            }
            if (msg.what == LiveConstants.HTTP_RESPONSE_RESULT_CODE) {
                JSONObject reulst = (JSONObject) msg.obj;
                if (reulst != null) {
                    SimpleResponseModel model = JsonUtils.fromJson(reulst.toString(), SimpleResponseModel.class);
                    if (model.getStatus() == 1) {
                        modelListener.back(); // 返回
                        modelListener.showToast("修改成功");
                    } else {
                        modelListener.showToast(model.getMessage());
                    }
                }
            }
            if (msg.what == HomeConstants.HTTP_RESPONSE_IS_BAN_RESULT_CODE) {
                JSONObject reulst = (JSONObject) msg.obj;
                if (reulst != null) {
                    SimpleResponseModel model = JsonUtils.fromJson(reulst.toString(), SimpleResponseModel.class);
                    if (model.getStatus() == 1) {
                        modelListener.intoLiveRoom((Boolean) model.getData());
                    } else {
                        modelListener.showToast(model.getMessage());
                    }
                }
            }
        }
    };
}
