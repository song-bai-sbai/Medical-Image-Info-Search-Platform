package tpt.action;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;

import tpt.hbase.RetriveImage;
import tpt.info.ImgPoint;
import tpt.info.TransRecord;

/**
 * Servlet implementation class GetImgInfo
 */
@WebServlet("/GetImgInfo")
public class GetImgInfo extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetImgInfo() {
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
		String imgName = request.getParameter("name");
		List<TransRecord> records = CacheInfo.searchResult.get(imgName);
		// generate image
		String sampleRowkey = records.get(0).getRowKey();
		List<ImgPoint> points = new LinkedList<ImgPoint>();
		for (TransRecord tr : records) {
			ImgPoint p = new ImgPoint(tr.getRowKey());
			points.add(p);
		}
		RetriveImage.generateResultImage(sampleRowkey, points);

		String result = (new JSONArray(records)).toString();
		response.getWriter().print(result);
	}

}
