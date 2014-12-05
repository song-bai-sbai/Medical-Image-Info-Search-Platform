package tpt.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tpt.hbase.HbaseOpt;
import tpt.info.PageSelectionInfo;
import tpt.info.QueryCondition;
import tpt.info.ReturnedResults;

/**
 * Servlet implementation class ParseBasicSelection
 */
@WebServlet("/ParseBasicSelection")
public class ParseBasicSelection extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String sp[] = { "Project", "TSS", "Paticipant",
			"Sample", "Vial" };

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ParseBasicSelection() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String allSelection = request.getParameter("basicSelection");
		CacheInfo.searchResult.clear();
		CacheInfo.featureValues.clear();
		if (allSelection != null && !allSelection.isEmpty()) {
			Map<String, String> parms = MyJsonParser.parseJson(allSelection);
			List<QueryCondition> conditions = generateCondition(parms);
			List<String> spList = Arrays.asList(sp);
			List<QueryCondition> specialConditions = new ArrayList<QueryCondition>();
			if (parms.size() != 0) {
				for (String s : parms.keySet()) {
					if (spList.contains(s) && parms.get(s) != null
							&& !parms.get(s).isEmpty()) {
						QueryCondition qc = new QueryCondition(s, "=",
								parms.get(s));
						specialConditions.add(qc);
					}
				}
				if (specialConditions.size() > 0) {
					conditions.addAll(specialConditions);
				}
			}
			if (conditions.size() == 0) {
				response.getWriter().print("error: No search condition");
				return;
			}

			ReturnedResults rr = HbaseOpt.searchByConition(
					PageSelectionInfo.tableName, conditions);
			CacheInfo.searchResult = rr.getImageCellMap();
			String result = MyJsonParser.generatedResult(rr);
			response.getWriter().print(result);
		}

	}

	private List<QueryCondition> generateCondition(Map<String, String> parms) {
		List<QueryCondition> result = new ArrayList<QueryCondition>();
		for (String s : parms.keySet()) {
			String frontV = parms.get(s);
			QueryCondition qc = new QueryCondition();
			qc.setVariable(s);
			if (frontV.contains("=") || frontV.contains("<") // < > =
					|| frontV.contains(">")) {
				if (frontV.contains("=")
						&& (frontV.contains(">") || frontV.contains("<"))) {
					if (frontV.length() <= 2) {
						continue;
					}
					qc.setOperator(frontV.substring(0, 2));
					qc.setValue(frontV.substring(2));
				} else {
					if (frontV.length() == 1) {
						continue;
					}
					qc.setOperator(frontV.substring(0, 1));
					qc.setValue(frontV.substring(1));
				}
				result.add(qc);
			}
		}
		return result;
	}
}
