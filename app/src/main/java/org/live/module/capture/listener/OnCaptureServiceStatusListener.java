package org.live.module.capture.listener;

/**
 * 录屏服务状态回掉接口
 * Created by KAM on 2017/3/15.
 */

public interface OnCaptureServiceStatusListener {
    /**
     * 通知服务已停止
     */
    public void onServiceStop(boolean isNormal);

    /**
     * 通知服务启动
     */
    public void onServiceStart();

    /**
     * 通知网络质量差
     */
    public void onServiceNetBusy();

    /**
     * 通知当前是否为隐私模式
     *
     * @param isPrivateMode
     */
    public void onPrivateModeStatus(boolean isPrivateMode);

}
