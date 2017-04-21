package org.live.module.anchor.view.impl;

import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import net.steamcrafted.materialiconlib.MaterialDrawableBuilder;

import org.live.R;
import org.live.common.util.BackThread;
import org.live.module.anchor.presenter.ApplyAnchorPresenter;
import org.live.module.anchor.presenter.impl.ApplyAnchorPresenterImpl;
import org.live.module.anchor.view.ApplyAnchorView;
import org.live.module.home.constants.HomeConstants;
import org.live.module.home.domain.LiveCategoryVo;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ‘申请主播’活动窗口
 */
public class ApplyAnchorActivity extends AppCompatActivity implements ApplyAnchorView {
    /**
     * 直播分类选择框
     */
    private LinearLayout aApplyAnchorCategorySelect;
    /**
     * 直播分类标签
     */
    private TextView aAnchorCategoryLabelTextView;
    /**
     * 身份证图片选择按钮
     */
    private LinearLayout aApplyAnchorIdCardImgChoose;
    /**
     * 身份证图像视图
     */
    private ImageView aIdCardImageView;
    /**
     * 身份证输入框
     */
    private EditText aIdCardEditText;
    /**
     * 真实姓名输入框
     */
    private EditText aRealNameEditText;
    /**
     * 直播分类数据
     */
    private List<LiveCategoryVo> liveCategorys;
    private String caregoryId; // 当前被选中的分类id;
    private Context context;
    private Bitmap idCard; // 身份证图片数据
    private ApplyAnchorPresenter applyAnchorPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_anchor);
        this.context = this;
        initUIElement();
        this.applyAnchorPresenter = new ApplyAnchorPresenterImpl(this, this);
        applyAnchorPresenter.requestCategoryList(); // 请求直播分类数据
    }

    /**
     * 初始化标题栏
     */
    private void initUIElement() {
        Toolbar lToolbar = (Toolbar) findViewById(R.id.tb_apply_anchor); // 工具栏
        aApplyAnchorCategorySelect = (LinearLayout) findViewById(R.id.ll_apply_anchor_select);
        aAnchorCategoryLabelTextView = (TextView) findViewById(R.id.tv_apply_anchor_category_label);
        aApplyAnchorIdCardImgChoose = (LinearLayout) findViewById(R.id.ll_apply_anchor_file_choose);
        aIdCardImageView = (ImageView) findViewById(R.id.tv_apply_anchor_id_card);
        lToolbar.setTitle("");
        setSupportActionBar(lToolbar); // 为action bar 添加 tool bar
        lToolbar.setNavigationIcon(getIconDrawable(MaterialDrawableBuilder.IconValue.CHEVRON_LEFT, Color.WHITE)); // 设置返回图标
        lToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new BackThread().start(); // 模拟返回键点击
            }
        }); // 绑定标题栏返回键

        aApplyAnchorIdCardImgChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final DialogPlus dialog = DialogPlus.newDialog(context).setContentBackgroundResource(R.color.colorWhite2)
                        .setContentHolder(new ViewHolder(R.layout.dialog_me_choose_type))
                        .setGravity(Gravity.CENTER)
                        .create();
                dialog.show();
                final View dialogView = dialog.getHolderView();
                dialogView.findViewById(R.id.btn_me_gallery).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_PICK, null);
                        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                        startActivityForResult(intent, HomeConstants.GALLERY_RESULT_CODE);
                        dialog.dismiss();
                    }
                });// 从相册中选取
                dialogView.findViewById(R.id.btn_me_camera).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                                Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "idCard.jpg")));
                        startActivityForResult(intent, HomeConstants.CAMERA_RESULT_CODE);
                        dialog.dismiss();
                    }
                });// 拍摄照片*/

            }
        });
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

    /**
     * 显示提示信息
     *
     * @param msg
     */
    @Override
    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 返回
     */
    @Override
    public void back() {
        new BackThread().start();
    }

    @Override
    public void showCategoryList(List<LiveCategoryVo> liveCategoryVos) {
        this.liveCategorys = liveCategoryVos;

        aApplyAnchorCategorySelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DialogPlus dialog = DialogPlus.newDialog(context).setContentBackgroundResource(R.color.colorWhite)
                        .setContentHolder(new ViewHolder(R.layout.dialog_anchor_live_category))
                        .setContentHeight(650)
                        .setGravity(Gravity.CENTER)
                        .create();
                dialog.show();
                final View dialogView = dialog.getHolderView();
                ListView liveCategoryListView = (ListView) dialogView.findViewById(R.id.lv_anchor_live_category);
                SimpleAdapter adapter = new SimpleAdapter(context, getData(liveCategorys), R.layout.item_anchor_live_category, new String[]{"label"}, new int[]{R.id.tv_apply_anchor_category_label});
                liveCategoryListView.setAdapter(adapter);
                liveCategoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Log.i("Global", "index:" + position);
                        dialog.dismiss(); // 取消对话框
                    }
                }); // 绑定分类列表项点击事件
            }
        }); // 绑定直播分类选择按钮
    }

    /**
     * 获取列表数据
     *
     * @return
     */
    private  List<Map<String, Object>> getData(List<LiveCategoryVo> liveCategoryVos) {
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        for (LiveCategoryVo liveCategoryVo : liveCategoryVos) {
            Map<String, Object> itemData = new HashMap<String, Object>();
            itemData.put("label", liveCategoryVo.getCategoryName());
            data.add(itemData);
        }
        return data;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        switch (requestCode) {
            case HomeConstants.GALLERY_RESULT_CODE:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    String path = getRealPathFromURI(uri); // 图像路径
                    idCard = BitmapFactory.decodeFile(path);
                    aIdCardImageView.setImageBitmap(idCard);
                    Log.i("Global", path);
                }

                break;
            case HomeConstants.CAMERA_RESULT_CODE:
                if (resultCode == RESULT_OK) {
                    File temp = new File(Environment.getExternalStorageDirectory() + "/idCard.jpg");
                    String path = temp.getAbsolutePath(); // 图像路径
                    idCard = BitmapFactory.decodeFile(path);
                    aIdCardImageView.setImageBitmap(idCard);
                    Log.i("Global", path);
                }

                break;
            default:
                break;

        }
        super.onActivityResult(requestCode, resultCode, data);


    }

    /**
     * 获得图像正式路径
     *
     * @param contentUri
     * @return
     */
    private String getRealPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }
}
