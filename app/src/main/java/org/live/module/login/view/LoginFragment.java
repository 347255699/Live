package org.live.module.login.view;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.live.R;
import org.live.common.constants.RequestMethod;
import org.live.common.listener.BackHandledFragment;
import org.live.common.util.JsonUtils;
import org.live.module.home.view.impl.HomeActivity;
import org.live.module.login.OnFragmentReplaceListener;
import org.live.module.login.domain.MobileUserVo;
import org.live.module.login.service.HttpService;
import org.live.module.login.util.Validator;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static android.content.Context.BIND_AUTO_CREATE;

/**
 * 登录模块
 * Created by KAM on 2017/4/11.
 */

public class LoginFragment extends BackHandledFragment implements View.OnClickListener {

    private static final String TAG = "Global";
    private View view;
    private OnFragmentReplaceListener fragmentReplaceListener;
    private HttpService.HttpServiceBinder httpServiceBinder;
    private String url = "http://10.20.197.154:8080/app/login";
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login, container, false);
        if (getActivity() instanceof OnFragmentReplaceListener) {
            fragmentReplaceListener = (OnFragmentReplaceListener) getActivity();
        }
        fragmentReplaceListener.setTitle("登录"); // 设置标题
        initUIElement();
        Intent bindIntent = new Intent(getActivity(), HttpService.class);
        getActivity().bindService(bindIntent, connection, BIND_AUTO_CREATE); // 绑定http服务
        return view;
    }

    @Override
    public void onStop() {
        getActivity().unbindService(connection); // 解绑http服务
        super.onStop();
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
                fragmentReplaceListener.replaceToRegisterFragment();
                break; // 替换为注册模块

            case R.id.tv_login_forget_password:
                fragmentReplaceListener.replaceToForgetPasswordFragment();
                break; // 替换为忘记密码模块
            case R.id.btn_login_submit:
                if (validateForm()) {
                    Map<String, Object> params = new HashMap<String, Object>();
                    params.put("account", lAccountEditText.getText().toString());
                    params.put("password", lPasswordEditText.getText().toString());
                    JSONObject jsonObject = httpServiceBinder.request(url, RequestMethod.POST, params);

                    try {
                        if (jsonObject.getInt("status") == 1) {
                            MobileUserVo mobileUserVo = JsonUtils.fromJson(jsonObject.getJSONObject("data").toString(), MobileUserVo.class); // 提取数据
                            // 服务器校验通过，跳转至主页
                            Intent intent = new Intent(getActivity(), HomeActivity.class);
                            // 携带参数至主页,未完成
                            startActivity(intent);
                        } else {
                            // 校验失败
                            Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show(); // 显示错误提示
                        }
                    } catch (JSONException e) {
                        Log.e(TAG, e.getMessage());
                    }
                }
                break;
        }
    }

    /**
     * service链接
     */
    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            httpServiceBinder = (HttpService.HttpServiceBinder) service;
        }
    };

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


        Validator validator = new Validator(getActivity());
        return validator.validate(vals, labels, rules); // 开启校验
    }


}
