package com.dataminer.crawler;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collection;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.dataminer.tfidf.ngram.NGram;
import com.google.common.collect.Lists;

public class BingCN {
	private static final String BING_URL = "http://cn.bing.com/search?q=";

	public static long getResultCount(String q) throws UnsupportedEncodingException, IOException {
		Document doc = Jsoup.connect(BING_URL + URLEncoder.encode(q, "UTF-8")).get();
		String resultsCount = doc.select("#b_tween > .sb_count").text();
		if (StringUtils.isEmpty(resultsCount)) return 0;
		return Long.parseLong(resultsCount.substring(0, resultsCount.length()-3).replace(",", "").trim());
	}

	public static void getQueryResults(String q) throws IOException {

		Document doc = Jsoup.connect(BING_URL + URLEncoder.encode(q, "UTF-8")).get();
		Elements eles = doc.select("ol#b_results > li.b_algo");

		// for (Element e : eles) {
		Element e = eles.first();
		String header = e.select("h2").text();
		String href = e.select("h2 > a").attr("href");
		System.out.println(href);

		String content = Jsoup.connect(href).get().text();
		Collection<String> nGramTerms = NGram.ngramDocumentTerms(Lists.newArrayList(1, 2, 3), content);
		for (String term : nGramTerms) {
			System.out.println(term);
		}

	}

	public static Collection<String> sample(String q) throws IOException {

		Document doc = Jsoup.connect(BING_URL + URLEncoder.encode(q, "UTF-8")).get();
		Elements eles = doc.select("ol#b_results > li.b_algo");

		// for (Element e : eles) {
		Element e = eles.first();
		String header = e.select("h2").text();
		String href = e.select("h2 > a").attr("href");
		System.out.println(href);

		String content = Jsoup.connect(href).get().text();
		Collection<String> nGramTerms = NGram.ngramDocumentTerms(Lists.newArrayList(1, 2, 3), content);
		return nGramTerms;
	}

}
