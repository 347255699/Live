package org.live.module.anchor.presenter;

import org.live.module.home.domain.LiveCategoryVo;

import java.util.List;
import java.util.Map;

/**
 * 申请主播表示层
 * Created by KAM on 2017/4/21.
 */

public interface ApplyAnchorPresenter {
    /**
     * 校验输入项
     */
    public void validateInputItem(Map<String, String> labels, Map<String, String> vals);

    /**
     * 提交主播信息
     */
    public void postAnchorInfo(Map<String,Object> params);

    /**
     * 请求分类信息列表
     *
     * @return
     */
    public void requestCategoryList();
}
