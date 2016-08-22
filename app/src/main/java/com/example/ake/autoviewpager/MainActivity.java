package com.example.ake.autoviewpager;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import utils.GlideHelper;

public class MainActivity extends AppCompatActivity implements PullToRefreshBase.OnRefreshListener2<ListView> {

    @InjectView(R.id.textView)
    TextView textView;
    @InjectView(R.id.imageView)
    ImageView imageView;
    private List<String> datas = new ArrayList<String>();

    private ArrayAdapter<String> adapter;

    public static final String URL_IMG2 = "http://img2.3lian.com/2014/f7/5/d/22.jpg";
    @InjectView(R.id.button)
    Button button;
    @InjectView(R.id.lv)
    PullToRefreshListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        Glide.with(this)
                .load(URL_IMG2)
                .transform(new GlideHelper.GlideRoundTransform(this))
                .into(imageView);

        lv.getRefreshableView().setDivider(null);
        lv.getRefreshableView().setSelector(android.R.color.transparent);
        lv.setOnRefreshListener(this);

        lv.setMode(PullToRefreshBase.Mode.BOTH);
        //这两个布尔值是:是否开启下拉刷新
        lv.getLoadingLayoutProxy(false, true).setPullLabel("下拉刷新");
        lv.getLoadingLayoutProxy(false, true).setRefreshingLabel("加载中");
        lv.getLoadingLayoutProxy(false, true).setReleaseLabel("释放刷新");

        lv.getLoadingLayoutProxy().setLoadingDrawable(getResources().getDrawable(R.mipmap.ic_launcher));
        for (int i = 0; i < 10; i++) {
            datas.add("我是数据"+i);
        }
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                android.R.id.text1,datas);
        lv.setAdapter(adapter);
    }


    public void click(View view) {
        Intent intents = new Intent(this, viewPager.class);
        startActivity(intents);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
    //撒发达发
    }

}
