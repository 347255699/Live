package org.live.module.chat.view.impl;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.widget.ListView;

/**
 * 聊天滚动视图
 */
public class ChatListView extends ListView {
    private static String TAG = "Global";
    private Context cContext;
    public static int maxHeight;
    public ChatListView(Context context) {
        super(context);
        init(context);
    }

    public ChatListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ChatListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        cContext = context;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        try {
            //最大高度显示为屏幕内容高度的一半
            Display display = ((Activity) cContext).getWindowManager().getDefaultDisplay();
            DisplayMetrics d = new DisplayMetrics();
            display.getMetrics(d);
            maxHeight = d.heightPixels / 4;
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(maxHeight, MeasureSpec.AT_MOST); // 高度设置为屏幕的4/1

        } catch (Exception e) {
            e.printStackTrace();
        }
        //重新计算控件高、宽
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}