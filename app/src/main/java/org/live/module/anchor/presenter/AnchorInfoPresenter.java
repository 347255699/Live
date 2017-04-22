package org.live.module.anchor.presenter;

/**
 * 主播信息表示层
 * Created by KAM on 2017/4/22.
 */

public interface AnchorInfoPresenter {
    /**
     * 提交单项主播信息
     */
    public void postAnchorItemInfo();

    /**
     * 上传主播封面图片
     */
    public void postAnchorCoverImg();
}
