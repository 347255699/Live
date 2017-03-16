package org.live.module.play.view.impl;


import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.flyco.animation.BaseAnimatorSet;
import com.flyco.animation.FadeExit.FadeExit;
import com.flyco.animation.FlipEnter.FlipVerticalSwingEnter;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.MaterialDialog;
import com.flyco.dialog.widget.NormalDialog;
import com.tencent.rtmp.ui.TXCloudVideoView;

import net.steamcrafted.materialiconlib.MaterialIconView;

import org.live.R;
import org.live.common.constants.LiveKeyConstants;
import org.live.module.play.presenter.PlayPresenter;
import org.live.module.play.presenter.impl.PlayPresenterImpl;
import org.live.module.play.view.PlayView;

/**
 * Created by Mr.wang on 2017/3/9.
 */

public class PlayFragment extends Fragment implements PlayView, View.OnClickListener {

    public static final String TAG = "PlayFragment" ;

    private static final int ALPHA_DEFAULT_VALUE = 30; // 默认透明度

    private View currentFragmentView = null ;   //当前的fragment视图

    private TXCloudVideoView mPlayerView = null ;   //播放器的view

    private PlayPresenter playPresenter = null ;    // 播放器的prsenter

    private MaterialIconView closeBtn = null ; //关闭页面的btn

    private MaterialIconView inputBtn = null ; //呼出输入文字的btn

    private ProgressBar loadingBar = null ;

    private ImageView bgImageView = null ;  //背景图片

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentFragmentView= inflater.inflate(R.layout.fragment_play, null) ;
        initUI();
        playPresenter = new PlayPresenterImpl(this, getActivity()) ;
        Intent content = getActivity().getIntent() ;
        String playIp = content.getStringExtra(LiveKeyConstants.Global_URL_KEY) ;  //播放地址
        this.play(playIp);
        return currentFragmentView ;
    }

    /**
     *  初始化UI
     */
    public void initUI() {
        mPlayerView = (TXCloudVideoView) currentFragmentView.findViewById(R.id.vv_play_player) ;   //播放器的view
        closeBtn = (MaterialIconView) currentFragmentView.findViewById(R.id.btn_play_close) ;
        inputBtn = (MaterialIconView) currentFragmentView.findViewById(R.id.btn_play_input) ;
        loadingBar = (ProgressBar) currentFragmentView.findViewById(R.id.pb_play_loading) ;
        closeBtn.setOnClickListener(this);
        inputBtn.setOnClickListener(this);
        closeBtn.getBackground().setAlpha(ALPHA_DEFAULT_VALUE) ;    //设置透明度
        inputBtn.getBackground().setAlpha(ALPHA_DEFAULT_VALUE) ;
        bgImageView = (ImageView) currentFragmentView.findViewById(R.id.iv_play_bg) ;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {   //竖屏
            this.playPresenter.setPlayViewOrientation(Configuration.ORIENTATION_PORTRAIT);
        } else if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){    //横屏
            this.playPresenter.setPlayViewOrientation(Configuration.ORIENTATION_LANDSCAPE) ;
        }
    }

    /**
     *  摧毁fragment时，同时也要摧毁播放器相关的组件
     */
    @Override
    public void onDestroy() {
        Log.d(TAG, "destroy") ;
        super.onDestroy();
        playPresenter.stopPlay() ;
        mPlayerView.onDestroy() ;
    }


    @Override
    public void play(String playIp) {
        playPresenter.play(playIp) ;
    }

    @Override
    public TXCloudVideoView getPlayerView() {
        return mPlayerView ;
    }

    @Override
    public void showLoading() {
        loadingBar.setVisibility(ProgressBar.VISIBLE) ;
    }

    @Override
    public void hideLoading() {
        loadingBar.setVisibility(ProgressBar.GONE) ;
    }

    static BaseAnimatorSet  bas_in = new FlipVerticalSwingEnter();  //动画，弹出提示框的动画
    static BaseAnimatorSet bas_out = new FadeExit();                //关闭提示框的动画
    @Override
    public void destroyPlayView() {
        final MaterialDialog dialog = new MaterialDialog(getActivity()) ;
        dialog.content("连接服务器失败，请进行如下操作! ") ;
        dialog.btnNum(2).btnText("返回" ,"刷新直播间") .btnTextColor(NormalDialog.STYLE_TWO) ;
        dialog.showAnim(bas_in)
                .dismissAnim(bas_out)
                .show() ;
        dialog.setOnBtnClickL(new OnBtnClickL() {
            @Override
            public void onBtnClick() {  //返回
                dialog.dismiss() ;
                getActivity().finish() ;
            }
        }, new OnBtnClickL() {
            @Override
            public void onBtnClick() {  //刷新直播间
                dialog.dismiss() ;
                ((PlayActivity)getActivity()).reLoadCurrentFragment() ;

            }
        });
        Toast.makeText(getActivity(), "网络重连失败，请重新进入直播间",Toast.LENGTH_LONG).show() ;
    }

    @Override
    public void firstStartPlay() {
        bgImageView.setVisibility(View.GONE);
        Toast.makeText(getActivity(), "欢迎进入直播间", Toast.LENGTH_SHORT).show() ;
    }

    /**
     *  按钮的点击事件
     * @param v
     */
    @Override
    public void onClick(View v) {
        int resourceFlag = v.getId() ;  //获取btn的id
        switch (resourceFlag) {
            case R.id.btn_play_close : {
                Log.d(TAG, "点击了关闭按钮") ;
                getActivity().finish() ;
                break ;
            }
            case R.id.btn_play_input : {
                Log.d(TAG, "点击了输入文字的按钮");
                break ;
            }
        }
    }
}
