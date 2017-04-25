package org.live.module.publish.view.custom;

import android.content.Context;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import org.live.R;

/**
 * 可点击文本
 * Created by KAM on 2017/4/24.
 */

public class NickNameSpan extends ClickableSpan {
    private Context context;
    private View.OnClickListener onClickListener;

    public NickNameSpan(Context context, View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
        this.context = context;
    }

    @Override
    public void onClick(View widget) {
        onClickListener.onClick(widget); // 点击事件
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setColor(context.getResources().getColor(R.color.colorShallowBlue)); // 设置颜色
        //ds.setUnderlineText(true);
    }
}

