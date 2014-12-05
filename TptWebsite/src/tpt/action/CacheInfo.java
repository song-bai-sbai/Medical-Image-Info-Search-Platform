package tpt.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tpt.info.TransRecord;

public class CacheInfo {

	public static Map<String, List<TransRecord>> searchResult = new HashMap<String, List<TransRecord>>();
	public static Map<String, List<Double>> featureValues = new HashMap<String, List<Double>>();
}
