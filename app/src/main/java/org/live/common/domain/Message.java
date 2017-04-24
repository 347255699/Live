package org.live.common.domain;

/**
 *  通讯信息
 *
 *  消息事件类型参考 {@link MessageType}
 *
 * Created by Mr.wang on 2017/3/17.
 */
public class Message {

    /**
     *  来自哪个直播间号
     */
    private String fromChatRoomNum;

    /**
     *  发送至某直播间，或某人
     *  当发送至某人时，格式：  "直播间号-用户账号"，例如："10001-201335020231"
     */
    private String destination;


    /**
     *  发送者的账号
     */
    private String usernameNum;
 
    /**
     *  发送者的名称
     */
    private String username ;

    /**
     *  信息内容
     */
    private String content ;


    /**
     *
     *  消息事件类型
     */
    private int messageType ;


    public String getFromChatRoomNum() {
        return fromChatRoomNum;
    }

    public void setFromChatRoomNum(String fromChatRoomNum) {
        this.fromChatRoomNum = fromChatRoomNum;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getUsernameNum() {
        return usernameNum;
    }

    public void setUsernameNum(String usernameNum) {
        this.usernameNum = usernameNum;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }
}
