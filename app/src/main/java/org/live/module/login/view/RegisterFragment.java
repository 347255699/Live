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
 * 注册模块
 * Created by KAM on 2017/4/11.
 */

public class RegisterFragment extends BackHandledFragment implements View.OnClickListener {
    private static final String TAG = "Global";
    private String url = "http://10.20.197.154:8080/app/login";
    /**
     * 账户输入框
     */
    private EditText rAccountEditText;
    /**
     * 昵称输入框
     */
    private EditText rNicknameEditText;
    /**
     * 密码输入框
     */
    private EditText rPasswordEditText;
    /**
     * 确认密码输入框
     */
    private EditText rRePasswordEditText;
    /**
     * 注册按钮
     */
    private Button rRegisterButton;
    /**
     * 服务条框
     */
    private TextView rServiceClauseTextView;
    private View view;
    private OnFragmentReplaceListener fragmentReplaceListener;
    private HttpService.HttpServiceBinder httpServiceBinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_register, container, false);
        if (getActivity() instanceof OnFragmentReplaceListener) {
            fragmentReplaceListener = (OnFragmentReplaceListener) getActivity();
        }
        initUIElement();
        Intent bindIntent = new Intent(getActivity(), HttpService.class);
        getActivity().bindService(bindIntent, connection, BIND_AUTO_CREATE); // 绑定http服务
        fragmentReplaceListener.setTitle("注册"); // 设置标题
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
        rAccountEditText = (EditText) view.findViewById(R.id.et_register_account);
        rNicknameEditText = (EditText) view.findViewById(R.id.et_register_nickname);
        rPasswordEditText = (EditText) view.findViewById(R.id.et_register_password);
        rRePasswordEditText = (EditText) view.findViewById(R.id.et_register_repassword);
        rRegisterButton = (Button) view.findViewById(R.id.btn_register_submit);
        rServiceClauseTextView = (TextView) view.findViewById(R.id.tv_service_clause);
        rServiceClauseTextView.setOnClickListener(this);
        rRegisterButton.setOnClickListener(this);
    }

    @Override
    public boolean onBackPressed() {
        fragmentReplaceListener.setTitle("登陆"); // 设置标题
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_register_submit:
                if (validateForm()) {
                    Map<String, Object> params = new HashMap<String, Object>();
                    params.put("account", rAccountEditText.getText().toString());
                    params.put("password", rPasswordEditText.getText().toString());
                    params.put("nickname", rNicknameEditText.getText().toString());
                    JSONObject jsonObject = httpServiceBinder.request(url, RequestMethod.POST, params);

                    try {
                        if (jsonObject.getInt("status") == 1) {
                            // 注册成功
                        } else {
                            // 注册失败
                            Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show(); // 显示错误提示
                        }
                    } catch (JSONException e) {
                        Log.e(TAG, e.getMessage());
                    }
                }
                break; // 重置密码
            case R.id.tv_service_clause:
                fragmentReplaceListener.replaceToServiceClauseFragment();
                break; //服务条框
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
        String[] columns = {"account", "nickname", "password", "rePassword"};
        String[] titles = {"账户", "昵称", "密码", "确认密码"};
        EditText[] editTexts = {rAccountEditText, rNicknameEditText, rPasswordEditText, rRePasswordEditText};
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
                case "nickname":
                    rule.put("required", true);
                    rule.put("maxLength", 10);
                    break;
                case "password":
                    rule.put("required", true);
                    rule.put("maxLength", 15);
                    break;
                case "rePassword":
                    rule.put("required", true);
                    rule.put("maxLength", 15);
                    rule.put("confirm", rPasswordEditText.getText().toString());
                    break;
            }
            rules.put(key, rule);
        } // 构建校验规则组


        Validator validator = new Validator(getActivity());
        return validator.validate(vals, labels, rules); // 开启校验
    }
}
