package org.live.module.home.model.impl;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.igexin.sdk.PushManager;
import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.AsyncHttpPost;
import com.koushikdutta.async.http.AsyncHttpPut;
import com.koushikdutta.async.http.AsyncHttpResponse;
import com.koushikdutta.async.http.body.MultipartFormDataBody;

import org.live.common.constants.LiveConstants;
import org.live.common.util.JsonUtils;
import org.live.common.util.SimpleResponseModel;
import org.live.module.home.constants.HomeConstants;
import org.live.module.home.model.MeModel;
import org.live.module.home.view.MeView;
import org.live.module.home.view.impl.HomeActivity;
import org.live.module.login.db.AppDbUtils;
import org.live.module.login.domain.MobileUserVo;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * '我的’模块逻辑处理层实现
 * Created by KAM on 2017/4/15.
 */

public class MeModelImpl implements MeModel {

    private Context context;
    private MeView meView;
    private String path = "/sdcard/myHead/";// sd路径
    private String url = LiveConstants.REMOTE_SERVER_HTTP_IP;

    public MeModelImpl(Context context, MeView meView) {
        this.context = context;
        this.meView = meView;
    }

    /**
     * 获得用户信息
     *
     * @return
     */
    @Override
    public MobileUserVo getUserData() {
        MobileUserVo mobileUserVo = new MobileUserVo();
        AppDbUtils dbUtils = new AppDbUtils(context, "live", 1);
        Cursor cursor = dbUtils.findOne("select * from live_mobile_user", null);
        if (cursor.getCount() > 0) {
            for (String columnName : cursor.getColumnNames()) {
                String val = cursor.getString(cursor.getColumnIndex(columnName));
                switch (columnName) {
                    case "user_id":
                        mobileUserVo.setUserId(val);
                        break;
                    case "account":
                        mobileUserVo.setAccount(val);
                        break;
                    case "password":
                        mobileUserVo.setPassword(val);
                        break;
                    case "nickname":
                        mobileUserVo.setNickname(val);
                        break;
                    case "head_img_url":
                        mobileUserVo.setHeadImgUrl(val);
                        break;
                    case "email":
                        mobileUserVo.setEmail(val);
                        break;
                    case "mobile_number":
                        mobileUserVo.setMobileNumber(val);
                        break;
                    case "real_name":
                        mobileUserVo.setRealName(val);
                        break;
                    case "sex":
                        mobileUserVo.setSex(val);
                        break;
                    case "birthday":
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        Date date = null;
                        try {
                            date = sdf.parse(val);
                        } catch (ParseException e) {
                            Log.e("Global", val);
                            Log.e("Global", e.getMessage());
                        }
                        mobileUserVo.setBirthday(date);
                        break;
                    case "anchor_flag":
                        boolean anchorFlag = false;
                        if (val.equals("1")) {
                            anchorFlag = true;
                        }
                        mobileUserVo.setAnchorFlag(anchorFlag);
                        break;
                }

            }
            if (mobileUserVo.isAnchorFlag()) {
                MobileUserVo.LiveRoomInUserVo liveRoomVo = mobileUserVo.newInstantLiveRoomVo();
                cursor = dbUtils.findOne("select * from live_room", null);
                if (cursor.getCount() > 0) {
                    for (String name2 : cursor.getColumnNames()) {
                        String val2 = cursor.getString(cursor.getColumnIndex(name2));
                        switch (name2) {
                            case "category_id":
                                liveRoomVo.setCategoryId(val2);
                                break;
                            case "category_name":
                                liveRoomVo.setCategoryName(val2);
                                break;
                            case "room_id":
                                liveRoomVo.setRoomId(val2);
                                break;
                            case "room_num":
                                liveRoomVo.setRoomNum(val2);
                                break;
                            case "room_name":
                                liveRoomVo.setRoomName(val2);
                                break;
                            case "room_cover_url":
                                liveRoomVo.setRoomCoverUrl(val2);
                                break;
                            case "live_room_url":
                                liveRoomVo.setLiveRoomUrl(val2);
                                break;
                            case "room_label":
                                liveRoomVo.setRoomLabel(val2);
                                break;
                            case "ban_live_flag":
                                boolean banLiveFlag = false;
                                if (val2.equals("1")) {
                                    banLiveFlag = true;
                                }
                                liveRoomVo.setBanLiveFlag(banLiveFlag);
                                break;
                            case "description":
                                liveRoomVo.setDescription(val2);
                                break;
                        }
                    }
                }
                mobileUserVo.setLiveRoomVo(liveRoomVo);
            }
        }
        dbUtils.close(); // 释放数据库资源
        return mobileUserVo;
    }

