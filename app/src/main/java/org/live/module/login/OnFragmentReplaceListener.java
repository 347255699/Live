package org.live.module.login;

/**
 * fragment替换回调
 * Created by KAM on 2017/4/11.
 */

public interface OnFragmentReplaceListener {
    /**
     * 替换成注册模块
     */
    public void replaceToRegisterFragment();

    /**
     * 替换成忘记密码模块
     */
    public void replaceToForgetPasswordFragment();

    /**
     * 替换成服务条款模块
     */
    public void replaceToServiceClauseFragment();

    /**
     * 设置标题
     */
    public void setTitle(String title);

}
