package org.live.module.home.view.impl;

import android.app.Instrumentation;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import net.steamcrafted.materialiconlib.MaterialDrawableBuilder;

import org.live.R;
import org.live.common.listener.BackHandledFragment;
import org.live.module.home.listener.OnUserInfoActivityListener;

/**
 * 用户信息修改模块
 * Created by KAM on 2017/4/18.
 */

public class UserInfoEditFragment extends BackHandledFragment {
    private static final String TAG = "Global";
    private View view;
    private OnUserInfoActivityListener userInfoActivityListener;
    /**
     * 保存按钮
     */
    private Button uUserInfoEditButton;
    /**
     * 修改信息输入框
     */
    private EditText uUserInfoItemEditView;
    private String itemKey; // 待修改项的key值
    private String itemVal; // 待修改项的val值

    /**
     * 设置待修改项信息
     *
     * @param itemKey
     * @param itemVal
     */
    public void setKeyAndVal(String itemKey, String itemVal) {
        this.itemKey = itemKey;
        this.itemVal = itemVal;
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Log.i(TAG, itemKey + ":" + itemVal);
        uUserInfoItemEditView.setText(itemVal);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user_info_edit, container, false);
        if (getActivity() instanceof OnUserInfoActivityListener) {
            userInfoActivityListener = (OnUserInfoActivityListener) getActivity();
        }
        initUIElement();
        initActionBar();
        return view;
    }

    /**
     * 初始化UI控件
     */
    private void initUIElement() {
        uUserInfoEditButton = (Button) view.findViewById(R.id.btn_user_info_edit);
        uUserInfoItemEditView = (EditText) view.findViewById(R.id.et_user_info_item);

        uUserInfoEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String label;
                switch (itemKey) {
                    case "nickname":
                        label = "昵称";
                        break;
                    case "email":
                        label = "邮箱";
                        break;
                    case "mobileNumber":
                        label = "手机号码";
                        break;
                    default:
                        label = null;
                        break;
                }
                boolean isPassed = userInfoActivityListener.getUserInfoPresenter().validateInputItem(itemKey, uUserInfoItemEditView.getText().toString(), label); // 校验信息

                if (isPassed && !itemVal.equals(uUserInfoItemEditView.getText().toString())) {
                    userInfoActivityListener.getUserInfoPresenter().editUserInfo(itemKey, uUserInfoItemEditView.getText().toString()); // 修改信息
                }
                Log.i(TAG, "保存按钮被点击了：" + itemKey + ":" + uUserInfoItemEditView.getText().toString());
            }
        });
    }

    /**
     * 初始化标题栏
     */
    private void initActionBar() {
        userInfoActivityListener.initActionBar(view, R.id.tb_user_info_edit);
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }

}
