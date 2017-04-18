package org.live.module.home.presenter;

import android.graphics.Bitmap;
import android.net.Uri;

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

    /**
     * 裁剪头像
     *
     * @param uri 头像地址
     */
    public void cropHeadImg(Uri uri);

    /**
     * 保存图片至sd卡
     */
    public String setPicToSd(Bitmap bitmap);

    /**
     * @param fileName
     * @param userId   上传头像
     */
    public void postHeadImg(String fileName, String userId);
}
