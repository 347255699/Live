package org.live.module.home.model;

import org.live.module.login.domain.MobileUserVo;

import java.util.Map;

/**
 * ‘我的’模块逻辑处理层
 * Created by KAM on 2017/4/15.
 */

public interface MeModel {

    /**
     * 取得用户信息
     *
     * @return
     */
    public MobileUserVo getUserData();

    /**
     * 注销
     * @param account 账号
     */
    public void logout(String account, String roomId);

    /**
     * 修改用户信息
     *
     * @param params
     */
    public void editUserInfo(Map<String, Object> params);
}
