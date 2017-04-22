package org.live.module.home.presenter;

/**
 * 直播模块表示层
 * Created by KAM on 2017/4/22.
 */

public interface LivePresenter {
    /**
     * 检查用户是否是主播
     */
    public void checkIsAnchor(String userId);
}
