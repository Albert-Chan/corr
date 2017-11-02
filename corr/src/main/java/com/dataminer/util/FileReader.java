package com.dataminer.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class FileReader {
	public static String getFileContent(String fileName) throws IOException {
		StringBuilder sb = new StringBuilder();
		try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
			stream.forEach(line -> sb.append(line).append("\n"));
		}
		String content = sb.toString();
		return content;
	}
}
