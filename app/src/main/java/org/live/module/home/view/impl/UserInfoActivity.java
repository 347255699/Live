package org.live.module.home.view.impl;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.Instrumentation;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import net.steamcrafted.materialiconlib.MaterialDrawableBuilder;

import org.live.R;
import org.live.common.listener.BackHandledFragment;
import org.live.common.listener.BackHandledInterface;
import org.live.module.home.listener.OnUserInfoActivityListener;
import org.live.module.home.presenter.UserInfoPresenter;
import org.live.module.home.presenter.impl.UserInfoPresenterImpl;
import org.live.module.home.view.UserInfoView;

/**
 * 用户信息模块
 */
public class UserInfoActivity extends AppCompatActivity implements BackHandledInterface, OnUserInfoActivityListener, UserInfoView {


    private BackHandledFragment mBackHandedFragment;
    /**
     * 详情模块
     */
    private UserInfoDetailFragment userInfoDetailFragment;
    /**
     * 用户信息修改模块
     */
    private UserInfoEditFragment userInfoEditFragment;

    private UserInfoPresenter userInfoPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        if (savedInstanceState == null) {
            setDefaultFragment();
        }
        userInfoPresenter = new UserInfoPresenterImpl(this, this);
    }

    /**
     * 设置默认fragment
     */
    private void setDefaultFragment() {
        FragmentManager fm = getFragmentManager();
        // 开启Fragment事务
        FragmentTransaction transaction = fm.beginTransaction();
        if (userInfoDetailFragment == null) {
            userInfoDetailFragment = new UserInfoDetailFragment();
        }
        transaction.add(R.id.fl_user_info, userInfoDetailFragment, "userInfoDetail"); // 添加用户详情模块

        transaction.commit(); // 提交事务
    }

    @Override
    public void setSelectedFragment(BackHandledFragment selectedFragment) {
        this.mBackHandedFragment = selectedFragment;
    }

    @Override
    public void onBackPressed() {
        if (mBackHandedFragment == null || !mBackHandedFragment.onBackPressed()) {
            if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                super.onBackPressed();
            } else {
                getSupportFragmentManager().popBackStack();
            }
        }
    }


    /**
     * 模拟返回键点击
     */
    @Override
    public void back() {
        new BackThread().start();
    }

    @Override
    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 关闭软键盘
     */
    @Override
    public void closeKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0); // 关闭软键盘
        }
    }

    /**
     * 初始化标题栏
     */
    @Override
    public void initActionBar(View view, int toolBarId) {
        Toolbar uToolbar = (Toolbar) view.findViewById(toolBarId);
        uToolbar.setTitle("");
        uToolbar.setNavigationIcon(getIconDrawable(MaterialDrawableBuilder.IconValue.CHEVRON_LEFT, Color.WHITE));
        uToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back(); // 模拟返回键点击
            }
        });
    }

    /**
     * 取得图标
     */
    @Override
    public Drawable getIconDrawable(MaterialDrawableBuilder.IconValue iconValue, int color) {
        Drawable drawable = MaterialDrawableBuilder.with(this) // provide a context
                .setIcon(iconValue)
                .setColor(color)
                .setToActionbarSize()
                .build();
        return drawable;
    }

    /**
     * 替换为用户信息修改模块
     *
     * @param key 待修改信息key
     * @param val 待修改信息val
     */
    @Override
    public void replaceUserInfoEditFragment(String key, String val) {
        FragmentManager fm = getFragmentManager();
        // 开启Fragment事务
        FragmentTransaction transaction = fm.beginTransaction();
        if (userInfoEditFragment == null) {
            userInfoEditFragment = new UserInfoEditFragment();
        }
        userInfoEditFragment.setKeyAndVal(key, val); // 设置待修改项信息
        transaction.replace(R.id.fl_user_info, userInfoEditFragment, "userInfoEdit"); // 添加用户信息修改模块
        transaction.addToBackStack(null); // 添加至回退栈
        transaction.commit(); // 提交事务
    }

    /**
     * 返回键点击线程
     */
    class BackThread extends Thread {
        public void run() {       //这个方法是不能写在你的主线程里面的，所以你要自己开个线程用来执行
            Instrumentation inst = new Instrumentation();

            try {
                inst.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
            } catch (Exception e) {
                Log.e("Global", e.getMessage());
            }

        }
    }

    /**
     * 取得用户模块表示层引用
     *
     * @return
     */
    @Override
    public UserInfoPresenter getUserInfoPresenter() {
        return this.userInfoPresenter;
    }

}
