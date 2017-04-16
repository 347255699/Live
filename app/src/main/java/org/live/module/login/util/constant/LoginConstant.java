package org.live.module.login.util.constant;

/**
 * 登陆模块相关常量
 * Created by KAM on 2017/4/14.
 */

public class LoginConstant {
    /**
     * 登陆业务标识
     */
    public static final String MODEL_TYPE_LOGIN = "LOGIN";
    /**
     * 注册业务标识
     */
    public static final String MODEL_TYPE_REGISTER = "REGISTER";
    /**
     * 忘记密码业务标识
     */
    public static final String MODEL_TYPE_FORGET_PASSWORD = "FORGET_PASSWORD";
    /**
     * 用于HttpRequestUtils工具类的，预登录响应结果标识
     */
    public static final int HTTP_RESPONSE_RESULT_PRELOGIN_CODE = 2001;
    /**
     * 登陆状态标记，已登录
     */
    public static final int LOGIN_STATUS_ON = 1;
    /**
     * 登陆状态标记，退出登录
     */
    public static final int LOGIN_STATUS_OFF = 0;
}
