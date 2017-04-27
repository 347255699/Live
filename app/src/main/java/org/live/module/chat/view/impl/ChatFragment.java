package org.live.module.chat.view.impl;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import org.live.R;
import org.live.common.domain.Message;
import org.live.common.domain.MessageType;
import org.live.common.domain.SimpleUserVo;
import org.live.common.listener.ChatActivityEvent;
import org.live.common.provider.AnchorInfoProvider;
import org.live.common.util.JsonUtils;
import org.live.common.util.ResponseModel;
import org.live.module.chat.service.AnchorChatService;
import org.live.module.home.constants.HomeConstants;
import org.live.module.home.presenter.LiveRoomPresenter;
import org.live.module.home.view.custom.UserInfoDialogView;
import org.live.module.home.view.impl.HomeActivity;
import org.live.module.login.domain.MobileUserVo;
import org.live.module.play.domain.LiveRoomInfo;
import org.live.module.publish.view.custom.NickNameSpan;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 主播聊天记录视图
 * Created by KAM on 2017/4/24.
 */

public class ChatFragment extends Fragment {
    private static final String TAG = "Global";
    private ListView cRecordsListView; // 记录列表
    private ChatFragment.ChatRecordAdapter adapter; // 聊天记录适配器
    private LinearLayout cUnreadMsgHintView; // 未读消息提示视图
    private TextView cUnreadMsgHintTextView; // 未读消息提示信息
    private int unReadMsgCount = 0; // 未读信息数量
    private boolean scrollBottomFlag = true; // 滚动条标记，判断滚动条是否在底部
    private LinearLayout cLastChatRecordView; // 最后一条消息记录视图
    private TextView cLastChatRecordFromUser; // 最后一条消息记录来自用户
    private View view; // 当前view
    private Typeface typeFace; // 字体样式

