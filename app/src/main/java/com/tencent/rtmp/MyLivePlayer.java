package com.tencent.rtmp;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;

import com.tencent.rtmp.audio.TXAudioPlayer;
import com.tencent.rtmp.player.TXFFPlayer;
import com.tencent.rtmp.player.i;
import com.tencent.rtmp.player.l;
import com.tencent.rtmp.player.o;
import com.tencent.rtmp.ui.TXCloudVideoView;

/**
 * 腾讯你这个狗娘养的。
 *
 * linkmicStartPlay,getAccelerateStreamPlayUrl方法都有相关代码注释，这些方法的业务都是跟连麦有关的。
 *
 * Created by Mr.wang on 2017/3/13.
 */

public class MyLivePlayer implements TXRtmpApi.c {

    public static final String TAG = "TXLivePlayer";
    public static final String ON_RECEIVE_VIDEO_FRAME = "com.tencent.rtmp.TXLivePlayer.OnReceiveVideoFrame";
    public static final int PLAY_TYPE_LIVE_RTMP = 0;
    public static final int PLAY_TYPE_LIVE_FLV = 1;
    public static final int PLAY_TYPE_VOD_FLV = 2;
    public static final int PLAY_TYPE_VOD_HLS = 3;
    public static final int PLAY_TYPE_VOD_MP4 = 4;
    public static final int PLAY_TYPE_LIVE_RTMP_ACC = 5;
    private TXAudioPlayer mAudioPlayer;
    private l mMediaPlayer;
    private TXCloudVideoView mVideoView;
    private ITXLivePlayListener mListener;
    private Context mApplicationContext;
    private Handler mHandler;
    private TXLivePlayConfig mConfig;
    private boolean mEnableHWDec = false;
    private boolean mIsNeedClearLastImg = true;
    private int mRenderMode;
    private int mRenderRotation;
    private String mPlayUrl = "";
    private boolean mMute = false;
    private static final String CGI_FETCH_LINKMIC_STREAMINFO = "https://livepull.myqcloud.com/getstreaminfos ";
    private static final String CGI_FETCH_LINKMIC_STREAMINFO_TEST = "https://livepulltest.myqcloud.com/getstreaminfos";

    public void setConfig(TXLivePlayConfig var1) {
        TXRtmpApi.checkCallingThread();
        this.mConfig = var1;
        if(this.mConfig == null) {
            this.mConfig = new TXLivePlayConfig();
        }

        if(this.mPlayUrl.length() > 0) {
            TXRtmpApi.setPlayConfig(this.mPlayUrl, this.mConfig);
        }

    }

    public static int[] getSDKVersion() {
        TXRtmpApi.checkCallingThread();
        return TXRtmpApi.getSDKVersion();
    }

    public MyLivePlayer(Context var1) {
        if(var1 != null) {
            this.mApplicationContext = var1.getApplicationContext();
            this.mHandler = new Handler();
            TXRtmpApi.initAudioEngine(var1);
        }

        this.mListener = null;
        TXRtmpApi.setTempPath(Environment.getExternalStorageDirectory().getAbsolutePath());
    }

    public void setPlayerView(TXCloudVideoView var1) {
        TXRtmpApi.checkCallingThread();
        this.mVideoView = var1;
        if(this.mMediaPlayer != null) {
            this.mMediaPlayer.setVideoView(this.mVideoView);
        }

    }

    public int startPlay(String var1, int var2) {
        TXRtmpApi.checkCallingThread();
        TXRtmpApi.initCrashReport(this.mApplicationContext);
        TXRtmpApi.setDeviceInfo(var1, this.mApplicationContext);
        if(5 == var2) {
            this.mPlayUrl = var1;
            return this.linkmicStartPlay(var1); //  连麦
        } else {
            return this.normalStartPlay(var1, var2);
        }
    }

