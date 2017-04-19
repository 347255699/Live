package org.live.module.login.view.impl;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.Instrumentation;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import net.steamcrafted.materialiconlib.MaterialDrawableBuilder;

import org.live.R;
import org.live.common.listener.BackHandledFragment;
import org.live.common.listener.BackHandledInterface;
import org.live.module.home.view.impl.HomeActivity;
import org.live.module.login.listener.OnLoginActivityEventListener;
import org.live.module.login.presenter.LoginPresenter;
import org.live.module.login.presenter.impl.LoginPresenterImpl;
import org.live.module.login.view.LoginView;


/**
 * 登陆模块主活动窗口
 */
public class LoginActivity extends AppCompatActivity implements BackHandledInterface, OnLoginActivityEventListener, LoginView {
    private static final String TAG = "Global";
    private BackHandledFragment mBackHandedFragment;
    private String currentFragmentFlag = "login"; // 当前fragment标记
    private TextView mLoginTitleTextView; // 标题
    /**
     * 登陆模块
     */
    private LoginFragment mLoginFragment = null;
    /**
     * 注册模块
     */
    private RegisterFragment mRegisterFragment = null;
    /**
     * 忘记密码模块
     */
    private ForgetPasswordFragment mForgetPasswordFragment = null;
    /**
     * 服务条款模块
     */
    private ServiceClauseFragment mServiceClauseFragment = null;

    /**
     * 加载框
     */
    private ProgressBar mLoginProgressBar;

    private LoginPresenter loginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.loginPresenter = new LoginPresenterImpl(this, this);
        initUIElement();
        if (savedInstanceState == null) {
            setDefaultFragment();
        }

    }

    /**
     * 设置默认fragment
     */
    private void setDefaultFragment() {
        FragmentManager fm = getFragmentManager();
        // 开启Fragment事务
        FragmentTransaction transaction = fm.beginTransaction();
        if (mLoginFragment == null) {
            mLoginFragment = new LoginFragment();
        }
        transaction.add(R.id.fl_login_content, mLoginFragment, "login"); // 添加登陆模块

        transaction.commit(); // 提交事务
    }

    /**
     * UI控件初始化
     */
    private void initUIElement() {
        mLoginTitleTextView = (TextView) findViewById(R.id.tv_login_title);
        mLoginProgressBar = (ProgressBar) findViewById(R.id.pb_login);
        initActionBar();
    }

    /**
     * 初始化标题栏
     */
    private void initActionBar() {
        Toolbar lToolbar = (Toolbar) findViewById(R.id.tb_login);
        lToolbar.setTitle("");
        setSupportActionBar(lToolbar);
        lToolbar.setNavigationIcon(getIconDrawable(MaterialDrawableBuilder.IconValue.ARROW_LEFT, Color.WHITE));
        lToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new BackThread().start(); // 模拟返回键点击
            }
        });

    }

    /**
     * 获取图标
     *
     * @param
     * @return
     */
    private Drawable getIconDrawable(MaterialDrawableBuilder.IconValue iconValue, int color) {
        Drawable drawable = MaterialDrawableBuilder.with(this) // provide a context
                .setIcon(iconValue) // provide an icon
                .setColor(color) // set the icon color
                .setToActionbarSize() // set the icon size
                .build(); // Finally call build
        return drawable;
    }

    @Override
    public void setSelectedFragment(BackHandledFragment selectedFragment) {
        this.mBackHandedFragment = selectedFragment;
    }

    @Override
    protected void onStop() {
        super.onStop();
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
     * 替换为注册模块
     */
    @Override
    public void replaceToRegisterFragment() {
        if (mRegisterFragment == null) {
            mRegisterFragment = new RegisterFragment();
        }
        FragmentManager fm = getFragmentManager();
        FragmentTransaction tx = fm.beginTransaction();
        tx.replace(R.id.fl_login_content, mRegisterFragment, "register");
        currentFragmentFlag = "register";
        tx.addToBackStack(null); // 添加至回退栈
        tx.commit();
    }

    /**
     * 替换为忘记密码模块
     */
    @Override
    public void replaceToForgetPasswordFragment() {
        if (mForgetPasswordFragment == null) {
            mForgetPasswordFragment = new ForgetPasswordFragment();
        }
        FragmentManager fm = getFragmentManager();
        FragmentTransaction tx = fm.beginTransaction();
        tx.replace(R.id.fl_login_content, mForgetPasswordFragment, "forgetPassword");
        currentFragmentFlag = "forgetPassword";
        tx.addToBackStack(null); // 添加至回退栈
        tx.commit();
    }

    /**
     * 替换为服务条款模块
     */
    @Override
    public void replaceToServiceClauseFragment() {
        if (mServiceClauseFragment == null) {
            mServiceClauseFragment = new ServiceClauseFragment();
        }
        FragmentManager fm = getFragmentManager();
        FragmentTransaction tx = fm.beginTransaction();
        tx.replace(R.id.fl_login_content, mServiceClauseFragment, "serviceClause");
        currentFragmentFlag = "serviceClause";
        tx.addToBackStack(null); // 添加至回退栈
        tx.commit();
    }

    /**
     * 设置标题
     */
    public void setTitle(String title) {
        mLoginTitleTextView.setText(title);
    }

    /**
     * 取得表示器
     *
     * @return
     */
    @Override
    public LoginPresenter getLoginPresenter() {
        return this.loginPresenter;
    }

    /**
     * 隐藏加载框
     */
    @Override
    public void hideProgressBar() {
        mLoginProgressBar.setVisibility(View.GONE);
    }

    /**
     * 显示加载框
     */
    @Override
    public void showProgressBar() {
        mLoginProgressBar.setVisibility(View.VISIBLE);
    }

    /**
     * 提示信息
     *
     * @param msg
     */
    @Override
    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 前往首页
     */
    @Override
    public void toHome() {
        startActivity(new Intent(this, HomeActivity.class));
    }

    /**
     * 返回至上一个fragment
     */
    @Override
    public void popBackStack() {
        getFragmentManager().popBackStack();
    }

    @Override
    public void toLogin() {

    }

    @Override
    public void finishSelf() {
        this.finish();
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
}
