package org.live.module.capture.listener;

/**
 * 录屏模型层事件监听(驱动view层ui做出相应的响应)
 * Created by KAM on 2017/3/10.
 */

public interface OnCaptureModelEventListener {
    public void onCapturingAndPushing(); // 通知view当前正在录屏直播

    public void onStopCaptureAndPusher(); // 通知view当前已退出录屏直播

    public void onShowToastMsg(String msg, int lengthType); // 通知view显示提示信息
}
