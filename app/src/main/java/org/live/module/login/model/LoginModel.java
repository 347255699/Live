package org.live.module.login.model;


import java.util.Map;

/**
 * 登陆模块逻辑处理层
 * Created by KAM on 2017/4/14.
 */

public interface LoginModel {
    /**
     * 校验表单
     *
     * @param vals
     * @param labels
     * @param rules
     * @return
     */
    public boolean validateForm(Map<String, String> vals, Map<String, String> labels, Map<String, Map<String, Object>> rules);

    /**
     * 发起http请求
     *
     * @param params    请求参数
     * @param modelType 业务类型
     */
    public void httpRequest(String modelType, Map<String, Object> params) throws Exception;

    /**
     * 预登陆
     */
    public void preLogin();
}
