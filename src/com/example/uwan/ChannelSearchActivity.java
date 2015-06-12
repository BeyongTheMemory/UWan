package com.example.uwan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.text.Editable;
import android.text.TextWatcher;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class ChannelSearchActivity extends Activity {
	
	EditText eSearch;
	ImageView ivDeleteText;
	ListView mListView;
	
	ArrayList<Map<String, Object>> mData = new ArrayList<Map<String, Object>>();
	
	ArrayList<String> mListTitle = new ArrayList<String>();
	ArrayList<String> mListText = new ArrayList<String>();
	
	SimpleAdapter adapter;
	
	Handler myhandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.channel_search);
        
        set_eSearch_TextChanged();//设置eSearch搜索框的文本改变时监听器
        
        set_ivDeleteText_OnClick();//设置叉叉的监听器
        
        set_mListView_adapter();//给listview控件添加一个adapter
        
    }
    
    /**
     * 设置ListView的Adapter
     */
    private void set_mListView_adapter()
    {
    	mListView = (ListView) findViewById(R.id.mListView);
        
        getmData(mData);
        
        adapter = new SimpleAdapter(this,mData,android.R.layout.simple_list_item_1, 
			    new String[]{"title"},new int[]{android.R.id.text1});
	    
        mListView.setAdapter(adapter);
    }
    
    /**
     * 设置搜索框的文本更改时的监听器
     */
    private void set_eSearch_TextChanged()
    {
    	eSearch = (EditText) findViewById(R.id.etSearch);
    	
    	eSearch.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
				//这个应该是在改变的时候会做的动作吧，具体还没用到过。
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				//这是文本框改变之前会执行的动作
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				/**这是文本框改变之后 会执行的动作
				 * 因为我们要做的就是，在文本框改变的同时，我们的listview的数据也进行相应的变动，并且如一的显示在界面上。
				 * 所以这里我们就需要加上数据的修改的动作了。
				 */
				if(s.length() == 0){
					ivDeleteText.setVisibility(View.GONE);//当文本框为空时，则叉叉消失
				}
				else {
					ivDeleteText.setVisibility(View.VISIBLE);//当文本框不为空时，出现叉叉
				}
				
				myhandler.post(eChanged);
			}
		});
    	
    }
    
    
    
    Runnable eChanged = new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			String data = eSearch.getText().toString();
			
			mData.clear();//先要清空，不然会叠加
			
			getmDataSub(mData, data);//获取更新数据
			
			adapter.notifyDataSetChanged();//更新
			
		}
	};
	
	/**
	 * 获得根据搜索框的数据data来从元数据筛选，筛选出来的数据放入mDataSubs里
	 * @param mDataSubs
	 * @param data
	 */
	
	private void getmDataSub(ArrayList<Map<String, Object>> mDataSubs, String data)
	{
		int length = mListTitle.size();
		for(int i = 0; i < length; ++i){
			if(mListTitle.get(i).contains(data) || mListText.get(i).contains(data)){
				Map<String,Object> item = new HashMap<String,Object>();
				item.put("title", mListTitle.get(i));
		        item.put("text",  mListText.get(i));
		        mDataSubs.add(item);
			}
		}
	}
	
	/**
	 * 设置叉叉的点击事件，即清空功能
	 */
    
    private void set_ivDeleteText_OnClick()
    {
    	ivDeleteText = (ImageView) findViewById(R.id.ivDeleteText);
    	ivDeleteText.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				eSearch.setText("");
			}
		});
    }
    
    /**
     * 获得元数据 并初始化mDatas
     * @param mDatas
     */

    private void getmData(ArrayList<Map<String, Object>> mDatas)
    {
    	Map<String, Object> item = new HashMap<String, Object>();
    	mListTitle.add("这是一个标题!");
    	mListText.add("这是文本.\n2014.09.18.19.50");
    	
    	item.put("title", mListTitle.get(0));
    	item.put("text", mListText.get(0));
    	mDatas.add(item);
    	
    	
    	mListTitle.add("这是另一个标题!");
    	mListText.add("这是另一个文本.\n2014.09.18.19.51");
    	
    	item = new HashMap<String, Object>();
    	item.put("title", mListTitle.get(1));
    	item.put("text", mListText.get(1));
    	mDatas.add(item);
    }
}
