package de.gfai.mobile.data.servlet;

import de.gfai.infocable.database.IfcaDatabase;
import de.gfai.mobile.data.database.Authorization;
import de.gfai.mobile.data.database.IfcaDatabaseHolder;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "AuthorizationServlet", urlPatterns =
{
  "/connect"
})
public class AuthorizationServlet extends HttpServlet
{
private static final long serialVersionUID = 1L;

  protected void doGet(HttpServletRequest request, HttpServletResponse response)
  throws ServletException, IOException
  {
    processRequest(request, response);
  }

  protected void doPost(HttpServletRequest request, HttpServletResponse response)
  throws ServletException, IOException
  {
    processRequest(request, response);
  }

  private void processRequest(HttpServletRequest request, HttpServletResponse response)
  throws ServletException, IOException
  {
    response.setContentType("text/html;charset=UTF-8");

    IfcaDatabase ifcaDatabase;

    try
    {
      Authorization.getInstance().connect(request, response);
      ifcaDatabase = IfcaDatabaseHolder.getIfcaDatabase();
    }
    catch (Throwable throwable)
    {
      throw new ServletException(throwable);
    }
  }
}
