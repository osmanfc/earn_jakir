package com.dailyreward.dailyrewardapp.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.dailyreward.dailyrewardapp.R;
import com.dailyreward.dailyrewardapp.utils.Constant;

public class ReferAndEarn extends AppCompatActivity {
    private ReferAndEarn activity;
    private TextView refer_code_textView, refer_decs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refer_and_earn);
        activity = this;


        refer_code_textView = findViewById(R.id.refer_code);
        refer_decs = findViewById(R.id.refer_decs);

        // refer_decs.setText(R.string.refer_desc);
        refer_decs.setText(Constant.getString(activity, Constant.REFER_TEXT));

        refer_code_textView.setText(Constant.getString(activity, Constant.USER_REFFER_CODE));
        findViewById(R.id.refer_and_win_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constant.referApp(activity, refer_code_textView.getText().toString());
            }
        });
    }
}