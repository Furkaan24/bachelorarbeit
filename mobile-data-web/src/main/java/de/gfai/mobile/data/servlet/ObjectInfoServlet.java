/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
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




/**
 *
 * @author lost
 */
public class ObjectInfoServlet extends HttpServlet implements ServletRequestParameter
{

  private static final long serialVersionUID = -4638461625943024334L;

  private transient ObjectInfo objectInfo;

  /**
   * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
   *
   * @param httpServletRequest servlet request
   * @param httpServletResponse servlet response
   * @throws ServletException if a servlet-specific error occurs
   * @throws IOException if an I/O error occurs
   */
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
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
  throws ServletException, IOException
  {
    processRequest(request, response);
  }

  /**
   * Handles the HTTP <code>POST</code> method.
   *
   * @param request servlet request
   * @param response servlet response
   * @throws ServletException if a servlet-specific error occurs
   * @throws IOException if an I/O error occurs
   */
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
  throws ServletException, IOException
  {
    processRequest(request, response);
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
/*
  private boolean pdfExport(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Throwable {
    // Hier sollte der Code zur Ermittlung von objectType und objectId stehen, z. B. aus dem Request

    // Lade das JRXML-Design (JasperDesign) aus einer JRXML-Datei
    InputStream jrxmlStream = getClass().getResourceAsStream("dein_bericht.jrxml");
    JasperDesign jasperDesign = JRXmlLoader.load(jrxmlStream);

    // Füge Hyperlink-Parameter hinzu, z. B. KTI_ID und PLV_ID
    Map<String, Object> parameters = new HashMap<>();
    String ktiId =" " ; // Hier den tatsächlichen Wert für KTI_ID setzen

            String plvId = " ";
    parameters.put("KTI_ID", ktiId);
    parameters.put("PLV_ID", plvId);

    // Erzeuge einen JasperReport aus dem JasperDesign
    JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

    // Fülle den Bericht mit Daten, falls erforderlich
    // JRDataSource dataSource = ...; // Du kannst hier deine Datenquelle einfügen
    // JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

    // Exportiere den Bericht in ein PDF
    byte[] pdfBytes = JasperExportManager.exportReportToPdf(jasperPrint);

    if (pdfBytes != null && pdfBytes.length > 0) {
        try (ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream()) {
            httpServletResponse.setContentType("application/pdf");
            servletOutputStream.write(pdfBytes);
            return true;
        } catch (Exception e) {
            // Fehlerbehandlung für Probleme beim Schreiben des PDFs
            e.printStackTrace();
        }
    }

    return false;
}*/



/*
private boolean htmlExport(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Throwable {
    byte[] htmlExport = getObjectInfo().htmlExport(getType(httpServletRequest), getId(httpServletRequest));

    if (Objects.nonNull(htmlExport) && htmlExport.length > 0) {
        try (ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream()) {
            httpServletResponse.setContentType("text/html");
            httpServletResponse.setContentLength(htmlExport.length);

            // Extrahiere die Parameterwerte aus dem jasperDesign-Objekt
            String ktiId = (String) jasperDesign.getParameter("KTI_ID").getDefaultValueExpression().getValue(null);
            String plvId = (String) jasperDesign.getParameter("PLV_ID").getDefaultValueExpression().getValue(null);
            ResourceBundle reportResourceBundle = (ResourceBundle) jasperDesign.getParameter("REPORT_RESOURCE_BUNDLE").getDefaultValueExpression().getValue(null);
            Connection reportConnection = (Connection) jasperDesign.getParameter("REPORT_CONNECTION").getDefaultValueExpression().getValue(null);

            // Erzeuge den Hyperlink mit den Parameterwerten
            String hyperlink = "instance_call_port.jasper?KTI_ID=" + ktiId + "&PLV_ID=" + plvId;

            // Fügen Sie den Hyperlink in Ihren HTML-Export ein
            String htmlContent = new String(htmlExport, StandardCharsets.UTF_8);
            htmlContent = htmlContent.replace("$HYPERLINK_PLACEHOLDER$", hyperlink);

            servletOutputStream.write(htmlContent.getBytes(StandardCharsets.UTF_8), 0, htmlContent.length());
            servletOutputStream.flush();

            return true;
        }
    }

    return false;
}*/


/*
  private boolean htmlExport(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Throwable
  {
    byte[] htmlExport = getObjectInfo().htmlExport(getType(httpServletRequest), getId(httpServletRequest));

    if (Objects.nonNull(htmlExport) && htmlExport.length > 0)
    {
      try (ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream())
      {
        httpServletResponse.setContentType("text/html");
        httpServletResponse.setContentLength(htmlExport.length);

        // Extrahiere die Parameterwerte für den Hyperlink
        String ktiId = "WERT_FUER_KTI_ID"; // Hier den tatsächlichen Wert für KTI_ID setzen
        String plvId = "WERT_FUER_PLV_ID"; // Hier den tatsächlichen Wert für PLV_ID setzen

        // Erzeuge den Hyperlink mit den Parameterwerten
        String hyperlink = "instance_call_port.jasper?KTI_ID=" + ktiId + "&PLV_ID=" + plvId;

        // Fügen Sie den Hyperlink in Ihren HTML-Export ein
        String htmlContent = new String(htmlExport, StandardCharsets.UTF_8);
        htmlContent = htmlContent.replace("$HYPERLINK_PLACEHOLDER$", hyperlink);

        servletOutputStream.write(htmlContent.getBytes(StandardCharsets.UTF_8), 0, htmlContent.length());
        servletOutputStream.flush();

        return true;
      }
    }

    return false;
  }*/

/*
  private boolean htmlExport(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Throwable {
    byte[] htmlExport = getObjectInfo().htmlExport(getType(httpServletRequest), getId(httpServletRequest));

    if (Objects.nonNull(htmlExport) && htmlExport.length > 0) {
        try (ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream()) {
            httpServletResponse.setContentType("text/html");
            httpServletResponse.setContentLength(htmlExport.length);

            // Die Zeilen aus der JRXML-Datei extrahieren
            String[] extractedLines = extractLinesFromJRXML(jrxmlFilePath, startLine, endLine);


            // Erzeuge den Hyperlink aus den extrahierten Zeilen
            String hyperlink = generateHyperlinkFromExtractedLines(extractedLines);

            // Den generierten Hyperlink in den HTML-Export einfügen
            String htmlContent = new String(htmlExport, StandardCharsets.UTF_8);
            htmlContent = htmlContent.replace("$HYPERLINK_PLACEHOLDER$", hyperlink);

            servletOutputStream.write(htmlContent.getBytes(StandardCharsets.UTF_8), 0, htmlContent.length());
            servletOutputStream.flush();

            return true;
        }
    }

    return false;
}*/









/*
  private boolean htmlExport(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Throwable
  {
    byte[] htmlExport = getObjectInfo().htmlExport(getType(httpServletRequest), getId(httpServletRequest));

    if (Objects.nonNull(htmlExport) && htmlExport.length > 0) {
        try (ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream()) {
            httpServletResponse.setContentType("text/html");
            httpServletResponse.setContentLength(htmlExport.length);

            //  <span> in ein anklickbares <a>-Element Konvertieren
            //String htmlContent = new String(htmlExport, StandardCharsets.UTF_8);
            String htmlContent = new String(htmlExport, Charset.forName("UTF-8"));
            //byte[] linkText = getObjectInfo().htmlExport(getType(httpServletRequest), getId(httpServletRequest))  ;

            String convertedHtmlContent = htmlContent.replaceAll(
                "<span style=\"font-family: SansSerif; color: #0000FF; font-size: 10px; line-height: 1.2578125; text-decoration: underline;\">(.*?)</span>",
                "<a href=\"https://de.wikipedia.org/wiki/Wikipedia:Hauptseite\" style=\"color: #0000FF; font-size: 10px;line-height: 1.2578125; text-decoration: underline;\">$1</a>"

            );
            //  der konvertierten HTML-Inhalt in die Ausgabestream Schreiben
            //servletOutputStream.write(convertedHtmlContent.getBytes(StandardCharsets.UTF_8));
            servletOutputStream.write(convertedHtmlContent.getBytes(StandardCharsets.UTF_8));

            servletOutputStream.flush();


            return true;
        }
    }

    return false;
  }*/

  /*
  public boolean htmlExport(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Throwable
  {
    byte[] htmlExport = getObjectInfo().htmlExport(getType(httpServletRequest), getId(httpServletRequest));

    if (Objects.nonNull(htmlExport) && htmlExport.length > 0)
    {
      try (ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream())
      {

        httpServletResponse.setContentType("text/html");
        httpServletResponse.setContentLength(htmlExport.length);

        // HTML-Inhalt als String dekodieren
        String htmlContent = new String(htmlExport, Charset.forName("UTF-8"));

        // Link-Text aus dem HTML-Export-Array extrahieren
        String linkText = new String(htmlExport, Charset.forName("UTF-8"));

        // HTML-Inhalt konvertieren
        String convertedHtmlContent = htmlContent.replaceAll(
        "<span style=\"font-family: SansSerif; color: #0000FF; font-size: 10px; line-height: 1.2578125; text-decoration: underline;\">(.*?)</span>",
        //"<a href=\"" + linkText + "\" style=\"color: #0000FF; font-size: 10px;line-height: 1.2578125; text-decoration: underline;\">$1</a>"
        //"<a href=\"https://de.wikipedia.org/wiki/Wikipedia:Hauptseite\" style=\"color: #0000FF; font-size: 10px;line-height: 1.2578125; text-decoration: underline;\">$1</a>"
        // "<input type=\"submit\" value=\" 	Detailseiten Hierarchie aufrufen 		\n" +"		\n" +"	\">"
        "<a href=\"#hyperlinks\">Hyperlinks</a>"
        //  "<form action=\"https://de.wikipedia.org/wiki/Wikipedia:Hauptseite\" method=\"GET\">\n" +
      //"  <input type=\"submit\" value=\" 	Detailseiten Hierarchie aufrufen\">\n" +
      //"</form>"
        );

        // Konvertierten HTML-Inhalt in den Ausgabestream schreiben
        servletOutputStream.write(convertedHtmlContent.getBytes(Charset.forName("UTF-8")));
        servletOutputStream.flush();

        return true;
      }
    }

    return false;
  }*/

/*
  public boolean htmlExport(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Throwable
  {
    byte[] htmlExport = getObjectInfo().htmlExport(getType(httpServletRequest), getId(httpServletRequest));

    if (Objects.nonNull(htmlExport) && htmlExport.length > 0)
    {
      try (ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream())
      {
        httpServletResponse.setContentType("text/html");
        httpServletResponse.setContentLength(htmlExport.length);

        String htmlContent = new String(htmlExport, Charset.forName("UTF-8"));

        String linkText = extractLinkTextFromHtmlExport(htmlExport);

        String convertedHtmlContent = convertHtmlContent(htmlContent, linkText);

        servletOutputStream.write(convertedHtmlContent.getBytes(Charset.forName("UTF-8")));
        servletOutputStream.flush();

        return true;
      }
    }

    return false;
  }

 */
