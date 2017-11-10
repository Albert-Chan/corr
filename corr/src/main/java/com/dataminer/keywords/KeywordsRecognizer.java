package com.dataminer.keywords;

import java.io.IOException;
import java.util.Map;

import org.apache.log4j.Logger;

import com.dataminer.crawler.BingCN;
import com.dataminer.lightweight.Rating;
import com.dataminer.tfidf.TfIdf;
import com.dataminer.tfidf.openie.OpenIE;
import com.dataminer.util.FileUtils;
import com.dataminer.util.TopN;

public class KeywordsRecognizer {
	private static final Logger LOG = Logger.getLogger(KeywordsRecognizer.class);

	public static void main(String[] args) throws IOException {

		//String content = FileUtils.content("bigdata.txt");
		//String content = BingCN.getContent("https://www.quora.com/How-do-you-learn-big-data");
		String content = "Big data is widely used in BI";
		LOG.debug(content);

		Map<String, Double> tf = TfIdf.newtf(OpenIE.terms(content));
		Map<String, Double> tfIdf = TfIdf.tfIdf(tf);

		LOG.debug("--------------------------Top ratings--------------------------------");
		for (Rating<Double, String> rating : TopN.get(tfIdf, 10)) {
			LOG.debug(rating);
		}
		FileUtils.saveTfIdf();
	}

}