    /**
     *  去掉rtmp地址的参数
     * @param var1
     * @param var2
     * @return
     */
    private int normalStartPlay(String var1, int var2) {
        if(var1 != null && !TextUtils.isEmpty(var1)) {
        /*    if(var1.indexOf("?") != -1) {
                var1 = var1 + "&txPlayerId=" + System.currentTimeMillis();
            } else {
                var1 = var1 + "?txPlayerId=" + System.currentTimeMillis();
            }*/

            try {
                byte[] var3 = var1.getBytes("UTF-8");
                StringBuilder var4 = new StringBuilder(var3.length);

                for(int var5 = 0; var5 < var3.length; ++var5) {
                    int var6;
                    if((var6 = var3[var5] < 0?var3[var5] + 256:var3[var5]) > 32 && var6 < 127 && var6 != 34 && var6 != 37 && var6 != 60 && var6 != 62 && var6 != 91 && var6 != 125 && var6 != 92 && var6 != 93 && var6 != 94 && var6 != 96 && var6 != 123 && var6 != 124) {
                        var4.append((char)var6);
                    } else {
                        var4.append(String.format("%%%02X", new Object[]{Integer.valueOf(var6)}));
                    }
                }

                var1 = var4.toString();
            } catch (Exception var7) {
                var7.printStackTrace();
            }

            var1 = var1.trim();
            TXRtmpApi.setTempPath(Environment.getExternalStorageDirectory().getAbsolutePath());
            this.stopPlay(this.mIsNeedClearLastImg);
            TXLog.d("TXLivePlayer", "===========================================================================================================================================================");
            TXLog.d("TXLivePlayer", "===========================================================================================================================================================");
            TXLog.d("TXLivePlayer", "=====  StartPlay url = " + var1 + " playType = " + var2 + "    ======");
            TXLog.d("TXLivePlayer", "===========================================================================================================================================================");
            TXLog.d("TXLivePlayer", "===========================================================================================================================================================");
            if(var2 == 0) {
                this.mMediaPlayer = new o(this.mApplicationContext, this.mEnableHWDec);
            } else if(var2 != 1 && var2 != 2) {
                if(var2 != 3 && var2 != 4) {
                    return -3;
                }

                this.mMediaPlayer = new TXFFPlayer(this.mApplicationContext, this.mEnableHWDec);
            } else {
                this.mMediaPlayer = new i(this.mApplicationContext, this.mEnableHWDec);
            }

            if(this.mMediaPlayer == null) {
                return -4;
            } else {
                this.mPlayUrl = var1;
                if(this.mConfig != null) {
                    this.mMediaPlayer.setConnectRetryCount(this.mConfig.mConnectRetryCount);
                    this.mMediaPlayer.setConnectRetryInterval(this.mConfig.mConnectRetryInterval);
                }

                if(this.mListener != null) {
                    this.mMediaPlayer.setPlayListener(this.mListener);
                } else {
                    this.mMediaPlayer.setPlayListener((ITXLivePlayListener)null);
                }

                if(this.mVideoView != null) {
                    this.mMediaPlayer.setVideoView(this.mVideoView);
                }

                this.mMediaPlayer.setRenderRotation(this.mRenderRotation);
                this.mMediaPlayer.setRenderMode(this.mRenderMode);
                this.mMediaPlayer.setPlayType(var2);
                this.mMediaPlayer.start(var1);
                this.mMediaPlayer.setPlayListener(this.mListener);
                this.mMediaPlayer.setHWDec(this.mEnableHWDec);
                this.mMediaPlayer.setMute(this.mMute);
                TXRtmpApi.setPlayConfig(this.mPlayUrl, this.mConfig);
                TXRtmpApi.addRtmpDataListener(this.mPlayUrl, this);
                this.setPlayListener(this.mListener);
                return 0;
            }
        } else {
            return -1;
        }
    }

    public int stopPlay(boolean var1) {
        TXRtmpApi.checkCallingThread();
        this.mIsNeedClearLastImg = var1;
        TXRtmpApi.delRtmpDataListener(this.mPlayUrl);
        TXRtmpApi.delPlayListener(this.mPlayUrl);
        if(this.mAudioPlayer != null) {
            this.mAudioPlayer.stop();
            this.mAudioPlayer = null;
        }

        if(this.mMediaPlayer != null) {
            this.mMediaPlayer.clearLastFrame(var1);
            this.mMediaPlayer.stop();
            this.mMediaPlayer = null;
        }

        this.mPlayUrl = "";
        return 0;
    }

    public boolean isPlaying() {
        TXRtmpApi.checkCallingThread();
        return this.mMediaPlayer != null?this.mMediaPlayer.isPlaying() && TXRtmpApi.isPlaying(this.mPlayUrl):false;
    }

    public void pause() {
        TXRtmpApi.checkCallingThread();
        if(this.mMediaPlayer != null) {
            this.mMediaPlayer.pause();
        }

    }

