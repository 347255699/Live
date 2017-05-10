package org.live.module.demo;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.live.R;
import org.live.common.constants.LiveConstants;
import org.live.module.login.view.impl.SplashActivity;

public class SettingActivity extends AppCompatActivity {

    private Button ipButton;
    private EditText ipEditText;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        context = this;
        ipEditText = (EditText) findViewById(R.id.et_ip);
        ipButton = (Button) findViewById(R.id.btn_ip);

        ipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ip = ipEditText.getText().toString();
                if (ip.length() > 0) {
                    LiveConstants.REMOTE_SERVER_IP = ip;
                    LiveConstants.REMOTE_SERVER_HTTP_IP = LiveConstants.HTTP_PREFIX + LiveConstants.REMOTE_SERVER_IP + ":" + LiveConstants.REMOTE_PORT;
                }
                startActivity(new Intent(context, SplashActivity.class));
            }
        });
    }
}