    /**
     * 注销登录
     *
     * @param account 账号
     * @param roomId  主播房间id
     */
    @Override
    public void logout(String account, String roomId) {
        AppDbUtils dbUtils = new AppDbUtils(context, "live", 1);
        dbUtils.delete("delete from live_mobile_user where account = ?", account); // 清除用户数据
        if (roomId != null) {
            dbUtils.delete("delete from live_room where room_id = ?", roomId); // 若是主播则清空主播房间数据
        }
        dbUtils.close(); // 释放数据库资源
        PushManager.getInstance().unBindAlias(context, account, true) ;  //解绑别名
        meView.finishSelf(); // 摧毁自己
        meView.toLogin(); // 前往登陆页面


    }

    /**
     * 裁剪头像
     *
     * @param uri
     */
    @Override
    public void cropHeadImg(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        meView.cropHeadImg(intent, HomeConstants.CROP_RESULT_CODE + HomeConstants.HEAD_IMG);
    }

    /**
     * 上传头像
     *
     * @param filePath
     */
    @Override
    public void postHeadImag(String filePath, String userId) {

        AsyncHttpPut put = new AsyncHttpPut(LiveConstants.REMOTE_SERVER_HTTP_IP + "/app/headImg/" + userId);
        MultipartFormDataBody body = new MultipartFormDataBody();
        body.addFilePart("file", new File(filePath));
        put.setBody(body);
        AsyncHttpClient.getDefaultInstance().executeString(put, new AsyncHttpClient.StringCallback() {
            @Override
            public void onCompleted(Exception ex, AsyncHttpResponse source, String result) {
                if (ex != null) {
                    Log.e("Global", ex.getMessage());
                    return;
                }
                Message message = responseHandler.obtainMessage(HomeConstants.HTTP_RESPONSE_RESULT_POST_HEAD_IMG_CODE);
                message.obj = result;
                responseHandler.sendMessage(message);
            }
        });
    }

    /**
     * 设置图像至sd卡
     *
     * @param mBitmap
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
        String fileName = path + "head.jpg";// 图片名字
        try {
            b = new FileOutputStream(fileName);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
        } catch (FileNotFoundException e) {
            Log.e("Global", e.getMessage());
        } finally {
            try {
                // 关闭流
                b.flush();
                b.close();
            } catch (IOException e) {
                Log.e("Global", e.getMessage());
            }
        }
        return fileName;
    }

    /**
     * 响应处理
     */
    Handler responseHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HomeConstants.HTTP_RESPONSE_RESULT_POST_HEAD_IMG_CODE:
                    String result = (String) msg.obj;
                    if (result != null) {
                        SimpleResponseModel model = JsonUtils.fromJson(result, SimpleResponseModel.class);
                        if (model.getStatus() == 1) {
                            MobileUserVo mobileUserVo = HomeActivity.mobileUserVo;
                            mobileUserVo.setHeadImgUrl((String) model.getData());
                            HomeActivity.mobileUserVo = mobileUserVo;
                            meView.setHeadImg(); // 设置头像
                        } else {
                            meView.showToast(model.getMessage());
                        }
                    } else {
                        meView.showToast("系统繁忙");
                    }
                    break; // 处理头像上传响应信息
                default:
                    break;
            }
        }
    };

}
