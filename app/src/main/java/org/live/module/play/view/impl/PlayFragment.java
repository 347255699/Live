package org.live.module.play.view.impl;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

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

    private View currentFragmentView = null ;   //当前的fragment视图

    private TXCloudVideoView mPlayerView = null ;   //播放器的view

    private PlayPresenter playPresenter = null ;    // 播放器的prsenter

    private MaterialIconView closeBtn = null ; //关闭页面的btn

    private MaterialIconView inputBtn = null ; //呼出输入文字的btn

    private ProgressBar loadingBar = null ;

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
    public void pause() {
        playPresenter.pause() ;
    }

    @Override
    public void resume() {
        playPresenter.resume() ;
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

    @Override
    public void destroyPlayView() {
        Toast.makeText(getActivity(), "网络重连失败，请重新进入直播间",Toast.LENGTH_LONG).show() ;
    }

    @Override
    public void firstStartPlay() {
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
