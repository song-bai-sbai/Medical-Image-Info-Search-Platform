package tpt.info;

public class QueryCondition {
	private String variable;
	private String operator;
	private String value;

	public QueryCondition(String v, String o, String val) {
		variable = v;
		operator = o;
		value = val;
	}

	public QueryCondition() {
	}

	public String getVariable() {
		return variable;
	}

	public String getOperator() {
		return operator;
	}

	public String getValue() {
		return value;
	}

	public void setErrorMessage(String err) {
		variable = err;
	}

	public void setVariable(String variable) {
		this.variable = variable;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
