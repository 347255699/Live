package org.live.module.play.view.impl;


import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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

import org.live.common.domain.MessageType;
import org.live.common.listener.ChatActivityEvent;
import org.live.common.listener.NoDoubleClickListener;
import org.live.common.provider.AnchorInfoProvider;
import org.live.common.util.JsonUtils;
import org.live.common.util.ResponseModel;
import org.live.module.chat.service.AnchorChatService;
import org.live.module.home.constants.HomeConstants;
import org.live.module.home.domain.AppAnchorInfo;
import org.live.module.home.presenter.LiveRoomPresenter;
import org.live.module.home.view.custom.AnchorInfoDialogView;
import org.live.module.home.view.impl.HomeActivity;
import org.live.module.login.domain.MobileUserVo;
import org.live.module.play.domain.LiveRoomInfo;
import org.live.module.play.listener.OnPlayActivityEvent;
import org.live.module.play.presenter.PlayPresenter;
import org.live.module.play.presenter.impl.PlayPresenterImpl;
import org.live.module.play.view.PlayView;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * 视频直播观看模块
 * Created by Mr.wang on 2017/3/9.
 */

public class PlayFragment extends Fragment implements PlayView, View.OnClickListener {

    public static final String TAG = "PlayFragment";

    private static final int ALPHA_DEFAULT_VALUE = 30; // 默认透明度

    private View currentFragmentView = null;   //当前的fragment视图

    private TXCloudVideoView mPlayerView = null;   //播放器的view

    private PlayPresenter playPresenter = null;    // 播放器的prsenter

    private LiveRoomPresenter liveRoomPresenter;     //直播间的presenter

    private MaterialIconView closeBtn = null; //关闭页面的btn

    private MaterialIconView inputBtn = null; //呼出输入文字的btn

    private ProgressBar loadingBar = null;

    private ImageView bgImageView = null;  //背景图片

    private ImageView headImgView;    //主播头像控件

    private TextView liveRoomNameView;     //直播间名控件

    private TextView onlineCountView;  //在线人数控件

    private View liveRoomInfoView;     //顶部房间信息的控件

    private LiveRoomInfo liveRoomInfo;

    private Handler handler;      //handler

    private AnchorInfoDialogView anchorInfoDialogView;

    private AnchorInfoProvider anchorInfoProvider;

    private OnPlayActivityEvent playActivityEvent;

    private AnchorChatService.ChatReceiveServiceBinder chatReceiveServiceBinder ;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        currentFragmentView = inflater.inflate(R.layout.fragment_play, null);
        newHandlerInstance();  //实例化handler
        if (getActivity() instanceof AnchorInfoProvider) {
            this.anchorInfoProvider = (AnchorInfoProvider) getActivity();
        }
        if (getActivity() instanceof OnPlayActivityEvent) {
            this.playActivityEvent = (OnPlayActivityEvent) getActivity();
        }

        this.liveRoomInfo = anchorInfoProvider.getLiveRoomInfo(); // 取得直播间信息

