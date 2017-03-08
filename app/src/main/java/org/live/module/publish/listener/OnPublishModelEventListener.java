package org.live.module.publish.listener;

/**
 * 主播模型层事件监听(驱动view层ui做出相应的响应)
 * Created by KAM on 2017/3/3.
 */

public interface OnPublishModelEventListener {

    public void onPushing(); // 开始推流

    public void onToastMsg(String msg, Integer lengthType); // 提示消息

    public void onCloseFlash(); // 关闭闪光灯

    public void onOpenFlash(); // 开启闪光灯

    public void onPausemPreview(); // 暂停预览

    public void onResumePreview(); // 恢复预览

    public void onStopPusher(); // 等值推流

    public void onSetBeautyRangeBarVal(Integer beauty, Integer whitening); // 设置美颜拉杆初始值

    public void onSetVolumeSettingsViewVal(Float microphone, Float volume); // 设置音量视图初始值

    public void onRefreshVolumeSettingsViewVal(Integer isVisible); // 刷新音量设置视图
}
