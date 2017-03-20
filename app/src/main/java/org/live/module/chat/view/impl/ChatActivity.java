package org.live.module.chat.view.impl;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import org.live.R;
import org.live.common.listener.BackHandledFragment;
import org.live.common.listener.BackHandledInterface;

/**
 * 聊天模块
 */
public class ChatActivity extends FragmentActivity implements BackHandledInterface {
    private BackHandledFragment mBackHandedFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
    }

    @Override
    public void setSelectedFragment(BackHandledFragment selectedFragment) {
        this.mBackHandedFragment = selectedFragment;
    }

    @Override
    public void onBackPressed() {
        if (mBackHandedFragment == null || !mBackHandedFragment.onBackPressed()) {
            if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                super.onBackPressed();
            } else {
                getSupportFragmentManager().popBackStack();
            }
        }
    }
}
