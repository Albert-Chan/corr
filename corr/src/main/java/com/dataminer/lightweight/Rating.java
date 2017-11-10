package com.dataminer.lightweight;

import lombok.Data;
import lombok.ToString;

@ToString(includeFieldNames = true)
@Data(staticConstructor = "of")
public class Rating<K, V> {
	private final K rating;
	private final V name;
}
