package com.crosx.app.UI.Activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.crosx.app.BaseActivity;
import com.crosx.app.R;
import com.crosx.app.UI.Fragment.IMFragment;
import com.crosx.app.Utils.FragmentTabHost;

public class MainActivity extends BaseActivity {

    private FragmentTabHost mFTH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        marginTop(findViewById(R.id.main_actionBar));
        mFTH = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mFTH.setup(this, getSupportFragmentManager(), R.id.realContent);
        initTabs();
    }

    private void initTabs() {
        addTabs("IM", "消息", IMFragment.class, null);
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
