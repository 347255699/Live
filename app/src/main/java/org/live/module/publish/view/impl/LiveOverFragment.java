package org.live.module.publish.view.impl;

import android.app.Fragment;
import android.media.midi.MidiOutputPort;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.live.R;
import org.live.common.constants.LiveConstants;
import org.live.common.provider.AnchorInfoProvider;
import org.live.common.util.ResponseModel;
import org.live.module.home.constants.HomeConstants;
import org.live.module.home.domain.AppAnchorInfo;
import org.live.module.home.presenter.LiveRoomPresenter;
import org.live.module.home.view.impl.HomeActivity;
import org.live.module.login.domain.MobileUserVo;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * 直播结束的activity
 * Created by wang on 2017/4/25.
 */
public class LiveOverFragment extends Fragment {

    private ImageView headImgView;  //头像的view

    private TextView nicknameView; //昵称的veiw

    private TextView accountView;  //账号的view

    private TextView attentionCountView;   //关注数的view

    private TextView descriptionView;      //个性签名的view

    private Button returnBtn;      //返回按钮

    private MobileUserVo mobileUserVo; // 用户引用

    private View view;

    private Handler handler;
    private LiveRoomPresenter liveRoomPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_live_over, container, false);
        initEventHandler();
        liveRoomPresenter = new LiveRoomPresenter(getActivity(), handler);
        if (getActivity() instanceof AnchorInfoProvider) {
            AnchorInfoProvider anchorInfoProvider = (AnchorInfoProvider) getActivity();
            this.mobileUserVo = anchorInfoProvider.getAnchorInfo(); // 取得主播信息
        }
        liveRoomPresenter.loadAnchorInfoData(mobileUserVo.getUserId(), mobileUserVo.getLiveRoomVo().getRoomId()); // 加载数据
        initial();
        return view;
    }

    /**
     * 初始化ui
     */
    private void initial() {

        MobileUserVo.LiveRoomInUserVo liveRoomInUserVo = mobileUserVo.getLiveRoomVo();

        headImgView = (ImageView) view.findViewById(R.id.iv_live_over_headImg);
        accountView = (TextView) view.findViewById(R.id.tv_live_over_account);
        nicknameView = (TextView) view.findViewById(R.id.tv_live_over_nickname);
        attentionCountView = (TextView) view.findViewById(R.id.tv_live_over_attentionCount);
        descriptionView = (TextView) view.findViewById(R.id.tv_live_over_description);

        Glide.with(getActivity()).load(LiveConstants.REMOTE_SERVER_HTTP_IP + mobileUserVo.getHeadImgUrl())
                .bitmapTransform(new CropCircleTransformation(getActivity()))
                .into(headImgView); // 设置头像
        accountView.setText(mobileUserVo.getAccount()); // 设置主播账号
        nicknameView.setText(mobileUserVo.getNickname()); // 设置主播昵称
        attentionCountView.setText("0");/*info.getAttentionCount() + ""*/ // 设置关注数
        descriptionView.setText("签名: " + liveRoomInUserVo.getDescription()); // 设置主播签名


        returnBtn = (Button) view.findViewById(R.id.btn_live_over_return);
        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish(); // 摧毁当前界面
            }
        });
    }

    private void initEventHandler(){
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == HomeConstants.LOAD_ANCHOR_INFO_SUCCESS_FLAG) {
                    ResponseModel<AppAnchorInfo> dataModel = (ResponseModel<AppAnchorInfo>) msg.obj;
                    if (dataModel.getStatus() == 1) {
                        AppAnchorInfo anchorInfo = dataModel.getData();
                        refreshUI(anchorInfo);     //数据显示到ui上
                    } else {
                        Toast.makeText(getActivity(), "服务器繁忙！", Toast.LENGTH_SHORT).show();

                    }
                } else {
                    Toast.makeText(getActivity(), "连接服务器失败！", Toast.LENGTH_SHORT).show();

                }
            }
        };
    }

    /**
     * 更新ui
     *
     * @param info
     */
    private void refreshUI(AppAnchorInfo info) {

        Glide.with(getActivity()).load(LiveConstants.REMOTE_SERVER_HTTP_IP + info.getHeadImgUrl())
                .bitmapTransform(new CropCircleTransformation(getActivity()))
                .into(headImgView); // 设置头像
        accountView.setText(info.getAccount());
        nicknameView.setText(info.getNickname());
        attentionCountView.setText(info.getAttentionCount() + "");
        descriptionView.setText("签名: " + info.getDescription());
    }


}
