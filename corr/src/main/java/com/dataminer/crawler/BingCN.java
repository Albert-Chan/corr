package com.dataminer.crawler;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collection;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.dataminer.tfidf.ngram.NGram;
import com.google.common.collect.Lists;

public class BingCN {
	private static final Logger LOG = Logger.getLogger(BingCN.class);
	private static final String BING_URL = "http://www.bing.com/search?q=";

	public static long getResultCount(String q) throws UnsupportedEncodingException, IOException {
		Document doc = get(q);
		String resultsCount = doc.select("#b_tween > .sb_count").text();

		if (StringUtils.isEmpty(resultsCount))
			return 0;
		return Long.parseLong(resultsCount.replace(" results", "").replace(",", "").trim());
	}

	public static void getQueryResults(String q) throws IOException {
		Document doc = get(q);
		Elements eles = doc.select("ol#b_results > li.b_algo");

		for (Element e : eles) {
			String header = e.select("h2").text();
			String href = e.select("h2 > a").attr("href");
			// LOG.debug(header);
			// LOG.debug(href);
		}
	}

	public static Collection<String> sample(String q) throws IOException {

		Document doc = get(q);
		Elements eles = doc.select("ol#b_results > li.b_algo");

		Element e = eles.first();
		String header = e.select("h2").text();
		String href = e.select("h2 > a").attr("href");
		LOG.debug(href);

		String content = Jsoup.connect(href).get().text();
		Collection<String> nGramTerms = NGram.ngramDocumentTerms(Lists.newArrayList(1, 2, 3), content);
		return nGramTerms;
	}

	public static String getContent(String href) throws IOException {
		String content = Jsoup.connect(href).get().text();
		return content;
	}

	private static Document get(String q) throws IOException {
		return Jsoup.connect(BING_URL + URLEncoder.encode(q, "UTF-8"))
				.cookie("DUP", "Q=YswLTrsLV7QHeBeSNCRsOA2&T=309762905&A=2&IG=6EFB799002BA45D8B7D4FE1CEDE1ECC3")
				.cookie("_RwBf", "s=70&o=16").cookie("_IFAV", "COUNT=0")
				.cookie("MUID", "2DD9C73C2E5B64D23395C0D12FFA651B").cookie("SRCHD", "AF=NOFORM")
				.cookie("SRCHUID", "V=2&GUID=671D065F28DF4194A6D593B555D7F61D&dmnchg=1")
				.cookie("SRCHUSR", "DOB=20171018").cookie("_FP", "hta=on")
				.cookie("MUIDB", "2DD9C73C2E5B64D23395C0D12FFA651B").cookie("ULC", "H=18FAD|2:1&T=18FAD|2:1")
				.cookie("ai_user", "xk8Ed|2017-10-23T06:51:18.166Z").cookie("ENSEARCHZOSTATUS", "STATUS=0")
				.cookie("ipv6", "hit=1508921046827").cookie("_FS", "intlF=0").cookie("RMS", "A=gUACEACAAAAQ")
				.cookie("_EDGE_S", "mkt=zh-cn&SID=1270464DFECF657A10FD4D52FF6E6467")
				.cookie("SRCHHPGUSR", "CW=793&CH=957&DPR=1.100000023841858&UTC=480&WTS=63644505147")
				.cookie("_SS", "SID=1270464DFECF657A10FD4D52FF6E6467&bIm=051081&HV=1508909460&h5comp=0")
				.cookie("SNRHOP", "I=&TS=").cookie("ENSEARCH", "BENVER=1").get();
	}

}
