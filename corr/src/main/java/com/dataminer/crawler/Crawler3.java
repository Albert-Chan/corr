package com.dataminer.crawler;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Stream;

import org.jsoup.Jsoup;

import com.dataminer.tfidf.TfIdf;
import com.dataminer.tfidf.ngram.NGram;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimaps;

public class Crawler3 extends Thread {

	public static void main(String[] args) throws IOException {

		System.out.println(new Date());

//		StringBuilder sb = new StringBuilder();
//		try (Stream<String> stream = Files.lines(Paths.get("quora"))) {
//			stream.filter(line -> line.length() > 0).forEach(line -> sb.append(line));
//		} catch (IOException e1) {
//			e1.printStackTrace();
//		}
//		String content = sb.toString();
		
		String content = BingCN.getContent("https://www.quora.com/How-do-you-learn-big-data");
		System.out.println(content);

		Collection<String> terms = NGram.ngramDocumentTerms(Lists.newArrayList(1, 2, 3), content);
		
		Map<String, Double> tf = TfIdf.tf(terms);
		Map<String, Double> tfIdf = TfIdf.tfIdf(tf);

		ArrayListMultimap<Double, String> multi = Multimaps.invertFrom(Multimaps.forMap(tfIdf),
				ArrayListMultimap.create());

		List<Double> keys = new ArrayList<>(multi.keySet());
		Collections.sort(keys);

		System.out.println("--------------------------Top ratings--------------------------------");
		for (int i = keys.size() - 1; i > Math.max(0, keys.size() - 11); i--) {
			double rating = keys.get(i);
			for (String term : multi.get(rating)) {
				System.out.println(rating + ":::" + term);
			}
		}

		try (PrintWriter writer = new PrintWriter(new FileWriter("idf.txt"))) {
			for (Entry<String, Double> idf : TfIdf.getIdfs().entrySet()) {
				writer.println(idf.getKey() + ":::" + idf.getValue());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println(new Date());

	}

}
