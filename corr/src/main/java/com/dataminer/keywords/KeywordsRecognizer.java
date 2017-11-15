package com.dataminer.keywords;

import java.io.IOException;
import java.util.Map;

import org.apache.log4j.Logger;

import com.dataminer.lightweight.Rating;
import com.dataminer.tfidf.TfIdf;
import com.dataminer.tfidf.openie.OpenIE;
import com.dataminer.util.FileUtils;
import com.dataminer.util.TopN;

public class KeywordsRecognizer {
	private static final Logger LOG = Logger.getLogger(KeywordsRecognizer.class);

	public static void main(String[] args) throws IOException {

		//String content = FileUtils.content("bigdata.txt");
//		String content = BingCN.getContent("https://www.quora.com/How-do-you-learn-big-data");
		String content = "once you have your environment set up, get to coding!  "
				+ "there is plenty of documentation and tutorials out there to reference and learn from [2].  and really, just type questions into google and you'll get a ton of resources.  read up on the tools and understand how the technology can be applied to solving for your use case.  think about the kinds of metrics you're looking to capture within your data.  think about what kind of map-reduce programs you will need to write to capture the data you want to analyze.  think about how you can leverage something like hive or pig to do a lot of the heavy number crunching.  something that probably wont be apparent in a single node environment but is a real world problem in any distributed environment is understanding data skew and how it affects performance [3]."; 
//				"Big Data Tutorials\n" + 
//				"\n" + 
//				"Pig Tutorial\n" + 
//				"Learning Spark using Python: Basics and Applications";
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
