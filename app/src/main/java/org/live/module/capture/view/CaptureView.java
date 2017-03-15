package org.live.module.capture.view;

import net.steamcrafted.materialiconlib.MaterialDrawableBuilder;

/**
 * 录屏视图响应接口
 * Created by KAM on 2017/3/10.
 */

public interface CaptureView {

    public void onShowNotification(); // 显示通知栏

    public void onShowToastMsg(String msg, int lengthType); // 显示提示信息

}
