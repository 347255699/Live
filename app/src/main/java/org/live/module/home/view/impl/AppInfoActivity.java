package org.live.module.home.view.impl;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import net.steamcrafted.materialiconlib.MaterialIconView;

import org.live.R;

/**
 * Created by wang on 2017/5/12.
 */

public class AppInfoActivity extends Activity {


    private MaterialIconView returnBtn ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_info) ;
        returnBtn = (MaterialIconView) this.findViewById(R.id.btn_app_info_return) ;
        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish() ;
            }
        });
    }
}
