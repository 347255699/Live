package org.live.module.home.constants;

/**
 * Created by wangzhancheng on 2017/4/5.
 */

public class HomeConstants {

    /**
     * 加载直播分类的时候，发生异常。
     */
    public static final int LOAD_CATEGORY_EXCEPTION_FLAG = 100;

    /**
     * 加载直播分类正确,
     */
    public static final int LOAD_CATEGORY_SUCCESS_FLAG = 101;

    /**
     * 加载直播间的时候，发生异常。
     */
    public static final int LOAD_LIVE_ROOM_EXCEPTION_FLAG = 103;

    /**
     * 加载直播间正确
     */
    public static final int LOAD_LIVE_ROOM_SUCCESS_FLAG = 104;

    /**
     * 加载直播间限制正确
     */
    public static final int LOAD_LIMITATION_SUCCESS_FLAG = 105 ;

    /**
     * 加载直播间限制的时候，发生异常。
     */
    public static final int LOAD_LIMITATION_EXCEPTION_FLAG = 106 ;

    /**
     * 加载主播信息的时候，发生异常。
     */
    public static final int LOAD_ANCHOR_INFO_EXCEPTION_FLAG = 107 ;

    /**
     * 加载主播信息正确
     */
    public static final int LOAD_ANCHOR_INFO_SUCCESS_FLAG = 108 ;

    /**
     * 分类名称的key
     */
    public static final String CATEGORY_NAME_KEY = "categoryNameKey";

    /**
     * 分类id的key
     */
    public static final String CATEGORY_ID_KEY = "categoryIdKey";

    /**
     * 关注直播间的用户id
     */
    public static final String ATTENTION_LIVEROOM_USER_ID = "userIdKey";

    /**
     * 调用系统相册功能成功时的返回标志
     */
    public static final int GALLERY_RESULT_CODE = 1001;
    /**
     * 调用系统拍照功能成功时的返回标志
     */
    public static final int CAMERA_RESULT_CODE = 1002;
    /**
     * 调用系统图像裁剪功能成功时的返回标志
     */
    public static final int CROP_RESULT_CODE = 1003;
    /**
     * 头像上传响应标志码
     */
    public static final int HTTP_RESPONSE_RESULT_POST_HEAD_IMG_CODE = 3001;

    /**
     * 用户信息修改上传响应标志码
     */
    public static final int HTTP_RESPONSE_RESULT_EDDIT_USER_INFO_CODE = 4001;

    /**
     * 检查用户主播身份响应标志码
     */
    public static final int HTTP_RESPONSE_RESULT_CHECK_IS_ANCHOR_CODE = 7001;

    /**
     * 进入singleCategoryActivity的key
     */
    public static final String ENTER_SINGLE_CATEGORY_KEY = "entryKey";

    /**
     * 进入某分类所有直播间的展示界面，key
     */
    public static final String SINGLE_CATEGORY_LIVEROOM = "singleCategoryLiveRoom";

    /**
     * 进入关注的所有直播间的展示界面，key
     */
    public static final String ATTENTION_LIVEROOM = "attentionLiveRoom";

    /**
     * 房间封面标识
     */
    public static final int LIVE_ROOM_COVER = 11;
    /**
     * 头像标识
     */
    public static final int HEAD_IMG = 15;

    /**
     * 限制类型，禁言
     */
    public static final int LIMIT_TYPE_SHUTUP = 1 ;

    /**
     * 限制类型，踢出房间
     */
    public static final int LIMIT_TYPE_KICKOUT = 2 ;

    /**
     * 限制类型的key
     */
    public static final String LIMIT_TYPE_KEY_FLAG = "limitTypeKey" ;

    /**
     * 头像的key
     */
    public static final String HEAD_IMG_URL_KEY = "headImgUrlKey" ;

    /**
     * 直播间名的key
     */
    public static final String LIVE_ROOM_NAME_KEY = "liveRoomNameKey" ;

    /**
     * 直播间拉推流地址的key
     */
    public static final String LIVE_ROOM_URL_KEY = "liveRoomUrlKey" ;

    /**
     * 直播间id的key
     */
    public static final String LIVE_ROOM_ID_KEY = "liveRoomIdKey" ;

    /**
     * 直播间在线人数的key
     */
    public static final String LIVE_ROOM_ONLINE_COUNT_KEY = "liveRoomOnlineCountKey" ;

    /**
     * 直播类型，录屏
     */
    public static final int LIVING_TYPE_CAPTURE = 0;
    /**
     * 直播类型，普通直播
     */
    public static final int LIVING_TYPE_GENERAL = 1;

}