    public void resume() {
        TXRtmpApi.checkCallingThread();
        if(this.mMediaPlayer != null) {
            this.mMediaPlayer.resume();
        }

    }

    public void seek(int var1) {
        TXRtmpApi.checkCallingThread();
        if(this.mMediaPlayer != null) {
            this.mMediaPlayer.seek((long)var1);
        }

    }

    public void setPlayListener(ITXLivePlayListener var1) {
        TXRtmpApi.checkCallingThread();
        this.mListener = var1;
        if(this.mMediaPlayer != null) {
            this.mMediaPlayer.setPlayListener(this.mListener);
        }

    }

    public void setRenderMode(int var1) {
        TXRtmpApi.checkCallingThread();
        this.mRenderMode = var1;
        if(this.mVideoView != null) {
            this.mVideoView.setRenderMode(var1);
            this.mVideoView.refreshLastFrame();
        }

    }

    public void setRenderRotation(int var1) {
        TXRtmpApi.checkCallingThread();
        this.mRenderRotation = var1;
        if(this.mVideoView != null) {
            this.mVideoView.setRenderRotation(var1);
            this.mVideoView.refreshLastFrame();
        }

    }

    public boolean enableHardwareDecode(boolean var1) {
        TXRtmpApi.checkCallingThread();
        if(var1) {
            if(Build.VERSION.SDK_INT < 18) {
                TXLog.e("HardwareDecode", "enableHardwareDecode failed, android system build.version = " + Build.VERSION.SDK_INT + ", the minimum build.version should be 18(android 4.3 or later)");
                return false;
            }

            if(this.isAVCDecBlacklistDevices()) {
                TXLog.e("HardwareDecode", "enableHardwareDecode failed, MANUFACTURER = " + Build.MANUFACTURER + ", MODEL" + Build.MODEL);
                return false;
            }
        }

        this.mEnableHWDec = var1;
        if(this.mMediaPlayer != null) {
            this.mMediaPlayer.setHWDec(this.mEnableHWDec);
        }

        return true;
    }

    public void setMute(boolean var1) {
        TXRtmpApi.checkCallingThread();
        this.mMute = var1;
        if(this.mMediaPlayer != null) {
            this.mMediaPlayer.setMute(var1);
        }

        if(this.mAudioPlayer != null) {
            this.mAudioPlayer.setMute(var1);
        }

    }

    public void onVideoData(String var1, byte[] var2, int var3, int var4, int var5, int var6, long var7) {
        if(var1 != null && this.mPlayUrl != null && var1.equalsIgnoreCase(this.mPlayUrl) && this.mMediaPlayer != null) {
            this.mMediaPlayer.onVideoData(var2, var3, var4, var5, var6, var7);
        }

    }

    public void onPcmData(String var1, byte[] var2, int var3, int var4, long var5) {
        if(var1 != null && var1.equalsIgnoreCase("TXRTMPSDK_AUDIO_PCMSOURCE_LOCALMERGER") || var1 != null && this.mPlayUrl != null && var1.equalsIgnoreCase(this.mPlayUrl)) {
            if(this.mAudioPlayer == null) {
                this.mAudioPlayer = new TXAudioPlayer();
                this.mAudioPlayer.setAudioParam(var3, var4, 16);
                this.mAudioPlayer.start(this.mPlayUrl);
                this.mAudioPlayer.setMute(this.mMute);
            }

            try {
                if(this.mAudioPlayer != null) {
                    this.mAudioPlayer.play(var2, var5);
                }

                return;
            } catch (Exception var7) {
                var7.printStackTrace();
            }
        }

    }

    public void onLogRecord(String var1) {
        TXLog.d("TXLivePlayer", var1);
    }

    private boolean isAVCDecBlacklistDevices() {
        return Build.MANUFACTURER.equalsIgnoreCase("HUAWEI") && Build.MODEL.equalsIgnoreCase("Che2-TL00");
    }

