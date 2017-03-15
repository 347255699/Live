package org.live.module.home.view;

import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.Window;

import net.steamcrafted.materialiconlib.MaterialDrawableBuilder;

import org.live.R;
import org.live.module.home.view.custom.LocalIconItemView;

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
        initTabLayout() ;
    }

    /**
     *  初始化底部导航栏
     */
    public void initTabLayout() {
        PageBottomTabLayout bottomTabLayout = (PageBottomTabLayout) this.findViewById(R.id.tab_home_nav) ;
        PageBottomTabLayout.CustomBuilder builder = bottomTabLayout.custom()
                .addItem(this.newTabItem(MaterialDrawableBuilder.IconValue.HOME_OUTLINE , MaterialDrawableBuilder.IconValue.HOME, "首页"))
                .addItem(this.newTabItem(MaterialDrawableBuilder.IconValue.FORMAT_LIST_BULLETED, MaterialDrawableBuilder.IconValue.FORMAT_LIST_BULLETED, "分类"))
                .addItem(this.newTabItem(MaterialDrawableBuilder.IconValue.PRESENTATION , MaterialDrawableBuilder.IconValue.PRESENTATION_PLAY, "直播"))
                .addItem(this.newTabItem(MaterialDrawableBuilder.IconValue.ACCOUNT_OUTLINE, MaterialDrawableBuilder.IconValue.ACCOUNT, "我的")) ;

        NavigationController navigationController = builder.build() ;
        navigationController.showBottomLayout();
    }
    /**
     *  新建一个tab_item
     *
     * @param defaultIcon 默认图标
     * @param checkedIcon 选中时的图标
     * @param title 标题
     * @return
     */
    private BaseTabItem newTabItem(MaterialDrawableBuilder.IconValue defaultIcon, MaterialDrawableBuilder.IconValue checkedIcon, String title)
    {
        LocalIconItemView localOnlyIconItemView = new LocalIconItemView(this);
        localOnlyIconItemView.initialize(defaultIcon, checkedIcon, title) ;
        return localOnlyIconItemView;
    }

}