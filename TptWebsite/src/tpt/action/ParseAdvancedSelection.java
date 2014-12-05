package tpt.action;

import java.io.IOException;
import java.util.ArrayList;
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
 * Servlet implementation class parseSelection
 */
@WebServlet("/ParseAdvancedSelection")
public class ParseAdvancedSelection extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public ParseAdvancedSelection() {
		super();
	}

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String allSelection = request.getParameter("advancedSelection");
		if (allSelection != null && !allSelection.isEmpty()) {
			Map<String, String> parms = MyJsonParser.parseJson(allSelection);
			ReturnedResults rr = null;
			String errorInfo = "";
			if (parms.containsKey("query")) {
				List<QueryCondition> conditions = QueryConditionParse
						.parseQueryCondition(parms.get("query"));
				List<QueryCondition> specialConditions = new ArrayList<QueryCondition>();
				if (!conditions.get(0).getVariable().isEmpty()) {
					// has error info
					errorInfo = conditions.get(0).getVariable();
					response.getWriter().print(errorInfo);
					return;
				} else if (conditions.size() == 1) {
					response.getWriter().print(
							"error: Please input query condition.");
					return;
				} else {
					conditions.remove(0);
					parms.remove("query");
					if (parms.size() != 0) {
						for (String s : parms.keySet()) {
							if (parms.get(s) != null && !parms.get(s).isEmpty()) {
								QueryCondition qc = new QueryCondition(s, "=",
										parms.get(s));
								specialConditions.add(qc);
							}
						}
						if (specialConditions.size() > 0) {
							conditions.addAll(specialConditions);
						}
					}
					// do search
					rr = HbaseOpt.searchByConition(PageSelectionInfo.tableName,
							conditions);
				}
			} else {
				response.getWriter().print("error: no query");
				return;
			}

			// Generate json
			String result = MyJsonParser.generatedResult(rr);
			response.getWriter().print(result);
		} else {
			response.getWriter().print("error: no msg from web page.");
		}
	}

}
