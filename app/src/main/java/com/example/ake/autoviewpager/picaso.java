package com.example.ake.autoviewpager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;
import butterknife.InjectView;
import utils.PicassoHelper;

public class picaso extends AppCompatActivity {

    @InjectView(R.id.imageView2)
    ImageView imageView2;
    public static final String URL_IMG2 = "http://img2.3lian.com/2014/f7/5/d/22.jpg";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picaso);
        ButterKnife.inject(this);
        Picasso.with(this).load(URL_IMG2). transform(new PicassoHelper.RoundedTransformation(40,10)).into(imageView2);



    }
}
