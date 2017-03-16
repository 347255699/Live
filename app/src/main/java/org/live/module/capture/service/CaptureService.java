package org.live.module.capture.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import org.live.R;
import org.live.common.constants.LiveKeyConstants;
import org.live.module.capture.listener.OnCaptureServiceStatusListener;
import org.live.module.capture.presenter.CapturePresenter;
import org.live.module.capture.presenter.impl.CapturePresenterImpl;
import org.live.module.capture.util.constant.CaptureConstant;
import org.live.module.capture.view.impl.CaptureActivity;

/**
 * 录屏服务
 * Created by KAM on 2017/3/13.
 */

public class CaptureService extends Service {
    private static final String TAG = "Global";
    public static final int NOTIFICATION_ID = "CaptureService".hashCode(); // 录屏通知栏唯一标识
    public static boolean isCapturing = false; // 是否在录屏直播
    public NotificationManager cNm; // 通知栏管理
    public Notification cN; // 录屏通知栏
    private String rtmpUrl = null; // 推流地址
    private CapturePresenter presenter;
    private OnCaptureServiceStatusListener serviceStatusListener; // 服务停止回掉接口

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        rtmpUrl = intent.getStringExtra(LiveKeyConstants.Global_URL_KEY); // 获取推流地址
        initNotification(); // 初始化通知栏
        captureHandler.post(new CaptureThread(this)); // 开始录屏直播
        return new CaptureBinder();
    }

    @Override
    public void onDestroy() {
        CaptureService.isCapturing = false; // 置换录屏状态
        presenter.stopScreenCaptureAndPublish(); // 关闭录屏直播
        /** 释放先关资源 **/
        super.onDestroy();
    }

    /**
     * 设置隐私模式
     */
    public void triggerPrivateMode(boolean isPrivateMode) {
        presenter.triggerPrivateMode(isPrivateMode);
    }

    /**
     * 初始化通知栏
     */
    private void initNotification() {
        cNm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        // 点击通知栏产生的 意图
        cN = builder.setContentTitle("录屏直播")//设置通知栏标题
                .setContentText("正在录屏直播...")
                .setContentIntent(getDefalutIntent(Notification.FLAG_AUTO_CANCEL)) //设置通知栏点击意图
                //  .setNumber(number) //设置通知集合的数量
                .setTicker("开始录屏") //通知首次出现在通知栏，带上升动画效果的
                .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
                .setPriority(Notification.PRIORITY_DEFAULT) //设置该通知优先级
//  .setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消
                .setOngoing(true)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
                .setDefaults(Notification.DEFAULT_VIBRATE)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合
                //Notification.DEFAULT_ALL  Notification.DEFAULT_SOUND 添加声音 // requires VIBRATE permission
                .setSmallIcon(R.mipmap.ic_launcher).build();//设置通知小ICON
        cN.flags = Notification.FLAG_ONGOING_EVENT;
    }

    /**
     * 通知栏意图
     *
     * @param flags
     * @return
     */
    private PendingIntent getDefalutIntent(int flags) {
        PendingIntent pendingIntent = PendingIntent.getActivity(this, NOTIFICATION_ID, new Intent(this, CaptureActivity.class), flags);
        return pendingIntent;
    }

    /**
     * 处理录屏直播服务状态信息
     */
    private Handler captureHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.arg1) {
                case CaptureConstant.CAPTURE_STATUS_SERVICE_CLOSE_NORMAL:
                    serviceStatusListener.onServiceStop(true); // 通知activity录屏服务已关闭
                    cNm.cancel(NOTIFICATION_ID); // 清除通知
                    Log.i(TAG, "service close normal");
                    break;
                case CaptureConstant.CAPTURE_STATUS_SERVICE_START_NORMAL:
                    isCapturing = true; // 置换录屏状态
                    serviceStatusListener.onServiceStart(); // 通知activity录屏已开始
                    cNm.notify(NOTIFICATION_ID, cN); // 发送通知
                    Log.i(TAG, "service start");
                    break;
                case CaptureConstant.CAPTURE_STATUS_SERVICE_NET_BUSY:
                    serviceStatusListener.onServiceNetBusy(); // 通知activity网络质量差
                    Log.i(TAG, "net busy");
                    break;
                case CaptureConstant.CAPTURE_STATYS_SERVICE_CLOSE_ABNORMALITY:
                    serviceStatusListener.onServiceStop(false); // 通知activity网络断开
                    cNm.cancel(NOTIFICATION_ID); // 清除通知
                    Log.i(TAG, "service close abnormality");
                    break;
                case CaptureConstant.CAPTURE_SERVICE_IS_PRIVATE_MODE_FALSE:
                    serviceStatusListener.onPrivateModeStatus(false);
                    break;
                case CaptureConstant.CAPTURE_SERVICE_IS_PRIVATE_MODE_TRUE:
                    serviceStatusListener.onPrivateModeStatus(true);
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 录屏子线程
     */
    private class CaptureThread implements Runnable {
        private Context context;

        public CaptureThread(Context context) {
            this.context = context;
        }

        @Override
        public void run() {
            presenter = new CapturePresenterImpl(context, captureHandler);
            presenter.startScreenCaptureAndPublish(rtmpUrl); // 开始录屏直播
        }
    }

    /**
     * 录屏Binder
     */
    public class CaptureBinder extends Binder {
        /**
         * 获取当前Service的实例
         *
         * @return
         */
        public CaptureService getCaptureService() {
            return CaptureService.this;
        }
    }

    /**
     * 设置服务状态回掉
     *
     * @param serviceStatusListener
     */
    public void setServiceStatusListener(OnCaptureServiceStatusListener serviceStatusListener) {
        this.serviceStatusListener = serviceStatusListener;
    }
}
