package org.live.module.login.model.impl;

import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.live.common.constants.LiveConstants;
import org.live.common.constants.RequestMethod;
import org.live.common.util.HttpRequestUtils;
import org.live.common.util.JsonUtils;
import org.live.module.login.db.AppDbUtils;
import org.live.module.login.domain.MobileUserVo;
import org.live.module.login.listener.LoginModelListener;
import org.live.module.login.model.LoginModel;
import org.live.module.login.util.Validator;
import org.live.module.login.util.constant.LoginConstant;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * 登陆模块逻辑处理层实现类
 * Created by KAM on 2017/4/14.
 */

public class LoginModelImpl implements LoginModel {
    private Context context;
    private String url = LiveConstants.REMOTE_SERVER_HTTP_IP + "/app"; // 请求地址
    private LoginModelListener listener;
    /**
     * 业务类型{login|register|forgetPassword}
     */
    private String modelType;

    public LoginModelImpl(Context context, LoginModelListener listener) {
        this.context = context;
        this.listener = listener;

    }

    /**
     * 校验表单
     *
     * @param vals   待校验值
     * @param labels 提示标签
     * @param rules  校验规则组
     * @return
     */
    @Override
    public boolean validateForm(Map<String, String> vals, Map<String, String> labels, Map<String, Map<String, Object>> rules) {
        Validator validator = new Validator(context);
        return validator.validate(vals, labels, rules); // 开启校验
    }

    /**
     * 发起http请求
     *
     * @param params 请求参数
     */
    @Override
    public void httpRequest(String modelType, Map<String, Object> params) throws Exception {
        String requestUrl = null;
        this.modelType = modelType;
        switch (modelType) {
            case LoginConstant.MODEL_TYPE_FORGET_PASSWORD:
                requestUrl = url + "/password/resetting";
                break;
            case LoginConstant.MODEL_TYPE_LOGIN:
                requestUrl = url + "/login";
                break;
            case LoginConstant.MODEL_TYPE_REGISTER:
                requestUrl = url + "/register";
                break;
        }
        if (requestUrl != null) {
            HttpRequestUtils.requestHttp(requestUrl, RequestMethod.POST, params, httpResponseHandler, LiveConstants.HTTP_RESPONSE_RESULT_CODE); // 发送请求
        }
    }

    /**
     * 预登陆
     *
     * @return 是否通过校验
     */
    @Override
    public void preLogin() {
        AppDbUtils dbUtils = new AppDbUtils(context, "live", 1);
        Cursor cursor = dbUtils.findOne("select * from live_mobile_user", null);
        if (cursor.getCount() > 0) {
            String account = cursor.getString(cursor.getColumnIndex("account"));
            String password = cursor.getString(cursor.getColumnIndex("password"));
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("account", account);
            params.put("password", password);
            try {
                HttpRequestUtils.requestHttp(url + "/login", RequestMethod.POST, params, httpResponseHandler, LoginConstant.HTTP_RESPONSE_RESULT_PRELOGIN_CODE); // 发送请求
            } catch (Exception e) {
                Log.e("Global", e.getMessage());
            }
        } else {
            listener.toLogin(); // 前往登录页

        }
    }

