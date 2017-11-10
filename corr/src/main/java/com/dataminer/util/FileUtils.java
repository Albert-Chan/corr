package com.dataminer.util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map.Entry;
import java.util.stream.Stream;

import com.dataminer.tfidf.TfIdf;

public class FileUtils {
	public static String content(String fileName) throws IOException {
		StringBuilder sb = new StringBuilder();
		try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
			stream.forEach(line -> sb.append(line).append("\n"));
		}
		String content = sb.toString();
		return content;
	}

	public static void saveTfIdf() throws IOException {
		try (PrintWriter writer = new PrintWriter(new FileWriter("idf.txt"))) {
			for (Entry<String, Double> idf : TfIdf.getIdfs().entrySet()) {
				writer.println(idf.getKey() + ":::" + idf.getValue());
			}
		}
	}

}
