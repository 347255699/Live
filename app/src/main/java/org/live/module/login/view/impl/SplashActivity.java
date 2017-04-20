package org.live.module.login.view.impl;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.igexin.sdk.PushManager;

import org.live.R;
import org.live.module.home.view.impl.HomeActivity;
import org.live.module.login.presenter.impl.LoginPresenterImpl;
import org.live.module.login.service.InitPushService;
import org.live.module.login.service.ReceiveIntentService;
import org.live.module.login.view.LoginView;

/**
 * 过渡画面
 */
public class SplashActivity extends AppCompatActivity implements LoginView {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //启动个推
        PushManager.getInstance().initialize(this.getApplicationContext(), InitPushService.class) ;
        PushManager.getInstance().registerPushIntentService(this.getApplicationContext(), ReceiveIntentService.class) ;

        new Handler().postDelayed(new NextActivity(this, this), 1500); // 延迟两秒跳转至下个页面

    }

    /**
     * 下个活动窗口
     */
    public class NextActivity implements Runnable {
        private Context context;
        private LoginView loginView;

        public NextActivity(Context context, LoginView loginView) {
            this.context = context;
            this.loginView = loginView;
        }

        @Override
        public void run() {
            new LoginPresenterImpl(context, loginView).preLogin(); // 预登录
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        finish();

    }

    @Override
    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show(); // 显示消息
    }

    @Override
    public void toHome() {
        startActivity(new Intent(this, HomeActivity.class)); // 前往首页
    }

    @Override
    public void popBackStack() {

    }

    @Override
    public void toLogin() {
        startActivity(new Intent(this, LoginActivity.class)); // 前往登录页面
    }

    @Override
    public void finishSelf() {
        finish();
    }
}
