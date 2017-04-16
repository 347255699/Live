package org.live.module.home.view.custom;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by wangzhancheng on 2017/4/6.
 */

public class LiveRoomItemDecoration extends RecyclerView.ItemDecoration {

    private int bottomSpace = 30 ;  //距离底部的间距

    private int rowSpace = 12 ;   //行内间距

    public LiveRoomItemDecoration() {
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left = rowSpace;
        outRect.right = rowSpace;
        outRect.bottom = bottomSpace;
    }

}
