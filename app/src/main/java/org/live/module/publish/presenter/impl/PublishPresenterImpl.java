package org.live.module.publish.presenter.impl;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.tencent.rtmp.ui.TXCloudVideoView;

import org.live.module.chat.entity.Message;
import org.live.module.publish.domain.LimitationVo;
import org.live.module.publish.listener.OnPublishModelEventListener;
import org.live.module.publish.model.PublishModel;
import org.live.module.publish.model.impl.PublishModelImpl;
import org.live.module.publish.presenter.PublishPresenter;
import org.live.module.publish.view.PublishView;

import java.util.List;
import java.util.Map;


/**
 * 推流表示器
 * Created by KAM on 2017/3/3.
 */

public class PublishPresenterImpl implements PublishPresenter, OnPublishModelEventListener {
    private PublishModel recorderModel = null;
    private PublishView recorderView = null;

    public PublishPresenterImpl(PublishView recorderView, Context context) {
        this.recorderView = recorderView;
        recorderModel = new PublishModelImpl(context, this);
    }

    /**
     * 开始预览
     */
    @Override
    public void startCameraPreview() {
        TXCloudVideoView previewVideoView = recorderView.getPreviewVideoView();
        previewVideoView.setVisibility(View.VISIBLE);
        recorderModel.startCameraPreview(previewVideoView);
    }

    /**
     * 开始推流
     *
     * @param rtmpUrl
     */
    @Override
    public void startPusher(String rtmpUrl) {
        recorderModel.startPusher(rtmpUrl);

    }

    /**
     * 设置美颜
     *
     * @param beauty
     * @param whitening
     */
    @Override
    public void setBeautyFilter(Integer beauty, Integer whitening) {
        recorderModel.setBeautyFilter(beauty, whitening);
    }

    /**
     * 切换摄像头
     *
     * @param isFrontCamera
     * @return
     */
    @Override
    public boolean switchCamera(boolean isFrontCamera) {
        return recorderModel.switchCamera(isFrontCamera);
    }

    /**
     * 切换闪光灯状态
     *
     * @param flashTurnOn
     * @return
     */
    @Override
    public boolean switchFlashLight(boolean flashTurnOn) {
        return recorderModel.switchFlashLight(flashTurnOn);
    }

    /**
     * 设置推流参数
     *
     * @param configType 参数类型
     * @param value      参数值
     */
    @Override
    public void setPushConfig(String configType, Object value) {
        recorderModel.setPushConfig(configType, value);
    }

    /**
     * 获取参数
     *
     * @return Map<String, Object> 参数集
     */
    @Override
    public void getPushConfig() {
        recorderModel.getPushConfig();
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

    /**
     * 监听横竖屏变化
     *
     * @param mobileRotation
     */
    @Override
    public void onDisplayRotationChanged(int mobileRotation) {
        recorderModel.onDisplayRotationChanged(mobileRotation);
    }

    /**
     * 停止推流
     */
    @Override
    public void stopRtmpPublish() {
        recorderModel.stopRtmpPublish();
    }

    /**
     * 获取美颜参数
     */
    @Override
    public void getBeautyAndWhiteningVal() {
        recorderModel.getBeautyAndWhiteningVal();
    }

    @Override
    public void stopCameraPreview() {

    }

    /**
     * 刷新参数并持久化
     */
    @Override
    public void refreshPreferences() {
        recorderModel.refreshPreferences();
    }

    /**
     * 获取音量值
     */
    @Override
    public void getVolumeVal() {
        recorderModel.getVolumeVal();
    }

    /**
     * 设置音量值
     *
     * @param microPhone
     * @param volume
     */
    @Override
    public void setVolumeVal(Float microPhone, Float volume) {
        recorderModel.setVolumeVal(microPhone, volume);
    }

    /**
     * 设置静音
     *
     * @param turnVolumeOff
     * @return
     */
    @Override
    public void setVolumeOff(boolean turnVolumeOff) {
        recorderModel.setVolumeOff(turnVolumeOff);
    }

    /**
     * 获取黑名单
     */
    @Override
    public void getBlackListData() {
        recorderModel.getBlackListData();
    }

    /**
     * 通知view正在直播
     */
    @Override
    public void onPushing() {
        recorderView.onShowPauseIconView();
    }

    /**
     * 通知view显示提示信息
     *
     * @param msg
     * @param lengthType
     */
    @Override
    public void onToastMsg(String msg, Integer lengthType) {
        recorderView.onShowToastMessage(msg, lengthType);
    }

    /**
     * 通知view闪光灯已关闭
     */
    @Override
    public void onCloseFlash() {
        recorderView.onShowFlashOffIconView();
    }

    /**
     * 通知view闪光灯已开启
     */
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

    /**
     * 通知view推流已关闭
     */
    @Override
    public void onStopPusher() {
        recorderView.onShowPlayIconView();
        //recorderView.onShowToastMessage("直播已经断开！", Toast.LENGTH_SHORT);
    }

    /**
     * 通知view显示美颜视图
     *
     * @param beauty
     * @param whitening
     */
    @Override
    public void onSetBeautyRangeBarVal(Integer beauty, Integer whitening) {
        recorderView.onShowBeautyRangeBarView(beauty, whitening);
    }

    /**
     * 通知view显示音量设置视图
     *
     * @param microphone
     * @param volume
     */
    @Override
    public void onSetVolumeSettingsViewVal(Float microphone, Float volume, boolean isVolumeOff) {
        recorderView.onShowVolumeSettingsView(microphone, volume, isVolumeOff);
    }

    @Override
    public void onSetVolumeOffSwitchButton(boolean isVolumeOff) {
        recorderView.onRefreshVolumeOffSwitchVal(isVolumeOff);
    }

    @Override
    public void onSetPublishSettingsViewVal(Map<String, Object> config) {
        recorderView.onShowPublishSettingsView(config);
    }

    /**
     * 刷新黑名单
     */
    @Override
    public void refreshBlackList(List<LimitationVo> limitationVos) {
        recorderView.refreshBlackList(limitationVos);
    }


}
