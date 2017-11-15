package com.dataminer.crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

public class TextExtractorTest extends Thread {

	@Test
	public void test() {

		String source = "<p class=\"qtext_para\"><b>Big Data Tutorials</b></p>"
				+ "<ul>"
				+ "<li><span class=\"qlink_container\">"
				+ "<a href=\"http://pig.apache.org/docs/r0.7.0/tutorial.html\" rel=\"noopener nofollow\" target=\"_blank\" onclick=\"return Q.openUrl(this);\" class=\"external_link\" data-qt-tooltip=\"apache.org\" data-tooltip=\"attached\">Pig Tutorial</a></span></li>"
				+ "<li><span class=\"qlink_container\">"
				+ "<a href=\"http://mlwhiz.com/blog/2015/09/07/Spark_Basics_Explained/\" rel=\"noopener nofollow\" target=\"_blank\" onclick=\"return Q.openUrl(this);\" class=\"external_link\" data-qt-tooltip=\"mlwhiz.com\">Learning Spark using Python: Basics and Applications</a></span></li>"
				+ "</ul>";

		Document doc = Jsoup.parse(source);

		String outputText = TextExtractor.getText(doc);
		System.out.println(outputText);
	}

}