    private LiveRoomPresenter liveRoomPresenter;
    private MobileUserVo mobileUserVo;
    private AnchorInfoProvider anchorInfoProvider;
     private LiveRoomInfo liveRoomInfo;
    /**
     * 用户弹出框信息
     */
    private UserInfoDialogView userInfoDialogView;
    private ChatActivityEvent chatActivityEvent;
    private String userAccount; // 当前选中的用户账户，点击用户消息昵称
    private String userNickname; // 当前选中的用户昵称，点击用户消息昵称


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_chat2, container, false);
        this.mobileUserVo = HomeActivity.mobileUserVo;
        if (getActivity() instanceof ChatActivityEvent) {
            this.chatActivityEvent = (ChatActivityEvent) getActivity();
        }
        if (getActivity() instanceof AnchorInfoProvider) {
            this.anchorInfoProvider = (AnchorInfoProvider) getActivity();
        }
        this.liveRoomInfo = anchorInfoProvider.getLiveRoomInfo();
        typeFace = Typeface.createFromAsset(getActivity().getAssets(), "fonts/AndroidClockMono-Thin.ttf"); // 外部字体目录
        registerReceiver(); // 注册服务
        initUIElements();
        liveRoomPresenter = new LiveRoomPresenter(getActivity(), handler);
        return view;
    }

    /**
     * 初始化UI控件
     */
    private void initUIElements() {
        cRecordsListView = (ListView) view.findViewById(R.id.lv_chat_records);
        cUnreadMsgHintView = (LinearLayout) view.findViewById(R.id.ll_chat_hint);
        cUnreadMsgHintTextView = (TextView) view.findViewById(R.id.tv_chat_hint_msg);
        cLastChatRecordView = (LinearLayout) view.findViewById(R.id.ll_chat_record_last);
        cLastChatRecordFromUser = (TextView) view.findViewById(R.id.tv_chat_from_user_last);

        cUnreadMsgHintTextView.setTypeface(typeFace);
        cLastChatRecordFromUser.setTypeface(typeFace);

        adapter = new ChatFragment.ChatRecordAdapter(getActivity());
        cRecordsListView.setAdapter(adapter);

        /**
         * 绑定未读消息提示按钮
         */
        cUnreadMsgHintView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "提示消息被点击.. ");
                cRecordsListView.setSelection(adapter.getCount() - 1); // 设置listview滚动至底部
                cRecordsListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL); // 设自动滚动到底部
                scrollBottomFlag = true;
                unReadMsgCount = 0; // 清零未读消息数目
                cUnreadMsgHintView.setVisibility(View.GONE); // 清除未读消息提示视图
                cLastChatRecordView.setVisibility(View.VISIBLE); // 显示最后一条记录视图
            }
        });

        /**
         * 设置滚动监听
         */
        cRecordsListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    // 当不滚动时
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        // 判断滚动到底部
                        if (cRecordsListView.getLastVisiblePosition() == (cRecordsListView.getCount() - 1)) {
                            Log.i(TAG, "滚动到底部");
                            cRecordsListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL); // 设自动滚动到底部
                            scrollBottomFlag = true;
                            unReadMsgCount = 0; // 清零未读消息数目
                            cUnreadMsgHintView.setVisibility(View.GONE); // 清除未读消息提示视图
                            cLastChatRecordView.setVisibility(View.VISIBLE); // 显示最后一条记录视图
                        } else {
                            Log.i(TAG, "不是在底部");
                            cRecordsListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_NORMAL); // 取消自动滚动到底部
                            scrollBottomFlag = false;
                            cLastChatRecordView.setVisibility(View.GONE); // 清除最后一条记录视图
                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });

    }

    /**
     * 追加消息记录
     */
    private void addRecord(Map<String, String> item) {
        adapter.data.add(item); // 添加聊天记录
        adapter.notifyDataSetChanged(); // 更新视图

        if (!scrollBottomFlag) {
            unReadMsgCount++;
            cUnreadMsgHintTextView.setText("你有" + unReadMsgCount + "条消息");
            cUnreadMsgHintView.setVisibility(View.VISIBLE); // 显示视图
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(chatReceiver); // 注销广播接收器
    }

    /**
     * 添加最后一条消息记录
     */
    private void addLastRecord(String user) {
        cLastChatRecordFromUser.setText(user + "进入直播间");
    }


    /**
     * 注册服务
     */
    private void registerReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(AnchorChatService.ACTION); // 绑定意图
        filter.setPriority(Integer.MAX_VALUE); // 高优先级
        getActivity().registerReceiver(chatReceiver, filter); // 注册广播接收器
    }

    /**
     * 广播接收器
     */
    private BroadcastReceiver chatReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Message message = JsonUtils.fromJson(intent.getStringExtra("msg"), Message.class);
            switch (message.getMessageType()) {
                case MessageType.SEND_TO_CHATROOM_MESSAGE_TYPE:
                    Map<String, String> item = new HashMap<>(3);
                    item.put("record", message.getContent());
                    item.put("account", message.getAccount());
                    item.put("nickname", message.getNickname());
                    addRecord(item); // 添加消息记录
                    break; // 普通聊天消息
                case MessageType.USER_ENTER_CHATROOM_MESSAGE_TYPE:
                    message.getContent();
                    addLastRecord(message.getNickname());
                    break; // 提示用户进入直播间
                case MessageType.USER_EXIT_CHATROOM_MESSAGE_TYPE:
                    Map<String, String> item2 = new HashMap<>(3);
                    item2.put("record", "离开了");
                    item2.put("account", "10000");
                    item2.put("nickname", message.getNickname());
                    addRecord(item2); // 添加消息记录
                    break; // 提示用户离开了
                case MessageType.SYSTEM_MESSAGE_TYPE:
                    Map<String, String> item3 = new HashMap<>(3);
                    item3.put("record", message.getContent());
                    item3.put("account", "10001");
                    item3.put("nickname", message.getNickname());
                    addRecord(item3); // 添加消息记录
                    break; // 提示用户被禁言
                // TODO 提示用户关注了主播
                default:
                    break;
            }
        }
    };

    /**
     * 聊天记录适配器
     */
    private class ChatRecordAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        public List<Map<String, String>> data; // 数据源
        private Context context;

        public ChatRecordAdapter(Context context) {
            this.context = context;
            inflater = LayoutInflater.from(context);
            data = new LinkedList<>();
            Map<String, String> item = new HashMap<>(3);
            item.put("record", "温馨提示：涉及色情、低俗、血腥、暴力、无版权等内容将被封停账号及追究法律责任，文明绿色直播从我做起！");
            item.put("nickname", "0");
            item.put("account", "0");
            data.add(item);// 添加默认信息
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return data.size();
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return arg0;
        }

        @Override
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return arg0;
        }

        @Override
        public View getView(final int position, View view, ViewGroup arg2) {
            if (view == null) {
                view = inflater.inflate(R.layout.item_chat_record, null);
            }

            TextView msgTextView = (TextView) view.findViewById(R.id.tv_chat_record);
            msgTextView.setTypeface(typeFace);

            if (position == 0) {
                msgTextView.setText(data.get(0).get("record")); // 显示提示信息
                msgTextView.setTextColor(context.getResources().getColor(R.color.colorRed));
            } // 第一条信息
            else {
                String account = data.get(position).get("account");
                switch (account) {
                    case "10000":
                        msgTextView.setText(data.get(position).get("nickname") + data.get(position).get("record")); // 显示提示信息
                        msgTextView.setTextColor(context.getResources().getColor(R.color.colorWhite2));
                        break; // 用户离开了
                    case "10001":
                        msgTextView.setText(data.get(position).get("record")); // 显示提示信息
                        msgTextView.setTextColor(context.getResources().getColor(R.color.colorRed1));
                        break; // 用户被禁言或被提出房间
                    default:
                        String msg = data.get(position).get("nickname") + "：" + data.get(position).get("record"); // 消息
                        int startIndex = msg.indexOf("："); // 消息间隔下标
                        SpannableStringBuilder spannable = new SpannableStringBuilder(msg);
                        spannable.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.colorWhite2)), startIndex + 1, msg.length()
                                , Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); // 设置局部颜色
                        NickNameSpan nickNameSpan = new NickNameSpan(getActivity(), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String account = data.get(position).get("account");
                                userAccount = account; // 取得当前点击的用户账号
                                userNickname = data.get(position).get("nickname");
                                liveRoomPresenter.loadSimpleUserData(account); //远程加载用户数据
                            }
                        });
                        spannable.setSpan(nickNameSpan, 0, startIndex + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); // 设置局部可点击和颜色
                        msgTextView.setText(spannable); // 显示消息
                        msgTextView.setMovementMethod(LinkMovementMethod.getInstance()); // 触发点击事件
                        break; // 普通聊天消息
                }
            }
            return view;
        }
    }

    /**
     * 弹出用户信息的弹出框
     */
    private void showUserInfoDialog(SimpleUserVo simpleUserVo) {
        if (userInfoDialogView == null) userInfoDialogView = new UserInfoDialogView(getActivity());
        userInfoDialogView.setValueAndShow(simpleUserVo);
        Button shutUpBtn = userInfoDialogView.getShutupBtnView(); // 禁言
        Button kickoutBtn = userInfoDialogView.getKickoutBtnView(); // 踢出
        shutUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message message = new Message();
                message.setNickname(userNickname);
                message.setAccount(userAccount);
                message.setDestination(mobileUserVo.getLiveRoomVo().getRoomNum() + "-" + userAccount); // 消息目的的
                message.setMessageType(MessageType.SHUTUP_USER_MESSAGE_TYPE);
                message.setFromChatRoomNum(mobileUserVo.getLiveRoomVo().getRoomNum());
                chatActivityEvent.getChatReceiveServiceBinder().sendMsg(message); // 发送消息
                userInfoDialogView.getAnchorInfoDialog().dismiss(); // 关闭对话框
            }
        });// 禁言

        kickoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message message = new Message();
                message.setNickname(userNickname);
                message.setAccount(userAccount);
                message.setDestination(mobileUserVo.getLiveRoomVo().getRoomNum() + "-" + userAccount); // 消息目的的
                message.setMessageType(MessageType.KICKOUT_USER_MESSAGE_TYPE);
                message.setFromChatRoomNum(mobileUserVo.getLiveRoomVo().getRoomNum());
                chatActivityEvent.getChatReceiveServiceBinder().sendMsg(message); // 发送消息
                userInfoDialogView.getAnchorInfoDialog().dismiss(); // 关闭对话框
            }
        }); // 剔出房间


    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            if (msg.what == HomeConstants.LOAD_USER_INFO_SUCCESS_FLAG) {     //加载数据用户成功
                ResponseModel<SimpleUserVo> dataModel = (ResponseModel<SimpleUserVo>) msg.obj;
                if (dataModel.getStatus() == 1) {
                    SimpleUserVo simpleUserVo = dataModel.getData();
                    showUserInfoDialog(simpleUserVo);
                } else {
                    Toast.makeText(getActivity(), "服务器繁忙！", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity(), "连接服务器失败！", Toast.LENGTH_SHORT).show();
            }
        }
    }; // 响应处理

    /**
     * 显示输入框
     */
    public void showInput(){
        final DialogPlus dialog = DialogPlus.newDialog(getActivity()).setContentBackgroundResource(R.color.colorWhite)
                .setContentHolder(new ViewHolder(R.layout.item_chat_input))
                .setOverlayBackgroundResource(R.color.transparent) // 设置为透明背景
                .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
                .setGravity(Gravity.BOTTOM)
                .create();
        dialog.show();
        View dialogView =  dialog.getHolderView();
        final EditText chatMsgEditText = (EditText) dialogView.findViewById(R.id.et_chat_msg); // 聊天内容输入框
        chatMsgEditText.setFocusable(true); // 自动对焦
        Button chatMsgSentBtn = (Button) dialogView.findViewById(R.id.btn_chat_send); // 发送按钮
        chatMsgSentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                org.live.common.domain.Message message = new org.live.common.domain.Message();
                MobileUserVo mobileUserVo = HomeActivity.mobileUserVo;
                message.setNickname(mobileUserVo.getNickname());
                message.setAccount(mobileUserVo.getAccount());
                message.setDestination(liveRoomInfo.getLiveRoomNum()); // 消息目的的
                message.setMessageType(MessageType.SEND_TO_CHATROOM_MESSAGE_TYPE);
                message.setFromChatRoomNum(liveRoomInfo.getLiveRoomNum());
                message.setContent(chatMsgEditText.getText().toString());
                chatActivityEvent.getChatReceiveServiceBinder().sendMsg(message); // 发送消息
            }
        });
    }

}
