package org.live.module.login.domain;

import java.util.Date;

/**
 * Created by wang on 2017/4/12.
 */
public class MobileUserVo {

    /**
     *  用户id
     */
    private String userId ;

    /**
     * 账号
     */
    private String account ;

    /**
     * 密码
     */
    private String password ;

    /**
     * 昵称
     */
    private String nickname ;

    /**
     * 头像url地址
     */
    private String headImgUrl ;

    /**
     * 邮箱
     */
    private String email ;

    /**
     * 手机号码
     */
    private String mobileNumber ;

    /**
     * 姓名
     */
    private String realName ;

    /**
     * 性别
     */
    private String sex ;

    /**
     * 生日
     */
    private Date birthday ;

    /**
     * 是否是主播的标记,默认是false
     */
    private boolean anchorFlag ;

    private LiveRoomInUserVo liveRoomVo ;

    public LiveRoomInUserVo newInstantLiveRoomVo() {
        return new LiveRoomInUserVo() ;
    }

    public class LiveRoomInUserVo {

        /**
         *  分类id
         */
        private String categoryId ;

        /**
         * 分类名称
         */
        private String categoryName ;

        /**
         * 房间id
         */
        private String roomId ;

        /**
         *  直播间号
         */
        private String roomNum ;

        /**
         *  房间名
         */
        private String roomName ;

        /**
         *  房间封面
         */
        private String roomCoverUrl ;

        /**
         * 直播地址
         */
        private String liveRoomUrl ;

        /**
         * 房间标签。多个用“,”逗号间隔
         */
        private String roomLabel ;

        /**
         * 禁播标记
         */
        private boolean banLiveFlag ;

        /**
         *  个性签名
         */
        private String description ;

        public String getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(String categoryId) {
            this.categoryId = categoryId;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }

        public String getRoomId() {
            return roomId;
        }

        public void setRoomId(String roomId) {
            this.roomId = roomId;
        }

        public String getRoomName() {
            return roomName;
        }

        public void setRoomName(String roomName) {
            this.roomName = roomName;
        }

        public String getRoomCoverUrl() {
            return roomCoverUrl;
        }

        public void setRoomCoverUrl(String roomCoverUrl) {
            this.roomCoverUrl = roomCoverUrl;
        }

        public String getRoomLabel() {
            return roomLabel;
        }

        public void setRoomLabel(String roomLabel) {
            this.roomLabel = roomLabel;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getRoomNum() {
            return roomNum;
        }

        public void setRoomNum(String roomNum) {
            this.roomNum = roomNum;
        }

        public boolean isBanLiveFlag() {
            return banLiveFlag;
        }

        public void setBanLiveFlag(boolean banLiveFlag) {
            this.banLiveFlag = banLiveFlag;
        }

        public String getLiveRoomUrl() {
            return liveRoomUrl;
        }

        public void setLiveRoomUrl(String liveRoomUrl) {
            this.liveRoomUrl = liveRoomUrl;
        }
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public boolean isAnchorFlag() {
        return anchorFlag;
    }

    public void setAnchorFlag(boolean anchorFlag) {
        this.anchorFlag = anchorFlag;
    }

    public LiveRoomInUserVo getLiveRoomVo() {
        return liveRoomVo;
    }

    public void setLiveRoomVo(LiveRoomInUserVo liveRoomVo) {
        this.liveRoomVo = liveRoomVo;
    }

}
