package org.live.module.home.model.impl;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import org.live.module.home.model.MeModel;
import org.live.module.home.view.MeView;
import org.live.module.login.db.AppDbUtils;
import org.live.module.login.domain.MobileUserVo;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * 我的’模块逻辑处理层实现
 * Created by KAM on 2017/4/15.
 */

public class MeModelImpl implements MeModel {

    private Context context;
    private MeView meView;

    public MeModelImpl(Context context, MeView meView) {
        this.context = context;
        this.meView = meView;
    }

    /**
     * 获得用户信息
     *
     * @return
     */
    @Override
    public MobileUserVo getUserData() {
        MobileUserVo mobileUserVo = new MobileUserVo();
        AppDbUtils dbUtils = new AppDbUtils(context, "live", 1);
        Cursor cursor = dbUtils.findOne("select * from live_mobile_user", null);
        if (cursor.getCount() > 0) {
            for (String columnName : cursor.getColumnNames()) {
                String val = cursor.getString(cursor.getColumnIndex(columnName));
                switch (columnName) {
                    case "account":
                        mobileUserVo.setAccount(val);
                        break;
                    case "password":
                        mobileUserVo.setPassword(val);
                        break;
                    case "nickname":
                        mobileUserVo.setNickname(val);
                        break;
                    case "head_img_url":
                        mobileUserVo.setHeadImgUrl(val);
                        break;
                    case "email":
                        mobileUserVo.setEmail(val);
                        break;
                    case "mobile_number":
                        mobileUserVo.setMobileNumber(val);
                        break;
                    case "real_name":
                        mobileUserVo.setRealName(val);
                        break;
                    case "sex":
                        mobileUserVo.setSex(val);
                        break;
                    case "birthday":
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        Date date = null;
                        try {
                            date = sdf.parse(val);
                        } catch (ParseException e) {
                            Log.e("Global", val);
                            Log.e("Global", e.getMessage());
                        }
                        mobileUserVo.setBirthday(date);
                        break;
                    case "anchorFlag":
                        boolean anchorFlag = false;
                        if (val.equals("1")) {
                            anchorFlag = true;
                        }
                        mobileUserVo.setAnchorFlag(anchorFlag);
                        break;
                }

            }
            if (mobileUserVo.isAnchorFlag()) {
                MobileUserVo.LiveRoomInUserVo liveRoomVo = mobileUserVo.getLiveRoomVo();
                cursor = dbUtils.findOne("select * from live_room", null);
                if (cursor.getCount() > 0) {
                    for (String name2 : cursor.getColumnNames()) {
                        String val2 = cursor.getString(cursor.getColumnIndex(name2));
                        switch (name2) {
                            case "category_id":
                                liveRoomVo.setCategoryId(val2);
                                break;
                            case "category_name":
                                liveRoomVo.setCategoryName(val2);
                                break;
                            case "room_id":
                                liveRoomVo.setRoomId(val2);
                                break;
                            case "room_name":
                                liveRoomVo.setRoomName(val2);
                                break;
                            case "room_cover_url":
                                liveRoomVo.setRoomCoverUrl(val2);
                                break;
                            case "room_label":
                                liveRoomVo.setRoomLabel(val2);
                                break;
                            case "description":
                                liveRoomVo.setDescription(val2);
                                break;
                        }
                    }
                }
                mobileUserVo.setLiveRoomVo(liveRoomVo);
            }
        }
        dbUtils.close(); // 释放数据库资源
        return mobileUserVo;
    }

    /**
     * 注销登录
     *
     * @param account 账号
     * @param roomId  主播房间id
     */
    @Override
    public void logout(String account, String roomId) {
        AppDbUtils dbUtils = new AppDbUtils(context, "live", 1);
        dbUtils.delete("delete from live_mobile_user where account = ?", account); // 清除用户数据
        if (roomId != null) {
            dbUtils.delete("delete from live_room where room_id = ?", roomId); // 若是主播则清空主播房间数据
        }
        dbUtils.close(); // 释放数据库资源
        meView.finishSelf(); // 摧毁自己
        meView.toLogin(); // 前往登陆页面


    }

    @Override
    public void editUserInfo(Map<String, Object> params) {

    }
}
