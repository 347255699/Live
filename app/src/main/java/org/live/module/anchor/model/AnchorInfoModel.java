package org.live.module.anchor.model;

/**
 * 主播信息逻辑处理层
 * Created by KAM on 2017/4/22.
 */

public interface AnchorInfoModel {
    /**
     * 提交单项主播信息
     */
    public void postAnchorItemInfo();

    /**
     * 上传主播封面图片
     */
    public void postAnchorCoverImg(String filePath);

}
