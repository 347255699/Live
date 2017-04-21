package org.live.module.anchor.model.impl;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.AsyncHttpGet;
import com.koushikdutta.async.http.AsyncHttpPost;
import com.koushikdutta.async.http.AsyncHttpPut;
import com.koushikdutta.async.http.AsyncHttpResponse;
import com.koushikdutta.async.http.body.MultipartFormDataBody;

import org.json.JSONObject;
import org.live.common.constants.LiveConstants;
import org.live.common.constants.RequestMethod;
import org.live.common.util.HttpRequestUtils;
import org.live.common.util.JsonUtils;
import org.live.common.util.SimpleResponseModel;
import org.live.module.anchor.constants.AnchorConstants;
import org.live.module.anchor.listener.ApplyAnchorModelListener;
import org.live.module.anchor.model.ApplyAnchorModel;
import org.live.module.home.constants.HomeConstants;
import org.live.module.home.domain.LiveCategoryVo;
import org.live.module.login.util.Validator;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 申请主播逻辑处理层实现
 * Created by KAM on 2017/4/21.
 */

public class ApplyAnchorModelImpl implements ApplyAnchorModel {
    private Context context;
    private ApplyAnchorModelListener modelListener;

    public ApplyAnchorModelImpl(Context context, ApplyAnchorModelListener modelListener) {
        this.context = context;
        this.modelListener = modelListener;
    }

    /**
     * 校验输入项
     *
     * @param labels
     * @param valsMap
     * @return
     */
    @Override
    public boolean validateInputItem(Map<String, String> labels, Map<String, Object> valsMap) {
        Map<String, Map<String, Object>> rules = new HashMap<String, Map<String, Object>>(); // 校验规则组
        Map<String, String> vals = new LinkedHashMap<>();
        for (String key : labels.keySet()) {
            vals.put(key, (String) valsMap.get(key));
            Map<String, Object> rule = new LinkedHashMap<String, Object>(); // 校验规则
            switch (key) {
                case "file":
                    rule.put("required", true);
                    break;
                case "idCard":
                    rule.put("required", true);
                    rule.put("matchRegEx", "^\\d{15}|\\d{18}$");
                    break;
                case "categoryId":
                    rule.put("required", true);
                    break;
                case "realName":
                    rule.put("required", true);
                default:
                    break;
            }
            rules.put(key, rule);
        } // 构建校验规则
        Validator validator = new Validator(context);
        return validator.validate(vals, labels, rules); // 开启校验
    }

    /**
     * 上传主播信息
     *
     * @param params
     */
    @Override
    public void postAnchorInfo(Map<String, Object> params) {
        AsyncHttpPost post = new AsyncHttpPost(LiveConstants.REMOTE_SERVER_HTTP_IP + "/app/applyAnchor");
        MultipartFormDataBody body = new MultipartFormDataBody();
        body.addFilePart("file", new File((String) params.get("file"))); // 添加文件
        for (String key : params.keySet()) {
            Object val = params.get(key);
            body.addStringPart(key, (String) val);
        } // 构建参数
        post.setBody(body);
        AsyncHttpClient.getDefaultInstance().executeString(post, new AsyncHttpClient.StringCallback() {
            @Override
            public void onCompleted(Exception ex, AsyncHttpResponse source, String result) {
                if (ex != null) {
                    Log.e("Global", ex.getMessage());
                    return;
                }
                Message message = responseHandler.obtainMessage(AnchorConstants.HTTP_RESPONSE_RESULT_POST_APPLY_ANCHOR_FORM_CODE);
                message.obj = result;
                responseHandler.sendMessage(message);
            }
        }); // 提交表单
    }

    /**
     * 请求分类列表数据
     */
    @Override
    public void requestCategoryList() {
        AsyncHttpGet get = new AsyncHttpGet(LiveConstants.REMOTE_SERVER_HTTP_IP + "/app/category");
        AsyncHttpClient.getDefaultInstance().executeString(get, new AsyncHttpClient.StringCallback() {
            @Override
            public void onCompleted(Exception ex, AsyncHttpResponse source, String result) {
                if (ex != null) {
                    Log.e("Global", ex.getMessage());
                    return;
                }
                Message message = responseHandler.obtainMessage(AnchorConstants.HTTP_RESPONSE_RESULT_REQUEST_CATEGORY_CODE);
                message.obj = result;
                responseHandler.sendMessage(message);
            }
        });
    }

    /**
     * 请求处理
     */
    Handler responseHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == AnchorConstants.HTTP_RESPONSE_RESULT_REQUEST_CATEGORY_CODE) {
                String result = (String) msg.obj;
                if (result != null) {
                    SimpleResponseModel model = JsonUtils.fromJson(result, new TypeToken<SimpleResponseModel<List<LiveCategoryVo>>>() {
                    }.getType());
                    if (model.getStatus() == 1) {
                        List<LiveCategoryVo> liveCategoryVos = (List<LiveCategoryVo>) model.getData();
                        modelListener.showCategoryList(liveCategoryVos); // 显示列表
                    } else {
                        modelListener.showToast(model.getMessage()); // 提示消息
                    }
                } else {
                    modelListener.showToast("系统繁忙");
                }
            }
            if (msg.what == AnchorConstants.HTTP_RESPONSE_RESULT_POST_APPLY_ANCHOR_FORM_CODE) {
                String result = (String) msg.obj;
                if (result != null) {
                    SimpleResponseModel model = JsonUtils.fromJson(result, SimpleResponseModel.class);
                    if (model.getStatus() == 1) {
                        modelListener.showToast("提交成功，请耐心等待审核结果"); // 提示信息
                        modelListener.back(); // 返回
                    } else {
                        modelListener.showToast(model.getMessage());
                    }
                } else {
                    modelListener.showToast("系统繁忙");
                }
            }
        }
    };


}
