package com.dataminer.crawler;

import java.io.IOException;
import java.util.Collection;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.dataminer.tfidf.ngram.NGram;
import com.google.common.collect.Lists;

public class Crawler extends Thread {

	public static void main(String[] args) throws IOException {

		// cn.bing
		Document doc = Jsoup.connect("http://cn.bing.com/search?q=big+data+learning").get();
		// System.out.println(doc.html());
		String resultsCount = doc.select("#b_tween > .sb_count").text();
		System.out.println(resultsCount);

		Elements eles = doc.select("ol#b_results > li.b_algo");

		//for (Element e : eles) {
		Element e = eles.first();
		String header = e.select("h2").text();
		String href = e.select("h2 > a").attr("href");
		System.out.println(href);
		
		String content = Jsoup.connect(href).get().text();
		Collection<String> nGramTerms = NGram.ngramDocumentTerms(Lists.newArrayList(1, 2, 3), content);
		for (String term : nGramTerms) {
			System.out.println(term);
		}
		
		
		

		// google
		// doc =
		// Jsoup.connect("https://www.google.com/search?q=big+data+learning").get();
		// System.out.println(doc.html());
		//
		// Elements e = doc.select("#resultStats");
		// e.size();
		// resultsCount = doc.select("#resultStats").text();
		// System.out.println(resultsCount);

	}

}
