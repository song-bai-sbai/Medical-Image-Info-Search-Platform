<%@page import="tpt.info.PageSelectionInfo"%>
<%@ page contentType="text/html; charset=gbk"%>

<%@ page import="java.io.*"%>

<%@ page import="java.awt.image.BufferedImage"%>

<%@ page import="java.nio.file.Files"%>

<%@ page import="javax.imageio.ImageIO, tpt.hbase.*"%>

<%
	String rowKey = request.getParameter("rowKey");

	try {
		BufferedImage img = RetriveImage.retriveImg(rowKey, HbaseOpt
				.getBoundary(PageSelectionInfo.tableName, rowKey));

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(img, "jpg", baos);
		baos.flush();
		byte[] imageInByte = baos.toByteArray();
		baos.close();

		response.setContentType("image/jpeg");
		OutputStream outs = response.getOutputStream();
		outs.write(imageInByte);
		outs.flush();
	} catch (IOException e) {

	}
%>