package org.live.module.capture.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;

import org.live.R;
import org.live.common.constants.LiveKeyConstants;
import org.live.common.listener.NoDoubleClickListener;
import org.live.module.capture.presenter.CapturePresenter;
import org.live.module.capture.presenter.impl.CapturePresenterImpl;
import org.live.module.capture.util.WindowManagerUtil;
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
    public static NotificationManager cNm = null; // 通知栏管理
    public static Notification cN = null; // 录屏通知栏
    private String rtmpUrl = null;
    private CapturePresenter presenter = null;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.rtmpUrl = intent.getStringExtra(LiveKeyConstants.Global_URL_KEY);
        this.isCapturing = true;
        initNotification(); // 初始化通知栏

        handler.post(new CaptureThread(this)); // 启动录屏服务
        //cNm.notify(NOTIFICATION_ID, cN); // 发送服务启动通知
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "CaptureService was destroyed.");
        cNm.cancel(NOTIFICATION_ID); // 取消通知
        CaptureService.isCapturing = false;
        WindowManagerUtil.removeCaptureFABView(getApplicationContext()); // 清除浮窗
        presenter.stopScreenCaptureAndPublish(); // 关闭录屏直播
        /** 释放先关资源 **/
        cNm = null;
        cN = null;
        presenter = null;

        startActivity(new Intent(this, CaptureActivity.class)); // 返回主页

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
     * 处理浮窗按钮事件
     */
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.arg1) {
                case CaptureConstant.CAPTURE_FAB_EVENT_CLOSE_SERVICE:

                    stopSelf(); // 关闭服务
                    break;
                case CaptureConstant.CAPTURE_FAB_EVENT_CAMERA_FRONT:
                    Log.i(TAG, "启动前置摄像头...");
                    break;
                case CaptureConstant.CAPTURE_FAB_EVENT_TO_HOME:
                    Log.i(TAG, "跳转至主页...");
                    break;
                case CaptureConstant.CAPTURE_FAB_EVENT_LOCK_PRIVATE:
                    Log.i(TAG, "开启隐私模式...");
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
            Message msg = handler.obtainMessage();
            WindowManagerUtil.createCaptureFABView(getApplicationContext(), new FABOnClickListener()); // 创建浮窗
            presenter = new CapturePresenterImpl(context, WindowManagerUtil.captureFABView);
            presenter.startScreenCaptureAndPublish(rtmpUrl); // 开始录屏直播
        }
    }

    /**
     * 防双击点击事件监听
     */
    public class FABOnClickListener extends NoDoubleClickListener {
        @Override
        protected void onNoDoubleClick(View v) {
            Message msg = handler.obtainMessage();
            switch (v.getId()) {
                case R.id.fab_capture_home:
                    msg.arg1 = CaptureConstant.CAPTURE_FAB_EVENT_TO_HOME;
                    break;
                case R.id.fab_capture_lock:
                    msg.arg1 = CaptureConstant.CAPTURE_FAB_EVENT_LOCK_PRIVATE;
                    break;
                case R.id.fab_capture_close:
                    msg.arg1 = CaptureConstant.CAPTURE_FAB_EVENT_CLOSE_SERVICE;
                    break;
                case R.id.fab_capture_camera_front:
                    msg.arg1 = CaptureConstant.CAPTURE_FAB_EVENT_CAMERA_FRONT;
                    break;
                default:
                    break;
            }
            handler.sendMessage(msg);
        }
    }
}
