package com.example.uwan;


import static android.view.Gravity.START;

import java.util.ArrayList;




import com.example.uwan.adapte.NewsFragmentPagerAdapter;
import com.example.uwan.app.AppApplication;
import com.example.uwan.bean.ChannelItem;
import com.example.uwan.bean.ChannelManage;
import com.example.uwan.fragment.NewsFragment;
import com.example.uwan.tool.BaseTools;
import com.example.uwan.tool.DateTools;
import com.example.uwan.view.ColumnHorizontalScrollView;
import com.example.uwan.view.HeadListView.IXListViewListener;
import com.example.uwan.widget.DrawerArrowDrawable;


import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Toast;
import android.widget.SearchView.OnCloseListener;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;

public class MainActivity extends FragmentActivity{
	  private DrawerArrowDrawable drawerArrowDrawable;
	  private float offset;
	  private boolean flipped;
	  private ViewPager mViewPager;//显示新闻概要的view
	  private ColumnHorizontalScrollView mColumnHorizontalScrollView;
	  private LinearLayout mRadioGroup_content;//放频道的容器
	  private LinearLayout ll_more_columns;//添加新频道
	  private RelativeLayout rl_column;//整个频道容器
	  private ArrayList<Fragment> fragments = new ArrayList<Fragment>();
	  /** 当前选中的栏目*/
		private int columnSelectIndex = 0;
		/** 屏幕宽度 */
		private int mScreenWidth = 0;
		/** Item宽度 */
		private int mItemWidth = 0;
		/** 左阴影部分*/
		public ImageView shade_left;
		/** 右阴影部分 */
		public ImageView shade_right;
		/** 用户选择的新闻分类列表*/
		private ArrayList<ChannelItem> userChannelList=new ArrayList<ChannelItem>();
		/** 请求CODE */
		public final static int CHANNELREQUEST = 1;
		/** 调整返回的RESULTCODE */
		public final static int CHANNELRESULT = 10;
		private Handler mHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mScreenWidth = BaseTools.getWindowsWidth(this);
		mItemWidth = mScreenWidth / 7;// 一个Item宽度为屏幕的1/7
		mHandler = new Handler();
        initActionBar();//初始化头部
        initContent();
    }
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		switch (requestCode) {
		case CHANNELREQUEST:
			if(resultCode == CHANNELRESULT){
				setChangelView();
			}
			break;

		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}



	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
	private long mExitTime;
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
				if ((System.currentTimeMillis() - mExitTime) > 2000) {
					Toast.makeText(this, "在按一次退出",
							Toast.LENGTH_SHORT).show();
					mExitTime = System.currentTimeMillis();
				} else {
					finish();
				}
			return true;
		}
		//拦截MENU按钮点击事件，让他无任何操作
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	/**
	 * 
	 */
	private void initContent(){
		mColumnHorizontalScrollView =  (ColumnHorizontalScrollView)findViewById(R.id.mColumnHorizontalScrollView);
		mRadioGroup_content = (LinearLayout) findViewById(R.id.mRadioGroup_content);
		mViewPager = (ViewPager) findViewById(R.id.mViewPager);
		shade_left = (ImageView) findViewById(R.id.shade_left);
		shade_right = (ImageView) findViewById(R.id.shade_right);
		rl_column = (RelativeLayout) findViewById(R.id.rl_column);
		ll_more_columns = (LinearLayout) findViewById(R.id.ll_more_columns);
		ll_more_columns.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				//频道管理
				Intent intent_channel = new  Intent(getApplicationContext(), ChannelActivity.class);
				startActivityForResult(intent_channel, CHANNELREQUEST);
				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			}
			
		});
		setChangelView();
	}
	/** 
	 *  当栏目项发生变化时候调用
	 * */
	private void setChangelView() {
		initColumnData();
		initTabColumn();
		initFragment();
	}
	
	/** 获取Column栏目 数据*/
	private void initColumnData() {
		userChannelList = ((ArrayList<ChannelItem>)ChannelManage.getManage(AppApplication.getApp().getSQLHelper()).getUserChannel());
	}
	/** 
	 *  初始化Column栏目项
	 * */
	private void initTabColumn() {
		mRadioGroup_content.removeAllViews();
		int count =  userChannelList.size();
		mColumnHorizontalScrollView.setParam(this, mScreenWidth, mRadioGroup_content, shade_left, shade_right, ll_more_columns, rl_column);
		for(int i = 0; i< count; i++){
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mItemWidth , LayoutParams.WRAP_CONTENT);
			params.leftMargin = 5;
			params.rightMargin = 5;
//			TextView localTextView = (TextView) mInflater.inflate(R.layout.column_radio_item, null);
			TextView columnTextView = new TextView(this);
			columnTextView.setTextAppearance(this, R.style.top_category_scroll_view_item_text);
//			localTextView.setBackground(getResources().getDrawable(R.drawable.top_category_scroll_text_view_bg));
			columnTextView.setBackgroundResource(R.drawable.radio_buttong_bg);
			columnTextView.setGravity(Gravity.CENTER);
			columnTextView.setPadding(5, 5, 5, 5);
			columnTextView.setId(i);
			columnTextView.setText(userChannelList.get(i).getName());
			columnTextView.setTextColor(getResources().getColorStateList(R.drawable.top_category_scroll_text_color_day));
			if(columnSelectIndex == i){
				columnTextView.setSelected(true);
			}
			columnTextView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
			          for(int i = 0;i < mRadioGroup_content.getChildCount();i++){
				          View localView = mRadioGroup_content.getChildAt(i);
				          if (localView != v)
				        	  localView.setSelected(false);
				          else{
				        	  localView.setSelected(true);
				              mViewPager.setCurrentItem(i);
				          }
			          }
			          Toast.makeText(getApplicationContext(), userChannelList.get(v.getId()).getName(), Toast.LENGTH_SHORT).show();
				}
			});
			mRadioGroup_content.addView(columnTextView, i ,params);
		}
	}
	
	/** 
	 *  初始化Fragment
	 * */
	private void initFragment() {
		fragments.clear();//清空
		int count =  userChannelList.size();
		for(int i = 0; i< count;i++){
			Bundle data = new Bundle();
    		data.putString("text", userChannelList.get(i).getName());
    		data.putInt("id", userChannelList.get(i).getId());
    		data.putInt("iscity",userChannelList.get(i).getIsCity());
			NewsFragment newfragment = new NewsFragment();
			newfragment.setArguments(data);
			fragments.add(newfragment);
		}
		NewsFragmentPagerAdapter mAdapetr = new NewsFragmentPagerAdapter(getSupportFragmentManager(), fragments);
//		mViewPager.setOffscreenPageLimit(0);
		mViewPager.setAdapter(mAdapetr);
		mViewPager.setOnPageChangeListener(pageListener);
		
	}
	
	
	private void initActionBar(){
		 initLeftMenu();
	     initSeachView();
	}
	
    private void initSeachView() {
		final ImageView searchView = (ImageView)findViewById(R.id.sv_iv);
	//	 TextView tv_actionbar_title = (TextView)findViewById(R.id.tv_actionbar_title);
		searchView.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				onSearchRequested();// 激活搜索对话框
			}
			
		});
