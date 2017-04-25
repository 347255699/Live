package org.live.module.home.view.impl;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.live.R;
import org.live.common.constants.LiveConstants;
import org.live.common.util.ResponseModel;
import org.live.module.home.constants.HomeConstants;
import org.live.module.home.domain.AppAnchorInfo;
import org.live.module.home.presenter.LiveRoomPresenter;
import org.live.module.home.view.custom.AnchorInfoDialogView;
import org.live.module.publish.view.impl.PublishActivity;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * 直播结束的activity
 * Created by wang on 2017/4/25.
 */
public class LiveOverActivity extends Activity {

    private ImageView headImgView ;  //头像的view

    private TextView nicknameView ; //昵称的veiw

    private TextView accountView ;  //账号的view

    private TextView attentionCountView ;   //关注数的view

    private TextView descriptionView ;      //个性签名的view

    private Button returnBtn ;      //返回按钮

    private Handler handler ;

    private LiveRoomPresenter liveRoomPresenter ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_over) ;
        Intent intent = getIntent() ;
        String liveRoomId = intent.getStringExtra(HomeConstants.LIVE_ROOM_ID_KEY) ; //直播间id
        String userId = intent.getStringExtra(HomeConstants.USER_ID_KEY) ;  //用户的id，用于判断与该直播间是否有关注关系。
        initial() ;
        liveRoomPresenter = new LiveRoomPresenter(getBaseContext(), handler) ;
        liveRoomPresenter.loadAnchorInfoData(userId, liveRoomId) ;

       if( PublishActivity.instanace != null) {     //推流activity
           PublishActivity.instanace.finish() ;
       }


    }

    /**
     * 初始化ui
     */
    private void initial() {

        headImgView = (ImageView) findViewById(R.id.iv_live_over_headImg) ;
        accountView = (TextView) findViewById(R.id.tv_live_over_account) ;
        nicknameView = (TextView) findViewById(R.id.tv_live_over_nickname) ;
        attentionCountView = (TextView) findViewById(R.id.tv_live_over_attentionCount);
        descriptionView = (TextView) findViewById(R.id.tv_live_over_description) ;

        returnBtn = (Button) findViewById(R.id.btn_live_over_return) ;
        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish() ;
            }
        });

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == HomeConstants.LOAD_ANCHOR_INFO_SUCCESS_FLAG) {
                    ResponseModel<AppAnchorInfo> dataModel = (ResponseModel<AppAnchorInfo>) msg.obj;
                    if(dataModel.getStatus() ==1) {
                        AppAnchorInfo anchorInfo = dataModel.getData() ;
                        refreshUI(anchorInfo) ;     //数据显示到ui上
                    } else {
                        Toast.makeText(LiveOverActivity.this, "服务器繁忙！", Toast.LENGTH_SHORT).show() ;
                        finish() ;  //结束当前页面
                    }
                } else {
                    Toast.makeText(LiveOverActivity.this, "连接服务器失败！", Toast.LENGTH_SHORT).show() ;
                    finish() ;  //结束当前页面
                }
            }
        } ;


    }

    /**
     * 更新ui
     * @param info
     */
    private  void refreshUI(AppAnchorInfo info) {

        Glide.with(getBaseContext()).load(LiveConstants.REMOTE_SERVER_HTTP_IP + info.getHeadImgUrl())
                .bitmapTransform(new CropCircleTransformation(getBaseContext()))
                .into(headImgView) ; // 设置头像
        accountView.setText(info.getAccount()) ;
        nicknameView.setText(info.getNickname()) ;
        attentionCountView.setText(info.getAttentionCount()+"") ;
        descriptionView.setText("签名: "+ info.getDescription()) ;
    }


    //点击返回按钮
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish() ;
    }
}
