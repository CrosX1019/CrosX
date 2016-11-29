package com.crosx.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.crosx.app.ui.im.IMFragment;
import com.crosx.app.ui.vr.VideoFragment;
import com.crosx.app.utils.BaseActivity;
import com.crosx.app.utils.FragmentTabHost;

public class MainActivity extends BaseActivity {

    private FragmentTabHost mFTH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFTH = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mFTH.setup(this, getSupportFragmentManager(), R.id.realContent);
        initTabs();
    }

    private void initTabs() {
        addTabs("IM", "消息", IMFragment.class, null);
        addTabs("Video", "视频", VideoFragment.class, null);
    }

    private void addTabs(String tag, String labelValue, Class clzss, Bundle args) {
        FragmentTabHost.TabSpec tab = mFTH.newTabSpec(tag);
        View v = LayoutInflater.from(this).inflate(R.layout.item_tab, null);
        TextView label = (TextView) v.findViewById(R.id.label);
        label.setText(labelValue);
        tab.setIndicator(v);
        //最终将Tab添加到TabHost
        mFTH.addTab(tab, clzss, args);
    }
}
