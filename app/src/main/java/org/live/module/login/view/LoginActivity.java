package org.live.module.login.view;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import net.steamcrafted.materialiconlib.MaterialDrawableBuilder;

import org.live.R;
import org.live.common.listener.BackHandledFragment;
import org.live.common.listener.BackHandledInterface;
import org.live.module.login.OnFragmentReplaceListener;
import org.w3c.dom.Text;


/**
 * 登陆模块主活动窗口
 */
public class LoginActivity extends AppCompatActivity implements BackHandledInterface, OnFragmentReplaceListener {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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
        initActionBar();
    }

    /**
     * 初始化标题栏
     */
    private void initActionBar() {
        Toolbar lToolbar = (Toolbar) findViewById(R.id.tb_login);
        lToolbar.setTitle("");
        setSupportActionBar(lToolbar);
      /*  lToolbar.setNavigationIcon(getIconDrawable(MaterialDrawableBuilder.IconValue.CLOSE, Color.WHITE));
        lToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });*/
    }

    /**
     * 获取图标
     *
     * @param
     * @return
     */
  /*  private Drawable getIconDrawable(MaterialDrawableBuilder.IconValue iconValue, int color) {
        Drawable drawable = MaterialDrawableBuilder.with(this) // provide a context
                .setIcon(iconValue) // provide an icon
                .setColor(color) // set the icon color
                .setToActionbarSize() // set the icon size
                .build(); // Finally call build
        return drawable;
    }*/
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

}
