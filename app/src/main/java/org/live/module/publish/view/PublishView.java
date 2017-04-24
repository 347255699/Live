package org.live.module.publish.view;

import com.tencent.rtmp.ui.TXCloudVideoView;

import java.util.Map;

/**
 * 主播模块ui响应监听
 * Created by KAM on 2017/3/3.
 */

public interface PublishView {
    public void onShowPauseIconView(); // 显示推流暂停图标

    public void onShowPlayIconView(); // 显示推流开始图标

    /**
     * 显示提示信息
     *
     * @param msg        信息
     * @param lengthType 长度类型(long|short)
     */
    public void onShowToastMessage(String msg, Integer lengthType);

    public void onShowFlashOffIconView(); // 显示闪光灯关闭图标

    public void onShowFlashIconView(); // 显示闪光灯开启图标

    public void onShowBeautyRangeBarView(Integer beauty, Integer whitening); // 显示美颜拉杆

    public TXCloudVideoView getPreviewVideoView(); // 获取预览视频ui控件

    public void onShowVolumeSettingsView(Float microphone, Float volume, boolean isVolumeOff); // 显示音量设置视图

    public void onRefreshVolumeOffSwitchVal(boolean isVolumeOff); // 刷新静音按钮状态

    public void onShowPublishSettingsView(Map<String, Object> config); // 显示推流参数设置视图

    /**
     * 刷新当前在线观看人数
     * @param count
     */
    public void refreshOnlineCount(int count);
}
