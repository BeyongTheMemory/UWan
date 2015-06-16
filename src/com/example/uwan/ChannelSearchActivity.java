package com.example.uwan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.text.Editable;
import android.text.TextWatcher;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;

public class ChannelSearchActivity extends Activity {
	
	EditText eSearch;
	ImageView ivDeleteText;
	ListView mListView;
	
	ArrayList<Map<String, Object>> mData = new ArrayList<Map<String, Object>>();
	
	ArrayList<String> mListTitle = new ArrayList<String>();
	//ArrayList<String> mListText = new ArrayList<String>();
	
	SimpleAdapter adapter;
	
	Handler myhandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.channel_search);
        
        RelativeLayout rd_addchannel = (RelativeLayout)findViewById(R.id.rd_addchannel);
        rd_addchannel.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent add = new Intent(ChannelSearchActivity.this,AddChannelActivity.class);
				startActivity(add);
			}
        	
        });
        
        set_eSearch_TextChanged();//����eSearch��������ı��ı�ʱ������
        
        set_ivDeleteText_OnClick();//���ò��ļ�����
        
        set_mListView_adapter();//��listview�ؼ����һ��adapter
        
    }
    
    /**
     * ����ListView��Adapter
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
     * ������������ı�����ʱ�ļ�����
     */
    private void set_eSearch_TextChanged()
    {
    	eSearch = (EditText) findViewById(R.id.etSearch);
    	
    	eSearch.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
				//���Ӧ�����ڸı��ʱ������Ķ����ɣ����廹û�õ�����
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				//�����ı���ı�֮ǰ��ִ�еĶ���
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				/**�����ı���ı�֮�� ��ִ�еĶ���
				 * ��Ϊ����Ҫ���ľ��ǣ����ı���ı��ͬʱ�����ǵ�listview������Ҳ������Ӧ�ı䶯��������һ����ʾ�ڽ����ϡ�
				 * �����������Ǿ���Ҫ�������ݵ��޸ĵĶ����ˡ�
				 */
				if(s.length() == 0){
					ivDeleteText.setVisibility(View.GONE);//���ı���Ϊ��ʱ��������ʧ
				}
				else {
					ivDeleteText.setVisibility(View.VISIBLE);//���ı���Ϊ��ʱ�����ֲ��
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
			
			mData.clear();//��Ҫ��գ���Ȼ�����
			
			getmDataSub(mData, data);//��ȡ��������
			
			adapter.notifyDataSetChanged();//����
			
		}
	};
	
	/**
	 * ��ø��������������data����Ԫ����ɸѡ��ɸѡ���������ݷ���mDataSubs��
	 * @param mDataSubs
	 * @param data
	 */
	
	private void getmDataSub(ArrayList<Map<String, Object>> mDataSubs, String data)
	{
		int length = mListTitle.size();
		for(int i = 0; i < length; ++i){
			if(mListTitle.get(i).contains(data)){
				Map<String,Object> item = new HashMap<String,Object>();
				item.put("title", mListTitle.get(i));
		        mDataSubs.add(item);
			}
		}
	}
	
	/**
	 * ���ò��ĵ���¼�������չ���
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
     * ���Ԫ���� ����ʼ��mDatas
     * @param mDatas
     */

    private void getmData(ArrayList<Map<String, Object>> mDatas)
    {
    	mListTitle.add("�ƾ�");
    	mListTitle.add("���");
    	mListTitle.add("����");
    	mListTitle.add("����");
    	mListTitle.add("����");
    	mListTitle.add("����");
    	mListTitle.add("����");
    	mListTitle.add("����");
    	mListTitle.add("����");
    	mListTitle.add("����");
    	mListTitle.add("����");
    	mListTitle.add("ʱ��");
    	mListTitle.add("��Ц");
    	mListTitle.add("����");
    	mListTitle.add("����");
    	mListTitle.add("ְ��");
    	
    	for(String title:mListTitle){
    		Map<String, Object> item = new HashMap<String, Object>();
    		item.put("title", title);
    		mDatas.add(item);
    	}
    	
    	//item.put("title", mListTitle.get(0));
//    	mDatas.add(item);
//    	
//    	
//    	mListTitle.add("������һ������!");
//    	
//    	item = new HashMap<String, Object>();
//    	item.put("title", mListTitle.get(1));
//    	mDatas.add(item);
    }
}
