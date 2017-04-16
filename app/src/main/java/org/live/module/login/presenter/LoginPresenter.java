package org.live.module.login.presenter;

import java.util.Map;

/**
 * 登陆模块表示层
 */
public interface LoginPresenter {
    /**
     * 校验表单
     *
     * @param vals
     * @param labels
     * @param rules
     * @return boolean
     */
    public boolean validateForm(Map<String, String> vals, Map<String, String> labels, Map<String, Map<String, Object>> rules);

    /**
     * 请求数据
     *
     * @param params
     * @param mdelType
     * @return
     */
    public void httpRequest(String mdelType, Map<String, Object> params) throws Exception;

    /**
     * 预先登录
     */
    public void preLogin();


}
