package view;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;
/**
 * 可以滚动的子Viewpager
 * @author wangdh
 *
 */
public class RollViewPager extends ViewPager {
    
    private Handler handler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    //切换下一个图片
//                    Log.i("RollViewPager", "切换下一张图片");
                	if (topImageUrls.size()!=0) {
						
                		int nextItem = (getCurrentItem()+1)%topImageUrls.size();
                		setCurrentItem(nextItem );
                		//循环
                		handler.sendEmptyMessageDelayed(1, 2000);
					}
                break;
                
                default:
                break;
            }
        };
    };
  //数据源
    private List<String> topImageUrls=new ArrayList<String>();
    //bitmapUtils初始化
    private MyBitmapUtils bitmapUtils;
    
    public RollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        bitmapUtils  = new MyBitmapUtils(getContext());
    }

    public RollViewPager(Context context) {
        super(context);
        bitmapUtils  = new MyBitmapUtils(getContext());
    }
    /**
     * 传递数据
     * @param topImageUrls
     */
    public void setData(List<String> topImageUrls) {
        this.topImageUrls = topImageUrls;
    }
    /**
     * 开始轮播
     */
    public void startRoll() {
        if(adapter == null){
            //设置适配器
            adapter = new MyRollPagerAdapter();
            setAdapter(adapter);
        }
        //开启轮播
        handler.sendEmptyMessageDelayed(1, 2000);
    }

    public void isopen(Boolean isopen) {
        bitmapUtils.isopenCaceh=isopen;
    }

    class MyRollPagerAdapter extends PagerAdapter{

        @Override
        public int getCount() {
        	System.out.println("getCount() :"+topImageUrls.size());
        	System.out.println("getLayoutParams().height:"+getLayoutParams().height);
        	
            return topImageUrls.size();
        }
        
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
			// ImageView imageView = (ImageView) View.inflate(getContext(), R.layout.viewpager_item, null);
			ImageView imageView = new ImageView(getContext());
			imageView.setScaleType(ImageView.ScaleType.FIT_XY);
			 imageView.setTag(position);
            //展示网络图片
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
             *
             *          
             *                    
             */
            if (topImageUrls.size()!=0) {
            	String imageUrl = topImageUrls.get(position);
				
            	//1.T 泛型:展示图片的控件
//                      ImageView / ImageButton  / 其他
            	//2.uri:图片网络url
            	bitmapUtils.display(imageView, imageUrl);
            	System.out.println(imageUrl);
			}
            container.addView(imageView);
            return imageView;
        }




        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
        
    }
    /**
     * 当前控件挂载到界面上
     */
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        //当控件挂载到屏幕上的时候开始轮播
//        startRoll();
        System.out.println("开始轮播");
    }
    /**
     * 当前控件从界面移除
     */
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        //停止轮播图
        handler.removeCallbacksAndMessages(null);
    }
    private int downX = 0;
    private int downY = 0;
    //按下时间
    private int downTime = 0;
    private MyRollPagerAdapter adapter;
    private OnItemClickListener onItemClickListener;
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        //子viewpager可以检测到down,一些move事件
        Log.i("RollViewPager", "action:"+ev.getAction());
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
            downX = (int) ev.getX();
            downY = (int) ev.getY();
            downTime = (int) System.currentTimeMillis();
            //大喊一声 爹 把事件给我 (请求不允许中断)
            getParent().requestDisallowInterceptTouchEvent(true);
            //停止轮播
            handler.removeCallbacksAndMessages(null);
            break;
            case MotionEvent.ACTION_MOVE:
            int moveX = (int) ev.getX();
            int moveY = (int) ev.getY();
            int disX = Math.abs(moveX - downX);
            int disY = Math.abs(moveY - downY);
            //一.如果是左右滑动 , 安装下边逻辑处理
            if(disX>disY){
                /**
                 * 1. 如果处于第一张图片 并且 向右滑动  (moveX > downX) 交给父亲  
                 * 2. 如果处于最后一张图片 并且 向左滑动  (moveX < downX) 交给父亲
                 * 3. 其他情况 自己处理
                 */
               
                //1. 如果处于第一张图片 并且 向右滑动  (moveX > downX) 交给父亲  
                if(getCurrentItem()==0&&moveX>downX){
                    getParent().requestDisallowInterceptTouchEvent(false);
                }else if(getCurrentItem() == topImageUrls.size()-1 && moveX<downX){
                    //2. 如果处于最后一张图片 并且 向左滑动  (moveX < downX) 交给父亲
                    getParent().requestDisallowInterceptTouchEvent(false);
                }else{
                    //其他情况 自己处理
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
            }else{
                //二.如果是上下滑动, 应该交给父亲
                getParent().requestDisallowInterceptTouchEvent(false);
            }
            
            
            break;
            case MotionEvent.ACTION_UP:
                startRoll();
                /**
                 * 如何回调onItemclick()?  onItemClickListener.onItemclick();
                 *  单击事件: down/up
                 *  单击事件 / 长按事件  : down/up时间间隔     小于500ms认为是单击
                 *  单击事件 / 拖动事件  : down/up距离间隔     防抖动效果: 5px  (水平/竖直)
                 */
                int upTime = (int) System.currentTimeMillis();
                int disTime = upTime - downTime;  //<500ms
                
                int upX = (int) ev.getX();
                int upY = (int) ev.getY();
                disX = Math.abs(upX - downX);
                disY = Math.abs(upY - downY);
                
                if(disTime<=500 && disX<=5 && disY<=5){
                    //单击事件
                    if(onItemClickListener!=null){
                        onItemClickListener.onItemClick(getCurrentItem());//position:当前显示的界面,点击的界面索引
                    }
                }
                
            break;
            case MotionEvent.ACTION_CANCEL:
                startRoll();
            break;
            
            default:
            break;
        }
        return super.onTouchEvent(ev);
    }
    /**
     * 1. 定义接口 监听  OnItemClickListener
     * 2. 暴露设置条目监听方法 : setOnItemClickListener();
     * 3. 回调 OnItemClickListener 监听的方法
     *      如何回调onItemclick()?  onItemClickListener.onItemclick();
     *          单击事件: down/up
     *              单击事件 / 长按事件  : down/up时间间隔     小于500ms认为是单击
     *              单击事件 / 拖动事件  : down/up距离间隔     防抖动效果: 5px  (水平/竖直)
     *              
     */
    public interface OnItemClickListener{
        //条目单击监听回调方法
        //position:点击的界面索引
        void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    
}
