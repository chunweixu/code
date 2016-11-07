package com.syntun.extractor;

import java.util.Map;
import java.util.Set;

import org.jsoup.nodes.Element;

public interface Selector {
	public String getSelectorId();
	public Set<ItemNode> getNodes();
	public boolean initSelector(Element element);
	public Map<String, String> extractSelectorInfo(String content, String baseUri);
}
