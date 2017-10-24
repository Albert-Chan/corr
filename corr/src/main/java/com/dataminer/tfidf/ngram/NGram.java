package com.dataminer.tfidf.ngram;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * Utility to calculate tf-idf for text n-grams
 */
public class NGram {
	/**
	 * Tokenize a set of documents and extract n-gram terms
	 *
	 * @param tokenizer
	 *            document tokenizer
	 * @param ns
	 *            n-gram orders
	 * @param documents
	 *            set of documents from which to extract terms
	 * @return iterator over document terms, where each document's terms is an
	 *         iterator over strings
	 */
	public static Collection<String> ngramDocumentTerms(Tokenizer tokenizer, List<Integer> ns, String document) {

		List<String> tokens = tokenizer.tokenize(document);

		// Extract N-grams as the terms in our model.
		Collection<String> terms = new ArrayList<>();
		for (int n : ns) {
			for (List<String> ngram : ngrams(n, tokens)) {
				String term = StringUtils.join(ngram, " ");
				terms.add(term);
			}
		}
		return terms;
	}

	/**
	 * Tokenize a set of documents as alphanumeric words and extract n-gram terms
	 *
	 * @param ns
	 *            n-gram orders
	 * @param documents
	 *            set of documents from which to extract terms
	 * @return iterator over document terms, where each document's terms is an
	 *         iterator over strings
	 */
	public static Collection<String> ngramDocumentTerms(List<Integer> ns, String document) {
		return ngramDocumentTerms(new RegularExpressionTokenizer(), ns, document);
	}

	private static List<List<String>> ngrams(int n, List<String> tokens) {
		List<List<String>> ngrams = new ArrayList<>();
		for (int i = 0; i < tokens.size() - n + 1; i++) {
			ngrams.add(tokens.subList(i, i + n));
		}
		return ngrams;
	}

}
