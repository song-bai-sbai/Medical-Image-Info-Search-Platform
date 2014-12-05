package tpt.action;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tpt.info.ReturnedResults;
import tpt.info.TransRecord;

public class MyJsonParser {

	public static Map<String, String> parseJson(String jsonString) {
		Map<String, String> result = new HashMap<String, String>();
		JSONObject jo = null;
		try {
			jo = new JSONObject(jsonString);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		@SuppressWarnings("unchecked")
		Iterator<String> keysItr = jo.keys();
		while (keysItr.hasNext()) {
			String key = keysItr.next();
			Object value = null;
			try {
				value = jo.get(key);
				if (value == null) {
					throw new JSONException("no match value");
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			result.put(key, value.toString());
		}
		return result;
	}

	public static String generatedResult(ReturnedResults r) {
		String result = "";
		if (r.getNumOfResult() == 0) {
			result = "No match records";
		} else {
			List<TransRecord> records = new LinkedList<TransRecord>();
			Map<String, List<TransRecord>> temp = r.getImageCellMap();
			for (String key : temp.keySet()) {
				TransRecord tr = new TransRecord(key, key);
				records.add(tr);
			}
			TransRecord counts = null;
			counts = new TransRecord("Statistic", "Find "
					+ String.valueOf(r.getNumOfResult()) + " records in "
					+ temp.size() + " slides.");
			records.add(counts);
			result = (new JSONArray(records)).toString();
		}
		return result;
	}
}
