package org.live.module.home.view.custom;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import org.live.R;

import me.majiajie.pagerbottomtabstrip.item.BaseTabItem;

/**
 * Created by Mr.wang on 2017/3/14.
 */

public class LocalIconItemView extends BaseTabItem {

    private int mCheckedDrawable;   //被选中时的图标

    private int mDefaultDrawable;   //默认的图标

    private int titleDefaultColor = R.color.tab_title_default_color ;   //标题默认颜色

    private int titleClickColor = R.color.tab_title_click_color ;   //选中时的标题颜色

    private ImageView mIcon;    //图标view

    private TextView titleView ;    //标题view


    public LocalIconItemView(Context paramContext) {
        this(paramContext, null);
    }

    public LocalIconItemView(Context paramContext, AttributeSet paramAttributeSet) {
        this(paramContext, paramAttributeSet, 0);
    }

    public LocalIconItemView(Context paramContext, AttributeSet paramAttributeSet, int iconResId) {
        super(paramContext, paramAttributeSet, iconResId);
        LayoutInflater.from(paramContext).inflate(R.layout.tab_item, this, true);
        this.mIcon = ((ImageView)findViewById(R.id.iv_home_tabIcon)) ;
        this.titleView = (TextView) findViewById(R.id.tv_home_tabTitle) ;
    }

    public String getTitle()
    {
        return this.titleView.getText().toString() ;
    }

    /**
     *
     * @param defaultDrawable 默认的图标资源
     * @param checkedDrawable 选中时的图标资源
     * @param title  标题
     */
    public void initialize(@DrawableRes int defaultDrawable , @DrawableRes int checkedDrawable, String title) {
        this.mDefaultDrawable = defaultDrawable ;
        this.mCheckedDrawable = checkedDrawable ;
        this.titleView.setText(title) ;
    }

    /**
     *
     * @param checked
     */
    public void setChecked(boolean checked) {
        ImageView localImageView = this.mIcon;
        if (checked) {
            localImageView.setImageResource(this.mCheckedDrawable) ;
            this.titleView.setTextColor(getResources().getColor(this.titleClickColor)) ;
        }
        localImageView.setImageResource(mDefaultDrawable);
        this.titleView.setTextColor(getResources().getColor(this.titleDefaultColor)) ;
    }

    public void setHasMessage(boolean paramBoolean) {
    }

    public void setMessageNumber(int paramInt) {
    }
}
