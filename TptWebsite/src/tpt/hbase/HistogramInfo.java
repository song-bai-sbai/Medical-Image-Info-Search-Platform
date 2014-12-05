package tpt.hbase;

import java.util.List;

import tpt.info.TransRecord;

public class HistogramInfo {
	String feature;
	List<TransRecord> stat;

	HistogramInfo() {
	};

	HistogramInfo(String f, List<TransRecord> s) {
		this.feature = f;
		this.stat = s;
	};

	public String getFeature() {
		return feature;
	}

	public List<TransRecord> getStat() {
		return stat;
	}

	public void setFreature(String f) {
		this.feature = f;
	}

	public void setStat(List<TransRecord> s) {
		this.stat = s;
	}

}
