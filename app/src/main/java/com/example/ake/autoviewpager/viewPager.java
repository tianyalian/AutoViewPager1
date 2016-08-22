package com.example.ake.autoviewpager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import view.AutoViewPager;

public class viewPager extends AppCompatActivity {

    @InjectView(R.id.autoviewpager)
    AutoViewPager autoviewpager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);
        ButterKnife.inject(this);



        List<String> strings = new ArrayList<>();
        strings.add("");
        strings.add("");
        List<String> strings1 = new ArrayList<>();
        strings1.add("http://img3.3lian.com/2013/v8/89/d/81.jpg");
        strings1.add("http://img3.3lian.com/2013/v8/89/d/82.jpg");
        autoviewpager.setData(strings1, strings);
        //是否让图片居中
//        autoviewpager.dotlayoutcenter();
        //是否开启三级缓存
//        autoviewpager.opencache(true);
        autoviewpager.startRoll();

    }
}
