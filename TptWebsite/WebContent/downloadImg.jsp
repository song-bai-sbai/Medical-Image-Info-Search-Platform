<%@page import="tpt.info.PageSelectionInfo"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" import="java.io.*,java.net.*"%>
<%
    
    response.reset();
    response.setContentType("application/x-download");
    String filedownload = request.getParameter("name");
    String filedisplay = filedownload+".jpg";
    filedisplay = URLEncoder.encode(filedisplay,"UTF-8");
    response.addHeader("Content-Disposition","attachment;filename=" + filedisplay);

    OutputStream outp = null;
    FileInputStream in = null;
    try
    {
        outp = response.getOutputStream();
        in = new FileInputStream(PageSelectionInfo.resultPath+filedownload+".jpg");

        byte[] b = new byte[1024];
        int i = 0;

        while((i = in.read(b)) > 0)
        {
            outp.write(b, 0, i);
        }
        outp.flush();
    }
    catch(Exception e)
    {
        System.out.println("Error!");
        e.printStackTrace();
    }
    finally
    {
        if(in != null)
        {
            in.close();
            in = null;
        }
        if(outp != null)
        {
            outp.close();
            outp = null;
        }
    }
%>