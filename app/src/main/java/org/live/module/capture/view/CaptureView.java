package org.live.module.capture.view;

/**
 * 录屏视图响应接口
 * Created by KAM on 2017/3/10.
 */

public interface CaptureView {

    public void onShowCapturePauseIcon(); // 显示录屏暂停按钮

    public void onShowCapturePlayIcon(); // 显示录屏开始按钮

    public void onShowToastMsg(String msg, int lengthType); // 显示提示信息
}
