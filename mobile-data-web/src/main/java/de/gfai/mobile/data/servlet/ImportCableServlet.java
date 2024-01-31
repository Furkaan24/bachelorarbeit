package de.gfai.mobile.data.servlet;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.gfai.infocable.modules.connections.patch.model.CatalogObjectPk;
import de.gfai.mobile.data.database.IfcaDatabaseHolder;
import gfai.mobile.data.patch.ImportCableMobile;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "ImportCableServlet", urlPatterns =
{
  "/importcable"
})
public class ImportCableServlet extends HttpServlet
{

  protected void processRequest(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException, SQLException, ClassNotFoundException {
    response.setContentType("application/json;charset=UTF-8");

    String poiId1List = request.getParameter("poiid1");
    String poiId2List = request.getParameter("poiid2");
    String cableLengthString = request.getParameter("cablelength");
    String ktIdCable = request.getParameter("ktidcable");
    String cableName = request.getParameter("cablename");

    ObjectMapper objectMapper = new ObjectMapper();
    Map<String, Object> jsonResponse = new HashMap<>();

    if (poiId1List != null && poiId2List != null) {
        ImportCableMobile importCableMobile = getImportCableMobile();
        double cableLength = importCableMobile.getCableLengthKKabel(poiId1List, poiId2List, 0);
        jsonResponse.put("cablelength", cableLength);
    }

    if (poiId1List != null && poiId2List != null && cableLengthString != null) {
        try {
            double cableLength = Double.parseDouble(cableLengthString);
            ImportCableMobile importCableMobile = getImportCableMobile();

            List<CatalogObjectPk> kkList = importCableMobile.getKKList(poiId1List, poiId2List, cableLength, true);
            jsonResponse.put("kkList", convertListToJson(kkList));
        } catch (NumberFormatException e) {
            jsonResponse.put("error", "Invalid number format for cableLengthString");
        } catch (SQLException e) {
            jsonResponse.put("error", "Error retrieving Cable List");
        }
    }

    if (poiId1List != null && poiId2List != null && ktIdCable != null && cableName != null) {
        try {
            Long ktIdCableLong = Long.parseLong(ktIdCable);
            ImportCableMobile importCableMobile = getImportCableMobile();
            int cableFinalImport = importCableMobile.importCable(poiId1List, poiId2List, 1, ktIdCableLong, 0, 0, cableName, 0, 1, 1, 0, 0, 0, 0, 0, "", 0, 0, 0, false, 1, 1, "Signalwegname");
            jsonResponse.put("import", cableFinalImport);
        } catch (NumberFormatException e) {
            jsonResponse.put("error", "Invalid number format for cableLengthString or ktIdCable");
        } catch (SQLException e) {
            jsonResponse.put("error", "Error importing cable");
        }
    }

    response.getWriter().println(objectMapper.writeValueAsString(jsonResponse));
}


  private ImportCableMobile getImportCableMobile() throws SQLException
  {
    return new ImportCableMobile(IfcaDatabaseHolder.getIfcaDatabase());
  }

 private String convertListToJson(List<CatalogObjectPk> list) throws JsonProcessingException
 {
    ObjectMapper objectMapper = new ObjectMapper();

    List<Map<String, Object>> resultList = new ArrayList<>();
    for (CatalogObjectPk obj : list)
    {
        Map<String, Object> item = new HashMap<>();
        item.put("id", obj.getId());
        item.put("classNames", obj.getName());
        resultList.add(item);
    }

    return objectMapper.writeValueAsString(resultList);
}


  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
  throws ServletException, IOException
  {
    try
    {
      processRequest(request, response);
    }
    catch (SQLException ex)
    {
      Logger.getLogger(ImportCableServlet.class.getName()).log(Level.SEVERE, null, ex);
    }
    catch (ClassNotFoundException ex)
    {
      Logger.getLogger(ImportCableServlet.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
  throws ServletException, IOException
  {
    try
    {
      processRequest(request, response);
    }
    catch (SQLException ex)
    {
      Logger.getLogger(ImportCableServlet.class.getName()).log(Level.SEVERE, null, ex);
    }
    catch (ClassNotFoundException ex)
    {
      Logger.getLogger(ImportCableServlet.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  @Override
  public String getServletInfo()
  {
    return "Short description";
  }

}
