package de.gfai.mobile.data.servlet;

import de.gfai.mobile.data.infocable.rack.SVGRackDocumentFactory;
import de.gfai.mobile.data.servlet.param.RackSVGControl;
import de.gfai.mobile.data.servlet.param.ServletRequestParameter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.function.Consumer;
import org.apache.batik.svggen.SVGGraphics2D;
import org.w3c.dom.svg.SVGDocument;

public class RackSVGServlet extends HttpServlet implements ServletRequestParameter
{
  private static final long serialVersionUID = -2513754139311684332L;

  private Consumer<String> portIdCallback;

    protected void processRequest(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException
    {
      httpServletResponse.setContentType("image/svg+xml");

      try (PrintWriter out = httpServletResponse.getWriter())
      {
        SVGDocument svgDocument = createRackViewSVGDocument(httpServletRequest);
        SVGGraphics2D svgGraphics2D = new SVGGraphics2D(svgDocument);
        svgGraphics2D.stream(svgDocument.getDocumentElement(), out);
      }
      catch (Exception ex)
      {
        throw new ServletException(ex.getMessage(), ex);
      }
    }

    private SVGDocument createRackViewSVGDocument(HttpServletRequest httpServletRequest) throws Exception
    {
      long ktiId = getId(httpServletRequest);

      String ppvVisibilityParam = httpServletRequest.getParameter("ppvvisibility");
      boolean isPpvVisibility = ppvVisibilityParam == null || Boolean.valueOf(ppvVisibilityParam);

      String instanceTextObjectParam = httpServletRequest.getParameter("instancetextobject");
      boolean isInstanceTextObject = instanceTextObjectParam == null || Boolean.valueOf(instanceTextObjectParam);

      String instanceTextPortParam = httpServletRequest.getParameter("instancetextport");
      boolean isInstanceTextPort = instanceTextPortParam == null || Boolean.valueOf(instanceTextPortParam);

      String freeTextObjectParam = httpServletRequest.getParameter("freetextobject");
      boolean isFreeTextObject = freeTextObjectParam == null || Boolean.valueOf(freeTextObjectParam);

      String freeTextPortParam = httpServletRequest.getParameter("freetextport");
      boolean isFreeTextPort = freeTextPortParam == null || Boolean.valueOf(freeTextPortParam);

      String ruleTextObjectParam = httpServletRequest.getParameter("ruletextobject");
      boolean isRuleTextObject = ruleTextObjectParam == null || Boolean.valueOf(ruleTextObjectParam);

      String ruleTextPortParam = httpServletRequest.getParameter("ruletextport");
      boolean isRuleTextPort = ruleTextPortParam == null || Boolean.valueOf(ruleTextPortParam);

    portIdCallback = portId ->
    {

      System.out.println("Port ID clicked on servlet: " + portId);
    };

    SVGRackDocumentFactory documentFactory = createSVGRackDocumentFactory(httpServletRequest);
    return documentFactory.loadSVGRackDocument(isPpvVisibility, isInstanceTextObject, isInstanceTextPort,
    isFreeTextObject, isFreeTextPort,
    isRuleTextObject, isRuleTextPort, ktiId);
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
  public boolean isPortRuleText(HttpServletRequest httpServletRequest)
  {
    try
    {
      return ServletRequestParameter.super.isPortRuleText(httpServletRequest);
    }
    catch (RuntimeException ex)
    {
      return false;
    }
  }

  private SVGRackDocumentFactory createSVGRackDocumentFactory(HttpServletRequest httpServletRequest) throws SQLException, IOException, CloneNotSupportedException, InterruptedException {
    RackSVGControl rackSVGControl = new RackSVGControl(getWebAppURL(httpServletRequest));
    return new SVGRackDocumentFactory(rackSVGControl);
}

  private String getWebAppURL(HttpServletRequest httpServletRequest)
  {
    String url = httpServletRequest.getRequestURL().toString();
    return url.replaceAll("svg$", "");
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
