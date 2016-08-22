package view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;



public class MyBitmapUtils {
    
    private Context context;
    public static boolean isopenCaceh=false;
    private LruCache<String, Bitmap> mLruCache;
    
    private File rootFile = null;
    Handler handler = new Handler();
    
    //线程池
    private Executor mExecutor;

    public MyBitmapUtils(Context context){
        this.context = context;
        //1.内存缓存 -- lrucache
        //(1).maxSize : 分配的最大内存空间
        int maxSize = (int) (Runtime.getRuntime().maxMemory()/8);//16m/8 = 2m
        mLruCache = new LruCache<String, Bitmap>(maxSize){
            /**
             * (2).存储对象占用内存空间的大小 : bitmap
             */
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getRowBytes() * bitmap.getHeight();//字节总长度 
            }
        };
        //2.本地缓存 -- 根路径
        rootFile = context.getCacheDir();// data/xxx/cache
        //3.网络缓存 -- 线程池
        mExecutor = Executors.newFixedThreadPool(3);//创建固定大小的线程池  : 3子线程
    }
    /**
     * 展示
     *
     *
     * @param imageUrl
     */
    public void display(ImageView imageview, String imageUrl) {
        /**
         * 三级缓存: ***
         * 1.内存缓存: 存储图片对象Bitmap. Map<key,value>:key:url,value:bitmap
         *           v4.LruCache<key,value>: 类似于hashmap. 管理内存中图片,算法Lru,less recent use : 将最近使用的图片,使用比较频繁的图保存起来.
         * 2.本地缓存: 将图片文件保存到本地:手机存储器/sd卡.
         *           mnt/sdcard/zhbj/cache/image/xxx.png
         *                           cache/file/
         *           xxx.png : xxx:图片名称, url+md5
         *                                 url 包含特殊字符.  linux 不允许特殊字符的文件名称
         * 3.网络缓存: 将图片文件缓存到网络
         *           缓存?不联网情况下能够获取的数据才叫做缓存.
         * 
         * 如何利用三级缓存加载图片:  优先选择速度最快的缓存.
         * 1. 先从内存缓存获取图片,如果获取到,直接展示.如果获取不到,再从本地获缓存取图片
         * 2. 从本地缓存获取图片,如果获取到,先加载到内存缓存,然后再展示.如果获取不到,最后从网络缓存获取图片
         * 3. 从网络缓存获取图片,如果获取到,先加载到内存缓存,再加载到本地缓存,最后展示. 
         *                   如果获取不到,展示默认图片.
         */

        if (isopenCaceh) {

        //1.从内存缓存获取图片
        Bitmap cacheBitmap = mLruCache.get(imageUrl);
        if(cacheBitmap!=null){
            imageview.setImageBitmap(cacheBitmap);
            Log.i("MyBitmapUtils", "从内存缓存获取");
            return;
        }
        //2.从本地缓存获取图片 
        // 确定图片路径  : 根路径+xxx.png
        File imageCacheFile = new File(rootFile, MD5Encoder.encode(imageUrl));
        //图片存在
        if(imageCacheFile.exists()&&imageCacheFile.length()>0){
            //(1).先加载到内存缓存  file -- bitmap
            Bitmap decodeImageFile = BitmapFactory.decodeFile(imageCacheFile.getAbsolutePath());
            mLruCache.put(imageUrl, decodeImageFile);
            //(2).然后再展示
            imageview.setImageBitmap(decodeImageFile);
            Log.i("MyBitmapUtils", "从本地缓存获取");
            return;
        }
        }

        //3.从网络缓存获取图片
        //联网 -- 子线程 (线程池)
        //获取tag
        int requestPosition = (Integer) imageview.getTag();
        mExecutor.execute(new MyDownLoadRunnable(imageview, imageUrl,requestPosition));
        Log.i("MyBitmapUtils", "从网络缓存获取");
    }

    class MyDownLoadRunnable implements Runnable{
        
        private ImageView imageview;
        private String imageUrl;
        private int requestPosition;//请求的位置


        public MyDownLoadRunnable(ImageView imageview, String imageUrl, int requestPosition){
            this.imageview = imageview;
            this.imageUrl = imageUrl;
            this.requestPosition =requestPosition;
        }

        @Override
        public void run() {
            //模拟真正网络环境

            // 请求网络,下载图片(ImageUrl--imageview) HttpUrlConnection
            try {
                URL url = new URL(imageUrl);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                //1.设置请求方式
                httpURLConnection.setRequestMethod("GET");
                //2.设置超时时间
                httpURLConnection.setConnectTimeout(5*1000);
                //3.获取响应码
                int code = httpURLConnection.getResponseCode();
                if(code == 200){
                    //4.获取图片流
                    InputStream inputStream = httpURLConnection.getInputStream();
                    //5.如果获取到,先加载到内存缓存,再加载到本地缓存,最后展示. 
                    //(1). 流 -- bitmap
                    final Bitmap decodeBitmapStream = BitmapFactory.decodeStream(inputStream);
                    //是否开启缓存
                    if (isopenCaceh) {
                    mLruCache.put(imageUrl, decodeBitmapStream);
                    //(2). 存储本地 流/bitmap -- file
                    //压缩1.format:压缩格式;2.int quality:质量,压缩率 30 -- 压缩70% , 100 -- 压缩0.
                        //3.outputStream : 写入本地的流
                    File file = new File(rootFile, MD5Encoder.encode(imageUrl));
                    OutputStream stream = new FileOutputStream(file);
                    decodeBitmapStream.compress(CompressFormat.PNG, 100, stream);
                    }

//                    (3).展示  主线程更新  runOnUIThread(); 
//                    MainActivity mainActivity = (MainActivity) context;
                   handler.post(new Runnable()
                    {
                        
                        @Override
                        public void run() {
                            //获取当前屏幕上imageview的tag
                            int screenPosition = (Integer) imageview.getTag();
                            if(requestPosition == screenPosition){
                                imageview.setImageBitmap(decodeBitmapStream);
                            }
                        }
                    });
                    
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            
        }
        
    }
    
}
