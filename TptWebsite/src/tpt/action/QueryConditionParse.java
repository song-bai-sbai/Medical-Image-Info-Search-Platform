package tpt.action;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tpt.info.Features;
import tpt.info.QueryCondition;

public class QueryConditionParse {

	private static boolean isValidFeatureName(String f) {
		for (Features feature : Features.values()) {
			if (f.equals(feature.toString()))
				return true;
		}
		return false;
	}

	private static boolean isValidOperator(String o) {
		if (o.equals("<") || o.equals(">") || o.equals("<=") || o.equals(">=")
				|| o.equals("=") || o.equals("!="))
			return true;
		else
			return false;
	}

	private static boolean isValidOperatorWithOOP(String o) {
		if (o.equals("<") || o.equals(">") || o.equals("<=") || o.equals(">="))
			return true;
		else
			return false;
	}

	private static boolean isValidInterval(String op1, String a, String op2,
			String b) {
		double da = Double.parseDouble(a);
		double db = Double.parseDouble(b);
		if (((op1.equals("<") || op1.equals("<="))
				&& (op1.equals("<") || op1.equals("<=")) && (da <= db))
				|| (op1.equals(">") || op1.equals(">=")
						&& (op1.equals(">") || op1.equals(">=")) && (da >= db)))
			return true;
		else
			return false;
	}

	private static String changeDirection(String op) {
		if (op.equals("<"))
			return ">";
		else if (op.equals(">"))
			return "<";
		else if (op.equals(">="))
			return "<=";
		else if (op.equals("<="))
			return ">=";
		return "=";
	}

	private static void printList(ArrayList<QueryCondition> QueryConditionList) {
		for (int i = 0; i < QueryConditionList.size(); i++)
			System.out.println("QueryCondition: "
					+ QueryConditionList.get(i).getVariable() + " "
					+ QueryConditionList.get(i).getOperator() + " "
					+ QueryConditionList.get(i).getValue());

	}

	public static List<QueryCondition> parseQueryCondition(String q) {
		String QueryConditionString = q;
		QueryConditionString = QueryConditionString.replaceAll("\\s", "");
		String errorMessage = "";
		ArrayList<QueryCondition> QueryConditionList = new ArrayList<QueryCondition>();
		QueryCondition err = new QueryCondition("", "", "");
		QueryConditionList.add(err);
		String[] conditions = (QueryConditionString.split(";"));
		for (String condition : conditions) {

			String pattern = "(('([-+]?\\d+(\\.{0,1}(\\d+?))?)'(>=?|<=?))?([a-zA-Z0-9]+)(>=?|<=?|=|!=)'([-+]?\\d+(\\.{0,1}(\\d+?))?)')|([a-zA-Z0-9]+)(=)'(\\w*)'|([a-zA-Z0-9]+)(=)'\\{(.*?)\\}'";
			Pattern r = Pattern.compile(pattern);
			Matcher m = r.matcher(condition);
			if (m.find()) {
				QueryCondition newQueryCondition;

				int type1OptionalValue = 3, type1OptionalOperator = 6, type1Variable = 7, type1Operator = 8, type1Value = 9, type2Variable = 12, type2Operator = 13, type2Value = 14;

				String var = "", op = "", val = "";
				if (m.group(type1Variable) != null) {
					// System.out.println("Type1");
					var = m.group(type1Variable);

					if (isValidFeatureName(var)) {
						// System.out.println("Variable: " + var);
						if (m.group(type1Operator) != null) {
							op = m.group(type1Operator);

							if (m.group(type1OptionalOperator) == null) {

								if (isValidOperator(op)) {

									// System.out.println("Operator: " + op);
									val = m.group(type1Value);
									// System.out.println("Value: " + val);
									if (!val.isEmpty()) {
										newQueryCondition = new QueryCondition(
												var, op, val);
										QueryConditionList
												.add(newQueryCondition);
									}
								} else
									errorMessage += " {Invalid Operator " + op
											+ " for variable " + var + "} ";

							} else {
								// System.out.println("a<x<b");
								String oop = m.group(type1OptionalOperator);

								if (isValidOperatorWithOOP(op)
										&& isValidOperatorWithOOP(oop)) {

									// System.out.println("Operator: " + op);
									// System.out.println("OptionalOperator: "+
									// oop);

									val = m.group(type1Value);
									String oval = m.group(type1OptionalValue);
									// System.out.println("Value: " + val);

									// System.out.println("OptionalValue: " +
									// oval);
									if (isValidInterval(oop, oval, op, val)) {
										newQueryCondition = new QueryCondition(
												var, op, val);
										QueryConditionList
												.add(newQueryCondition);
										String oop_changed = changeDirection(oop);
										newQueryCondition = new QueryCondition(
												var, oop_changed, oval);
										QueryConditionList
												.add(newQueryCondition);
									} else
										errorMessage += " {Invalid Interval Values for variable "
												+ var + "} ";

								} else
									errorMessage += " {Invalid Operator " + op
											+ " for variable " + var + "} ";
							}

						}

						else
							errorMessage += " {Operator Missing} ";
					} else
						errorMessage += " {Invalid Variable " + var + " } ";
				} else if (m.group(type2Variable) != null) {
					// System.out.println("Type 2");
					var = m.group(type2Variable);

					if (isValidFeatureName(var)) {
						// System.out.println("Variable: " + var);
						if (m.group(type2Operator) != null) {
							op = m.group(type2Operator);
							if (op.equals("=") || op.equals("!=")) {// System.out.println("Operator: "
																	// + op);
								if (m.group(type2Value) != null) {
									val = m.group(type2Value);
									if (!val.isEmpty()) {
										newQueryCondition = new QueryCondition(
												var, op, val);
										QueryConditionList
												.add(newQueryCondition);
									}
								} else
									errorMessage += " {Missing Value for variable "
											+ var + "} ";
							}
						} else
							errorMessage += " {Missing Operator for variable "
									+ var + "} ";

					} else
						errorMessage += " {Invalid Variable " + var + " } ";

				}
			} else {
				errorMessage += " {Invalid Input String " + condition + " } ";
			}

		}

		if (errorMessage.length() > 0) {// System.out.println(errorMessage);
			QueryConditionList.get(0).setErrorMessage(errorMessage);
		}
		printList(QueryConditionList);
		return QueryConditionList;

	}

}