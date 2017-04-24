package org.live.common.domain;

/**
 *  信息事件类型
 * Created by Mr.wang on 2017/3/17.
 */
public class MessageType {

    /**
     *  发送信息至直播间
     */
    public static final int SEND_TO_CHATROOM_MESSAGE_TYPE = 0 ;

    /**
     * 发送信息至某用户
     */
    public static final int SEND_TO_USER_MESSAGE_TYPE = 1 ;

    /**
     *  主播离开主播间，即直播结束
     */
    public static final int ANCHOR_EXIT_CHATROOM_MESSAGE_TYPE = 2 ;

    /**
     *  用户进入直播间
     */
    public static final int USER_ENTER_CHATROOM_MESSAGE_TYPE = 3 ;

    /**
     *  用户离开直播间
     */
    public static final int USER_EXIT_CHATROOM_MESSAGE_TYPE = 4 ;

    /**
     *  主播禁言某个用户
     */
    public static final int SHUTUP_USER_MESSAGE_TYPE = 5 ;

    /**
     *  主播解除禁言某个用户
     */
    public static final int RELIEVE_SHUTUP_USER_MESSAGE_TYPE = 6 ;

    /**
     * 主播踢出用户
     */
    public static final int KICKOUT_USER_MESSAGE_TYPE = 7 ;

    /**
     *  主播解除踢出用户
     */
    public static final int RELIEVE_KICKOUT_USER_MESSAGE_TYPE = 8 ;

    /**
     *  用户关注直播间。
     */
    public static final int USER_ATTENTION_CHATROOM = 9 ;

    /**
     * 用户解散关注直播间
     */
    public static final int RELIEVE_USER_ATTENTION_CHATROOM = 10 ;

}
