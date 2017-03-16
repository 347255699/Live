package org.live.module.home.view;

import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.Toast;

import net.steamcrafted.materialiconlib.MaterialDrawableBuilder;

import org.live.R;
import org.live.module.home.view.custom.LocalIconItemView;

import java.util.ArrayList;
import java.util.List;

import me.majiajie.pagerbottomtabstrip.NavigationController;
import me.majiajie.pagerbottomtabstrip.PageBottomTabLayout;
import me.majiajie.pagerbottomtabstrip.item.BaseTabItem;
import me.majiajie.pagerbottomtabstrip.listener.OnTabItemSelectedListener;


/**
 * 主界面
 * Created by Mr.wang on 2017/3/14.
 */
public class HomeActivity extends FragmentActivity {

    public static final String TAG = "HomeActivity" ;

    private List<Fragment> fragmentList = null ;    // fragment的集合，用于保存fragment

    private ViewPager viewPager = null ;    //

    private FragmentPagerAdapter fragmentPagerAdapter = null ;   //fragment的适配器

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
                .addItem(this.newTabItem(MaterialDrawableBuilder.IconValue.FORMAT_LIST_BULLETED, MaterialDrawableBuilder.IconValue.FORMAT_LIST_BULLETED_TYPE, "分类"))
                .addItem(this.newTabItem(MaterialDrawableBuilder.IconValue.PRESENTATION , MaterialDrawableBuilder.IconValue.PRESENTATION_PLAY, "直播"))
                .addItem(this.newTabItem(MaterialDrawableBuilder.IconValue.ACCOUNT_OUTLINE, MaterialDrawableBuilder.IconValue.ACCOUNT, "我的")) ;

        viewPager = (ViewPager) this.findViewById(R.id.vp_home_mainDump) ;  //获取viewPager
        fragmentPagerAdapter = initFragmentPageAdapter() ;  //获取适配器
        viewPager.setAdapter(fragmentPagerAdapter) ;   //设置适配器
        final NavigationController navigationController = builder.build() ;   //导航栏控制器

        Log.d(TAG, "viewPage是否等于null-->"+(viewPager == null)) ;
        navigationController.setupWithViewPager(viewPager) ;
        navigationController.addTabItemSelectedListener(new OnTabItemSelectedListener() {
            @Override
            public void onSelected(int index, int old) {
                Log.d(TAG, "checked："+index+", old: "+ old) ;
            }

            @Override
            public void onRepeat(int index) {
                Log.d(TAG, "orepeatClick: "+index) ;
            }
        });

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
        return new CustomFragmentPagerAdapter(getSupportFragmentManager(), fragmentList) ;
    }

    private long lastExitTime = 0 ; //上次点击返回键的按钮
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if(System.currentTimeMillis() - lastExitTime > 2000) {
                Toast.makeText(this, "再按一次退出应用",Toast.LENGTH_SHORT).show();
                lastExitTime = System.currentTimeMillis() ;
            } else {
                finish();
                System.exit(0);
            }
            return true ;
        }
        return super.onKeyDown(keyCode, event);
    }



    /**
     * 自定义FragmentPagerAdapter
     */
    public class CustomFragmentPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragmentList ;

        public CustomFragmentPagerAdapter(FragmentManager fragmentManager, List<Fragment> fragmentList) {
            super(fragmentManager) ;
            this.fragmentList = fragmentList ;
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size() ;
        }
    }

}