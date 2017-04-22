package org.live.module.home.view.impl;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import net.steamcrafted.materialiconlib.MaterialDrawableBuilder;

import org.live.R;
import org.live.module.home.constants.HomeConstants;
import org.live.module.home.listener.OnHomeActivityEventListener;
import org.live.module.home.presenter.MePresenter;
import org.live.module.home.presenter.impl.MePresenterImpl;
import org.live.module.home.view.MeView;
import org.live.module.home.view.custom.LocalIconItemView;
import org.live.module.login.domain.MobileUserVo;
import org.live.module.login.view.impl.LoginActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import me.majiajie.pagerbottomtabstrip.NavigationController;
import me.majiajie.pagerbottomtabstrip.PageBottomTabLayout;
import me.majiajie.pagerbottomtabstrip.item.BaseTabItem;
import me.majiajie.pagerbottomtabstrip.listener.OnTabItemSelectedListener;


/**
 * 主界面
 * Created by Mr.wang on 2017/3/14.
 */
public class HomeActivity extends FragmentActivity implements OnHomeActivityEventListener, MeView {

    public static final String TAG = "Global";

    private List<Fragment> fragmentList = null;    // fragment的集合，用于保存fragment

    private ViewPager viewPager = null;

    private FragmentPagerAdapter fragmentPagerAdapter = null;   //fragment的适配器

    public static MobileUserVo mobileUserVo; // 用户数据引用

