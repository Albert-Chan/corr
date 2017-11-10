package com.dataminer.util;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.dataminer.lightweight.Rating;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimaps;

public class TopN {

	/**
	 * Gets the topN from a given map of name->rating.
	 * @param mapToSortOnValue	the original map
	 * @param theN	the N of topN
	 * @return	a list of the topN with descending order
	 */
	public static <K, V> List<Rating<V, K>> get(Map<K, V> mapToSortOnValue, int theN) {

		ArrayListMultimap<V, K> multi = Multimaps.invertFrom(Multimaps.forMap(mapToSortOnValue),
				ArrayListMultimap.create());

		List<V> ratings = Lists.newArrayList(multi.keySet());
		Collections.sort(ratings, Collections.reverseOrder());

		List<Rating<V, K>> leadingBoard = Lists.newArrayList();

		for (int i = 0; i < Math.min(theN, ratings.size()); i++) {
			V rating = ratings.get(i);
			for (K name : multi.get(rating)) {
				leadingBoard.add(Rating.of(rating, name));
			}
		}
		return leadingBoard;
	}
}
