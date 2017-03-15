package org.live.module.home.view.custom;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import net.steamcrafted.materialiconlib.MaterialDrawableBuilder;
import net.steamcrafted.materialiconlib.MaterialIconView;

import org.live.R;

import me.majiajie.pagerbottomtabstrip.item.BaseTabItem;

/**
 * Created by Mr.wang on 2017/3/14.
 */

public class LocalIconItemView extends BaseTabItem {

    private MaterialDrawableBuilder.IconValue defaultIcon ;     //默认的图标

    private MaterialDrawableBuilder.IconValue checkedIcon ;     //被选中时的图标

    private static final int TAB_DEFAULT_COLOR = R.color.tab_default_color ;   //标题默认颜色

    private static final int TAB_CHECKED_COLOR = R.color.tab_checked_color ;   //选中时的标题颜色

    private MaterialIconView iconView ; //图标view

    private TextView titleView ;    //标题view


    public LocalIconItemView(Context paramContext) {
        this(paramContext, null);
    }

    public LocalIconItemView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet) ;
        LayoutInflater.from(paramContext).inflate(R.layout.tab_item, this, true);
        this.titleView = (TextView) findViewById(R.id.tv_home_tabTitle) ;
        this.iconView = (MaterialIconView) findViewById(R.id.iv_home_tabIcon) ;
    }

    public String getTitle()
    {
        return this.titleView.getText().toString() ;
    }

    /**
     *
     * @param defaultIcon 默认的图标资源
     * @param checkedIcon 选中时的图标资源
     * @param title  标题
     */
    public void initialize(MaterialDrawableBuilder.IconValue defaultIcon, MaterialDrawableBuilder.IconValue checkedIcon, String title) {
        this.defaultIcon = defaultIcon ;
        this.checkedIcon = checkedIcon ;
        this.titleView.setText(title) ;
    }

    /**
     *
     * @param checked
     */
    public void setChecked(boolean checked) {
        MaterialIconView localImageView = this.iconView;
        if (checked) {
            localImageView.setIcon(checkedIcon) ;
           // localImageView.setColor(TAB_CHECKED_COLOR);
            localImageView.setColorResource(TAB_CHECKED_COLOR );
            titleView.setTextColor(getResources().getColor(TAB_CHECKED_COLOR)) ;
            return ;
        }
        localImageView.setIcon(defaultIcon) ;
        //localImageView.setColor(TAB_DEFAULT_COLOR);
        localImageView.setColorResource(TAB_DEFAULT_COLOR );
        titleView.setTextColor(getResources().getColor(TAB_DEFAULT_COLOR)) ;
    }

    public void setHasMessage(boolean paramBoolean) {
    }

    public void setMessageNumber(int paramInt) {
    }
}
