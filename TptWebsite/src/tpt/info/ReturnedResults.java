package tpt.info;

import java.util.List;
import java.util.Map;

public class ReturnedResults {
	int numOfResult;
	String stat;
	Map<String, List<TransRecord>> trs;

	public ReturnedResults() {
	};

	public ReturnedResults(int c, String s, Map<String, List<TransRecord>> t) {
		numOfResult = c;
		stat = s;
		trs = t;
	}

	public int getNumOfResult() {
		return numOfResult;
	}

	public String getStat() {
		return stat;
	}

	public Map<String, List<TransRecord>> getImageCellMap() {
		return trs;
	}



}