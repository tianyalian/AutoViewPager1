package utils;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.view.animation.Transformation;

/**
 * Created by 瑜哥 on 2016/8/20.
 */
public class PicassoHelper {

//    1. 添加依赖 compile 'com.squareup.picasso:picasso:2.5.2'
//            2. 添加权限:
//
//    <uses-permission android:name="android.permission.INTERNET"/>
//            3. 加载图片,示例代码:
//
//            Picasso.with(this)// 指定Context
//            .load(URL_IMG3) //指定图片URL
//    .placeholder(R.mipmap.ic_launcher) //指定图片未加载成功前显示的图片
//    .error(R.mipmap.ic_launcher)// 指定图片加载失败显示的图片
//    .resize(300, 300)// 指定图片的尺寸
//    .fit()// 指定图片缩放类型为fit
//    .centerCrop()// 指定图片缩放类型为centerCrop
//    .centerInside()// 指定图片缩放类型为centerInside
//    .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)// 指定内存缓存策略
//    .priority(Picasso.Priority.HIGH)// 指定优先级
//    .into(imageView); // 指定显示图片的ImageView

//    4. 显示圆形图片.示例代码:

    // 自定义Transformation
    // 自定义Transformation
    Transformation transform = new Transformation() {

        public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());
            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;
            Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
            if (squaredBitmap != source) {
                source.recycle();
            }
            Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());
            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            BitmapShader shader = new BitmapShader(squaredBitmap,
                    BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
            paint.setShader(shader);
            paint.setAntiAlias(true);
            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);
            squaredBitmap.recycle();
            return bitmap;
        }


        public String key() {
            return "circle";
        }
    };


//    5. 显示圆角图片

    public static  class RoundedTransformation implements com.squareup.picasso.Transformation {
        private final int radius;
        private final int margin;  // dp

        // radius is corner radii in dp
        // margin is the board in dp
        public RoundedTransformation(final int radius, final int margin) {
            this.radius = radius;
            this.margin = margin;
        }

        @Override
        public Bitmap transform(final Bitmap source) {
            final Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setShader(new BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));

            Bitmap output = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(output);
            canvas.drawRoundRect(new RectF(margin, margin, source.getWidth() - margin, source.getHeight() - margin), radius, radius, paint);

            if (source != output) {
                source.recycle();
            }

            return output;
        }

//        Picasso
//                .with(this)// 指定Context
//                .load(URL_IMG2) //指定图片URL
//        .transform(transform) // 指定图片转换器
//        .into(imageView); // 指定显示图片的ImageView
        @Override
        public String key() {
            return "rounded(radius=" + radius + ", margin=" + margin + ")";
        }
    }
}
