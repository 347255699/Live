package org.live.module.home.view.custom;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import net.steamcrafted.materialiconlib.MaterialDrawableBuilder;
import net.steamcrafted.materialiconlib.MaterialIconView;

import org.live.R;
import org.live.common.constants.LiveConstants;
import org.live.module.home.domain.AppAnchorInfo;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by wang on 2017/4/24.
 */

public class AnchorInfoDialogView {

    private View reportView ;   //举报的view

    private ImageView headImgView ;     //头像的view

    private TextView accountView ;      //账号的view

    private TextView nicknameView ;     //昵称的view

    private TextView attentionCountView ;    //关注数的view

    private View attentionHold  ;   //包裹关注按钮和文字的view

    private MaterialIconView attentionBtnView ;     //关注的view

    private TextView attentionTypeView ;    // 关注的类型 ， 关注，已关注

    private TextView descriptionView ;      //个性签名的view

    private DialogPlus AnchorInfoDialog ;   //弹出框的view

    private Context context ;   //

    private boolean attentionFlag ;     //关注的状态。 true: 已关注， false: 关注。

    private CropCircleTransformation cropCircleTransformation ; //图片圆形处理

    public AnchorInfoDialogView(Context context) {
        this.context = context ;
        cropCircleTransformation = new CropCircleTransformation(context) ;

        AnchorInfoDialog = DialogPlus.newDialog(context).setContentBackgroundResource(R.color.colorWhite)
                .setContentHolder(new ViewHolder(R.layout.dialog_anchor_in_liveroom))
                .setGravity(Gravity.CENTER)
                .create() ;

        View holderView = AnchorInfoDialog.getHolderView() ;
        reportView = holderView.findViewById(R.id.ll_dialog_play_report) ;
        headImgView = (ImageView) holderView.findViewById(R.id.iv_dialog_play_anchorImg) ;
        accountView = (TextView) holderView.findViewById(R.id.tv_dialog_play_account) ;
        nicknameView = (TextView) holderView.findViewById(R.id.tv_dialog_play_nickname) ;
        attentionCountView = (TextView) holderView.findViewById(R.id.tv_dialog_play_attentionCount) ;
        attentionHold = holderView.findViewById(R.id.ll_dialog_play_attention) ;    //关注包裹view
        attentionBtnView = (MaterialIconView) holderView.findViewById(R.id.iv_dialog_play_attention);
        attentionTypeView = (TextView) holderView.findViewById(R.id.tv_dialog_play_attentionType) ;
        descriptionView = (TextView) holderView.findViewById(R.id.tv_dialog_play_descrptioin) ;
    }

    /**
     * 往view中赋值，并弹出模态框
     */
    public void setValueAndShow(AppAnchorInfo info) {
        if(info == null) return  ;
        Glide.with(context).load(LiveConstants.REMOTE_SERVER_HTTP_IP + info.getHeadImgUrl())
                .bitmapTransform(cropCircleTransformation)
                .into(headImgView) ; // 设置头像
        accountView.setText(info.getAccount()) ;    //设置账号
        nicknameView.setText(info.getNickname()) ;  //设置昵称
        attentionCountView.setText(info.getAttentionCount() +"") ;
        descriptionView.setText("签名：" +info.getDescription()) ;
        attentionFlag = info.isAttentionFlag() ;    //关注标记暂存
        if (info.isAttentionFlag()) {   //已关注
            attentionBtnView.setIcon(MaterialDrawableBuilder.IconValue.ACCOUNT) ;
            attentionTypeView.setText("已关注") ;
        } else {    //未关注
            attentionBtnView.setIcon(MaterialDrawableBuilder.IconValue.ACCOUNT_PLUS) ;
            attentionTypeView.setText("关注") ;
        }

        AnchorInfoDialog.show() ;
    }



    public View getReportView() {
        return reportView;
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

    public TextView getAttentionCountView() {
        return attentionCountView;
    }

    public MaterialIconView getAttentionBtnView() {
        return attentionBtnView;
    }

    public TextView getAttentionTypeView() {
        return attentionTypeView;
    }

    public TextView getDescriptionView() {
        return descriptionView;
    }

    public DialogPlus getAnchorInfoDialog() {
        return AnchorInfoDialog;
    }

    public boolean isAttentionFlag() {
        return attentionFlag;
    }

    public View getAttentionHold() {
        return attentionHold;
    }

    public void setAttentionFlag(boolean attentionFlag) {
        this.attentionFlag = attentionFlag;
    }
}
