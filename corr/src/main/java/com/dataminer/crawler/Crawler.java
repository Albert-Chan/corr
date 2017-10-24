package com.dataminer.crawler;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import com.dataminer.tfidf.TfIdf;

public class Crawler extends Thread {

	public static void main(String[] args) throws IOException {

		Collection<String> terms = BingCN.sample("big+data+learning");

		Map<String, Double> tf = TfIdf.tf(terms);

		Map<String, Double> tfIdf = TfIdf.tfIdf(tf);
		
		
		System.out.println(tfIdf);

	}

}
