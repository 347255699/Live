package org.live.module.home.view.custom;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by wangzhancheng on 2017/4/4.
 */

public class CategoryItemDecoration extends RecyclerView.ItemDecoration {


    private int bottomSpace = 20 ;  //距离底部的间距

    private int rowSpace = 7 ;   //行内间距

    public CategoryItemDecoration() {

    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        outRect.left = rowSpace;
        outRect.right = rowSpace;
        outRect.bottom = bottomSpace;

    }


}
