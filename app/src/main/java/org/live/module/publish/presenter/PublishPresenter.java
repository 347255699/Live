package org.live.module.publish.presenter;

import java.util.Map;

/**
 * 表示器业务
 * Created by KAM on 2017/3/3.
 */

public interface PublishPresenter{
    public void startCameraPreview(); // 开始预览

    public void startPusher(String rtmpUrl); // 开始推流

    public void setBeautyFilter(Integer beauty, Integer whitening); // 设置美颜

    //  public void setFilter(Bitmap bmp); // 设置滤镜

    public boolean switchCamera(boolean isFrontCamera); // 切换摄像头

    public boolean switchFlashLight(boolean flashTurnOn); // 打开闪光灯

    public void setPushConfig(String configType, Object value); // 设置推流参数

    public void getPushConfig(); // 获取推流参数

    public void pausePusher(); // 切换后台处理

    public void resumePusher(); // 切换前台处理

    public void pushNetBusyWarning(); // 提醒网络不佳

    public void onDisplayRotationChanged(int mobileRotation); // 自动旋转屏幕

    public void stopRtmpPublish(); // 结束推流

    public void getBeautyAndWhiteningVal(); // 获取磨皮和美白级别

    public void stopCameraPreview(); // 停止预览

    public void refreshPreferences(); // 更新推流参数

    public void getVolumeVal(); // 获得音量数值

    public void setVolumeVal(Float microPhone, Float volume); // 设置音量

    public void setVolumeOff(boolean turnVolumeOff); // 设置静音
    /** 背景混音暂未计划开发**/
}
