package com.example.uwan.asyncTask;
/**
 * 下载事故信息
 */
import java.io.IOException;
import java.util.ArrayList;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import com.example.uwan.adapte.NewsAdapter;
import com.example.uwan.bean.NewsEntity;
import com.example.uwan.fragment.NewsFragment;
import com.example.uwan.tool.JsonTools;
import com.example.uwan.tool.UrlUtil;
import com.example.uwan.view.HeadListView;



import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;

public class GetNewsAsync extends AsyncTask<String, Void,String[]>{
//	private String param;
	//private String url = "http://wcmc.csu.edu.cn:5906/accident/select_time";
//private String url = "http://192.168.1.166:9001/accident/select_time";
	private String url;
	private NewsFragment newsFragment;
	private ProgressDialog progressDialog = null; //进度条
    private ArrayList<NewsEntity> newsArray;
    private  NewsAdapter mAdapter;
    private HeadListView mListView;
    private int end = -1;
	public GetNewsAsync(String param,NewsFragment newsFragment,ArrayList<NewsEntity> newsArray,NewsAdapter mAdapter,HeadListView mListView){
		super();
		this.url = UrlUtil.url+param;
		this.newsFragment =  newsFragment;
	    this.newsArray = newsArray;
	    this.mAdapter = mAdapter;
	    this.mListView = mListView;
	}
	public GetNewsAsync(String param,NewsFragment newsFragment,ArrayList<NewsEntity> newsArray,NewsAdapter mAdapter,HeadListView mListView,int end){
		super();
		this.url = UrlUtil.url+param;
		this.newsFragment =  newsFragment;
	    this.newsArray = newsArray;
	    this.mAdapter = mAdapter;
	    this.mListView = mListView;
	    this.end = end;
	}

	protected void onPreExecute(){
		super.onPreExecute();
		progressDialog = ProgressDialog.show(newsFragment.activity, "请稍等...", "正在获取数据...", true);
	}
	
	protected String[] doInBackground(String... params) {
		HttpGet get = new HttpGet(url);
	    get.addHeader("Content-Type", "text/html;charset=UTF-8");  
		HttpClient httpClient = new DefaultHttpClient();
		HttpResponse httpResponse;
				 String[] r = new String[2];
		try {
			httpResponse = httpClient.execute(get);
			  if (httpResponse.getStatusLine().getStatusCode() == 200) {
		            // 获取服务器响应的字符串
		        	r[0] = "200";
				    r[1] = EntityUtils.toString(httpResponse.getEntity());
				    System.out.println(r[1]);
			  }
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			r[0] = e.toString();
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			r[0] = e.toString();
			e.printStackTrace();
		}
     
		return r;
        }
		
//		HttpPost httpPost = new HttpPost(url);  
//		httpPost.setHeader("source","lf5m33d4-0545-4ca5-9efd-a16web6ef8b2h");
//		httpPost.setHeader("Content-Type","application/json");
//		httpPost.setHeader("Cookie", "token2014");
//		StringEntity entity;
//		 String[] r = new String[2];
//		  
//		try {
//			entity = new StringEntity(param.toString(),HTTP.UTF_8);
//			entity.setContentType("application/json");
//			entity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
//			httpPost.setEntity(entity);
//			HttpResponse httpResponse = new DefaultHttpClient().execute(httpPost);
//			String tag=httpResponse.getStatusLine().getStatusCode()+"";
//  		    String result = EntityUtils.toString(httpResponse.getEntity());
//  		    r[0] = tag;
//		    r[1] = result;
//  		    return r;
//		} catch (UnsupportedEncodingException e) {
//			r[0] = e.toString();
//			e.printStackTrace();
//		} catch (ClientProtocolException e) {
//			r[0] = e.toString();
//			e.printStackTrace();
//		} catch (IOException e) {
//			r[0] = e.toString();
//			e.printStackTrace();
//		}
	
	
	protected void onPostExecute(String[] result) {
		 progressDialog.dismiss();
		   if( result[0].equals("200")){
		   System.out.println(result[1]);
		   if(end == -1){
		   JsonTools.JsonToNews(result[1], newsArray);
			mAdapter = new NewsAdapter(newsFragment.activity, newsArray);
			mListView.setAdapter(mAdapter);
			newsFragment.onLoad();
		   }
		   else{
			   ArrayList<NewsEntity> tempnewsList = new ArrayList<NewsEntity>();
			   JsonTools.JsonToNews(result[1], tempnewsList);
				for(NewsEntity tempnew:tempnewsList){
					newsArray.add(tempnew);
					}
				mAdapter.notifyDataSetChanged();
				newsFragment.onLoad();
		   }
//		   JsonTools.JsontoWords(result[1], accidents);
//		   System.out.println(accidents.toString());
//		   mNodeList.clear();
//		   for(int i = 0;i < accidents.size();i++){
//				AccidentInfo node1 = new AccidentInfo(accidents.get(i).getCars().size()+"", 
//						accidents.get(i).getLocationdomain().getAddress(), 
//						accidents.get(i).getEventReason().getHurt_man(),
//						accidents.get(i).getAccidentDate(),accidents.get(i).getCasualtie().getTotal_dead(), 
//						accidents.get(i).getCasualtie().getTotal_hurt(),accidents.get(i).getUsername(),accidents.get(i).getUseraccount());
//				node1.setAccident(accidents.get(i));
//				mNodeList.add(node1);
//		}
//		mAdapter = new QueryItemAdapter(activity, mNodeList);
//		mListView.setAdapter(mAdapter);// 填充
		   }
		   else{
			 
			   new AlertDialog.Builder(newsFragment.activity)  
	            .setTitle("获取数据失败")
	            .setMessage("错误代码:"+result[0]+",请确保网络通畅！")
	            .setPositiveButton("确定", null)
	            .show();
		   }
	}
	

}
