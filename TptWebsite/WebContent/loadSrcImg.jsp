<%@page import="tpt.info.PageSelectionInfo"%>
<%@ page contentType="text/html; charset=gbk"%>

<%@ page import="java.io.*"%>

<%@ page import="java.awt.image.BufferedImage"%>

<%@ page import="java.nio.file.Files"%>

<%@ page import="java.util.LinkedList"%>

<%@ page import="java.util.List"%>

<%@ page
	import="javax.imageio.ImageIO, tpt.hbase.ImageClip ,tpt.hbase.*"%>

<%
	String name = request.getParameter("name");
	try {
		BufferedImage image = ImageIO.read(new File(
				PageSelectionInfo.resultPath+name+".jpg"));
		BufferedImage resizedImg = RetriveImage.resize(image, 256, 256);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(resizedImg, "jpg", baos);
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