//		searchView.setSubmitButtonEnabled(true);
//		 searchView.setOnQueryTextListener(new OnQueryTextListener() {
//   
//
//			@Override
//			public boolean onQueryTextChange(String arg0) {
//				tv_actionbar_title.setVisibility(View.GONE);
//                return true;
//			}
//
//			@Override
//			public boolean onQueryTextSubmit(String arg0) {
//				// TODO Auto-generated method stub
//				tv_actionbar_title.setVisibility(View.VISIBLE);
//				return false;
//			}
//		 });
//		 searchView.setOnCloseListener(new OnCloseListener() {
//             
//             @Override
//             public boolean onClose() {
//            		tv_actionbar_title.setVisibility(View.VISIBLE);
//            		//返回false收起输入框，true不收
//                     return false;
//             }
//       });
	 	
	}

    
    private void initLeftMenu(){
    	final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        final ImageView imageView = (ImageView) findViewById(R.id.drawer_indicator);
        final Resources resources = getResources();

        drawerArrowDrawable = new DrawerArrowDrawable(resources);
        drawerArrowDrawable.setStrokeColor(resources.getColor(R.color.light_gray));
        imageView.setImageDrawable(drawerArrowDrawable);

        drawer.setDrawerListener(new DrawerLayout.SimpleDrawerListener() {
          @Override public void onDrawerSlide(View drawerView, float slideOffset) {
            offset = slideOffset;

            // Sometimes slideOffset ends up so close to but not quite 1 or 0.
            if (slideOffset >= .995) {
              flipped = true;
              drawerArrowDrawable.setFlip(flipped);
            } else if (slideOffset <= .005) {
              flipped = false;
              drawerArrowDrawable.setFlip(flipped);
            }

            drawerArrowDrawable.setParameter(offset);
          }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
          @Override public void onClick(View v) {
            if (drawer.isDrawerVisible(START)) {
              drawer.closeDrawer(START);
            } else {
              drawer.openDrawer(START);
            }
          }
        });
    }
	/** 
	 *  ViewPager切换监听方法
	 * */
	public OnPageChangeListener pageListener= new OnPageChangeListener(){

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageSelected(int position) {
			// TODO Auto-generated method stub
			mViewPager.setCurrentItem(position);
			selectTab(position);
		}
	};
	/** 
	 *  选择的Column里面的Tab
	 * */
	private void selectTab(int tab_postion) {
		columnSelectIndex = tab_postion;
		for (int i = 0; i < mRadioGroup_content.getChildCount(); i++) {
			View checkView = mRadioGroup_content.getChildAt(tab_postion);
			int k = checkView.getMeasuredWidth();
			int l = checkView.getLeft();
			int i2 = l + k / 2 - mScreenWidth / 2;
			// rg_nav_content.getParent()).smoothScrollTo(i2, 0);
			mColumnHorizontalScrollView.smoothScrollTo(i2, 0);
			// mColumnHorizontalScrollView.smoothScrollTo((position - 2) *
			// mItemWidth , 0);
		}
		//判断是否选中
		for (int j = 0; j <  mRadioGroup_content.getChildCount(); j++) {
			View checkView = mRadioGroup_content.getChildAt(j);
			boolean ischeck;
			if (j == tab_postion) {
				ischeck = true;
			} else {
				ischeck = false;
			}
			checkView.setSelected(ischeck);
		}
	}

}
