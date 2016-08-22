package utils;

import android.os.Handler;

import java.io.IOException;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by 瑜哥 on 2016/8/20.
 */
public class OkHttpHelper  {

    public static final String  BASEURL= "http://127.0.0.1:8090";
    private OkHttpClient okHttpClient = new OkHttpClient();
    Handler handler = new Handler();
    String url;

    public OkHttpHelper(String url) {
        this.url = url;
    }

    /**
     * 同步的get请求
     * @return
     */
    public String getSync(){
        //请求对象
        Request request = new Request.Builder()
                .url(url)
                .build();

        try {
            Response response = okHttpClient.newCall(request).execute();//同步方法
            if (response.isSuccessful()){
                return response.body().string();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return  null;
    }


    /**
     * 异步的get请求
     * @param callback
     */
    public void getAsync(Callback callback){
        //请求对象
        Request request = new Request.Builder()
                .url(url)
                .build();

        okHttpClient.newCall(request).enqueue(callback);
    }


}
