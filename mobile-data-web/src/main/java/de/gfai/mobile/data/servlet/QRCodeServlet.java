package de.gfai.mobile.data.servlet;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

import com.google.zxing.WriterException;
import de.gfai.mobile.data.qrc.QRCodeGenerator;
import de.gfai.mobile.data.servlet.param.ServletRequestParameter;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.WebApplicationException;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Optional;
import javax.imageio.ImageIO;

/**
 *
 * @author lost
 */
public class QRCodeServlet extends HttpServlet implements ServletRequestParameter
{
  private static final long serialVersionUID = -4407157075651189880L;
  private static final int STD_QRCODE_SIZE = 200;

  /**
   * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
   *
   * @param httpServletRequest servlet request
   * @param httpServletResponse servlet response
   * @throws ServletException if a servlet-specific error occurs
   * @throws IOException if an I/O error occurs
   */
  protected void processRequest(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
  throws ServletException, IOException
  {
    try (ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream())
    {
      String svgUrl = "http://localhost:3000/svg/http%3A%2F%2Flocalhost%3A8080%2Fmobile-data-web%2Fsvg%3Ftype%3DRack%26id%3D" +getId(httpServletRequest);
      int qrsize = getQrSize(httpServletRequest);
      BufferedImage bufferedImage = QRCodeGenerator.createBufferedImage(svgUrl, qrsize);
      httpServletResponse.setContentType("image/png");
      ImageIO.write(bufferedImage, "PNG", servletOutputStream);
    }
    catch (UnsupportedEncodingException | WriterException | RuntimeException ex)
    {
      throw new ServletException(ex.getMessage(), ex);
    }
  }

  private String getSvgUrl(HttpServletRequest httpServletRequest)
  {
    String url ="http://localhost:3000/svg/http%3A%2F%2Flocalhost%3A8080%2Fmobile-data-web%2Fsvg%3Ftype%3DRack%26id%3D30026";
    return url;
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

  private int getQrSize(HttpServletRequest httpServletRequest)
  {
    Integer qrcSize = null;

    try
    {
      qrcSize = getInt(httpServletRequest, "qrsize");
    }
    catch (WebApplicationException ex)
    {
    }

    return Optional.ofNullable(qrcSize)
                   .filter(size -> size >= 0 && size < 2000)
                   .orElse(STD_QRCODE_SIZE);
  }

  // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
  /**
   * Handles the HTTP <code>GET</code> method.
   *
   * @param request servlet request
   * @param response servlet response
   * @throws ServletException if a servlet-specific error occurs
   * @throws IOException if an I/O error occurs
   */
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
  {
    processRequest(request, response);
  }

  /**
   * Handles the HTTP <code>POST</code> method.
   *
   * @param httpServletRequest servlet request
   * @param httpServletResponse servlet response
   * @throws ServletException if a servlet-specific error occurs
   * @throws IOException if an I/O error occurs
   */
  @Override
  protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException
  {
    processRequest(httpServletRequest, httpServletResponse);
  }

  /**
   * Returns a short description of the servlet.
   *
   * @return a String containing servlet description
   */
  @Override
  public String getServletInfo()
  {
    return "Short description";
  }// </editor-fold>

}
