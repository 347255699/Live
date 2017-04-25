package org.live.module.publish.listener;

import org.live.module.publish.domain.LimitationVo;

import java.util.List;
import java.util.Map;

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

    public void onSetVolumeSettingsViewVal(Float microphone, Float volume, boolean isVolumeOff); // 设置音量视图初始值

    public void onSetVolumeOffSwitchButton(boolean isVolumeOff); // 刷新静音按钮状态

    public void onSetPublishSettingsViewVal(Map<String, Object> config); // 设置推流参数视图初始值

    public void refreshBlackList(List<LimitationVo> limitationVos); // 刷新黑名单
}
