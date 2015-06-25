package com.example.uwan.fragment;
/**
 * 所有新闻概要展示fragment
 */
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;


import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Text;

import com.example.uwan.CityListActivity;
import com.example.uwan.DetailsActivity;
import com.example.uwan.R;
import com.example.uwan.adapte.NewsAdapter;
import com.example.uwan.asyncTask.GetNewsAsync;
import com.example.uwan.bean.NewsEntity;
import com.example.uwan.tool.Constants;
import com.example.uwan.tool.DateTools;
import com.example.uwan.tool.JsonTools;
import com.example.uwan.tool.UrlUtil;
import com.example.uwan.view.HeadListView;
import com.example.uwan.view.HeadListView.IXListViewListener;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class NewsFragment extends Fragment implements IXListViewListener{
	private final static String TAG = "NewsFragment";
	public Activity activity;
	ArrayList<NewsEntity> newsList = new ArrayList<NewsEntity>();
	public HeadListView mListView;
	public NewsAdapter mAdapter;
	String text;
	int channel_id;
	int iscity;
	ImageView detail_loading;
	public final static int SET_NEWSLIST = 0;
	//Toast提示框
	private RelativeLayout notify_view;
	private TextView notify_view_text;
	private String channel;//频道
	
	
	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Bundle args = getArguments();
		text = args != null ? args.getString("text") : "";
		channel_id = args != null ? args.getInt("id", 0) : 0;
		iscity =  args != null ? args.getInt("iscity") : 0;
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		this.activity = activity;
		super.onAttach(activity);
	}
	/** 此方法意思为fragment是否可见 ,可见时候加载数据 */
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		if (isVisibleToUser) {
			//fragment可见时加载数据
			if(newsList !=null && newsList.size() !=0){
				handler.obtainMessage(SET_NEWSLIST).sendToTarget();
			}else{
				new Thread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						try {
							Thread.sleep(2);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						handler.obtainMessage(SET_NEWSLIST).sendToTarget();
					}
				}).start();
			}
		}else{
			//fragment不可见时不执行操作
		}
		super.setUserVisibleHint(isVisibleToUser);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		System.out.println("xxx");
		View view = LayoutInflater.from(getActivity()).inflate(R.layout.news_fragment, null);
		System.out.println(view);
		mListView = (HeadListView) view.findViewById(R.id.mListView);
		mListView.setXListViewListener(this);
		TextView item_textview = (TextView)view.findViewById(R.id.item_textview);
		detail_loading = (ImageView)view.findViewById(R.id.detail_loading);
		//Toast提示框
		notify_view = (RelativeLayout)view.findViewById(R.id.notify_view);
		notify_view_text = (TextView)view.findViewById(R.id.notify_view_text);
		item_textview.setText(text);
		
		initData();
		return view;
	}

	private void initData() {
		newsList = new ArrayList<NewsEntity>();
		//String param = getChannel()+"/0/10";
		String param ="";
		try {
			param =  URLEncoder.encode("传媒", "UTF-8")+"/20/10";
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		GetNewsAsync getNews = new GetNewsAsync(param,this,newsList,mAdapter,mListView);
		getNews.execute();
	}
	
	public Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case SET_NEWSLIST:
			//	newsList = Constants.getNewsList();//测试用
				detail_loading.setVisibility(View.GONE);
				if(mAdapter == null){
					mAdapter = new NewsAdapter(activity, newsList);
					//判断是不是城市的频道
					if(iscity ==1){
						//是城市频道
						mAdapter.setCityChannel(true);
						initCityChannel();
					}
				}
				mListView.setAdapter(mAdapter);
				mListView.setOnScrollListener(mAdapter);
				mListView.setPinnedHeaderView(LayoutInflater.from(activity).inflate(R.layout.list_item_section, mListView, false));
				mListView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						//进入新闻详情页面
					Intent intent = new Intent(activity, DetailsActivity.class);
						if(iscity == 1){//城市列表上面多了一个
							if(position == 1){//城市选择
								Intent city = new Intent(activity, CityListActivity.class);
								startActivity(city);
							}
						    else if(position != 0  && position <= mAdapter.getCount()){
								intent.putExtra("news", mAdapter.getItem(position-2));
								startActivity(intent);
								activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
							}else if(position > mAdapter.getCount()){
								onLoadMore();
							}
						}else{
							if(position != 0 && position <= mAdapter.getCount()){
							Log.v("click里的Position",position+"");
							intent.putExtra("news", mAdapter.getItem(position-1));
							startActivity(intent);
							activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
							}else if(position > mAdapter.getCount()){//加载更多
								onLoadMore();
							}
						}
					}
				});
				if(channel_id == 1){
					initNotify();
				}
				break;
			default:
				break;
			}
			
			super.handleMessage(msg);
		}
	};
	
	/* 初始化选择城市的header*/
	public void initCityChannel() {
		View headview = LayoutInflater.from(activity).inflate(R.layout.city_category_list_tip, null);
		TextView chose_city_tip = (TextView) headview.findViewById(R.id.chose_city_tip);
		chose_city_tip.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//城市选择
			// TODO Auto-generated method stub
				Intent intent = new Intent(activity, CityListActivity.class);
				startActivity(intent);
			}
		});
		mListView.addHeaderView(headview);
	}
	
	/* 初始化通知栏目*/
	private void initNotify() {
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				notify_view_text.setText(String.format(getString(R.string.ss_pattern_update), 10));
				notify_view.setVisibility(View.VISIBLE);
				new Handler().postDelayed(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						notify_view.setVisibility(View.GONE);
					}
				}, 2000);
			}
		}, 1000);
	}
	/* 摧毁视图 */
	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		Log.d("onDestroyView", "channel_id = " + channel_id);
		mAdapter = null;
	}
	/* 摧毁该Fragment，一般是FragmentActivity 被摧毁的时候伴随着摧毁 */
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.d(TAG, "channel_id = " + channel_id);
	}

	public void onLoad() {
		mListView.stopRefresh();
		mListView.stopLoadMore();
		mListView.setRefreshTime(DateTools.getNewsDetailsDate(DateTools.getTime()));
	}
	@Override
	public void onRefresh() {
		handler.postDelayed(new Runnable() {
			public void run() {
			
//				// mAdapter.notifyDataSetChanged();
//				HttpGet get = new HttpGet(UrlUtil.url+getChannel()+"/0/10");
//				HttpClient httpClient = new DefaultHttpClient();
//				HttpResponse httpResponse;
//				 String[] r = new String[2];
//					try {
//						httpResponse = httpClient.execute(get);
//						  if (httpResponse.getStatusLine().getStatusCode() == 200) {
//					            // 获取服务器响应的字符串
//					        	r[0] = "200";
//							    r[1] = EntityUtils.toString(httpResponse.getEntity());
//							    JsonTools.JsonToNews(r[1], newsList);
//							    System.out.println(r[1]);
//						  }
//					} catch (ClientProtocolException e) {
//						// TODO Auto-generated catch block
//						r[0] = e.toString();
//						e.printStackTrace();
//					} catch (IOException e) {
//						// TODO Auto-generated catch block
//						r[0] = e.toString();
//						e.printStackTrace();
//					}
//			//	newsList = Constants.getNewsList();//测试用
//				mAdapter = new NewsAdapter(activity, newsList);
//				mListView.setAdapter(mAdapter);
//				onLoad();
				newsList = new ArrayList<NewsEntity>();
				String param = getChannel()+"/0/10";
				//String param ="0/0/10";
				GetNewsAsync getNews = new GetNewsAsync(param,NewsFragment.this,newsList,mAdapter,mListView);
				getNews.execute();
				
			}
		}, 2000);
		
	}

	@Override
	public void onLoadMore() {
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
//			ArrayList<NewsEntity> tempnewsList = new ArrayList<NewsEntity>();
//			HttpGet get = new HttpGet(UrlUtil.url+getChannel()+"/"+newsList.get(newsList.size()).getId()+"/10");
//			HttpClient httpClient = new DefaultHttpClient();
//			HttpResponse httpResponse;
//			 String[] r = new String[2];
//				try {
//					httpResponse = httpClient.execute(get);
//					  if (httpResponse.getStatusLine().getStatusCode() == 200) {
//				            // 获取服务器响应的字符串
//				        	r[0] = "200";
//						    r[1] = EntityUtils.toString(httpResponse.getEntity());
//						    JsonTools.JsonToNews(r[1], tempnewsList);
//							for(NewsEntity tempnew:tempnewsList){
//								newsList.add(tempnew);
//								}
//					  }
//				} catch (ClientProtocolException e) {
//					// TODO Auto-generated catch block
//					r[0] = e.toString();
//					e.printStackTrace();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					r[0] = e.toString();
//					e.printStackTrace();
//				}
//			
//		
//				mAdapter.notifyDataSetChanged();
//				onLoad();
				newsList = new ArrayList<NewsEntity>();
				//String param = getChannel()+"/0/10";
				String param =getChannel()+"/"+newsList.get(newsList.size()).getId()+"/10";
				GetNewsAsync getNews = new GetNewsAsync(param,NewsFragment.this,newsList,mAdapter,mListView,newsList.get(newsList.size()).getId());
				getNews.execute();
			}
		}, 2000);
		
	}
}
