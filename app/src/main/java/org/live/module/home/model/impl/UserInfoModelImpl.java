package org.live.module.home.model.impl;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.live.common.constants.LiveConstants;
import org.live.common.util.HttpRequestUtils;
import org.live.module.home.constants.HomeConstants;
import org.live.module.home.listener.UserInfoModelListener;
import org.live.module.home.model.UserInfoModel;
import org.live.module.home.view.impl.HomeActivity;
import org.live.module.login.domain.MobileUserVo;
import org.live.module.login.util.Validator;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by KAM on 2017/4/18.
 */

public class UserInfoModelImpl implements UserInfoModel {
    private Context context;
    private UserInfoModelListener modelListener;
    /**
     * 修改项的key
     */
    private String key;
    /**
     * 修改项的新值
     */
    private String val;

    public UserInfoModelImpl(Context context, UserInfoModelListener modelListener) {
        this.context = context;
        this.modelListener = modelListener;
    }

    /**
     * 修改用户信息
     *
     * @param key
     * @param val
     */
    @Override
    public void editUserInfo(String key, String val) {
        this.key = key;
        this.val = val;
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(key, val);
        try {
            HttpRequestUtils.requestHttp(LiveConstants.REMOTE_SERVER_HTTP_IP + "/app/user/" + HomeActivity.mobileUserVo.getUserId(), "PUT", params, editUserInfoResponseHandler, HomeConstants.HTTP_RESPONSE_RESULT_EDDIT_USER_INFO_CODE); // 发送修改请求
        } catch (Exception e) {
            Log.e("Global", e.getMessage());
        }
    }

    /**
     * 校验输入项
     *
     * @param key
     * @param val
     * @param label
     */
    @Override
    public boolean validateInputItem(String key, String val, String label) {
        Map<String, String> vals = new LinkedHashMap<String, String>(); // 待校验的字符组
        Map<String, String> labels = new HashMap<String, String>(); // 字符组对应的标签组
        Map<String, Map<String, Object>> rules = new HashMap<String, Map<String, Object>>();// 字符组对应的规则组
        vals.put(key, val);
        labels.put(key, label);
        Map<String, Object> rule = new LinkedHashMap<String, Object>();
        switch (key) {
            case "nickname":
                rule.put("maxLength", 10);
                rule.put("required", true);
                break;
            case "email":
                rule.put("required", true);
                rule.put("matchRegEx", "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$");
                break;
            case "mobileNumber":
                rule.put("required", true);
                rule.put("matchRegEx", "^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\\d{8}$");
                break;
        }
        rules.put(key, rule);
        // 构建校验规则组

        Validator validator = new Validator(context);
        return validator.validate(vals, labels, rules); // 开启校验
    }

    /**
     * 服务器响应处理类
     */
    Handler editUserInfoResponseHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == HomeConstants.HTTP_RESPONSE_RESULT_EDDIT_USER_INFO_CODE) {
                JSONObject jsonObject = (JSONObject) msg.obj;
                if (jsonObject != null) {
                    try {
                        if (jsonObject.getInt("status") == 1) {

                            MobileUserVo mobileUserVo = HomeActivity.mobileUserVo;

                            switch (key) {
                                case "nickname":
                                    mobileUserVo.setNickname(val);
                                    break;
                                case "email":
                                    mobileUserVo.setEmail(val);
                                    break;
                                case "mobileNumber":
                                    mobileUserVo.setMobileNumber(val);
                                    break;
                            }
                            HomeActivity.mobileUserVo = mobileUserVo; // 刷新缓存
                            modelListener.showToast(jsonObject.getString("message"));
                            modelListener.closeKeyboard(); // 关闭软键盘
                            modelListener.back(); // 返回
                        } else {
                            modelListener.showToast(jsonObject.getString("message"));
                        }
                    } catch (JSONException e) {
                        Log.e("Global", e.getMessage());
                    }
                } else {
                    modelListener.showToast("系统繁忙");
                }
            }
        }
    };
}
