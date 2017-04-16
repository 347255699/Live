package org.live.module.home.view.impl;

import android.content.Intent;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.Toast;

import net.steamcrafted.materialiconlib.MaterialDrawableBuilder;

import org.live.R;
import org.live.module.home.listener.OnHomeActivityEventListener;
import org.live.module.home.presenter.MePresenter;
import org.live.module.home.presenter.impl.MePresenterImpl;
import org.live.module.home.view.MeView;
import org.live.module.home.view.custom.LocalIconItemView;
import org.live.module.login.domain.MobileUserVo;
import org.live.module.login.view.impl.LoginActivity;

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
public class HomeActivity extends FragmentActivity implements OnHomeActivityEventListener, MeView {

    public static final String TAG = "HOME";

    private List<Fragment> fragmentList = null;    // fragment的集合，用于保存fragment

    private ViewPager viewPager = null;

    private FragmentPagerAdapter fragmentPagerAdapter = null;   //fragment的适配器

    private MobileUserVo mobileUserVo; // 用户数据引用

    private MePresenter mePresenter; // '我的'模块表示器引用

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);     //隐藏标题
        setContentView(R.layout.activity_home);
        initTabLayout();
        this.mePresenter = new MePresenterImpl(this, this); // 取得'我的模块'表示器
        this.mobileUserVo = mePresenter.getUserData(); // 取得用户数据
    }

    /**
     * 初始化底部导航栏
     */
    public void initTabLayout() {
        PageBottomTabLayout bottomTabLayout = (PageBottomTabLayout) this.findViewById(R.id.tab_home_nav);
        PageBottomTabLayout.CustomBuilder builder = bottomTabLayout.custom()
                .addItem(this.newTabItem(MaterialDrawableBuilder.IconValue.HOME_OUTLINE, MaterialDrawableBuilder.IconValue.HOME, "首页"))
                .addItem(this.newTabItem(MaterialDrawableBuilder.IconValue.FORMAT_LIST_BULLETED, MaterialDrawableBuilder.IconValue.FORMAT_LIST_BULLETED_TYPE, "分类"))
                .addItem(this.newTabItem(MaterialDrawableBuilder.IconValue.PRESENTATION, MaterialDrawableBuilder.IconValue.PRESENTATION_PLAY, "直播"))
                .addItem(this.newTabItem(MaterialDrawableBuilder.IconValue.ACCOUNT_OUTLINE, MaterialDrawableBuilder.IconValue.ACCOUNT, "我的"));

        viewPager = (ViewPager) this.findViewById(R.id.vp_home_mainDump);  //获取viewPager
        fragmentPagerAdapter = initFragmentPageAdapter();  //获取适配器
        viewPager.setAdapter(fragmentPagerAdapter);   //设置适配器
        final NavigationController navigationController = builder.build();   //导航栏控制器

        Log.d(TAG, "viewPage是否等于null-->" + (viewPager == null));
        navigationController.setupWithViewPager(viewPager);
        navigationController.addTabItemSelectedListener(new OnTabItemSelectedListener() {
            @Override
            public void onSelected(int index, int old) {
                Log.d(TAG, "checked：" + index + ", old: " + old);
            }

            @Override
            public void onRepeat(int index) {
                Log.d(TAG, "orepeatClick: " + index);
            }
        });

    }

    /**
     * 新建一个tab_item
     *
     * @param defaultIcon 默认图标
     * @param checkedIcon 选中时的图标
     * @param title       标题
     * @return
     */
    private BaseTabItem newTabItem(MaterialDrawableBuilder.IconValue defaultIcon, MaterialDrawableBuilder.IconValue checkedIcon, String title) {
        LocalIconItemView localOnlyIconItemView = new LocalIconItemView(this);
        localOnlyIconItemView.initialize(defaultIcon, checkedIcon, title);
        return localOnlyIconItemView;
    }

    /**
     * 初始化fragmentPageAdapter，并交给底部导航栏的控制器
     */
    public FragmentPagerAdapter initFragmentPageAdapter() {

        fragmentList = new ArrayList<>(4);
        HomeFragment homeFragment = new HomeFragment();
        CategoryFragment categoryFragment = new CategoryFragment();
        LiveFragment liveFragment = new LiveFragment();
        MeFragment meFragment = new MeFragment();
        fragmentList.add(homeFragment);
        fragmentList.add(categoryFragment);
        fragmentList.add(liveFragment);
        fragmentList.add(meFragment);
        //创建FragmentPagerAdapter
        return new CustomFragmentPagerAdapter(getSupportFragmentManager(), fragmentList);
    }

    private long lastExitTime = 0; //上次点击返回键的按钮

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (System.currentTimeMillis() - lastExitTime > 2000) {
                Toast.makeText(this, "再按一次退出应用", Toast.LENGTH_SHORT).show();
                lastExitTime = System.currentTimeMillis();
            } else {
             /*   finish();
                System.exit(0);*/
                Intent intent = new Intent(this, HomeActivity.class);
                intent.putExtra(HomeActivity.TAG_EXIT, true);
                startActivity(intent);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private static final String TAG_EXIT = "exit";

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
            boolean isExit = intent.getBooleanExtra(TAG_EXIT, false);
            if (isExit) {
                this.finish();
            }
        }
    }

    /**
     * 注销登陆
     */
    @Override
    public void logout() {
        String roomId = mobileUserVo.isAnchorFlag() ? mobileUserVo.getLiveRoomVo().getRoomId() : null;
        mePresenter.logout(mobileUserVo.getAccount(), roomId);
    }

    /**
     * 取得用户数据
     *
     * @return
     */
    @Override
    public MobileUserVo getUserData() {
        return this.mobileUserVo;
    }


    /**
     * 自定义FragmentPagerAdapter
     */
    public class CustomFragmentPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragmentList;

        public CustomFragmentPagerAdapter(FragmentManager fragmentManager, List<Fragment> fragmentList) {
            super(fragmentManager);
            this.fragmentList = fragmentList;
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
    }

    /**
     * 前往登陆页
     */
    @Override
    public void toLogin() {
        startActivity(new Intent(this, LoginActivity.class));
    }

    /**
     * 摧毁自己
     */
    @Override
    public void finishSelf() {
        finish();
    }

}