package org.live.module.anchor.model;

import org.live.module.home.domain.LiveCategoryVo;

import java.util.List;
import java.util.Map;

/**
 * 申请主播逻辑处理层
 * Created by KAM on 2017/4/21.
 */

public interface ApplyAnchorModel {
    /**
     * 校验输入项
     */
    public boolean validateInputItem(Map<String, String> labels, Map<String, String> vals);

    /**
     * 提交主播信息
     */
    public void postAnchorInfo(Map<String, Object> params);

    /**
     * 请求分类信息列表
     *
     * @return
     */
    public void requestCategoryList();
}
