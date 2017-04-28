package org.live.module.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.live.R;
import org.live.common.constants.LiveKeyConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends Activity {

    private TextView rtmpInputView ;

    private TextView roomNumInputView ;

    private Button playBtnView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main) ;
        rtmpInputView = (TextView) this.findViewById(R.id.tv_main_inputRtmp) ;
        roomNumInputView = (TextView) this.findViewById(R.id.tv_main_inputRoomNum) ;
        playBtnView = (Button) this.findViewById(R.id.btn_main_play) ;

        playBtnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String rtmpPlayIp = rtmpInputView.getText().toString()+roomNumInputView.getText().toString() ;
                Intent intent = new Intent(MainActivity.this, PlayDemoActivity.class) ;
                intent.putExtra("rtmpPlay", rtmpPlayIp) ;
                startActivity(intent);
            }
        });

    }
}
