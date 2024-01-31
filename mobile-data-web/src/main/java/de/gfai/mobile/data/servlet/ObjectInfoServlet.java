package de.gfai.mobile.data.servlet;


import de.gfai.mobile.data.database.IfcaDatabaseHolder;
import de.gfai.mobile.data.info.ObjectInfo;
import de.gfai.mobile.data.servlet.param.ServletRequestParameter;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Objects;

public class ObjectInfoServlet extends HttpServlet implements ServletRequestParameter
{

  private static final long serialVersionUID = -4638461625943024334L;

  private transient ObjectInfo objectInfo;

  protected void processRequest(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException
  {
    try
    {
      if (!htmlExport(httpServletRequest, httpServletResponse))
      {
        printDefaultInfo(httpServletRequest, httpServletResponse);
      }


    }
    catch (Throwable ex)
    {
      throw new ServletException(ex);
    }
  }

  private boolean pdfExport(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Throwable
  {
    byte[] pdfExport = getObjectInfo().pdfExport(getType(httpServletRequest), getId(httpServletRequest));

    if (Objects.nonNull(pdfExport)
    && pdfExport.length > 0)
    {
      try (ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream())
      {


        httpServletResponse.setContentType("application/pdf");

        servletOutputStream.write(pdfExport);
        return true;
      }
    }

    return false;
  }


  private boolean htmlExport(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Throwable
  {
    byte[] htmlExport = getObjectInfo().htmlExport(getType(httpServletRequest), getId(httpServletRequest));

    if (Objects.nonNull(htmlExport)
        && htmlExport.length > 0)
    {
      try (ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream())
      {

        httpServletResponse.setContentType("text/html");
        httpServletResponse.setContentLength(htmlExport.length);
        servletOutputStream.write(htmlExport, 0, htmlExport.length);
        servletOutputStream.flush();

        return true;
      }
    }

    return false;
  }


  public ObjectInfo getObjectInfo()
  {

    if (Objects.isNull(objectInfo))
    {
      objectInfo = new ObjectInfo(IfcaDatabaseHolder.getIfcaDatabase());
    }
    return objectInfo;
  }

  private void printDefaultInfo(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException
  {
    try (PrintWriter out = httpServletResponse.getWriter())
    {
      out.println(String.format("<!DOCTYPE html>"
      + "<html>"
      + "  <head>"
      + "    <link rel='shortcut icon' type='image/x-icon/' href='favicon.ico'>"
      + "    <title>ObjectInfoServlet</title>"
      + "  </head>"
      + "  <body>"
      + "    <b>type:</b> %s, <b>id:</b> %d"
      + "  </body>"
      + "</html>", getType(httpServletRequest), getId(httpServletRequest)));
    }
  }

  @Override
  public String getType(HttpServletRequest httpServletRequest)
  {

    try
    {
      return ServletRequestParameter.super.getType(httpServletRequest); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
    }
    catch (RuntimeException ex)
    {
      return "KTI";
    }
  }


  @Override
  public long getId(HttpServletRequest httpServletRequest)
  {
    try
    {
      return ServletRequestParameter.super.getId(httpServletRequest);
    }
    catch (RuntimeException ex)
    {
      return 0l;
    }
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
  throws ServletException, IOException
  {
    processRequest(request, response);
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
  throws ServletException, IOException
  {
    processRequest(request, response);
  }

  @Override
  public String getServletInfo()
  {
    return "Short description";
  }

}