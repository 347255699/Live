package org.live.module.home.model;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.AsyncHttpGet;
import com.koushikdutta.async.http.AsyncHttpPost;
import com.koushikdutta.async.http.AsyncHttpRequest;
import com.koushikdutta.async.http.AsyncHttpResponse;
import com.koushikdutta.async.http.BasicNameValuePair;
import com.koushikdutta.async.http.NameValuePair;
import com.koushikdutta.async.http.body.UrlEncodedFormBody;

import org.live.common.constants.LiveConstants;
import org.live.common.util.JsonUtils;
import org.live.common.util.ResponseModel;
import org.live.common.util.SimpleResponseModel;
import org.live.module.home.constants.HomeConstants;
import org.live.module.home.domain.LiveRoomVo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wang on 2017/4/21.
 */

public class LiveRoomModel {

    public static final String TAG = "HOME" ;

    protected final String url = LiveConstants.REMOTE_SERVER_HTTP_IP +"/app/liveroom" ;

    /**
     * 用户关注的直播间的url
     */
    protected final String attentionLiveRoomUrl = LiveConstants.REMOTE_SERVER_HTTP_IP +"/app/user/liveroom" ;

    protected final String searchLiveRoomUrl =  LiveConstants.REMOTE_SERVER_HTTP_IP + "/app/liveroom/search" ;

    /**
     * 查看用户在该直播间的限制
     */
    protected final String liveRoomLimitationUrl = LiveConstants.REMOTE_SERVER_HTTP_IP + "/app/liveroom/limit" ;

    private Context context ;

    private Handler handler ;

    public LiveRoomModel(Context context, Handler handler) {
        this.context = context ;
        this.handler = handler ;
    }

    /**
     * 加载直播间的数据
     */
    public void loadLiveRoomData() {
        AsyncHttpRequest request = new AsyncHttpRequest(Uri.parse(url), "GET") ;
        requestLiveRoomData(request) ;
    }

    /**
     * 根据直播分类id加载数据
     * @param categoryId
     */
    public void loadLiveRoomDataByCategoryId(String categoryId) {

        AsyncHttpGet request = new AsyncHttpGet(url+"?categoryId="+categoryId) ;
        requestLiveRoomData(request) ;
    }

    /**
     * 加载用户关注的直播间
     */
    public void loadAttentionLiveRoomByUserId(String userId) {

        AsyncHttpGet request = new AsyncHttpGet(attentionLiveRoomUrl+"?userId="+userId) ;
        requestLiveRoomData(request) ;
    }

    /**
     * 搜索直播间
     * @param searchStr 搜索条件
     */
    public void loadSearchLiveRoomData(String searchStr) {
        AsyncHttpPost request = new AsyncHttpPost(searchLiveRoomUrl) ;

        List<NameValuePair> params = new ArrayList<>(2) ;
        params.add(new BasicNameValuePair("searchStr", searchStr)) ;
        UrlEncodedFormBody requestBody = new UrlEncodedFormBody(params) ;
        request.setBody(requestBody) ;

        requestLiveRoomData(request) ;
    }

    /**
     * 查看直播间的限制
     * @param userId
     * @param liveRoomId
     */
    public void loadLiveRoomLimitations(String userId, String liveRoomId) {
        AsyncHttpGet request = new AsyncHttpGet(liveRoomLimitationUrl+ "?userId=" + userId + "&liveRoomId=" + liveRoomId) ;
        AsyncHttpClient.getDefaultInstance().executeString(request, new AsyncHttpClient.StringCallback(){
            @Override
            public void onCompleted(Exception e, AsyncHttpResponse source, String result) {
                Message message = Message.obtain() ;
                if(e != null) {
                    Log.e(TAG, e.getMessage()) ;
                    message.what = HomeConstants.LOAD_LIMITATION_EXCEPTION_FLAG ;
                    handler.sendMessage(message) ;
                    return ;
                }
                ResponseModel<List<Integer>> dataModel = JsonUtils.fromJson(result, new TypeToken<SimpleResponseModel<List<Integer>>>(){}.getType()) ;
                if(dataModel == null) {
                    dataModel = new SimpleResponseModel<List<Integer>>() ;
                }
                message.obj = dataModel ;
                message.what = HomeConstants.LOAD_LIMITATION_SUCCESS_FLAG ;
                handler.sendMessage(message) ;
            }
        }) ;
    }


    private void requestLiveRoomData(AsyncHttpRequest request) {
        AsyncHttpClient.getDefaultInstance().executeString(request, new AsyncHttpClient.StringCallback() {
            @Override
            public void onCompleted(Exception e, AsyncHttpResponse source, String result) {
                Message message = Message.obtain() ;
                if(e != null) {
                    Log.e(TAG, e.getMessage()) ;
                    message.what = HomeConstants.LOAD_LIVE_ROOM_EXCEPTION_FLAG ;
                    handler.sendMessage(message) ;
                    return ;
                }
                Log.d(TAG, "result-->" + result) ;
                ResponseModel<List<LiveRoomVo>> dataModel = JsonUtils.fromJson(result, new TypeToken<SimpleResponseModel<List<LiveRoomVo>>>(){}.getType()) ;
                if(dataModel == null) {
                    dataModel = new SimpleResponseModel<List<LiveRoomVo>>() ;
                }
                message.obj = dataModel ;
                message.what = HomeConstants.LOAD_LIVE_ROOM_SUCCESS_FLAG ;
                handler.sendMessage(message) ;
            }
        }) ;
    }


}