    private MePresenter mePresenter; // '我的'模块表示器引用
    private MeFragment meFragment; // '我的'fragment引用
    private Bitmap head;// 头像Bitmap

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mePresenter = new MePresenterImpl(this, this); // 取得'我的模块'表示器
        this.mobileUserVo = mePresenter.getUserData(); // 取得用户数据
        requestWindowFeature(Window.FEATURE_NO_TITLE);     //隐藏标题
        setContentView(R.layout.activity_home);
        initTabLayout();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        int fragmentIndex = viewPager.getCurrentItem();
        if (fragmentList.get(fragmentIndex) instanceof MeFragment) {
            meFragment = (MeFragment) fragmentList.get(fragmentIndex);
        }
        if (meFragment != null) {
            meFragment.reLoadData(); // 刷新数据
        }

    }

    /**
     * 初始化底部导航栏
     */
    public void initTabLayout() {
        PageBottomTabLayout bottomTabLayout = (PageBottomTabLayout) this.findViewById(R.id.tab_home_nav);
        PageBottomTabLayout.CustomBuilder builder = bottomTabLayout.custom()
                .addItem(this.newTabItem(MaterialDrawableBuilder.IconValue.HOME_OUTLINE, MaterialDrawableBuilder.IconValue.HOME, "首页"))
                .addItem(this.newTabItem(MaterialDrawableBuilder.IconValue.FORMAT_LIST_BULLETED, MaterialDrawableBuilder.IconValue.FORMAT_LIST_BULLETED_TYPE, "分类"))
                .addItem(this.newTabItem(MaterialDrawableBuilder.IconValue.PRESENTATION, MaterialDrawableBuilder.IconValue.PRESENTATION_PLAY, "直播"))
                .addItem(this.newTabItem(MaterialDrawableBuilder.IconValue.ACCOUNT_OUTLINE, MaterialDrawableBuilder.IconValue.ACCOUNT, "我的"));

        viewPager = (ViewPager) this.findViewById(R.id.vp_home_mainDump);  //获取viewPager
        fragmentPagerAdapter = initFragmentPageAdapter();  //获取适配器

        viewPager.setAdapter(fragmentPagerAdapter);   //设置适配器
        final NavigationController navigationController = builder.build();   //导航栏控制器

        Log.d(TAG, "viewPage是否等于null-->" + (viewPager == null));
        navigationController.setupWithViewPager(viewPager);
        navigationController.addTabItemSelectedListener(new OnTabItemSelectedListener() {
            @Override
            public void onSelected(int index, int old) {
                Log.d(TAG, "checked：" + index + ", old: " + old);
            }

            @Override
            public void onRepeat(int index) {
                Log.d(TAG, "orepeatClick: " + index);
            }
        });

    }

    /**
     * 新建一个tab_item
     *
     * @param defaultIcon 默认图标
     * @param checkedIcon 选中时的图标
     * @param title       标题
     * @return
     */
    private BaseTabItem newTabItem(MaterialDrawableBuilder.IconValue defaultIcon, MaterialDrawableBuilder.IconValue checkedIcon, String title) {
        LocalIconItemView localOnlyIconItemView = new LocalIconItemView(this);
        localOnlyIconItemView.initialize(defaultIcon, checkedIcon, title);
        return localOnlyIconItemView;
    }

    /**
     * 初始化fragmentPageAdapter，并交给底部导航栏的控制器
     */
    public FragmentPagerAdapter initFragmentPageAdapter() {

        fragmentList = new ArrayList<>(4);
        HomeFragment homeFragment = new HomeFragment();
        CategoryFragment categoryFragment = new CategoryFragment();
        LiveFragment liveFragment = new LiveFragment();
        MeFragment meFragment = new MeFragment();
        fragmentList.add(homeFragment);
        fragmentList.add(categoryFragment);
        if (mobileUserVo.isAnchorFlag()) {
            fragmentList.add(new LiveFragment2());
        } else {
            fragmentList.add(liveFragment);
        }
        fragmentList.add(meFragment);
        //创建FragmentPagerAdapter
        return new CustomFragmentPagerAdapter(getSupportFragmentManager(), fragmentList);
    }

    private long lastExitTime = 0; //上次点击返回键的按钮

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (System.currentTimeMillis() - lastExitTime > 2000) {
                Toast.makeText(this, "再按一次退出应用", Toast.LENGTH_SHORT).show();
                lastExitTime = System.currentTimeMillis();
            } else {
             /*   finish();
                System.exit(0);*/
                exit();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private static final String TAG_EXIT = "exit";

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
            boolean isExit = intent.getBooleanExtra(TAG_EXIT, false);
            if (isExit) {
                this.finish();
            }
        }
    }

    /**
     * 注销登陆
     */
    @Override
    public void logout() {
        String roomId = mobileUserVo.isAnchorFlag() ? mobileUserVo.getLiveRoomVo().getRoomId() : null;
        mePresenter.logout(mobileUserVo.getAccount(), roomId);
    }

    /**
     * 取得用户数据
     *
     * @return
     */
    @Override
    public MobileUserVo getUserData() {
        return this.mobileUserVo;
    }

    @Override
    protected void onDestroy() {
        if (mobileUserVo != null) {
            mobileUserVo = null;
        } // 释放资源
        super.onDestroy();
    }

    @Override
    public void exit() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra(HomeActivity.TAG_EXIT, true);
        startActivity(intent);
    }

    /**
     * 从相册选取头像
     */
    @Override
    public void chooseHeadImgFromGallery(int flag) {
        Intent intent1 = new Intent(Intent.ACTION_PICK, null);
        intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent1, flag);
    }


    /**
     * 拍摄头像
     */
    @Override
    public void chooseHeadImgFromCamera(int flag) {
        Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent2.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "head.jpg")));
        startActivityForResult(intent2, flag);
    }

    /**
     * 更换fragment视图
     */
    @Override
    public void replaceLiveFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.remove(fragmentList.get(2));
        ft.commit();
        fm.executePendingTransactions();
        fragmentList.set(2, new LiveFragment2());
        fragmentPagerAdapter.notifyDataSetChanged();
    }


    /**
     * 自定义FragmentPagerAdapter
     */
    public class CustomFragmentPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragmentList;

        public CustomFragmentPagerAdapter(FragmentManager fragmentManager, List<Fragment> fragmentList) {
            super(fragmentManager);
            this.fragmentList = fragmentList;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;//返回这个表示该对象已改变,需要刷新

        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }


        @Override
        public int getCount() {
            return fragmentList.size();
        }
    }

    /**
     * 前往登陆页
     */
    @Override
    public void toLogin() {
        startActivity(new Intent(this, LoginActivity.class));
    }

    /**
     * 摧毁自己
     */
    @Override
    public void finishSelf() {
        finish();
    }

    /**
     * 调用裁剪头像功能
     *
     * @param intent
     * @param requestCode 请求标识，返回时携带的标志
     */
    @Override
    public void cropHeadImg(Intent intent, int requestCode) {
        startActivityForResult(intent, requestCode);
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
     * 设置头像
     */
    @Override
    public void setHeadImg() {
        if (meFragment != null && head != null) {
            Glide.with(this).load(Bitmap2Bytes(head))
                    .bitmapTransform(new CropCircleTransformation(this))
                    .into(meFragment.getMHeadImageView()); // 设置头像
            head = null; // 释放资源
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        int fragmentIndex = viewPager.getCurrentItem();
        meFragment = null;
        if (fragmentList.get(fragmentIndex) instanceof MeFragment) {
            meFragment = (MeFragment) fragmentList.get(fragmentIndex);
        }
        if (meFragment != null) {
            switch (requestCode) {
                case HomeConstants.GALLERY_RESULT_CODE + HomeConstants.HEAD_IMG:
                    if (resultCode == RESULT_OK) {
                        cropPhoto(data.getData());// 裁剪图片
                    }

                    break;
                case HomeConstants.CAMERA_RESULT_CODE + HomeConstants.HEAD_IMG:
                    if (resultCode == RESULT_OK) {
                        File temp = new File(Environment.getExternalStorageDirectory() + "/head.jpg");
                        cropPhoto(Uri.fromFile(temp));// 裁剪图片
                    }

                    break;
                case HomeConstants.CROP_RESULT_CODE:
                    if (data != null) {
                        Bundle extras = data.getExtras();
                        head = extras.getParcelable("data");
                        if (head != null) {
                            String fileName = setPicToSdCard(head);// 保存在SD卡中
                            mePresenter.postHeadImg(fileName, mobileUserVo.getUserId()); // 上传头像
                        }
                    }
                    break;
                default:
                    break;

            }
            super.onActivityResult(requestCode, resultCode, data);
        }

    }

    /**
     * 调用系统的裁剪功能
     *
     * @param uri
     */
    public void cropPhoto(Uri uri) {
        mePresenter.cropHeadImg(uri);
    }

    /**
     * 保存图像至sd卡
     *
     * @param mBitmap
     */
    private String setPicToSdCard(Bitmap mBitmap) {
        return mePresenter.setPicToSd(mBitmap);
    }

    /**
     * 把Bitmap转Byte
     */
    public static byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }
}