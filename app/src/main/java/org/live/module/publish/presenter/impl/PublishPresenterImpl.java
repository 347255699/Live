package org.live.module.publish.presenter.impl;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.tencent.rtmp.ui.TXCloudVideoView;

import org.live.module.publish.listener.OnPublishModelEventListener;
import org.live.module.publish.model.PublishModel;
import org.live.module.publish.model.impl.PublishModelImpl;
import org.live.module.publish.presenter.PublishPresenter;
import org.live.module.publish.view.PublishView;

import java.util.Map;


/**
 * Created by KAM on 2017/3/3.
 */

public class PublishPresenterImpl implements PublishPresenter, OnPublishModelEventListener {
    private PublishModel recorderModel = null;
    private PublishView recorderView = null;

    public PublishPresenterImpl(PublishView recorderView, Context context) {
        this.recorderView = recorderView;
        recorderModel = new PublishModelImpl(context, this);
    }

    @Override
    public void startCameraPreview() {
        TXCloudVideoView previewVideoView = recorderView.getPreviewVideoView();
        previewVideoView.setVisibility(View.VISIBLE);
        recorderModel.startCameraPreview(previewVideoView);
    }

    @Override
    public void startPusher(String rtmpUrl) {
        recorderModel.startPusher(rtmpUrl);

    }

    @Override
    public void setBeautyFilter(Integer beauty, Integer whitening) {
        recorderModel.setBeautyFilter(beauty, whitening);
    }

    @Override
    public boolean switchCamera(boolean isFrontCamera) {
        return recorderModel.switchCamera(isFrontCamera);
    }

    @Override
    public boolean switchFlashLight(boolean flashTurnOn) {
        return recorderModel.switchFlashLight(flashTurnOn);
    }

    @Override
    public void setPushConfig(Map<Integer, Object> configs) {

    }

    @Override
    public void pausePusher() {

    }

    @Override
    public void resumePusher() {

    }

    @Override
    public void pushNetBusyWarning() {

    }

    @Override
    public void onDisplayRotationChanged(int mobileRotation) {

    }

    @Override
    public void stopRtmpPublish() {
        recorderModel.stopRtmpPublish();
    }

    @Override
    public void getBeautyAndWhiteningVal() {
        recorderModel.getBeautyAndWhiteningVal();
    }

    @Override
    public void stopCameraPreview() {

    }

    @Override
    public void refreshPreferences() {

    }

    @Override
    public void getVolumeVal() {
        recorderModel.getVolumeVal();
    }

    @Override
    public void setVolumeVal(Float microPhone, Float volume) {
        recorderModel.setVolumeVal(microPhone, volume);
    }

    @Override
    public boolean setVolumeOff(boolean turnVolumeOff) {
        return recorderModel.setVolumeOff(turnVolumeOff);
    }

    @Override
    public void onPushing() {
        recorderView.onShowPauseIconView();
        recorderView.onShowToastMessage("开始直播！", Toast.LENGTH_SHORT);
    }

    @Override
    public void onToastMsg(String msg, Integer lengthType) {
        recorderView.onShowToastMessage(msg, lengthType);
    }

    @Override
    public void onCloseFlash() {
        recorderView.onShowFlashOffIconView();
    }

    @Override
    public void onOpenFlash() {
        recorderView.onShowFlashIconView();
    }

    @Override
    public void onPausemPreview() {

    }

    @Override
    public void onResumePreview() {

    }

    @Override
    public void onStopPusher() {
        recorderView.onShowPlayIconView();
        recorderView.onShowToastMessage("直播已经断开！", Toast.LENGTH_SHORT);
    }

    @Override
    public void onSetBeautyRangeBarVal(Integer beauty, Integer whitening) {
        recorderView.onShowBeautyRangeBarView(beauty, whitening);
    }

    @Override
    public void onSetVolumeSettingsViewVal(Float microphone, Float volume) {
        recorderView.onShowVolumeSettingsView(microphone, volume);
    }

    @Override
    public void onRefreshVolumeSettingsViewVal(Integer isVisible) {
        recorderView.onRefreshVolumeSettingsView(isVisible);
    }


}
