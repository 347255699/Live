package org.live.module.publish.model;

import android.graphics.Bitmap;

import com.tencent.rtmp.ui.TXCloudVideoView;

import java.util.Map;

/**
 * 主播模块相关业务
 * Created by KAM on 2017/3/2.
 */

public interface PublishModel {
    public void startCameraPreview(TXCloudVideoView previewVideoView); // 启动预览

    public void startPusher(String rtmpUrl); // 启动推流

    public void setBeautyFilter(Integer beauty, Integer whitening); // 设置美颜

    public void setFilter(Bitmap bmp); // 设置滤镜

    public boolean switchCamera(boolean isFrontCamera); // 切换摄像头

    public boolean switchFlashLight(boolean flashTurnOn); // 打开闪光灯

    public void setPushConfig(Map<Integer, Object> configs); // 设置推流参数

    public void pausePusher(); // 切换后台处理

    public void resumePusher(); // 切换前台处理

    public void pushNetBusyWarning(); // 提醒网络不佳

    public void onDisplayRotationChanged(int mobileRotation); // 自动旋转屏幕

    public void stopRtmpPublish(); // 结束推流

    public void getBeautyAndWhiteningVal(); // 获取磨皮和美白级别

    public void stopCameraPreview(); // 停止预览

    public void refreshPreferences(); // 更新推流参数

    public void getVolumeVal(); // 获取音量数值

    public void setVolumeVal(Float microPhone, Float volume); // 设置音量

    public boolean setVolumeOff(boolean turnVolumeOff); // 设置静音

    /** 背景混音暂未计划开发**/
}
