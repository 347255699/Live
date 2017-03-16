package org.live.module.capture.view.impl;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import net.steamcrafted.materialiconlib.MaterialIconView;

import org.live.R;
import org.live.common.listener.NoDoubleClickListener;
import org.live.module.capture.view.CaptureView;

import java.lang.reflect.Field;

/**
 * 录屏浮动按钮视图
 * Created by KAM on 2017/3/13.
 */

public class CaptureFABView extends LinearLayout implements CaptureView {
    private NoDoubleClickListener onClickListener;
    private View view;
    private Context context;
    public MaterialIconView cPrivateLockFAB;
    /**
     * 当前浮窗的宽度
     */
    public static int viewWidth;

    /**
     * 当前悬浮窗的高度
     */
    public static int viewHeight;

    /**
     * 记录系统状态栏的高度
     */
    private static int statusBarHeight;

    /**
     * 录屏浮窗视图参数
     */
    private static WindowManager.LayoutParams captureFABViewParams = null;

    /**
     * 用于更新悬浮窗的位置
     */
    private WindowManager windowManager;

    /**
     * 记录当前手指位置在屏幕上的横坐标值
     */
    private float xInScreen;

    /**
     * 记录当前手指位置在屏幕上的纵坐标值
     */
    private float yInScreen;

    /**
     * 记录手指按下时在小悬浮窗的View上的横坐标的值
     */
    private float xInView;

    /**
     * 记录手指按下时在小悬浮窗的View上的纵坐标的值
     */
    private float yInView;

    public CaptureFABView(Context context, NoDoubleClickListener onClickListener) {
        super(context);
        this.context = context;
        view = LayoutInflater.from(context).inflate(R.layout.fab_capture, this); // 填充布局
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        this.onClickListener = onClickListener;
        initUIElements();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 手指按下时记录必要数据,纵坐标的值都需要减去状态栏高度
                xInView = event.getX();
                yInView = event.getY();
                xInScreen = event.getRawX();
                yInScreen = event.getRawY() - getStatusBarHeight();
                break;
            case MotionEvent.ACTION_MOVE:
                xInScreen = event.getRawX();
                yInScreen = event.getRawY() - getStatusBarHeight();
                // 手指移动的时候更新小悬浮窗的位置
                updateViewPosition();
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * 更新小悬浮窗在屏幕中的位置。
     */
    private void updateViewPosition() {
        captureFABViewParams.x = (int) (xInScreen - xInView);
        captureFABViewParams.y = (int) (yInScreen - yInView);
        windowManager.updateViewLayout(this, captureFABViewParams);
    }

    /**
     * 初始化控件
     */
    private void initUIElements() {
        View thisView = view.findViewById(R.id.ll_capture_fab);
        thisView.setAlpha(0.7f);
        LinearLayout thisLinearLayout = (LinearLayout) findViewById(R.id.ll_capture_fab_frame);
        thisLinearLayout.getBackground().setAlpha(50);

        cPrivateLockFAB = (MaterialIconView) findViewById(R.id.fab_capture_lock);
        for (int i = 0; i < thisLinearLayout.getChildCount(); i++) {
            View childView = thisLinearLayout.getChildAt(i);
            if (childView instanceof MaterialIconView) {
                childView.getBackground().setAlpha(30); // 设置透明度
                childView.setOnClickListener(onClickListener); // 绑定按钮点击事件监听
            }
        }
        viewWidth = thisView.getLayoutParams().width;
        viewHeight = thisView.getLayoutParams().height;
    }

    /**
     * 设置当前视图参数
     *
     * @param captureFABViewParams
     */
    public void setCaptureFABViewParams(WindowManager.LayoutParams captureFABViewParams) {
        this.captureFABViewParams = captureFABViewParams;
    }

    /**
     * 用于获取状态栏的高度。
     *
     * @return 返回状态栏高度的像素值。
     */
    private int getStatusBarHeight() {
        if (statusBarHeight == 0) {
            try {
                Class<?> c = Class.forName("com.android.internal.R$dimen");
                Object o = c.newInstance();
                Field field = c.getField("status_bar_height");
                int x = (Integer) field.get(o);
                statusBarHeight = getResources().getDimensionPixelSize(x);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return statusBarHeight;
    }

}
