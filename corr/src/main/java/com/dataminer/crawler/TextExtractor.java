package com.dataminer.crawler;

import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;

public class TextExtractor {
	private static void appendNormalisedText(StringBuilder accum, TextNode textNode) {
		String text = textNode.getWholeText();

		if (preserveWhitespace(textNode.parentNode()))
			accum.append(text);
		else
			StringUtil.appendNormalisedWhitespace(accum, text, isEndWithWhitespace(accum));
	}

	static boolean preserveWhitespace(Node node) {
		// looks only at this element and one level up, to prevent recursion & needless
		// stack searches
		if (node != null && node instanceof Element) {
			Element element = (Element) node;
			return element.tag().preserveWhitespace()
					|| element.parent() != null && element.parent().tag().preserveWhitespace();
		}
		return false;
	}

	public static String getText(Document doc) {
		final StringBuilder accum = new StringBuilder();
		new NodeTraversor(new NodeVisitor() {
			public void head(Node node, int depth) {
				if (node instanceof TextNode) {
					TextNode textNode = (TextNode) node;
					appendNormalisedText(accum, textNode);
				} else if (node instanceof Element) {
					Element element = (Element) node;
					if (accum.length() > 0 && (element.isBlock() || element.tagName().equals("br"))
							&& !isEndWithWhitespace(accum))
						accum.append("\n");
				}
			}

			public void tail(Node node, int depth) {
			}
		}).traverse(doc);
		return accum.toString().trim();
	}

	static boolean isEndWithWhitespace(StringBuilder sb) {
		return sb.length() != 0 && sb.charAt(sb.length() - 1) == ' ';
	}
}
