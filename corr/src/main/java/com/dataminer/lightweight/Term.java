package com.dataminer.lightweight;

import lombok.Data;
import lombok.ToString;

@ToString(includeFieldNames = true)
@Data(staticConstructor = "of")
public class Term {
	private final String gloss;
	private int wordsInGloss;
	private double confidence;
	private int numOfTriples;
	private double weight;
}
