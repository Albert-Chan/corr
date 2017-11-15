package com.dataminer.tfidf.openie;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.dataminer.keywords.KeywordsRecognizer;

import edu.stanford.nlp.ie.util.RelationTriple;
import edu.stanford.nlp.simple.Document;
import edu.stanford.nlp.simple.Sentence;

public class OpenIE {
	private static final Logger LOG = Logger.getLogger(KeywordsRecognizer.class);

	public static Map<String, Double> terms(String content) {
		HashMap<String, Double> terms = new HashMap<>();

		Document doc = new Document(content);
		// Iterate over the sentences in the document
		for (Sentence sent : doc.sentences()) {
			HashMap<String, Double> termsInSentence = new HashMap<>();
			// Iterate over the triples in the sentence
			for (RelationTriple triple : sent.openieTriples()) {
				// gloss occurrence * confidence
				reduceTermsFrequency(termsInSentence, triple.subjectGloss(), triple.confidence);
				reduceTermsFrequency(termsInSentence, triple.objectGloss(), triple.confidence);
			}

			int triplesNum = sent.openieTriples().size();
			for (Entry<String, Double> e : termsInSentence.entrySet()) {
				double newValue = e.getValue() * Math.log(1 + e.getKey().split(" ").length) / triplesNum;

				LOG.debug(e.getKey() + ">>>" + newValue);
				reduceTermsFrequency(terms, e.getKey(), newValue);
			}
		}
		return terms;
	}

	private static void reduceTermsFrequency(HashMap<String, Double> termsMap, String term, double newFreq) {
		if (termsMap.containsKey(term)) {
			termsMap.put(term, newFreq + termsMap.get(term));
		} else {
			termsMap.put(term, newFreq);
		}
	}

}
