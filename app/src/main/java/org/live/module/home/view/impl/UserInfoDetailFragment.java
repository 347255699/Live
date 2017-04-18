package org.live.module.home.view.impl;

import android.app.Fragment;
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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import net.steamcrafted.materialiconlib.MaterialDrawableBuilder;

import org.live.R;
import org.live.common.listener.BackHandledFragment;
import org.live.module.home.listener.OnUserInfoActivityListener;
import org.live.module.login.domain.MobileUserVo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户信息详情模块
 * Created by KAM on 2017/4/18.
 */

public class UserInfoDetailFragment extends BackHandledFragment {

    private ListView uUserInfoListView;
    private ListView uUserInfoListView2;
    private View view;
    String[] keys = new String[]{"nickname", "email", "mobileNumber"};
    Object[] vals = null;
    private OnUserInfoActivityListener userInfoActivityListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user_info_detail, container, false);
        if (getActivity() instanceof OnUserInfoActivityListener) {
            userInfoActivityListener = (OnUserInfoActivityListener) getActivity();
        }
        initActionBar();
        return view;
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        initUIElement();
    }

    /**
     * 初始化UI控件
     */
    private void initUIElement() {
        uUserInfoListView = (ListView) view.findViewById(R.id.lv_user_info);
        uUserInfoListView2 = (ListView) view.findViewById(R.id.lv_user_info2);

        SimpleAdapter adapter = new SimpleAdapter(getActivity(),
                getUserInfo(),//数据源
                R.layout.item_user_info,//显示布局
                new String[]{"label", "val"}, //数据源的属性字段
                new int[]{R.id.tv_user_info_label, R.id.tv_user_info_val}); //布局里的控件id
        SimpleAdapter adapter2 = new SimpleAdapter(getActivity(),
                getUserInfo2(),
                R.layout.item_user_info,
                new String[]{"label", "val"}, //数据源的属性字段
                new int[]{R.id.tv_user_info_label, R.id.tv_user_info_val}); //布局里的控件id
        uUserInfoListView.setAdapter(adapter);
        uUserInfoListView2.setAdapter(adapter2);

        uUserInfoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                userInfoActivityListener.replaceUserInfoEditFragment(keys[position], (String) vals[position]); // 前往用户信息修改模块

            }
        });
    }

    /**
     * 初始化标题栏
     */
    private void initActionBar() {
        userInfoActivityListener.initActionBar(view, R.id.tb_user_info_detail);
    }

    /**
     * 获取用户信息
     *
     * @return
     */
    private List<Map<String, Object>> getUserInfo() {
        MobileUserVo mobileUserVo = HomeActivity.mobileUserVo;
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        vals = new Object[]{mobileUserVo.getNickname(), mobileUserVo.getEmail(), mobileUserVo.getMobileNumber()};
        String[] labels = new String[]{"昵称", "邮箱", "手机号码"};
        for (int i = 0; i < vals.length; i++) {
            Map<String, Object> dataItem = new HashMap<String, Object>();
            Object val = vals[i];
            String label = labels[i];
            dataItem.put("label", label);
            dataItem.put("val", val);
            data.add(i, dataItem);
        }
        return data;
    }

    /**
     * 获取用户信息
     *
     * @return
     */
    private List<Map<String, Object>> getUserInfo2() {
        MobileUserVo mobileUserVo = HomeActivity.mobileUserVo;
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String birthday = format.format(mobileUserVo.getBirthday());
        Object[] vals = new Object[]{mobileUserVo.getAccount(), mobileUserVo.getRealName(), mobileUserVo.getSex().equals("1") ? "男" : "女", birthday};
        String[] labels = new String[]{"账户", "姓名", "性别", "生日"};
        for (int i = 0; i < vals.length; i++) {
            Map<String, Object> dataItem = new HashMap<String, Object>();
            Object val = vals[i];
            String label = labels[i];
            dataItem.put("label", label);
            dataItem.put("val", val);
            data.add(i, dataItem);
        }
        return data;
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }


}
