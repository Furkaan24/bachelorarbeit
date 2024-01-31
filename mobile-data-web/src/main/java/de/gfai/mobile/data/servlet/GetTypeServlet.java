package de.gfai.mobile.data.servlet;

import de.gfai.infocable.database.IfcaDatabase;
import de.gfai.mobile.data.database.Authorization;
import de.gfai.mobile.data.database.GetTypeMethods;
import de.gfai.mobile.data.database.IfcaDatabaseHolder;
import de.gfai.mobile.data.servlet.param.ServletRequestParameter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "TypeServlet", urlPatterns =
{
  "/gettype"
})
public class GetTypeServlet extends HttpServlet implements ServletRequestParameter
{
  private static final long serialVersionUID = 1L;
  IfcaDatabase ifcaDatabase;

  protected void doGet(HttpServletRequest request, HttpServletResponse response)
  throws ServletException, IOException
  {
    try
    {
      processRequest(request, response);
    }
    catch (Throwable ex)
    {
      Logger.getLogger(GetTypeServlet.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  protected void doPost(HttpServletRequest request, HttpServletResponse response)
  throws ServletException, IOException
  {
    try
    {
      processRequest(request, response);
    }
    catch (Throwable ex)
    {
      Logger.getLogger(GetTypeServlet.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  private void processRequest(HttpServletRequest request, HttpServletResponse response)
  throws ServletException, IOException, Throwable
  {
    response.setContentType("text/html;charset=UTF-8");

    String[] gewaehlteTypen = request.getParameterValues("type");
    String portName = request.getParameter("portname");

    if (gewaehlteTypen != null && gewaehlteTypen.length > 0)
    {
      try
      {
        Authorization.getInstance().connect(request, response);
        ifcaDatabase = IfcaDatabaseHolder.getIfcaDatabase();

        if (portName == null || portName.isEmpty())
        {
          GetTypeMethods.selectTypes(ifcaDatabase, response.getWriter(), gewaehlteTypen[0]);
        }
        else
        {
          GetTypeMethods.selectPortInstancesWithPrefix(ifcaDatabase, response.getWriter(), portName);
        }
      }
      catch (jakarta.servlet.ServletException ex)
      {
        Logger.getLogger(GetTypeServlet.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
  }
}
