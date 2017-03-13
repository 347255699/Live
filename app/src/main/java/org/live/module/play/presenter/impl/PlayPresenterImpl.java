package org.live.module.play.presenter.impl;

import android.content.Context;

import org.live.module.play.listener.OnPlayListener;
import org.live.module.play.model.PlayModel;
import org.live.module.play.model.impl.PlayModelImpl;
import org.live.module.play.presenter.PlayPresenter;
import org.live.module.play.view.PlayView;

/**
 * Created by Mr.wang on 2017/3/10.
 */
public class PlayPresenterImpl implements PlayPresenter, OnPlayListener {

    private PlayModel playModel = null ;

    private PlayView playView = null ;

    public PlayPresenterImpl(PlayView view, Context context) {
        this.playView = view ;
        this.playModel = new PlayModelImpl(context, this) ;
        this.playModel.initPlayerView(view.getPlayerView()) ;   //为播放器绑定视图
    }


    @Override
    public void play(String playIp) {
        playModel.play(playIp) ;
    }

    @Override
    public void stopPlay() {
        playModel.stopPlay() ;
    }

    @Override
    public void setPlayViewOrientation(int orientation) {
        this.playModel.setPlayViewOrientation(orientation) ;
    }

    @Override
    public void onFirstStartingPlay() {
        playView.firstStartPlay() ;
    }

    @Override
    public void onStartingPlay() {
        playView.hideLoading() ;
    }

    @Override
    public void onLoading() {
        playView.showLoading() ;
    }

    @Override
    public void onDestroyPlayFromNetWorkFail() {
        playView.destroyPlayView();
    }

    @Override
    public void onPlayEnd() {

    }
}
