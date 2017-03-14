package org.live.module.home.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.view.Window;

import org.live.R;
import org.live.module.home.view.custom.LocalIconItemView;

import me.majiajie.pagerbottomtabstrip.MaterialMode;
import me.majiajie.pagerbottomtabstrip.NavigationController;
import me.majiajie.pagerbottomtabstrip.PageBottomTabLayout;
import me.majiajie.pagerbottomtabstrip.item.BaseTabItem;


/**
 * 主界面
 * Created by Mr.wang on 2017/3/14.
 */
public class HomeActivity extends FragmentActivity {


    public static final int DEFAULT_COLOR = 0xFFFF00;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE) ;     //隐藏标题
        setContentView(R.layout.activity_home) ;
        PageBottomTabLayout bottomTabLayout = (PageBottomTabLayout) this.findViewById(R.id.tab_home_nav) ;
        PageBottomTabLayout.CustomBuilder builder = bottomTabLayout.custom()
                .addItem(this.newItem(R.drawable.tab_user,R.drawable.tab_user_click, "首页"))
                .addItem(this.newItem(R.drawable.tab_play,R.drawable.tab_play_click, "位置"))
                .addItem(this.newItem(R.drawable.tab_video,R.drawable.tab_video_click, "搜索"))
                .addItem(this.newItem(R.drawable.tab_user,R.drawable.tab_user_click, "帮助")) ;


        NavigationController navigationController = builder.build() ;
        navigationController.showBottomLayout();
    }

    private BaseTabItem newItem(int defaultDrawable, int checkedDrawable, String title)
    {
        LocalIconItemView localOnlyIconItemView = new LocalIconItemView(this);
        localOnlyIconItemView.initialize(defaultDrawable, checkedDrawable, title) ;
        return localOnlyIconItemView;
    }

}