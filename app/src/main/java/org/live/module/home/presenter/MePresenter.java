package org.live.module.home.presenter;

import org.live.module.login.domain.MobileUserVo;

/**
 * ‘我的’模块表示层
 * Created by KAM on 2017/4/15.
 */

public interface MePresenter {

    /**
     * 获取用户数据
     *
     * @return
     */
    public MobileUserVo getUserData();

    /**
     * 注销登录
     *
     * @param account 账号
     * @param roomId  房间id
     */
    public void logout(String account, String roomId);

}
