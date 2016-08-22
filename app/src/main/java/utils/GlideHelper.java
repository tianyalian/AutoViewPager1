package utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

/**
 * Created by 瑜哥 on 2016/8/20.
 */
public class GlideHelper {
    //显示圆角图片
    //使用方法
    // Glide.with(this)
//    .load(URL)
//    .transform(new GlideRoundTransform(this))
//            .into(imageView);
//    1. 添加依赖 compile 'com.github.bumptech.glide:glide:3.7.0' , 同时还依赖于supportV4.如果没有请自行添加
//    2. 添加权限:
//
//    <uses-permission android:name="android.permission.INTERNET"/>
//
//            3. 加载图片.示例代码:
//
//            Glide
//                    .with(this) // 指定Context
//            .load(URL_GIF)// 指定图片的URL
//    .placeholder(R.mipmap.ic_launcher)// 指定图片未成功加载前显示的图片
//    .error(R.mipmap.ic_launcher)// 指定图片加载失败显示的图片
//    .override(300, 300)//指定图片的尺寸
//    .fitCenter()//指定图片缩放类型为fitCenter
//    .centerCrop()// 指定图片缩放类型为centerCrop
//    .skipMemoryCache(true)// 跳过内存缓存
//    .diskCacheStrategy(DiskCacheStrategy.NONE)//跳过磁盘缓存
//    .diskCacheStrategy(DiskCacheStrategy.SOURCE)//仅仅只缓存原来的全分辨率的图像
//    .diskCacheStrategy(DiskCacheStrategy.RESULT)//仅仅缓存最终的图像
//    .diskCacheStrategy(DiskCacheStrategy.ALL)//缓存所有版本的图像
//    .priority(Priority.HIGH)//指定优先级.Glide 将会用他们作为一个准则，并尽可能的处理这些请求，但是它不能保证所有的图片都会按照所要求的顺序加载。优先级排序:IMMEDIATE > HIGH > NORMAL >　LOW
//    .into(imageView);//指定显示图片的ImageView

    public  static  class GlideRoundTransform extends BitmapTransformation {

        private  float radius = 0f;

        public GlideRoundTransform(Context context) {
            this(context,90);
        }

        public GlideRoundTransform(Context context, int dp) {
            super(context);
            this.radius = Resources.getSystem().getDisplayMetrics().density * dp;
        }

        @Override protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
            return roundCrop(pool, toTransform);
        }

        private  Bitmap roundCrop(BitmapPool pool, Bitmap source) {
            if (source == null) return null;

            Bitmap result = pool.get(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
            if (result == null) {
                result = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
            }

            Canvas canvas = new Canvas(result);
            Paint paint = new Paint();
            paint.setShader(new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
            paint.setAntiAlias(true);
            RectF rectF = new RectF(0f, 0f, source.getWidth(), source.getHeight());
            canvas.drawRoundRect(rectF, radius, radius, paint);
            return result;
        }

        @Override public String getId() {
            return getClass().getName() + Math.round(radius);
        }
    }


    //显示圆形图片
    public  static class GlideCircleTransform extends BitmapTransformation {
        public GlideCircleTransform(Context context) {
            super(context);
        }

        @Override
        protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
            return circleCrop(pool, toTransform);
        }

        private  Bitmap circleCrop(BitmapPool pool, Bitmap source) {
            if (source == null) return null;

            int size = Math.min(source.getWidth(), source.getHeight());
            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            // TODO this could be acquired from the pool too
            Bitmap squared = Bitmap.createBitmap(source, x, y, size, size);

            Bitmap result = pool.get(size, size, Bitmap.Config.ARGB_8888);
            if (result == null) {
                result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
            }

            Canvas canvas = new Canvas(result);
            Paint paint = new Paint();
            paint.setShader(new BitmapShader(squared, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
            paint.setAntiAlias(true);
            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);
            return result;
        }

        @Override
        public String getId() {
            return getClass().getName();
        }
    }
}
