package com.example.uwan;


import static android.view.Gravity.START;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;




import com.example.uwan.adapte.NewsFragmentPagerAdapter;
import com.example.uwan.app.AppApplication;
import com.example.uwan.bean.ChannelItem;
import com.example.uwan.bean.ChannelManage;
import com.example.uwan.fragment.NewsFragment;
import com.example.uwan.tool.BaseTools;
import com.example.uwan.tool.DateTools;
import com.example.uwan.tool.Util;
import com.example.uwan.view.ColumnHorizontalScrollView;
import com.example.uwan.view.HeadListView.IXListViewListener;
import com.example.uwan.widget.DrawerArrowDrawable;

import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
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
	  private ViewPager mViewPager;//��ʾ���Ÿ�Ҫ��view
	  private ColumnHorizontalScrollView mColumnHorizontalScrollView;
	  private LinearLayout mRadioGroup_content;//��Ƶ��������
	  private LinearLayout ll_more_columns;//�����Ƶ��
	  private RelativeLayout rl_column;//����Ƶ������
	  private ArrayList<Fragment> fragments = new ArrayList<Fragment>();
	  /** ��ǰѡ�е���Ŀ*/
		private int columnSelectIndex = 0;
		/** ��Ļ��� */
		private int mScreenWidth = 0;
		/** Item��� */
		private int mItemWidth = 0;
		/** ����Ӱ����*/
		public ImageView shade_left;
		/** ����Ӱ���� */
		public ImageView shade_right;
		/** �û�ѡ������ŷ����б�*/
		private ArrayList<ChannelItem> userChannelList=new ArrayList<ChannelItem>();
		/** ����CODE */
		public final static int CHANNELREQUEST = 1;
		/** �������ص�RESULTCODE */
		public final static int CHANNELRESULT = 10;
		private Handler mHandler;
		
		private ImageButton ib_qq;//QQ��½
    @SuppressLint("NewApi") @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mScreenWidth = BaseTools.getWindowsWidth(this);
		mItemWidth = mScreenWidth / 7;// һ��Item���Ϊ��Ļ��1/7
		mHandler = new Handler(){
			@Override
			public void handleMessage(Message msg){
				if(msg.what == 1){
					Bitmap bitmap = (Bitmap) msg.obj;
					Drawable d = new BitmapDrawable(bitmap);
					ib_qq.setBackground(d);
				}
			}
		};
        initActionBar();//��ʼ��ͷ��
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
					Toast.makeText(this, "�ڰ�һ���˳�",
							Toast.LENGTH_SHORT).show();
					mExitTime = System.currentTimeMillis();
				} else {
					finish();
				}
			return true;
		}
		//����MENU��ť����¼����������κβ���
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
				//Ƶ������
				Intent intent_channel = new  Intent(getApplicationContext(), ChannelActivity.class);
				startActivityForResult(intent_channel, CHANNELREQUEST);
				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			}
			
		});
		setChangelView();
	}
	/** 
	 *  ����Ŀ����仯ʱ�����
	 * */
	private void setChangelView() {
		initColumnData();
		initTabColumn();
		initFragment();
	}
	
	/** ��ȡColumn��Ŀ ����*/
	private void initColumnData() {
		userChannelList = ((ArrayList<ChannelItem>)ChannelManage.getManage(AppApplication.getApp().getSQLHelper()).getUserChannel());
	}
	/** 
	 *  ��ʼ��Column��Ŀ��
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
	 *  ��ʼ��Fragment
	 * */
	private void initFragment() {
		fragments.clear();//���
		int count =  userChannelList.size();
		for(int i = 0; i< count;i++){
			Bundle data = new Bundle();
    		data.putString("text", userChannelList.get(i).getName());
    		data.putInt("id", userChannelList.get(i).getId());
    		data.putInt("iscity",userChannelList.get(i).getIsCity());
			NewsFragment newfragment = new NewsFragment();
			newfragment.setChannel(userChannelList.get(i).getName());
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
				onSearchRequested();// ���������Ի���
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
//            		//����false���������true����
//                     return false;
//             }
//       });
	 	
	}
	Tencent mTencent;
	// �����ǵ���QQ��¼�Ĺؼ�����
	public void LoginQQ() {
		// �����APP_ID�뻻����Ӧ�������APP_ID��������ʹ�õ���DEMO�йٷ��ṩ�Ĳ���APP_ID 222222
		String mAppid = "222222";
		// ��һ����������������˵�������APPID���ڶ�����ȫ�ֵ�Context�����ģ���仰ʵ���˵���QQ��¼
		 mTencent = Tencent.createInstance(mAppid, getApplicationContext());
		/**
		 * ͨ�������룬SDKʵ����QQ�ĵ�¼�����������������������һ��������context�����ģ��ڶ�������SCOPO
		 * ��һ��String���͵��ַ�������ʾһЩȨ�� �ٷ��ĵ��е�˵����Ӧ����Ҫ�����ЩAPI��Ȩ�ޣ��ɡ������ָ������磺SCOPE =
		 * ��get_user_info,add_t��������Ȩ���á�all��
		 * ��������������һ���¼���������IUiListener�ӿڵ�ʵ���������õ��Ǹýӿڵ�ʵ����
		 */
		mTencent.login(MainActivity.this, "all", new BaseUiListener());

	}
	/**
	 * ���Զ���ļ�����ʵ��IUiListener�ӿں󣬱���Ҫʵ�ֽӿڵ����������� onComplete onCancel onError
	 * �ֱ��ʾ��������¼�ɹ���ȡ�� ������
	 */
	ProgressDialog dialog = null;
	private class BaseUiListener implements IUiListener {

		public void onCancel() {
			// TODO Auto-generated method stub

		}

		public void onComplete(Object response) {
		
			dialog = new ProgressDialog(MainActivity.this);
			 dialog.setMessage("���ڵ�¼");
			dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			
			// dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.show();
			// TODO Auto-generated method stub
			// Toast.makeText(getApplicationContext(), "��¼�ɹ�", 0).show();
			try {
				// ��õ�������JSON��ʽ�ģ���������õ�����
				// ����㲻֪�����ܻ��ʲô����һ�������LOG
				Log.e("uwan", "-------------" + response.toString());
				String openidString = ((JSONObject) response).getString("openid");
				// openidTextView.setText(openidString);
				Log.e("uwan", "-------------" + openidString);
				// access_token= ((JSONObject)
				// response).getString("access_token"); //expires_in =
				// ((JSONObject) response).getString("expires_in");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			/**
			 * �����Ѿ����OpneID�Լ����������õ�������
			 * QQ��¼�ɹ��ˣ����ǻ����ȡһЩQQ�Ļ�����Ϣ�������ǳƣ�ͷ��ʲô�ģ����ʱ����ô�죿
			 * sdk�������ṩ��һ����UserInfo��������з�װ��QQ�û���һЩ��Ϣ����ô����ͨ��������õ���Щ��Ϣ
			 * ��εõ����UserInfo���أ�
			 */
			QQToken qqToken = mTencent.getQQToken();
			UserInfo info = new UserInfo(getApplicationContext(), qqToken);
			// �������Ǿ��õ�������ˣ�֮��Ĳ����͸������һ���ˣ�ͬ���ǽ���JSON
			info.getUserInfo(new IUiListener() {

				public void onComplete(final Object response) {
					// TODO Auto-generated method stub
					Log.e("uwan", "---------------111111");
					Message msg = new Message();
					msg.obj = response;
					msg.what = 0;
					mHandler.sendMessage(msg);
					// nicknameString = response.toString();
					Log.e("uwan", "-----111---" + response.toString());
					/**
					 * ����ͼƬ��Ҫ������������ʹ�����̣߳��������������������Ϣֱ�� ��mHandler����в���
					 * 
					 */

					new Thread() {
						@Override
						public void run() {
						//	 TODO Auto-generated method stub
							JSONObject json = (JSONObject) response;
							Bitmap bitmap = null;
							try {
								bitmap = Util.getbitmap(json
										.getString("figureurl_qq_2"));
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							Message msg = new Message();
							msg.obj = bitmap;
							msg.what = 1;
							mHandler.sendMessage(msg);
							dialog.dismiss();
//							customView.setVisibility(View.INVISIBLE);
						}
					}.start();
				}

				public void onCancel() {
					Log.e("Tag", "--------------111112");
					// TODO Auto-generated method stub
				}

				public void onError(UiError arg0) {
					// TODO Auto-generated method stub
					Log.e("Tag", "-111113" + ":" + arg0.toString());
				}

			});

			// startActivity(new
			// Intent(MainActivity.this,PersionalActivity.class));
		}

		public void onError(UiError arg0) {
			// TODO Auto-generated method stub

		}

	}
    private void initLeftMenu(){
        ib_qq = (ImageButton)findViewById(R.id.ib_qq);
    	ib_qq.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				LoginQQ();
			}
    		
    	});
    	
    	
     	RelativeLayout 	rl_local= (RelativeLayout)findViewById(R.id.rl_local);
    	rl_local.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent_local = new  Intent(MainActivity.this, LocalActivity.class);
				startActivity(intent_local);
			}
    		
    	});
    	
    	
    	RelativeLayout 	rl_collect= (RelativeLayout)findViewById(R.id.rl_collect);
    	rl_collect.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent_collect = new  Intent(MainActivity.this, CollectActivity.class);
				startActivity(intent_collect);
			}
    		
    	});
    	
    	RelativeLayout rl_record= (RelativeLayout)findViewById(R.id.rl_record);
    	rl_record.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent_record = new  Intent(MainActivity.this, RecordActivity.class);
				startActivity(intent_record);
			}
    		
    	});
    	
    	
    	RelativeLayout rl_option= (RelativeLayout)findViewById(R.id.rl_option);
    	rl_option.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent_record = new  Intent(MainActivity.this, MenuSettingActivity.class);
				startActivity(intent_record);
			}
    		
    	});
    	
    	
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
	 *  ViewPager�л���������
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
	 *  ѡ���Column�����Tab
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
		//�ж��Ƿ�ѡ��
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
