package tpt.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;

import tpt.hbase.Histogram;
import tpt.hbase.HistogramInfo;

/**
 * Servlet implementation class GetHistogram
 */
@WebServlet("/GetHistogram")
public class GetHistogram extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetHistogram() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String feature = request.getParameter("name");
		HistogramInfo hi = Histogram.generateHistogram(feature,
				CacheInfo.featureValues.get(feature));
		String result = (new JSONArray(hi.getStat())).toString();
		response.getWriter().print(result);
	}

}
