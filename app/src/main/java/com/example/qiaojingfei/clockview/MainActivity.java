package com.example.qiaojingfei.clockview;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ClockView clockView;
    private int seconds;
    private Button timer_btn;
    private TextView tv_total_time;
    private Button btn_reset;
    private boolean isStop = true;
    public static final int START = 1;
    public static final int STOP = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    private void initViews() {
        clockView = (ClockView) findViewById(R.id.clockView);
        timer_btn = (Button) findViewById(R.id.timer_btn);
        tv_total_time = (TextView) findViewById(R.id.tv_total_time);
        btn_reset = (Button) findViewById(R.id.btn_reset);
        timer_btn.setOnClickListener(this);
        btn_reset.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.timer_btn:
                isStop = !isStop;
                if (!isStop) {
                    timer_btn.setText("停止计时");
                    handler.sendEmptyMessage(START);
                } else {
                    timer_btn.setText("开始计时");
                    handler.sendEmptyMessage(STOP);
                }
                break;
            case R.id.btn_reset:
                seconds = 0;
                tv_total_time.setText(seconds + "s");
                clockView.setTime(0);
                break;
        }

    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            handler.removeMessages(START);
            switch (msg.what) {
                case START:
                    seconds++;
                    clockView.setTime(seconds % 60);
                    tv_total_time.setText(seconds + "s");
                    handler.sendEmptyMessageDelayed(START, 1000);
                    break;
                case STOP:
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}
