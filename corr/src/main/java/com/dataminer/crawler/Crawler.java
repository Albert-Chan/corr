package com.dataminer.crawler;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.dataminer.tfidf.TfIdf;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimaps;

public class Crawler extends Thread {

	public static void main(String[] args) throws IOException {

		System.out.println(new Date());

		System.out.println(BingCN.getResultCount("big data learning"));

		BingCN.getQueryResults("big data learning");

		Collection<String> terms = BingCN.sample("big data learning");
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
