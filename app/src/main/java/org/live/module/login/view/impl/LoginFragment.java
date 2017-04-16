package org.live.module.login.view.impl;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.live.R;
import org.live.common.listener.BackHandledFragment;
import org.live.module.login.listener.OnLoginActivityEventListener;
import org.live.module.login.presenter.LoginPresenter;
import org.live.module.login.util.constant.LoginConstant;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * 登录模块
 * Created by KAM on 2017/4/11.
 */

public class LoginFragment extends BackHandledFragment implements View.OnClickListener {

    private static final String TAG = "Global";
    private View view;
    private OnLoginActivityEventListener loginActivityEventListener;
    /**
     * 注册按钮
     */
    private Button lRegisterButton;
    /**
     * 忘记密码按钮
     */
    private TextView lForgetPasswordTextView;
    /**
     * 账户输入框
     */
    private EditText lAccountEditText;
    /**
     * 密码输入框
     */
    private EditText lPasswordEditText;

    /**
     * 表单提交按钮
     */
    private Button lSubmitButton;
    /**
     * 登陆模块表示器引用
     */
    private LoginPresenter loginPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login, container, false);
        if (getActivity() instanceof OnLoginActivityEventListener) {
            this.loginActivityEventListener = (OnLoginActivityEventListener) getActivity();
        }
        loginActivityEventListener.setTitle("登录"); // 设置标题
        this.loginPresenter = loginActivityEventListener.getLoginPresenter(); // 取得登陆模块表示器
        initUIElement();
        return view;
    }

    /**
     * 初始化UI控件
     */
    private void initUIElement() {
        lRegisterButton = (Button) view.findViewById(R.id.btn_login_register);
        lForgetPasswordTextView = (TextView) view.findViewById(R.id.tv_login_forget_password);
        lSubmitButton = (Button) view.findViewById(R.id.btn_login_submit);
        lForgetPasswordTextView.setOnClickListener(this);
        lRegisterButton.setOnClickListener(this);
        lSubmitButton.setOnClickListener(this);
        lAccountEditText = (EditText) view.findViewById(R.id.et_login_account);
        lPasswordEditText = (EditText) view.findViewById(R.id.et_login_password);
    }

    private long lastExitTime = 0; //上次点击返回键的按钮

    @Override
    public boolean onBackPressed() {
        if (System.currentTimeMillis() - lastExitTime > 2000) {
            Toast.makeText(getActivity(), "再按一次退出应用", Toast.LENGTH_SHORT).show();
            lastExitTime = System.currentTimeMillis();
        } else {
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login_register:
                loginActivityEventListener.replaceToRegisterFragment();
                break; // 替换为注册模块

            case R.id.tv_login_forget_password:
                loginActivityEventListener.replaceToForgetPasswordFragment();
                break; // 替换为忘记密码模块
            case R.id.btn_login_submit:
                if (validateForm()) {
                    Map<String, Object> params = new HashMap<String, Object>();
                    params.put("account", lAccountEditText.getText().toString());
                    params.put("password", lPasswordEditText.getText().toString());
                    try {
                        loginActivityEventListener.showProgressBar();
                        loginPresenter.httpRequest(LoginConstant.MODEL_TYPE_LOGIN, params); // 登陆请求
                        loginActivityEventListener.hideProgressBar();
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage());
                        loginActivityEventListener.hideProgressBar();
                    }
                }
                break;
        }
    }

    /**
     * 检验表单
     */
    public boolean validateForm() {

        Map<String, String> vals = new LinkedHashMap<String, String>(); // 待校验的字符组
        Map<String, String> labels = new HashMap<String, String>(); // 字符组对应的标签组
        Map<String, Map<String, Object>> rules = new HashMap<>(); // 字符组对应的规则组
        String[] columns = {"account", "password"};
        String[] titles = {"账户", "密码"};
        EditText[] editTexts = {lAccountEditText, lPasswordEditText};
        for (int i = 0; i < columns.length; i++) {
            String key = columns[i];
            String val = editTexts[i].getText().toString();
            String label = titles[i];
            vals.put(key, val);
            labels.put(key, label);
        } // 构建校验数据源

        for (String key : columns) {
            Map<String, Object> rule = new HashMap<String, Object>();
            switch (key) {
                case "account":
                    rule.put("required", true);
                    rule.put("number", true);
                    rule.put("maxLength", 15);
                    break;
                case "password":
                    rule.put("required", true);
                    rule.put("maxLength", 15);
                    break;
            }
            rules.put(key, rule);
        } // 构建校验规则组


        return loginPresenter.validateForm(vals, labels, rules); // 开启校验
    }


}
