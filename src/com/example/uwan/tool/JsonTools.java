package com.example.uwan.tool;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.uwan.bean.NewsEntity;

public class JsonTools {
public static void JsonToNews(String result,ArrayList<NewsEntity> newsArray){
	try {
		JSONArray array = new JSONArray(result);
	    for(int i=0;i<array.length();i++){
	    	JSONObject newsJson = array.getJSONObject(i);
	    	NewsEntity news = new NewsEntity();
	    	news.setId(newsJson.optInt("id"));
	    	news.setNewsId(newsJson.optInt("id"));
	    	news.setCollectStatus(false);//ÊÇ·ñÊÕ²Ø
	    	 Random rdm = new Random(System.currentTimeMillis());
	    	news.setCommentNum(Math.abs(rdm.nextInt())%130);//ÆÀÂÛ
	    	news.setInterestedStatus(true);//ÊÇ·ñÓÐÐËÈ¤
	    	news.setLikeStatus(true);//Ï²»¶×´Ì¬
			news.setReadStatus(false);//ÊÕ²Ø×´Ì¬
			news.setNewsCategory(newsJson.optString("class"));
			news.setNewsCategoryId(1);
			news.setSource_url(newsJson.optString("url"));
			news.setSource(newsJson.optString("source"));
			news.setTitle(newsJson.optString("title"));
			List<String> url_list = new ArrayList<String>();
//			if(newsJson.optString("picture1URL") != null && !newsJson.optString("picture1URL").equals("null")){
				news.setPicOne(newsJson.optString("picture1URL"));
				url_list.add(newsJson.optString("picture1URL"));
		//	}
			
			news.setPicList(url_list);
			news.setPublishTime(Long.valueOf(DateTools.getTime()));
			news.setReadStatus(false);
			news.setMark(newsJson.optInt("id"));
			if(i>8){
				news.setPublishTime(Long.valueOf(DateTools.getTime()) - 86400);
			}
			System.out.println(news.toString());
			newsArray.add(news);
	    }
	} catch (JSONException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
}
}
