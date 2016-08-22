package com.example.ake.autoviewpager;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.ButterKnife;
import butterknife.InjectView;
import okhttp3.Call;
import okhttp3.OkHttpClient;

public class okhttp extends AppCompatActivity {

    public static final String URL_GET = "http://apis.juhe.cn/mobile/get?phone=13812345678&key=daf8fa858c330b22e342c882bcbac622";
    public static final String URL_POST = "http://apis.juhe.cn/mobile/get ";
    @InjectView(R.id.textView2)
    TextView textView2;
    private OkHttpClient okHttpClient;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_okhttp);
        ButterKnife.inject(this);

        OkHttpUtils
                .get()
                .url(URL_GET)
                .build()
                .execute(new StringCallback()
                {

                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        textView2.setText(response.toString());
                    }

                });




    }

}