package tpt.info;

public class TransRecord {

	private String rowKey;
	private String content;

	public TransRecord(String rowKey, String content) {
		this.rowKey = rowKey;
		this.content = content;
	}

	public TransRecord() {
	}

	public String getRowKey() {
		return rowKey;
	}

	public void setRowKey(String rowKey) {
		this.rowKey = rowKey;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
