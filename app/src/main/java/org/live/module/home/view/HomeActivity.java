package org.live.module.home.view;

import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import android.support.v4.app.FragmentPagerAdapter;
import android.view.Window;

import net.steamcrafted.materialiconlib.MaterialDrawableBuilder;

import org.live.R;
import org.live.module.home.view.custom.LocalIconItemView;

import java.util.ArrayList;
import java.util.List;

import me.majiajie.pagerbottomtabstrip.NavigationController;
import me.majiajie.pagerbottomtabstrip.PageBottomTabLayout;
import me.majiajie.pagerbottomtabstrip.item.BaseTabItem;


/**
 * 主界面
 * Created by Mr.wang on 2017/3/14.
 */
public class HomeActivity extends FragmentActivity {


    private List<Fragment> fragmentList = null ;    // fragment的集合，用于保存fragment


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
        //navigationController.setupWithViewPager(initFragmentPageAdapter());
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

    /**
     *  初始化fragmentPageAdapter，并交给底部导航栏的控制器
     */
    public FragmentPagerAdapter initFragmentPageAdapter() {

        fragmentList = new ArrayList<>(4) ;
        HomeFragment homeFragment = new HomeFragment() ;
        CategoryFragment categoryFragment = new CategoryFragment() ;
        LiveFragment liveFragment = new LiveFragment() ;
        MeFragment meFragment = new MeFragment() ;
        fragmentList.add(homeFragment) ;
        fragmentList.add(categoryFragment) ;
        fragmentList.add(liveFragment) ;
        fragmentList.add(meFragment) ;

        //创建FragmentPagerAdapter
        FragmentPagerAdapter fragmentPagerAdapter = new FragmentPagerAdapter(this.getSupportFragmentManager()) {

            @Override
            public int getCount() {
                return fragmentList.size() ;
            }

            @Override
            public Fragment getItem(int position) {
                return fragmentList.get(position);
            }
        } ;
        return fragmentPagerAdapter ;

    }

}