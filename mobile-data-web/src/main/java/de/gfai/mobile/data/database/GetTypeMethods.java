package de.gfai.mobile.data.database;

import de.gfai.infocable.database.IfcaDatabase;
import de.gfai.infocable.model.catalog.Rack;
import de.gfai.infocable.model.infrastructure.zone.Zone;
import de.gfai.infocable.model.instance.Instance;
import de.gfai.infocable.model.instance.port.PortInstance;
import jakarta.servlet.ServletException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

public class GetTypeMethods
{
  // Method to select types based on a given criteria
  public static void selectTypes(IfcaDatabase ifcaDatabase, PrintWriter out, String auswahltyp) throws ServletException
  {
    try
    {
       // Check the type and call appropriate methods
      if (Objects.equals(auswahltyp, Rack.class.getSimpleName()))
        selectZonesAndRacks(ifcaDatabase, out);
      else if (Objects.equals(auswahltyp, Instance.class.getSimpleName()))
        selectInstances(ifcaDatabase, out);
      else if (Objects.equals(auswahltyp, PortInstance.class.getSimpleName()))
        selectPortInstances(ifcaDatabase, out);
      else if (Objects.equals(auswahltyp, Zone.class.getSimpleName()))
        selectZones(ifcaDatabase, out);
      else{
        // Handle unexpected type
        System.err.println("Unexpected auswahltyp value: " + auswahltyp);
        throw new IllegalArgumentException(auswahltyp);
      }
    }
    catch (SQLException | IOException | RuntimeException ex)
    {
      throw new ServletException(ex);
    }
  }

  // Method to select zones and racks
private static void selectZonesAndRacks(IfcaDatabase ifcaDatabase, PrintWriter out) throws SQLException, IOException
  {
    // Prepare SQL statement to select zone name, kti Id and kti name
    String sql = ifcaDatabase.prepareSql("select z.ZON_NAME, i.KTI_ID, i.KTI_NAME "
                                         + "from INFOCABLE.ZONEN~ z, "
                                         + "INFOCABLE.KTI~ i,"
                                         + "where ... " 
                                         + "order by z.ZON_NAME, i.KTI_NAME");
    try (Statement s = ifcaDatabase.createStatement(); ResultSet rs = s.executeQuery(sql))
    {
      String zoneName = null;

      while (rs.next())
      {
        //check if the zone has changed
        if (!Objects.equals(zoneName, rs.getString(1)))
        {
          //close previous optgroup tag if zone has changed
          if (Objects.nonNull(zoneName))
            out.println("</optgroup>");
          // Update zoneName and start new optgroup for the zone
          zoneName = rs.getString(1);
          out.println("<optgroup label='ZONE: " + zoneName + "'>");
        }
        //write option tag for the rack within the current zone
        out.println("<option value='" + rs.getLong(2) + "'>" + rs.getString(3) + "</option>");
      }
      //close the last optgroup tag if there was a zone
      if (Objects.nonNull(zoneName))
        out.println("</optgroup>");
    }
  }

  // Method to select instances
  private static void selectInstances(IfcaDatabase ifcaDatabase, PrintWriter out) throws SQLException, IOException
  {
    String sql = ifcaDatabase.prepareSql("select z.ZON_NAME, i.KTI_ID, i.KTI_NAME, s.CHILD "
                                         + "from INFOCABLE.ZONEN~ z, INFOCABLE.TKC_ZON~ tkc,"
                                         + "INFOCABLE.KTI~ i,"
                                         + "where ... "
                                         + "order by z.ZON_NAME, s.CHILD, i.KTI_NAME");
    try (Statement s = ifcaDatabase.createStatement(); ResultSet rs = s.executeQuery(sql))
    {
      String zonName = null;

      while (rs.next())
      {
        if (!Objects.equals(zonName, rs.getString(1)))
        {
          if (Objects.nonNull(zonName))
            out.println("</optgroup>");
          zonName = rs.getString(1);
          out.println("<optgroup label='ZONE: " + zonName + "'>");
        }

        out.println("<option value='" + rs.getLong(2) + "'>" + rs.getString(3) + " [" + rs.getString(4) + "]</option>");
      }

      if (Objects.nonNull(zonName))
        out.println("</optgroup>");
    }
  }

  // Method to select port instances
  private static void selectPortInstances(IfcaDatabase ifcaDatabase, PrintWriter out) throws SQLException, IOException
  {
    String sql = ifcaDatabase.prepareSql("select POI_ID, POI_NAME from INFOCABLE.POI~ order by POI_NAME");

    try (Statement s = ifcaDatabase.createStatement(); ResultSet rs = s.executeQuery(sql))
    {
      while (rs.next())
      {
        out.println("<option value='" + rs.getLong(1) + "'>" + rs.getString(2) + "</option>");
      }
    }
  }

  // Method to select port instances with a given prefix
  public static void selectPortInstancesWithPrefix(IfcaDatabase ifcaDatabase, PrintWriter out, String prefix) throws SQLException, IOException
  {
    String sql = ifcaDatabase.prepareSql("select POI_ID, POI_NAME from INFOCABLE.POI~ where POI_NAME LIKE '" + prefix + "%' and poi_lage = 'v' order by POI_NAME");

    try (Statement s = ifcaDatabase.createStatement(); ResultSet rs = s.executeQuery(sql)) {
        while (rs.next()) {
            out.println("<option value='" + rs.getLong(1) + "'>" + rs.getString(2) + "</option>");
        }
    }
  }

  // Method to select zones
  private static void selectZones(IfcaDatabase ifcaDatabase, PrintWriter out) throws SQLException, IOException
  {
    String sql = ifcaDatabase.prepareSql("select ZON_ID, ZON_NAME from INFOCABLE.ZONEN~ order by ZON_NAME");

    try (Statement s = ifcaDatabase.createStatement(); ResultSet rs = s.executeQuery(sql))
    {
      while (rs.next())
      {
        out.println("<option value='" + rs.getLong(1) + "'>" + rs.getString(2) + "</option>");
      }
    }
  }
}
