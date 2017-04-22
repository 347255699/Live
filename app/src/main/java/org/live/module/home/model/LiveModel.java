package org.live.module.home.model;

/**
 * 直播模块逻辑处理层
 * Created by KAM on 2017/4/22.
 */

public interface LiveModel {
    /**
     * 检查用户是否是主播
     */
    public void checkIsAnchor(String userId);


}
