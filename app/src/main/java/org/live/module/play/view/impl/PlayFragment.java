package org.live.module.play.view.impl;


import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.flyco.animation.BaseAnimatorSet;
import com.flyco.animation.FadeExit.FadeExit;
import com.flyco.animation.FlipEnter.FlipVerticalSwingEnter;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.MaterialDialog;
import com.flyco.dialog.widget.NormalDialog;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.tencent.rtmp.ui.TXCloudVideoView;

import net.steamcrafted.materialiconlib.MaterialDrawableBuilder;
import net.steamcrafted.materialiconlib.MaterialIconView;

import org.live.R;
import org.live.common.constants.LiveConstants;
import org.live.common.constants.LiveKeyConstants;
import org.live.common.listener.NoDoubleClickListener;
import org.live.common.util.ResponseModel;
import org.live.module.home.constants.HomeConstants;
import org.live.module.home.domain.AppAnchorInfo;
import org.live.module.home.presenter.LiveRoomPresenter;
import org.live.module.home.view.custom.AnchorInfoDialogView;
import org.live.module.home.view.impl.HomeActivity;
import org.live.module.play.presenter.PlayPresenter;
import org.live.module.play.presenter.impl.PlayPresenterImpl;
import org.live.module.play.view.PlayView;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by Mr.wang on 2017/3/9.
 */

public class PlayFragment extends Fragment implements PlayView, View.OnClickListener {

    public static final String TAG = "PlayFragment" ;

    private static final int ALPHA_DEFAULT_VALUE = 30; // 默认透明度

    private View currentFragmentView = null ;   //当前的fragment视图

    private TXCloudVideoView mPlayerView = null ;   //播放器的view

    private PlayPresenter playPresenter = null ;    // 播放器的prsenter

    private LiveRoomPresenter liveRoomPresenter ;     //直播间的presenter

    private MaterialIconView closeBtn = null ; //关闭页面的btn

    private MaterialIconView inputBtn = null ; //呼出输入文字的btn

    private ProgressBar loadingBar = null ;

    private ImageView bgImageView = null ;  //背景图片

    private ImageView headImgView ;    //主播头像控件

    private TextView liveRoomNameView ;     //直播间名控件

    private TextView onlineCountView ;  //在线人数控件

    private View liveRoomInfoView ;     //顶部房间信息的控件

    private String liveRoomId ; //直播间id

    private String liveRoomName ;   //直播间名

    private String liveRoomUrl ;    //拉流地址

    private String headImgUrl ;     //主播头像地址

    private String onlineCount ;    //在线人数

    private boolean shutupFlag ;    //禁言标记

    private Handler handler ;      //handler

    private AnchorInfoDialogView anchorInfoDialogView ;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentFragmentView= inflater.inflate(R.layout.fragment_play, null) ;

        playPresenter = new PlayPresenterImpl(this, getActivity()) ;
        newHandlerInstance() ;  //实例化handler
        liveRoomPresenter = new LiveRoomPresenter(getContext(), handler) ;


        Intent intent = getActivity().getIntent() ;
        liveRoomId = intent.getStringExtra(HomeConstants.LIVE_ROOM_ID_KEY);//直播间id
        liveRoomName = intent.getStringExtra(HomeConstants.LIVE_ROOM_NAME_KEY) ; //直播间名
        liveRoomUrl = intent.getStringExtra(HomeConstants.LIVE_ROOM_URL_KEY);//拉流地址
        headImgUrl = intent.getStringExtra(HomeConstants.HEAD_IMG_URL_KEY);//主播头像
        onlineCount = intent.getIntExtra(HomeConstants.LIVE_ROOM_ONLINE_COUNT_KEY, 1)+"" ; //在线人数
        shutupFlag = intent.getIntExtra(HomeConstants.LIMIT_TYPE_KEY_FLAG, 0) == 1  ;   //禁言标记

