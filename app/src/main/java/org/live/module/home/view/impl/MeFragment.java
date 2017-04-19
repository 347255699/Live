package org.live.module.home.view.impl;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import org.live.R;
import org.live.common.constants.LiveConstants;
import org.live.module.home.listener.OnHomeActivityEventListener;
import org.live.module.login.domain.MobileUserVo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * 进入‘我的’的fragment
 * <p>
 * Created by Mr.wang on 2017/3/14.
 */

public class MeFragment extends Fragment {

    public static final String TAG = "Global";

    private View currentFragmentView = null;   //当前的fragment视图
    /**
     * 用户头像
     */
    private ImageView mHeadImageView;
    /**
     * 用户昵称
     */
    private TextView mNicknameTextView;
    /**
     * 用户账号
     */
    private TextView mAccountTextView;
    /**
     * '我的'功能列表
     */
    private ListView mMeListView;
    /**
     * 退出登录选项
     */
    private LinearLayout mLogoutLinearLayout;
    private String httpUrl = LiveConstants.REMOTE_SERVER_HTTP_IP; // 服务器http基础链接
    private OnHomeActivityEventListener homeActivityEventListener;
    private MobileUserVo mobileUserVo; // 用户数据

    private String[] mItemLabel = {"编辑个人信息", "我的收藏", "关于高校直播"};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "执行了onCreateView");
        if (currentFragmentView != null) return currentFragmentView;
        currentFragmentView = inflater.inflate(R.layout.fragment_me, null);
        if (getActivity() instanceof OnHomeActivityEventListener) {
            this.homeActivityEventListener = (OnHomeActivityEventListener) getActivity();
        }

        return currentFragmentView;

    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        this.mobileUserVo = homeActivityEventListener.getUserData(); // 取得用户数据
        initUIElement();
    }

    /**
     * 初始化UI控件
     */
    public void initUIElement() {
        mHeadImageView = (ImageView) currentFragmentView.findViewById(R.id.iv_me_head_image);
        mAccountTextView = (TextView) currentFragmentView.findViewById(R.id.tv_me_account);
        mNicknameTextView = (TextView) currentFragmentView.findViewById(R.id.tv_me_nickname);
        mMeListView = (ListView) currentFragmentView.findViewById(R.id.lv_me);
        mLogoutLinearLayout = (LinearLayout) currentFragmentView.findViewById(R.id.ll_me_logout);
        if (mobileUserVo != null) {
            mAccountTextView.setText("ID: " + mobileUserVo.getAccount());
            mNicknameTextView.setText(mobileUserVo.getNickname());
        } // 填充封面用户数据

        Glide.with(this).load(httpUrl + mobileUserVo.getHeadImgUrl())
                .bitmapTransform(new CropCircleTransformation(getActivity()))
                .into(mHeadImageView); // 设置头像

        SimpleAdapter adapter = new SimpleAdapter(getActivity(),
                getItemLabel(),//数据源
                R.layout.item_me,//显示布局
                new String[]{"label"}, //数据源的属性字段
                new int[]{R.id.tv_me_label}); //布局里的控件id
        mMeListView.setAdapter(adapter); // 添加适配器

        mMeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        Intent intent = new Intent(getActivity(), UserInfoActivity.class);
                        startActivity(intent); // 跳转至用户信息编辑窗口
                        break;
                }
            }
        }); // 绑定列表项点击事件

        mLogoutLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogPlus dialog = DialogPlus.newDialog(getActivity()).setContentBackgroundResource(R.color.colorWhite2)
                        .setContentHolder(new ViewHolder(R.layout.dialog_me_logout))
                        .setGravity(Gravity.CENTER)
                        .create();
                dialog.show();
                View dialogView = dialog.getHolderView();
                dialogView.findViewById(R.id.btn_me_logout).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        homeActivityEventListener.logout(); // 注销登陆
                    }
                });
                dialogView.findViewById(R.id.btn_me_exit).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        homeActivityEventListener.exit(); // 退出应用
                    }
                });

            }
        }); // 绑定退出登录选项

        mHeadImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHeadImgChooseTypeDialog();
            }
        }); // 绑定更换头像操作
    }

    /**
     * 显示头像获取方式对话框
     */
    private void showHeadImgChooseTypeDialog() {
        final DialogPlus dialog = DialogPlus.newDialog(getActivity()).setContentBackgroundResource(R.color.colorWhite2)
                .setContentHolder(new ViewHolder(R.layout.dialog_me_choose_type))
                .setGravity(Gravity.CENTER)
                .create();
        dialog.show();
        final View dialogView = dialog.getHolderView();
        dialogView.findViewById(R.id.btn_me_gallery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeActivityEventListener.chooseHeadImgFromGallery();
                dialog.dismiss();
            }
        });// 从相册中选取
        dialogView.findViewById(R.id.btn_me_camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeActivityEventListener.chooseHeadImgFromCamera();
                dialog.dismiss();
            }
        });// 拍摄照片
    }

    /**
     * 获取ListView列表项的标签
     *
     * @return
     */
    private List<HashMap<String, String>> getItemLabel() {

        List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < mItemLabel.length; i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("label", mItemLabel[i]);
            list.add(map);
        }
        return list;
    }

    /**
     * 取得头像显示视图
     *
     * @return
     */
    public ImageView getMHeadImageView() {
        return this.mHeadImageView;
    }

    /**
     * 重新刷新数据
     */
    public void reLoadData() {
        if(mAccountTextView != null && mNicknameTextView != null){
            mAccountTextView.setText("ID: " + homeActivityEventListener.getUserData().getAccount());
            mNicknameTextView.setText(homeActivityEventListener.getUserData().getNickname());
        }
    }
}
