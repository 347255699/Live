package org.live.module.anchor.view;

import org.live.module.home.domain.LiveCategoryVo;

import java.util.List;

/**
 * 申请主播视图
 * Created by KAM on 2017/4/21.
 */

public interface ApplyAnchorView {
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
     * 显示分类列表
     * @param liveCategoryVos
     */
    public void showCategoryList(List<LiveCategoryVo> liveCategoryVos);
}
