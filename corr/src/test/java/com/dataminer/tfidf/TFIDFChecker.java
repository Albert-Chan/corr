package com.dataminer.tfidf;

import java.io.IOException;

import com.dataminer.tfidf.TfIdf;

public class TFIDFChecker {

	public static void main(String[] args) throws IOException {

		System.out.println(TfIdf.idf("widely used"));
	}

}
