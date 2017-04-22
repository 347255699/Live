package org.live.module.anchor.view.impl;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import net.steamcrafted.materialiconlib.MaterialDrawableBuilder;

import org.live.R;
import org.live.common.util.BackThread;
import org.live.module.anchor.presenter.AnchorInfoPresenter;
import org.live.module.anchor.presenter.impl.AnchorInfoPresenterImpl;
import org.live.module.anchor.view.AnchorInfoView;

/**
 * 主播信息模块
 */
public class AnchorInfoActivity extends AppCompatActivity implements AnchorInfoView {
    /**
     * 待修改key值
     */
    private String key;
    /**
     * 待修改值
     */
    private String val;
    /**
     * 修改输入框
     */
    private EditText aAnchorInfoItemEditText;
    /**
     * 主播信息修改按钮
     */
    private Button aAnchorInfoEditButton;
    private AnchorInfoPresenter anchorInfoPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.anchorInfoPresenter = new AnchorInfoPresenterImpl(this,this);
        setContentView(R.layout.activity_anchor_info);
        this.val = getIntent().getStringExtra("val");
        this.key = getIntent().getStringExtra("key");
        initUIElement();
    }

    /**
     * 初始化UI控件
     */
    private void initUIElement() {
        aAnchorInfoItemEditText = (EditText) findViewById(R.id.et_anchor_info_item);
        Toolbar aToolbar = (Toolbar) findViewById(R.id.tb_anchor_info);
        aAnchorInfoEditButton = (Button) findViewById(R.id.btn_anchor_info_edit);
        aToolbar.setTitle("");
        setSupportActionBar(aToolbar); // 为action bar 添加 tool bar
        aToolbar.setNavigationIcon(getIconDrawable(MaterialDrawableBuilder.IconValue.CHEVRON_LEFT, Color.WHITE)); // 设置返回图标
        aToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new BackThread().start(); // 模拟返回键点击
            }
        }); // 绑定标题栏返回键
        if (val != null) {
            aAnchorInfoItemEditText.setText(val); // 设置待修改值
        }

        aAnchorInfoEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Global", "点击");
            }
        });// 绑定主播信息修改按钮



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

    @Override
    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show(); // 提示信息
    }

    @Override
    public void back() {
        InputMethodManager imm = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0); // 关闭软键盘
        }
        new BackThread().start(); // 模拟返回键点击
    }
}
