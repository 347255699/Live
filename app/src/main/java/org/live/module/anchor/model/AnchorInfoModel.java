package org.live.module.anchor.model;

import android.graphics.Bitmap;
import android.net.Uri;

/**
 * 主播信息逻辑处理层
 * Created by KAM on 2017/4/22.
 */

public interface AnchorInfoModel {
    /**
     * 提交单项主播信息
     */
    public void postAnchorItemInfo(String key, String val);

    /**
     * 上传主播封面图片
     */
    public void postAnchorCoverImg(String filePath);

    /**
     * 裁剪房间封面
     *
     * @param uri
     */
    public void cropRoomCover(Uri uri);

    /**
     * 保存图片至sd卡
     */
    public String setPicToSd(Bitmap bitmap);

    /**
     * 校验输入项
     */
    public boolean validateInputItem(String key, String val);

}
