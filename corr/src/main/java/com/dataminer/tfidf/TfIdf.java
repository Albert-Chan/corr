package com.dataminer.tfidf;

import java.io.IOException;
import java.util.*;

import org.apache.commons.lang.StringUtils;

import com.dataminer.crawler.BingCN;

/**
 * Term frequency-Inverse document frequency
 */
public class TfIdf {

	/**
	 * Word count method used for term frequencies
	 */
	public enum TfType {
		/**
		 * Term frequency
		 */
		NATURAL,
		/**
		 * Log term frequency plus 1
		 */
		LOGARITHM,
		/**
		 * 1 if term is present, 0 if it is not
		 */
		BOOLEAN
	}

	/**
	 * Normalization of the tf-idf vector
	 */
	public enum Normalization {
		/**
		 * Do not normalize the vector
		 */
		NONE,
		/**
		 * Normalize by the vector elements added in quadrature
		 */
		COSINE
	}

	/**
	 * Term frequency for a single document
	 *
	 * @param document
	 *            bag of terms
	 * @param type
	 *            natural or logarithmic
	 * @param <TERM>
	 *            term type
	 * @return map of terms to their term frequencies
	 */
	public static <TERM> Map<TERM, Double> tf(Collection<TERM> document, TfType type) {
		Map<TERM, Double> tf = new HashMap<>();
		for (TERM term : document) {
			tf.put(term, tf.getOrDefault(term, 0.0) + 1);
		}
		if (type != TfType.NATURAL) {
			for (TERM term : tf.keySet()) {
				switch (type) {
				case LOGARITHM:
					tf.put(term, 1 + Math.log(tf.get(term)));
					break;
				case BOOLEAN:
					tf.put(term, tf.get(term) == 0.0 ? 0.0 : 1.0);
					break;
				}
			}
		}
		return tf;
	}

	/**
	 * Natural term frequency for a single document
	 *
	 * @param document
	 *            bag of terms
	 * @param <TERM>
	 *            term type
	 * @return map of terms to their term frequencies
	 */
	public static <TERM> Map<TERM, Double> tf(Collection<TERM> document) {
		return tf(document, TfType.NATURAL);
	}

	private static final long NUM_ALL_DOCS = 5180000000L;

	/**
	 * Inverse document frequency for a set of documents
	 *
	 * @param documentVocabularies
	 *            sets of terms which appear in the documents
	 * @param smooth
	 *            smooth the counts by treating the document set as if it contained
	 *            an additional document with every term in the vocabulary
	 * @param addOne
	 *            add one to idf values to prevent divide by zero errors in tf-idf
	 * @param <TERM>
	 *            term type
	 * @return map of terms to their inverse document frequency
	 */
	public static double idf(String term) {
		long numDocWithTerm;
		try {
			numDocWithTerm = BingCN.getResultCount(term) + 1;
		} catch (IOException e) {
			numDocWithTerm = 1;
		}
		return Math.log(NUM_ALL_DOCS / numDocWithTerm);

	}

	/**
	 * tf-idf for a document
	 *
	 * @param tf
	 *            term frequencies of the document
	 * @param idf
	 *            inverse document frequency for a set of documents
	 * @param normalization
	 *            none or cosine
	 * @param <TERM>
	 *            term type
	 * @return map of terms to their tf-idf values
	 */
	public static <TERM> Map<TERM, Double> tfIdf(Map<TERM, Double> tf, Map<TERM, Double> idf,
			Normalization normalization) {
		Map<TERM, Double> tfIdf = new HashMap<>();
		for (TERM term : tf.keySet()) {
			tfIdf.put(term, tf.get(term) * idf.get(term));
		}
		if (normalization == Normalization.COSINE) {
			double n = 0.0;
			for (double x : tfIdf.values()) {
				n += x * x;
			}
			n = Math.sqrt(n);

			for (TERM term : tfIdf.keySet()) {
				tfIdf.put(term, tfIdf.get(term) / n);
			}
		}
		return tfIdf;
	}
	
	public static Map<String, Double> tfIdf(Map<String, Double> tf) {
		Map<String, Double> tfIdf = new HashMap<>();
		for (String term : tf.keySet()) {
			tfIdf.put(term, tf.get(term) * idf(term));
			
			System.out.println(term + ":::" + tfIdf.get(term));
		}
		
		return tfIdf;
	}
	
	

	/**
	 * Unnormalized tf-idf for a document
	 *
	 * @param tf
	 *            term frequencies of the document
	 * @param idf
	 *            inverse document frequency for a set of documents
	 * @param <TERM>
	 *            term type
	 * @return map of terms to their tf-idf values
	 */
	public static <TERM> Map<TERM, Double> tfIdf(Map<TERM, Double> tf, Map<TERM, Double> idf) {
		return tfIdf(tf, idf, Normalization.NONE);
	}

	public static String termStatistics(Map<String, Double> stats) {
		// Print terms in decreasing numerical order
		List<Map.Entry<String, Double>> es = new ArrayList<>(stats.entrySet());
		Collections.sort(es, new Comparator<Map.Entry<String, Double>>() {
			@Override
			public int compare(Map.Entry<String, Double> a, Map.Entry<String, Double> b) {
				return b.getValue().compareTo(a.getValue());
			}
		});
		List<String> fields = new ArrayList<>();
		for (Map.Entry<String, Double> e : es) {
			fields.add(String.format("%s = %6f", e.getKey(), e.getValue()));
		}
		return StringUtils.join(fields, "\t");
	}

	/**
	 * Iterator over the key sets of a set of maps.
	 *
	 * @param <KEY>
	 *            map key type
	 * @param <VALUE>
	 *            map value type
	 */
	static private class KeySetIterable<KEY, VALUE> implements Iterable<Iterable<KEY>> {
		final private Iterator<Map<KEY, VALUE>> maps;

		public KeySetIterable(Iterable<Map<KEY, VALUE>> maps) {
			this.maps = maps.iterator();
		}

		@Override
		public Iterator<Iterable<KEY>> iterator() {
			return new Iterator<Iterable<KEY>>() {
				@Override
				public boolean hasNext() {
					return maps.hasNext();
				}

				@Override
				public Iterable<KEY> next() {
					return maps.next().keySet();
				}
			};
		}
	}
}
