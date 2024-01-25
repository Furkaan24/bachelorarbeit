/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package de.gfai.mobile.data.servlet;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.gfai.infocable.model.connect.SignalPath;
import de.gfai.infocable.model.instance.port.PortInstance;
import de.gfai.mobile.data.database.IfcaDatabaseHolder;
import de.gfai.mobile.data.info.Signalpath;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

/**
 *
 * @author buelbuel
 */
@WebServlet(name = "SignalpathServlet", urlPatterns =
{
  "/signalpath"
})
public class SignalpathServlet extends HttpServlet
{

  protected void processRequest(HttpServletRequest request, HttpServletResponse response)
  throws ServletException, IOException
  {
    response.setContentType("application/json;charset=UTF-8");

    String portInstanceType = request.getParameter("type");
    String portInstanceId = request.getParameter("id");

    if (portInstanceType != null && portInstanceType.equalsIgnoreCase("PortInstance") && portInstanceId != null)
    {
      try
      {
        Signalpath signalpath = getSignalpath();
        PortInstance portInstance = createPortInstanceFromIdentifier(portInstanceId);

        if (portInstance != null)
        {
          Collection<SignalPath> signalPaths = signalpath.getSignalPathsFromPortInstance(portInstance);

          String jsonResponse = convertToJson(signalPaths);
          response.getWriter().write(jsonResponse);
        }
        else
        {
          response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
          response.getWriter().write("Invalid PortInstance identifier");
        }
      }
      catch (SQLException e)
      {
        throw new ServletException("Error retrieving signal paths", e);
      }
    }
    else
    {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      response.getWriter().write("Invalid or missing parameters in the URL");
    }
  }

  private PortInstance createPortInstanceFromIdentifier(String portInstanceId)
  {
    try
    {
      int id = Integer.parseInt(portInstanceId);
      return newPortInstance(id);
    }
    catch (NumberFormatException e)
    {
      return null;
    }
  }

  private PortInstance newPortInstance(int id)
  {
    return new PortInstance(id);
  }

  private Signalpath getSignalpath()
  {
    return new Signalpath(IfcaDatabaseHolder.getIfcaDatabase());
  }

  private String convertToJson(Collection<SignalPath> signalPaths)
  {

    ObjectMapper objectMapper = new ObjectMapper();
    ArrayNode signalPathsArray = objectMapper.createArrayNode();

    for (SignalPath signalPath : signalPaths)
    {
      ObjectNode signalPathNode = objectMapper.createObjectNode();
      signalPathNode.put("id", signalPath.getId());
      signalPathNode.put("name", signalPath.getName());
      signalPathNode.put("Signalwegtyp", signalPath.getType().getName());
      signalPathNode.put("Symbol", signalPath.getType().getSgArt().getSymbol());

      signalPathsArray.add(signalPathNode);
    }

    try
    {
      return objectMapper.writeValueAsString(signalPathsArray);
    }
    catch (JsonProcessingException e)
    {
      throw new RuntimeException("Error converting to JSON", e);
    }
  }

  protected void doGet(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response)
  throws jakarta.servlet.ServletException, IOException
  {
    processRequest(request, response);
  }

}
