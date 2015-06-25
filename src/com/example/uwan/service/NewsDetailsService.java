package com.example.uwan.service;
/**
 * 该类用于提取新闻内容
 */
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class NewsDetailsService {
	public static String getComment(String url, String news_title,
			String news_date) {
		Document document;
		 String data ="";
		try {
			document = Jsoup.connect(url).timeout(9000).get();
			 data =   document.toString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	   
		return data;
	}
	
	public static String getNewsDetails(String url, String news_title,
			String news_date) {
//		String data = "<body>" +
//				"<center><h2 style='font-size:16px;'>" + news_title + "</h2></center>";
//		data = data + "<p align='left' style='margin-left:10px'>" 
//				+ "<span style='font-size:10px;'>" 
//				+ news_date
//				+ "</span>" 
//				+ "</p>";
//		data = data + "<hr size='1' />";
			String data= "<body><center><h2 style='font-size:16px;'>该条新闻已删除！</h2></center></body>";
		try {
			Document document = Jsoup.connect(url).timeout(9000).get();
		    document.toString();
		    document.select("div.cover_content").remove();
		    document.select("header").remove();
		    document.select("div.wx_app_card").remove();
		    document.select("div.article_op").remove();
		    document.select("section.section_wrap").remove();
		    document.select("footer").remove();
			data = document.toString() + "<style type=\"text/css\">ul{ margin:0; padding:0; list-style-type:none;} " +
					"ul#navlist{font:12px verdana;padding-bottom: 13px;}ul#navlist li span{ background: #FBFBFB;}ul#navlist li{float: left; height: 30px; border: 0px solid #cccccc; width: 100%;}"
				+"ul#navlist .list1{border-bottom: 1px solid #cccccc;width: 100%;margin-bottom: -15px;}#navlist a{display: block;color: #666;text-decoration: none;padding: 6px 5px;width: 100%;text-align: center;}" +
				"</style><ul id=\"navlist\"><li class=\"list1\"></li><li class=\"list2\"><a href=\"\"><span>内容来自网络，不代表UWan新闻观点</span></a></li></ul>"+"javascript:(function(){"
				+ "var objs = document.getElementsByTagName(\"img\");"
				+ "var imgurl=''; " + "for(var i=0;i<objs.length;i++)  " + "{"
				+ "imgurl+=objs[i].src+',';"
				+ "    objs[i].onclick=function()  " + "    {  "
				+ "        window.imagelistner.openImage(imgurl);  "
				+ "    }  " + "}" + "})()";


			//System.out.println(document.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		 
	
//		Document document = null;
//		String data = "<body>" +
//				"<center><h2 style='font-size:16px;'>" + news_title + "</h2></center>";
//		data = data + "<p align='left' style='margin-left:10px'>" 
//				+ "<span style='font-size:10px;'>" 
//				+ news_date
//				+ "</span>" 
//				+ "</p>";
//		data = data + "<hr size='1' />";
//		try {
//			document = Jsoup.connect(url).timeout(9000).get();
//			Element element = null;
//			if (TextUtils.isEmpty(url)) {//现在并不是这种方法
//				data = "";
//				element = document.getElementById("memberArea");
//			} else {
//				element = document.getElementById("artibody");//
//			}
//			if (element != null) {
//				data = data + element.toString();
//			}
//			System.out.println(data);
//			data = data + "</body>";
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	
		return data;
	}

}
