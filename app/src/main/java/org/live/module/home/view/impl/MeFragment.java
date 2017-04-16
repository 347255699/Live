package org.live.module.home.view.impl;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
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
import com.flyco.animation.BaseAnimatorSet;
import com.flyco.animation.FadeExit.FadeExit;
import com.flyco.animation.FlipEnter.FlipVerticalSwingEnter;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.MaterialDialog;
import com.flyco.dialog.widget.NormalDialog;

import org.live.R;
import org.live.common.constants.LiveConstants;
import org.live.module.home.listener.OnHomeActivityEventListener;
import org.live.module.home.view.MeView;
import org.live.module.login.domain.MobileUserVo;
import org.live.module.login.view.impl.LoginActivity;
import org.live.module.play.view.impl.PlayActivity;

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
        this.mobileUserVo = homeActivityEventListener.getUserData(); // 取得用户数据
        initUIElement();
        return currentFragmentView;

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
                Log.i(TAG, "" + position);
            }
        }); // 绑定列表项点击事件

        mLogoutLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseAnimatorSet bas_in = new FlipVerticalSwingEnter();  //动画，弹出提示框的动画
                BaseAnimatorSet bas_out = new FadeExit();                //关闭提示框的动画
                final MaterialDialog dialog = new MaterialDialog(getActivity());
                dialog.content("确定要注销登录吗？ ");
                dialog.btnNum(2).btnText("取消", "确认").btnTextColor(NormalDialog.STYLE_TWO);
                dialog.showAnim(bas_in)
                        .dismissAnim(bas_out)
                        .show();
                dialog.setOnBtnClickL(new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {  //取消
                        dialog.dismiss();
                    }
                }, new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {  //确定
                        dialog.dismiss();
                        homeActivityEventListener.logout(); // 注销登陆
                    }
                });

            }
        }); // 绑定退出登录选项

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


}