        initUI();
        this.play(liveRoomUrl) ;
        return currentFragmentView ;
    }

    /**
     *  初始化UI
     */
    public void initUI() {
        mPlayerView = (TXCloudVideoView) currentFragmentView.findViewById(R.id.vv_play_player) ;   //播放器的view
        closeBtn = (MaterialIconView) currentFragmentView.findViewById(R.id.btn_play_close) ;   //关闭按钮
        inputBtn = (MaterialIconView) currentFragmentView.findViewById(R.id.btn_play_input) ;
        if(shutupFlag) {    //被禁言
            switchInputBtnState(shutupFlag) ;
        }
        loadingBar = (ProgressBar) currentFragmentView.findViewById(R.id.pb_play_loading) ;

        //房间信息相关的控件
        headImgView = (ImageView) currentFragmentView.findViewById(R.id.iv_play_headImg);
        Glide.with(this).load(LiveConstants.REMOTE_SERVER_HTTP_IP+ headImgUrl)
                .bitmapTransform(new CropCircleTransformation(getActivity()))
                .into(headImgView); // 设置头像
        liveRoomNameView = (TextView) currentFragmentView.findViewById(R.id.tv_play_liveRoomName) ;
        liveRoomNameView.setText(liveRoomName) ;    //设置房间名
        onlineCountView = (TextView) currentFragmentView.findViewById(R.id.tv_play_onlineCount) ;
        onlineCountView.setText(onlineCount) ;  //设置在线人数
        liveRoomInfoView = currentFragmentView.findViewById(R.id.rl_play_liveroom_info) ;
        liveRoomInfoView.getBackground().setAlpha(50) ;    //设置透明度
        liveRoomInfoView.setOnClickListener(new NoDoubleClickListener() {    //点击显示房间信息
            @Override
            protected void onNoDoubleClick(View v) {
                liveRoomPresenter.loadAnchorInfoData(HomeActivity.mobileUserVo.getUserId(), liveRoomId) ;   //加载数据
            }
        });

        closeBtn.setOnClickListener(this);
        inputBtn.setOnClickListener(this);
        closeBtn.getBackground().setAlpha(ALPHA_DEFAULT_VALUE) ;    //设置透明度
        inputBtn.getBackground().setAlpha(ALPHA_DEFAULT_VALUE) ;
        bgImageView = (ImageView) currentFragmentView.findViewById(R.id.iv_play_bg) ;
    }

    /**
     * 实例化handler
     */
    private void newHandlerInstance() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == HomeConstants.LOAD_ANCHOR_INFO_SUCCESS_FLAG) {   //加载主播信息成功
                    ResponseModel<AppAnchorInfo> dataModel = (ResponseModel) msg.obj ;
                    if(dataModel.getStatus() == 1) {
                        AppAnchorInfo info = dataModel.getData() ;
                        showAnchorInfoDialog(info) ;
                    }
                }
            }
        } ;
    }


    /**
     * 弹出主播信息的弹出框
     * @param info
     */
    private void showAnchorInfoDialog(AppAnchorInfo info) {
       if(anchorInfoDialogView == null) anchorInfoDialogView = new AnchorInfoDialogView(getContext()) ;
        anchorInfoDialogView.setValueAndShow(info) ;
        anchorInfoDialogView.getReportView().setOnClickListener(new View.OnClickListener() { //点击举报
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "举报他", Toast.LENGTH_SHORT).show();
            }
        });
        //点击关注
        anchorInfoDialogView.getAttentionHold().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "关注", Toast.LENGTH_SHORT).show();
            }

        });
    }

    /**
     * 切换呼出输入文字的按钮
     * @param shutupFlag
     */
    private void switchInputBtnState(boolean shutupFlag) {
        this.shutupFlag = shutupFlag ;
        if(shutupFlag) {    //被禁言
            inputBtn.setIcon(MaterialDrawableBuilder.IconValue.COMMENT_REMOVE_OUTLINE) ;
        } else {
            inputBtn.setIcon(MaterialDrawableBuilder.IconValue.COMMENT_TEXT_OUTLINE) ;
        }
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
        playPresenter.stopPlay() ;
        mPlayerView.onDestroy() ;
        super.onDestroy();
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
        dialog.show() ;
        dialog.setOnBtnClickL(new OnBtnClickL() {
            @Override
            public void onBtnClick() {  //返回
                dialog.dismiss() ;
                onDestroy();
                getActivity().finish() ;
            }
        }, new OnBtnClickL() {
            @Override
            public void onBtnClick() {  //刷新直播间
                dialog.dismiss() ;
                ((PlayActivity)getActivity()).reloadCurrentFragment() ;

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
                Log.d(TAG, "点击了输入文字的按钮") ;
                if (shutupFlag) {   //被禁言
                    Toast.makeText(getContext(), "您被禁言，暂时不能发言", Toast.LENGTH_SHORT).show();
                    return ;
                }

                break ;
            }
        }
    }
}
