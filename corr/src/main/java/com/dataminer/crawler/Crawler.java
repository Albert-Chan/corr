package com.dataminer.crawler;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Crawler extends Thread {

	public Crawler(String roomName, int roomId) {

	}
	
	
	public static void main(String[] args) throws IOException {
		Document doc = Jsoup.connect("http://cn.bing.com/search?q=big+data+learning").get();
		String resultsCount = doc.select("#b_tween > .sb_count").text();
		System.out.println(resultsCount);
	}
	


}
