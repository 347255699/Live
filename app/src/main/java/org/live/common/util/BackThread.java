package org.live.common.util;


import android.app.Instrumentation;
import android.util.Log;
import android.view.KeyEvent;

/**
 * 返回键点击线程
 * Created by KAM on 2017/4/21.
 */
public class BackThread extends Thread {
    public void run() {       //这个方法是不能写在你的主线程里面的，所以你要自己开个线程用来执行
        Instrumentation inst = new Instrumentation();

        try {
            inst.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
        } catch (Exception e) {
            Log.e("Global", e.getMessage());
        }

    }
}

