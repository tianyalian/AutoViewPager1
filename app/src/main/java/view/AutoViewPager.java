package view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ake.autoviewpager.R;

import java.util.ArrayList;
import java.util.List;


//是整个大布局包含viewpager
public class AutoViewPager extends LinearLayout {
	
    private final class OnNewsItemPageChangeListener implements OnPageChangeListener {
        @Override
        public void onPageSelected(int position) {
            //1. 改变标题
            tv_top_news_title.setText(list_text.get(position));
            //2. 指示点
            //(1).当前点 -- 红色  (Imageview.set)
            //(2).上一个点 -- 白色
            for (int i = 0; i < dots.size(); i++) {
                ImageView imageView = dots.get(i);
                if(i==position){
                    imageView.setImageResource(R.drawable.dot_focus);
                }else{
                    imageView.setImageResource(R.drawable.dot_normal);
                }
            }
        }
        
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
        @Override
        public void onPageScrollStateChanged(int state) {}
    }
    
    private RollViewPager viewPager;
    private TextView tv_top_news_title;
    //图片url的集合
	private List<String> listurl= new ArrayList<String>();
	//标题的集合
	private List<String> list_text= new ArrayList<String>();
    //指示点集合
    private List<ImageView> dots = new ArrayList<ImageView>();
	private LinearLayout ll_dots;
	private View view;
	
	
	
	//这个构造函数是给自己使用的,创建时给它添加一个viewpager的url集合,文字集合
	public AutoViewPager(Context context,List<String> lsiturl,List<String> list_text) {
		super(context);
		this.listurl=lsiturl;
		this.list_text=list_text;
		initView();
	}

	@SuppressLint("NewApi")
	public AutoViewPager(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView();
	}

	public AutoViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}
	
	//在大布局中添加viewpager
	private void initView() {
		view = View.inflate(getContext(), R.layout.layout_roll_view, null);
		LinearLayout ll_container=(LinearLayout) view.findViewById(R.id.ll_top_news_viewpager);
		tv_top_news_title=(TextView) view.findViewById(R.id.tv_top_news_title);
		ll_dots = (LinearLayout) view.findViewById(R.id.ll_dots);
		
		
		viewPager = new RollViewPager(getContext());
		System.out.println("new RollViewPager");
		//viewpager轮播图添加界面改变监听
        viewPager.setOnPageChangeListener(new OnNewsItemPageChangeListener());
        //rollViewPager 添加条目点击监听
        viewPager.setOnItemClickListener(new RollViewPager.OnItemClickListener()
        {
            @Override
            public void onItemClick(int position) {
                //弹出吐司  (轮播图的标题)
                Toast.makeText(getContext(), 
                		list_text.get(position), Toast.LENGTH_SHORT).show();
            }
        });
        LayoutParams params=new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        viewPager.setLayoutParams(params);
        ll_container.addView(viewPager);
		
        //把整个轮播图框架添加到这个linearlayout里面,这里不用添加布局参数,在用的时候直接写这个类的全类名
        //在布局文件中已经都生明了.
//		LayoutParams params1=new LayoutParams(LayoutParams.MATCH_PARENT, 420);
//		view.setLayoutParams(params1);
		addView(view);
	}
	
	//让轮播图开始旋转
	public void startRoll(){
		viewPager.startRoll();
		
	}

    //去掉显示的文字和黑色背景，圆点居中
    public void dotlayoutcenter() {
        tv_top_news_title.setVisibility(View.GONE);
        LinearLayout linelayout = (LinearLayout) findViewById(R.id.ll_bottem);
        linelayout.setBackgroundColor(Color.TRANSPARENT);

    }

    //是否开启三级缓存
    public  void  isopencache(Boolean isopen) {
        viewPager.isopen(isopen);

    }

    public void setData(List<String> lsiturl,List<String> list_text){
		this.listurl=lsiturl;
		this.list_text=list_text;
		viewPager.setData(lsiturl);
		addPoint();
	}

	 /**
     * 添加指示点
     */
    private void addPoint() {
        //移除点
        ll_dots.removeAllViews();
        dots.clear();
        for (int i = 0; i < listurl.size(); i++) {
            ImageView imageView = new ImageView(getContext());
            //默认第一个图片是红色
            if(i==0){
                imageView.setImageResource(R.drawable.dot_focus);
            }else{
                //其他的白色
                imageView.setImageResource(R.drawable.dot_normal);
            }
            //添加到线性布局
            LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            //间距
            params.leftMargin = 5;//px (dp-px)
            ll_dots.addView(imageView, params);
            //添加到集合中
            dots.add(imageView);
        }
        
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    	super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    	heightMeasureSpec=LayoutParams.WRAP_CONTENT;
    }
}
