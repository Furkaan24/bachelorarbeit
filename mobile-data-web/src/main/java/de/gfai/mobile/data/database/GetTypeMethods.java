/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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

/**
 *
 * @author lost
 */
public class GetTypeMethods
{
  public static void selectTypes(IfcaDatabase ifcaDatabase, PrintWriter out, String auswahltyp) throws ServletException
  {
    try
    {
      if (Objects.equals(auswahltyp, Rack.class.getSimpleName()))
        selectZonesAndRacks(ifcaDatabase, out);
      else if (Objects.equals(auswahltyp, Instance.class.getSimpleName()))
        selectInstances(ifcaDatabase, out);
      else if (Objects.equals(auswahltyp, PortInstance.class.getSimpleName()))
        selectPortInstances(ifcaDatabase, out);
      else if (Objects.equals(auswahltyp, Zone.class.getSimpleName()))
        selectZones(ifcaDatabase, out);
      else{
        System.err.println("Unexpected auswahltyp value: " + auswahltyp);
        throw new IllegalArgumentException(auswahltyp);
      }
    }
    catch (SQLException | IOException | RuntimeException ex)
    {
      throw new ServletException(ex);
    }
  }

  private static void selectZonesAndRacks(IfcaDatabase ifcaDatabase, PrintWriter out) throws SQLException, IOException
  {
    String sql = ifcaDatabase.prepareSql("select z.ZON_NAME, i.KTI_ID, i.KTI_NAME "
                                         + "from INFOCABLE.ZONEN~ z, INFOCABLE.TKC_ZON~ tkc,"
                                         + "     INFOCABLE.NETZKN~ n, INFOCABLE.KTI~ i,"
                                         + "     INFOCABLE.KT_ST ks,  INFOCABLE.STRUKTUR s "
                                         + "where s.CHILD in ('SCHRANK', 'GESTELL') and s.ST_ID=ks.ST_ID "
                                         + "and ks.KT_ID=i.KT_ID and i.KTI_ID=n.KTI_ID "
                                         + "and n.TKN_ID=tkc.TKN_ID and tkc.ZON_ID=z.ZON_ID "
                                         + "and z.ZON_NAME like 'D%'"
                                         + "order by z.ZON_NAME, i.KTI_NAME");
    try (Statement s = ifcaDatabase.createStatement(); ResultSet rs = s.executeQuery(sql))
    {
      String zoneName = null;

      while (rs.next())
      {
        if (!Objects.equals(zoneName, rs.getString(1)))
        {
          if (Objects.nonNull(zoneName))
            out.println("</optgroup>");
          zoneName = rs.getString(1);
          out.println("<optgroup label='ZONE: " + zoneName + "'>");
        }

        out.println("<option value='" + rs.getLong(2) + "'>" + rs.getString(3) + "</option>");
      }

      if (Objects.nonNull(zoneName))
        out.println("</optgroup>");
    }
  }

  private static void selectInstances(IfcaDatabase ifcaDatabase, PrintWriter out) throws SQLException, IOException
  {
    String sql = ifcaDatabase.prepareSql("select z.ZON_NAME, i.KTI_ID, i.KTI_NAME, s.CHILD "
                                         + "from INFOCABLE.ZONEN~ z, INFOCABLE.TKC_ZON~ tkc,"
                                         + "     INFOCABLE.NETZKN~ n, INFOCABLE.KTI~ i,"
                                         + "     INFOCABLE.STRUKTUR s, INFOCABLE.KT_ST ks, INFOCABLE.KT_EB ke "
                                         + "where ke.EB_ID=1 and ke.KT_ID=ks.KT_ID "
                                         + "and ks.ST_ID=s.ST_ID and ks.KT_ID=i.KT_ID "
                                         + "and i.KTI_ID=n.KTI_ID and n.TKN_ID=tkc.TKN_ID "
                                         + "and tkc.ZON_ID=z.ZON_ID "
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

  public static void selectPortInstancesWithPrefix(IfcaDatabase ifcaDatabase, PrintWriter out, String prefix) throws SQLException, IOException
  {
    String sql = ifcaDatabase.prepareSql("select POI_ID, POI_NAME from INFOCABLE.POI~ where POI_ID LIKE '" + prefix + "%' order by POI_NAME");

    try (Statement s = ifcaDatabase.createStatement(); ResultSet rs = s.executeQuery(sql)) {
        while (rs.next()) {
            out.println("<option value='" + rs.getLong(1) + "'>" + rs.getString(2) + "</option>");
        }
    }
  }

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
