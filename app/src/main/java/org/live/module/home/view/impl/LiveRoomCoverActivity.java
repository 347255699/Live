package org.live.module.home.view.impl;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import net.steamcrafted.materialiconlib.MaterialDrawableBuilder;

import org.live.R;
import org.live.common.constants.LiveConstants;
import org.live.common.util.BackThread;

/**
 * 直播封面窗口
 */
public class LiveRoomCoverActivity extends AppCompatActivity {
    /**
     * 封面视图
     */
    private ImageView lRoomCoverImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_room_cover);
        initUIElement();
    }

    /**
     * 初始化UI控件
     */
    private void initUIElement() {
        Toolbar lToolbar = (Toolbar) findViewById(R.id.tb_live_room_cover);
        lRoomCoverImageView = (ImageView) findViewById(R.id.iv_live_room_cover);
        lToolbar.setTitle("");
        setSupportActionBar(lToolbar); // 为action bar 添加 tool bar
        lToolbar.setNavigationIcon(getIconDrawable(MaterialDrawableBuilder.IconValue.CHEVRON_LEFT, Color.WHITE)); // 设置返回图标
        lToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new BackThread().start(); // 模拟返回键点击
            }
        }); // 绑定标题栏返回键
        Glide.with(this).load(LiveConstants.REMOTE_SERVER_HTTP_IP + HomeActivity.mobileUserVo.getLiveRoomVo().getRoomCoverUrl())
                .into(lRoomCoverImageView); // 设置主播封面

    }

    /**
     * 获取图标
     *
     * @param
     * @return
     */
    private Drawable getIconDrawable(MaterialDrawableBuilder.IconValue iconValue, int color) {
        Drawable drawable = MaterialDrawableBuilder.with(this)
                .setIcon(iconValue)
                .setColor(color)
                .setSizeDp(35)
                .build();
        return drawable;
    }
}
