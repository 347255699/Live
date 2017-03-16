package org.live.module.capture.util;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.WindowManager;

import org.live.common.listener.NoDoubleClickListener;
import org.live.module.capture.view.impl.CaptureFABView;

/**
 * 系统WindowManager工具类
 * Created by KAM on 2017/3/11.
 */

public class WindowManagerUtil {
    /**
     * 录屏浮窗视图
     */
    public static CaptureFABView captureFABView = null;

    /**
     * 录屏浮窗视图参数
     */
    private static WindowManager.LayoutParams captureFABViewParams = null;

    /**
     * 录屏前置摄像头预览视图
     */
    //public static CaptureCameraPreviewView captureCameraPreviewView = null;

    /**
     * 录屏前置摄像头预览视图参数
     */
    //private static WindowManager.LayoutParams captureCameraPreviewViewParams = null;

    /**
     * 用于控制在屏幕上添加或移除悬浮窗
     */
    private static WindowManager windowManager = null;

    /**
     * 创建录屏悬浮窗视图，初始位置为屏幕的右部中间位置。
     *
     * @param context            必须为应用程序的Context.
     * @param fabOnClickListener
     */
    public static void createCaptureFABView(Context context, NoDoubleClickListener fabOnClickListener) {
        WindowManager windowManager = getWindowManager(context);
        int screenWidth = windowManager.getDefaultDisplay().getWidth();
        int screenHeight = windowManager.getDefaultDisplay().getHeight();
        if (captureFABView == null) {
            captureFABView = new CaptureFABView(context, fabOnClickListener);
            if (captureFABViewParams == null) {
                captureFABViewParams = new WindowManager.LayoutParams();
                captureFABViewParams.type = WindowManager.LayoutParams.TYPE_PHONE;
                captureFABViewParams.format = PixelFormat.RGBA_8888;
                captureFABViewParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                        | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
                captureFABViewParams.gravity = Gravity.LEFT | Gravity.TOP;
                captureFABViewParams.width = CaptureFABView.viewWidth;
                captureFABViewParams.height = CaptureFABView.viewHeight;
                captureFABViewParams.x = 0;
                captureFABViewParams.y = screenHeight / 2;
            }
            captureFABView.setCaptureFABViewParams(captureFABViewParams);
            windowManager.addView(captureFABView, captureFABViewParams); // 创建浮窗
        }
    }

    /**
     * 获取WindowManager
     *
     * @param context
     * @return
     */
    private static WindowManager getWindowManager(Context context) {
        if (windowManager == null) {
            windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        }
        return windowManager;
    }

    /**
     * 将悬浮窗从屏幕上移除。
     *
     * @param context 必须为应用程序的Context.
     */
    public static void removeCaptureFABView(Context context) {
        if (captureFABView != null) {
            WindowManager windowManager = getWindowManager(context);
            windowManager.removeView(captureFABView);
            captureFABView = null;
        }
    }

    /**
     * 创建前置摄像头预览视图
     */
/*
    public static void createCaptureCameraPreviewView(Context context) {
        WindowManager windowManager = getWindowManager(context);
        int screenWidth = windowManager.getDefaultDisplay().getWidth();
        int screenHeight = windowManager.getDefaultDisplay().getHeight();
        if (captureCameraPreviewView == null) {
            captureCameraPreviewView = new CaptureCameraPreviewView(context);
            if (captureCameraPreviewViewParams == null) {
                captureCameraPreviewViewParams = new WindowManager.LayoutParams();
                captureCameraPreviewViewParams.type = WindowManager.LayoutParams.TYPE_PHONE;
                captureCameraPreviewViewParams.format = PixelFormat.RGBA_8888;
                captureCameraPreviewViewParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                        | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
                captureCameraPreviewViewParams.gravity = Gravity.LEFT | Gravity.TOP;
                captureCameraPreviewViewParams.width = CaptureCameraPreviewView.viewWidth;
                captureCameraPreviewViewParams.height = CaptureCameraPreviewView.viewHeight;
                captureCameraPreviewViewParams.x = 0;
                captureCameraPreviewViewParams.y = 0;
            }
            captureCameraPreviewView.setCaptureCameraPreviewViewParams(captureCameraPreviewViewParams);
            windowManager.addView(captureCameraPreviewView, captureCameraPreviewViewParams); // 创建浮窗
        }
    }
*/

    /**
     * 移除前置摄像头预览视图
     */
   /* public static void removeCaptureCameraPreviewView(Context context) {
        if (captureCameraPreviewView != null) {
            WindowManager windowManager = getWindowManager(context);
            windowManager.removeView(captureCameraPreviewView);
            captureCameraPreviewView = null;
        }
    }*/
}