    /**
     * http响应回调
     */
    Handler httpResponseHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            JSONObject result;
            switch (msg.what) {
                case LiveConstants.HTTP_RESPONSE_RESULT_CODE:
                    result = (JSONObject) msg.obj;
                    if (result != null) {
                        try {
                            int status = result.getInt("status"); // 状态码
                            if (1 == status) {
                                switch (modelType) {
                                    case LoginConstant.MODEL_TYPE_FORGET_PASSWORD:
                                        listener.showToast("密码重置成功");
                                        listener.popBackStack(); // 回退至上一个fragment
                                        break;
                                    case LoginConstant.MODEL_TYPE_LOGIN:
                                        MobileUserVo mobileUserVo = JsonUtils.fromJson(result.getJSONObject("data").toString(), MobileUserVo.class); // 取出用户数据
                                        saveUserData(mobileUserVo); // 持久化用户信息
                                        listener.toHome(); // 前往首页
                                        listener.finishSelf(); // 摧毁自己
                                        break;
                                    case LoginConstant.MODEL_TYPE_REGISTER:
                                        listener.showToast("注册成功");
                                        listener.popBackStack(); // 回退至上一个fragment
                                        break;
                                }
                            } else {
                                String errorMsg = result.getString("message") == null ? "系统繁忙" : result.getString("message");
                                listener.showToast(errorMsg); // 提示错误信息
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        listener.showToast("系统繁忙");
                    }
                    break; // 根据登录相关业务处理请求结果
                case LoginConstant.HTTP_RESPONSE_RESULT_PRELOGIN_CODE:
                    result = (JSONObject) msg.obj;
                    if (result != null) {
                        try {
                            if (1 == result.getInt("status")) {
                                MobileUserVo mobileUserVo = JsonUtils.fromJson(result.getJSONObject("data").toString(), MobileUserVo.class); // 取出用户数据
                                saveUserData(mobileUserVo);
                                listener.toHome(); // 前往首页
                            } else {
                                listener.showToast("密码过期");
                                listener.toLogin(); // 前往登录页

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    break; // 处理预登录请求结果
            }
        }
    };

    /**
     * 持久化用户信息
     */
    private void saveUserData(MobileUserVo mobileUserVo) {
        httpResponseHandler.post(new UserDataThread(mobileUserVo)); // 处理用户相关数据
        //new UserDataThread(mobileUserVo).start();
    }

    /**
     * 用户数据处理线程
     */
    private class UserDataThread extends Thread {
        /**
         * 用户信息引用
         */
        private MobileUserVo mobileUserVo;

        public UserDataThread(MobileUserVo mobileUserVo) {
            this.mobileUserVo = mobileUserVo;
        }

        @Override
        public void run() {
            AppDbUtils dbUtils = new AppDbUtils(context, "live", 1);
            Cursor cursor = dbUtils.findOne("select * from live_mobile_user where account = ?", new String[]{mobileUserVo.getAccount()}); // 查询用户信息
            Object[] mobileUsers = null;
            Object[] liveRooms = null;
            MobileUserVo.LiveRoomInUserVo liveRoomInUserVo = null;
            if (mobileUserVo != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String birthday = sdf.format(mobileUserVo.getBirthday());
                mobileUserVo.getBirthday();
                mobileUsers = new Object[]{
                        mobileUserVo.getUserId(),
                        mobileUserVo.getAccount(),
                        mobileUserVo.getPassword(),
                        mobileUserVo.getNickname(),
                        mobileUserVo.getHeadImgUrl(),
                        mobileUserVo.getEmail(),
                        mobileUserVo.getMobileNumber(),
                        mobileUserVo.getRealName(),
                        mobileUserVo.getSex(),
                        birthday,
                        mobileUserVo.isAnchorFlag()};
                if (mobileUserVo.isAnchorFlag()) {
                    // 确认为主播
                    liveRoomInUserVo = mobileUserVo.getLiveRoomVo();
                    liveRooms = new Object[]{liveRoomInUserVo.getCategoryId(),
                            liveRoomInUserVo.getCategoryName(),
                            liveRoomInUserVo.getRoomId(),
                            liveRoomInUserVo.getRoomNum(),
                            liveRoomInUserVo.getRoomName(),
                            liveRoomInUserVo.getRoomCoverUrl(),
                            liveRoomInUserVo.getLiveRoomUrl(),
                            liveRoomInUserVo.getRoomLabel(),
                            liveRoomInUserVo.isBanLiveFlag(),
                            liveRoomInUserVo.getDescription()};

                }
            }
            if (cursor.getCount() == 0 && mobileUsers != null) {
                Log.i("Global", "------------>>持久化用户数据");

                dbUtils.save("insert into live_mobile_user values(?,?,?,?,?,?,?,?,?,?,?)", mobileUsers); // 保存用户信息
                if (mobileUserVo.isAnchorFlag() && liveRooms != null) {
                    dbUtils.save("insert into live_room values(?,?,?,?,?,?,?,?,?,?)", liveRooms); // 保存用户的直播房间信息
                }
            }
            if (cursor.getCount() == 1 && mobileUsers != null) {
                Log.i("Global", "------------>>更改用户数据");
                dbUtils.save("update live_mobile_user set " +
                        "user_id = ?, " +
                        "account = ?, " +
                        "password = ?, " +
                        "nickname = ?, " +
                        "head_img_url = ?, " +
                        "email = ?, " +
                        "mobile_number = ?, " +
                        "real_name = ?, " +
                        "sex = ?, " +
                        "birthday = ?, " +
                        "anchor_flag = ? " +
                        "where user_id = '"+ mobileUserVo.getUserId() +"'", mobileUsers); // 更新用户信息
                if (mobileUserVo.isAnchorFlag() && liveRooms != null) {
                    dbUtils.save("update live_room set " +
                            "category_id = ?, " +
                            "category_name = ?, " +
                            "room_id = ?, " +
                            "room_num = ?, " +
                            "room_name = ?, " +
                            "room_cover_url = ?, " +
                            "live_room_url = ?, " +
                            "room_label = ?, " +
                            "ban_live_flag = ?, " +
                            "description = ? " +
                            "where room_id = '"+ liveRoomInUserVo.getRoomId() +"'", liveRooms); // 更新用户的直播房间信息

                }
            }
            dbUtils.close(); // 释放资源
        }
    }


}
