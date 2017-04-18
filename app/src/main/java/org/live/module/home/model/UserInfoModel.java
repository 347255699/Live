package org.live.module.home.model;


/**
 * 用户信息逻辑处理层
 * Created by KAM on 2017/4/18.
 */

public interface UserInfoModel {

    /**
     * 修改用户信息
     *
     * @param key
     * @param val
     */
    public void editUserInfo(String key, String val);

    /**
     * 校验输入项
     */
    public boolean validateInputItem(String key, String val, String label);
}
