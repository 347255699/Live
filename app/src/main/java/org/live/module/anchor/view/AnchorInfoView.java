package org.live.module.anchor.view;

import android.content.Intent;

/**
 * 主播信息视图
 * Created by KAM on 2017/4/22.
 */

public interface AnchorInfoView {
    /**
     * 显示提示信息
     *
     * @param msg
     */
    public void showToast(String msg);

    /**
     * 返回
     */
    public void back();

    /**
     * 裁剪封面
     *
     * @param intent
     * @param requestCode 请求标识，返回时携带的标志
     */
    public void cropRoomCover(Intent intent, int requestCode);


    /**
     * 设置头像
     */
    public void setRoomCover();

    /**
     * 进入直播间
     */
    public void intoLiveRoom(boolean isBan);
}
