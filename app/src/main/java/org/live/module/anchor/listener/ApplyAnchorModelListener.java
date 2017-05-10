package org.live.module.anchor.listener;

        import org.live.module.home.domain.LiveCategoryVo;

        import java.util.List;

/**
 * model层对视图层的反向驱动
 * Created by KAM on 2017/4/21.
 */

public interface ApplyAnchorModelListener {
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
     *
     * @param liveCategoryVos
     */
    public void showCategoryList(List<LiveCategoryVo> liveCategoryVos);



}
