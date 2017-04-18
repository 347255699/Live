package org.live.module.home.model;

import android.graphics.Bitmap;
import android.net.Uri;

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
     *
     * @param account 账号
     */
    public void logout(String account, String roomId);

    /**
     * 裁剪头像
     */
    public void cropHeadImg(Uri uri);

    /**
     * 上传头像
     */
    public void postHeadImag(String filePath, String userId);

    /**
     * 保存图片至sd卡
     */
    public String setPicToSd(Bitmap bitmap);
}