        initUI();
        liveRoomPresenter = new LiveRoomPresenter(getActivity(), handler);
        playPresenter = new PlayPresenterImpl(this, getActivity());
        this.play(LiveConstants.RTMP_PLAY_IP_PREFIX + liveRoomInfo.getLiveRoomNum()) ;
        return currentFragmentView;
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        registerReceiver() ;
    }


    /**
     * 初始化UI
     */
    public void initUI() {
        mPlayerView = (TXCloudVideoView) currentFragmentView.findViewById(R.id.vv_play_player);   //播放器的view
        closeBtn = (MaterialIconView) currentFragmentView.findViewById(R.id.btn_play_close);   //关闭按钮
        inputBtn = (MaterialIconView) currentFragmentView.findViewById(R.id.btn_play_input);
        if (liveRoomInfo.isShutupFlag()) {    //被禁言
            switchInputBtnState(liveRoomInfo.isShutupFlag());
        }
        loadingBar = (ProgressBar) currentFragmentView.findViewById(R.id.pb_play_loading);

        //房间信息相关的控件
        headImgView = (ImageView) currentFragmentView.findViewById(R.id.iv_play_headImg);
        Glide.with(this).load(LiveConstants.REMOTE_SERVER_HTTP_IP + liveRoomInfo.getHeadImgUrl())
                .bitmapTransform(new CropCircleTransformation(getActivity()))
                .into(headImgView); // 设置头像
        liveRoomNameView = (TextView) currentFragmentView.findViewById(R.id.tv_play_liveRoomName);
        liveRoomNameView.setText(liveRoomInfo.getLiveRoomName());    //设置房间名
        onlineCountView = (TextView) currentFragmentView.findViewById(R.id.tv_play_onlineCount);
        onlineCountView.setText(liveRoomInfo.getOnlineCount());  //设置在线人数
        liveRoomInfoView = currentFragmentView.findViewById(R.id.rl_play_liveroom_info);
        liveRoomInfoView.getBackground().setAlpha(50);    //设置透明度
        liveRoomInfoView.setOnClickListener(new NoDoubleClickListener() {    //点击显示房间信息
            @Override
            protected void onNoDoubleClick(View v) {
                liveRoomPresenter.loadAnchorInfoData(HomeActivity.mobileUserVo.getUserId(), liveRoomInfo.getLiveRoomId());   //加载数据
            }
        });

        closeBtn.setOnClickListener(this);
        inputBtn.setOnClickListener(this);
        closeBtn.getBackground().setAlpha(ALPHA_DEFAULT_VALUE);    //设置透明度
        inputBtn.getBackground().setAlpha(ALPHA_DEFAULT_VALUE);
        bgImageView = (ImageView) currentFragmentView.findViewById(R.id.iv_play_bg);
    }

    /**
     * 实例化handler
     */
    private void newHandlerInstance() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == HomeConstants.LOAD_ANCHOR_INFO_SUCCESS_FLAG) {   //加载主播信息成功
                    ResponseModel<AppAnchorInfo> dataModel = (ResponseModel) msg.obj;
                    if (dataModel.getStatus() == 1) {
                        AppAnchorInfo info = dataModel.getData();
                        showAnchorInfoDialog(info);
                    }
                } else if(msg.what == HomeConstants.LOAD_REPORT_SUCCESS_FLAG) {     //加载举报信息
                    ResponseModel<Object> dataModel = (ResponseModel<Object>) msg.obj ;
                    if(dataModel.getStatus() ==1) {
                        Toast.makeText(getActivity(), "举报成功！", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "举报失败！", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        };
    }


    /**
     * 弹出主播信息的弹出框
     *
     * @param info
     */
    private void showAnchorInfoDialog(AppAnchorInfo info) {
        if (anchorInfoDialogView == null) {
            anchorInfoDialogView = new AnchorInfoDialogView(getActivity());
            anchorInfoDialogView.getReportView().setOnClickListener(new View.OnClickListener() { //点击举报
                @Override
                public void onClick(View v) {
                    liveRoomPresenter.reportLiveRoomByUser(HomeActivity.mobileUserVo.getUserId(), liveRoomInfo.getLiveRoomId()) ;
                }
            });

            ClickAttentionListener listener = new ClickAttentionListener() ;
            //点击关注
            anchorInfoDialogView.getAttentionHold().setOnClickListener(listener) ;
            anchorInfoDialogView.getAttentionBtnView().setOnClickListener(listener) ;
            anchorInfoDialogView.getAttentionTypeView().setOnClickListener(listener) ;
        }
        anchorInfoDialogView.setValueAndShow(info);

    }

    /**
     * 切换呼出输入文字的按钮
     *
     * @param shutupFlag
     */
    private void switchInputBtnState(boolean shutupFlag) {
        liveRoomInfo.setShutupFlag(shutupFlag);
        if (liveRoomInfo.isShutupFlag()) {    //被禁言
            inputBtn.setIcon(MaterialDrawableBuilder.IconValue.COMMENT_REMOVE_OUTLINE);
        } else {
            inputBtn.setIcon(MaterialDrawableBuilder.IconValue.COMMENT_TEXT_OUTLINE);
        }
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {   //竖屏
            this.playPresenter.setPlayViewOrientation(Configuration.ORIENTATION_PORTRAIT);
        } else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {    //横屏
            this.playPresenter.setPlayViewOrientation(Configuration.ORIENTATION_LANDSCAPE);
        }
    }

    /**
     * 摧毁fragment时，同时也要摧毁播放器相关的组件
     */
    @Override
    public void onDestroy() {
        Log.d(TAG, "destroy");
        getActivity().unregisterReceiver(chatReceiver) ;    //注销广播接收器
        playPresenter.stopPlay();
        mPlayerView.onDestroy();
        super.onDestroy();
    }


    @Override
    public void play(String playIp) {
        playPresenter.play(playIp);
    }

    @Override
    public TXCloudVideoView getPlayerView() {
        return mPlayerView;
    }

    @Override
    public void showLoading() {
        loadingBar.setVisibility(ProgressBar.VISIBLE);
    }

    @Override
    public void hideLoading() {
        loadingBar.setVisibility(ProgressBar.GONE);
    }


    @Override
    public void destroyPlayView() {
        //连接拉流失败，重新刷新fragment，
        ((PlayActivity) getActivity()).reloadCurrentFragment();

    /*    final MaterialDialog dialog = new MaterialDialog(getActivity());
        dialog.content("连接服务器失败，请进行如下操作! ");
        dialog.btnNum(2).btnText("返回", "刷新直播间").btnTextColor(NormalDialog.STYLE_TWO);
        dialog.show();
        dialog.setOnBtnClickL(new OnBtnClickL() {
            @Override
            public void onBtnClick() {  //返回
                dialog.dismiss();
                onDestroy();
                getActivity().finish();
            }
        }, new OnBtnClickL() {
            @Override
            public void onBtnClick() {  //刷新直播间
                dialog.dismiss();
                ((PlayActivity) getActivity()).reloadCurrentFragment();

            }
        });*/
       // Toast.makeText(getActivity(), "网络重连失败，请重新进入直播间", Toast.LENGTH_LONG).show();
    }

    @Override
    public void firstStartPlay() {
        bgImageView.setVisibility(View.GONE);
        Toast.makeText(getActivity(), "欢迎进入直播间", Toast.LENGTH_SHORT).show();
    }

    /**
     * 按钮的点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        int resourceFlag = v.getId();  //获取btn的id
        switch (resourceFlag) {
            case R.id.btn_play_close: {
                playActivityEvent.logoutService(); // 注销服务
                getActivity().finish();
                break;
            }
            case R.id.btn_play_input: {
                if (liveRoomInfo.isShutupFlag()) {   //被禁言
                    Toast.makeText(getActivity(), "您被禁言，暂时不能发言", Toast.LENGTH_SHORT).show();
                    return;
                }
                playActivityEvent.showChatInputView(); //  显示输入框
                break;
            }
        }
    }

    /**
     * 注册广播接收器
     */
    private void registerReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(AnchorChatService.ACTION); // 绑定意图
        filter.setPriority(Integer.MAX_VALUE); // 高优先级
        getActivity().registerReceiver(chatReceiver, filter); // 注册广播接收器
    }

    /**
     * 广播接收器
     *
     */
    private BroadcastReceiver chatReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            org.live.common.domain.Message message =
                    JsonUtils.fromJson(intent.getStringExtra("msg"), org.live.common.domain.Message.class);
            switch (message.getMessageType()) {
                case MessageType.USER_ENTER_CHATROOM_MESSAGE_TYPE : {   //用户进入直播间
                    onlineCountView.setText(message.getContent()) ; //刷新在线人数
                    break;
                }
                case MessageType.USER_EXIT_CHATROOM_MESSAGE_TYPE: {     //用户离开直播间
                    onlineCountView.setText(message.getContent()) ;
                    break;
                }
                case MessageType.SHUTUP_USER_MESSAGE_TYPE: {    //用户被禁言
                    switchInputBtnState(true) ;
                    break;
                }
                case MessageType.RELIEVE_SHUTUP_USER_MESSAGE_TYPE : {   //用户被解除禁言
                    switchInputBtnState(false) ;
                    break ;
                }

                case MessageType.KICKOUT_USER_MESSAGE_TYPE: {   //用户踢出主播间
                    Toast.makeText(getActivity(), "您被主播踢出房间", Toast.LENGTH_SHORT).show();
                    getActivity().finish() ;
                    break ;
                }

                case MessageType.ANCHOR_EXIT_CHATROOM_MESSAGE_TYPE: {   //主播离开
                    playActivityEvent.logoutService() ;
                    playActivityEvent.replaceLiveOverFragment() ;
                    break ;
                }

            }
        }
    } ;

    /**
     *
     * @return
     */
    private AnchorChatService.ChatReceiveServiceBinder getChatServiceBinder() {
        if(chatReceiveServiceBinder == null)
            chatReceiveServiceBinder = ((ChatActivityEvent) getActivity()).getChatReceiveServiceBinder() ;
        return chatReceiveServiceBinder ;
    }

    /**
     * 关注的点击事件
     */
     class ClickAttentionListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            String account = HomeActivity.mobileUserVo.getAccount() ;
            org.live.common.domain.Message message = new org.live.common.domain.Message() ;
            message.setFromChatRoomNum(liveRoomInfo.getLiveRoomNum()) ;
            message.setAccount(account) ;
            message.setDestination(liveRoomInfo.getLiveRoomNum() + "-" + account) ;
            message.setNickname(HomeActivity.mobileUserVo.getNickname()) ;

            boolean attentionFlag = anchorInfoDialogView.isAttentionFlag() ;
            if(attentionFlag) { //当前是关注状态， 改成未关注
                message.setMessageType(MessageType.RELIEVE_USER_ATTENTION_CHATROOM) ;
                getChatServiceBinder().sendMsg(message) ;

                anchorInfoDialogView.getAttentionBtnView().setIcon(MaterialDrawableBuilder.IconValue.ACCOUNT_PLUS) ;
                anchorInfoDialogView.getAttentionTypeView().setText("关注");

            } else {    //当前是未关注状态，改成已关注状态
                message.setMessageType(MessageType.USER_ATTENTION_CHATROOM) ;
                getChatServiceBinder().sendMsg(message) ;

                anchorInfoDialogView.getAttentionBtnView().setIcon(MaterialDrawableBuilder.IconValue.ACCOUNT) ;
                anchorInfoDialogView.getAttentionTypeView().setText("已关注") ;
            }
            anchorInfoDialogView.setAttentionFlag(!attentionFlag) ;
        }
    }

}
