package org.live.module.home.view.custom;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import org.live.R;
import org.live.common.constants.LiveConstants;
import org.live.common.domain.SimpleUserVo;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * 用户信息的弹出框
 * Created by wang on 2017/4/24.
 */
public class UserInfoDialogView {

    private ImageView headImgView ;  //头像的view

    private TextView accountView ;  //账号的view

    private TextView nicknameView ; //昵称的view

    private View btnHoldView ;      //包裹禁言按钮和踢出按钮的view

    private Button shutupBtnView ;  //禁言的按钮view

    private Button kickoutBtnView ;  //踢出的view

    private DialogPlus AnchorInfoDialog ;   //弹出框的view

    private Context context ;   //

    private CropCircleTransformation cropCircleTransformation ; //图片圆形处理

    public UserInfoDialogView(Context context) {
        this.context = context;
        cropCircleTransformation = new CropCircleTransformation(context) ;

        AnchorInfoDialog = DialogPlus.newDialog(context).setContentBackgroundResource(R.color.colorWhite)
                .setContentHolder(new ViewHolder(R.layout.dialog_user_in_liveroom))
                .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
                .setContentWidth(ViewGroup.LayoutParams.WRAP_CONTENT)
                .setGravity(Gravity.CENTER)
                .create() ;

        View holderView = AnchorInfoDialog.getHolderView() ;
        headImgView = (ImageView) holderView.findViewById(R.id.iv_dialog_play_userImg) ;
        accountView = (TextView) holderView.findViewById(R.id.tv_dialog_play_account) ;
        nicknameView = (TextView) holderView.findViewById(R.id.tv_dialog_play_nickname) ;
        btnHoldView = holderView.findViewById(R.id.rl_dialog_play_btnHold) ;
        shutupBtnView = (Button) holderView.findViewById(R.id.btn_dialog_play_shutup) ;
        kickoutBtnView = (Button) holderView.findViewById(R.id.btn_dialog_play_kickout) ;
    }

    /**
     * 往view中赋值，并弹出模态框
     */
    public void setValueAndShow(SimpleUserVo userVo) {
        if(userVo == null)  return ;
        Glide.with(context).load(LiveConstants.REMOTE_SERVER_HTTP_IP + userVo.getHeadImgUrl())
                .bitmapTransform(cropCircleTransformation)
                .into(headImgView) ; // 设置头像
        accountView.setText(userVo.getAccount()) ;
        nicknameView.setText(userVo.getNickname()) ;
        AnchorInfoDialog.show() ;
    }

    public ImageView getHeadImgView() {
        return headImgView;
    }

    public TextView getAccountView() {
        return accountView;
    }

    public TextView getNicknameView() {
        return nicknameView;
    }

    public View getBtnHoldView() {
        return btnHoldView;
    }

    public Button getShutupBtnView() {
        return shutupBtnView;
    }

    public Button getKickoutBtnView() {
        return kickoutBtnView;
    }

    public DialogPlus getAnchorInfoDialog() {
        return AnchorInfoDialog;
    }
}
