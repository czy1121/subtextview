package com.github.czy1121.subtextview.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.github.czy1121.view.SubTextView;


public class MainActivity extends AppCompatActivity implements View.OnClickListener  {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.stv1).setOnClickListener(this);
        findViewById(R.id.stv2).setOnClickListener(this);


     }

    boolean mIsSubtitle = false;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.stv1:
            SubTextView stv1 = (SubTextView)v;
            stv1.setSubtitle(mIsSubtitle);
            mIsSubtitle = !mIsSubtitle;
            stv1.setSubText("呵呵哒");
            break;
        case R.id.stv2:
            SubTextView stv2 = (SubTextView)v;
            stv2.setSubTextColor(0xffff0000);
            stv2.setSubtitle(mIsSubtitle);
            mIsSubtitle = !mIsSubtitle;
            break;

        }
    }
}