    /**
     *  这里注释了代码 return !this.getAccelerateStreamPlayUrl(Integer.valueOf(var4).intValue(), var3, var2, new b(this, var1, var5, var4, var3))?-1:0;
     * @param var1
     * @return
     */
    private int linkmicStartPlay(String var1) {
        String var2;
        if((var2 = this.getStreamIDByStreamUrl(var1)) == null) {
            TXLog.e("TXLivePlayer", "startPlay: invalid playUrl, without streamID");
            return -1;
        } else {
            String var3;
            if((var3 = this.getParamsFromStreamUrl("session_id", var1)) == null) {
                TXLog.e("TXLivePlayer", "startPlay: invalid playUrl, without sessionID");
                return -1;
            } else {
                String var4 = this.getParamsFromStreamUrl("bizid", var1);
                String var5 = this.getParamsFromStreamUrl("txTime", var1);
                var1 = this.getParamsFromStreamUrl("txSecret", var1);
                if(var4 != null && var5 != null && var1 != null) {
                    return -1 ;
                    //return !this.getAccelerateStreamPlayUrl(Integer.valueOf(var4).intValue(), var3, var2, new b(this, var1, var5, var4, var3))?-1:0;
                } else {
                    TXLog.e("TXLivePlayer", "startPlay: invalid playUrl, without signature");
                    return -1;
                }
            }
        }
    }

    /**
     *  这里注释了代码   (new com.tencent.rtmp.c(this, var1, var6, var5, var7, var2, var3, var4)).start();
     * @param var1
     * @param var2
     * @param var3
     * @param var4
     * @return
     */
    private boolean getAccelerateStreamPlayUrl(int var1, String var2, String var3, MyLivePlayer.a var4) {
        if(var2 != null && var2.length() != 0) {
            if(var3 != null && var3.length() != 0) {
                String var5;
                if((var5 = TXRtmpApi.getCurrentPushUrl()) != null && var5.length() != 0) {
                    String var6;
                    if((var6 = this.getStreamIDByStreamUrl(var5)) != null && var6.length() != 0) {
                        String var7;
                        if((var7 = this.getParamsFromStreamUrl("txTime", var5)) != null && var7.length() != 0) {
                            if((var5 = this.getParamsFromStreamUrl("txSecret", var5)) != null && var5.length() != 0) {

                             //   (new com.tencent.rtmp.c(this, var1, var6, var5, var7, var2, var3, var4)).start();
                                return true;
                            } else {
                                TXLog.e("TXLivePlayer", "getAccelerateStreamPlayUrl: current txSecret is invalid");
                                return false;
                            }
                        } else {
                            TXLog.e("TXLivePlayer", "getAccelerateStreamPlayUrl: current txTime is invalid");
                            return false;
                        }
                    } else {
                        TXLog.e("TXLivePlayer", "getAccelerateStreamPlayUrl: current streamID is invalid");
                        return false;
                    }
                } else {
                    TXLog.e("TXLivePlayer", "getAccelerateStreamPlayUrl: current push url is invalid");
                    return false;
                }
            } else {
                TXLog.e("TXLivePlayer", "getAccelerateStreamPlayUrl: invalid streamID");
                return false;
            }
        } else {
            TXLog.e("TXLivePlayer", "getAccelerateStreamPlayUrl: invalid sessionID");
            return false;
        }
    }

    private String getStreamIDByStreamUrl(String var1) {
        if(var1 != null && var1.length() != 0) {
            var1 = var1.toLowerCase();
            String var2 = "/live/";
            int var3;
            String[] var4;
            return (var3 = var1.indexOf(var2)) == -1?null:((var4 = var1.substring(var3 + var2.length()).split("[?.]")).length > 0?var4[0]:null);
        } else {
            return null;
        }
    }

    private String getParamsFromStreamUrl(String var1, String var2) {
        if(var1 != null && var1.length() != 0 && var2 != null && var2.length() != 0) {
            var1 = var1.toLowerCase();
            String[] var7;
            int var3 = (var7 = var2.split("[?&]")).length;

            for(int var4 = 0; var4 < var3; ++var4) {
                String var5;
                String[] var8;
                if((var5 = var7[var4]).indexOf("=") != -1 && (var8 = var5.split("[=]")).length == 2) {
                    String var6 = var8[0];
                    var5 = var8[1];
                    if(var6 != null && var6.toLowerCase().equalsIgnoreCase(var1)) {
                        return var5;
                    }
                }
            }

            return null;
        } else {
            return null;
        }
    }

    private int getPlayType(String var1) {
        if(var1.startsWith("rtmp://")) {
            return 0;
        } else if((var1.startsWith("http://") || var1.startsWith("https://")) && var1.contains(".flv")) {
            return 1;
        } else {
            TXLog.e("TXLivePlayer", "播放地址不合法，直播目前仅支持rtmp,flv播放方式!");
            return -1;
        }
    }

    private interface a {
        void a(String var1);
    }
}
