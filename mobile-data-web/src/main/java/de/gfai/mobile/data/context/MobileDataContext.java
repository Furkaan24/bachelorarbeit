/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Singleton.java to edit this template
 */
package de.gfai.mobile.data.context;

import java.util.Objects;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author lost
 */
public class MobileDataContext
{
  private static InitialContext initialContext;
  private static Context mobileDataContext;

  private MobileDataContext()
  {
  }

  public static String getVersion() throws NamingException
  {
    return (String) getMobileDataContext().lookup("Version");
  }

  public static String getAppName() throws NamingException
  {
    return (String) getMobileDataContext().lookup("Application");
  }

  public static String[] getCommandLineArgs()
  {
    try
    {
      return new String[] {(String) getMobileDataContext().lookup("Debug")};
    }
    catch (NamingException ex)
    {
      return new String[0];
    }
  }

  public static String getOracleDriverClassName() throws NamingException
  {
    return (String) getMobileDataContext().lookup("OracleDriverClassName");
  }

  public static String getDatabaseServer() throws NamingException
  {
    return (String) getMobileDataContext().lookup("DatabaseServer");
  }

  public static String getDatabasePort() throws NamingException
  {
    return (String) getMobileDataContext().lookup("DatabasePort");
  }

  public static String getDatabaseSID() throws NamingException
  {
    return (String) getMobileDataContext().lookup("DatabaseSID");
  }

  /**
   * @return
   * @throws NamingException
   * @deprecated nach dem Login eine eigene Projektseite mit der Auswahl für Projekt und Planvariante (ifcaDatabase.jsp)
   */
  @Deprecated
  public static Long getProId() throws NamingException
  {
    return (Long) getMobileDataContext().lookup("PRO_ID");
  }

  /**
   * @return
   * @throws NamingException
   * @deprecated nach dem Login eine eigene Projektseite mit der Auswahl für Projekt und Planvariante (ifcaDatabase.jsp)
   */
  public static String getPlvTyp() throws NamingException
  {
    return (String) getMobileDataContext().lookup("PLV_TYP");
  }

  public static Context getMobileDataContext() throws NamingException
  {
    if (Objects.isNull(mobileDataContext))
      mobileDataContext = (Context) getInitialContext().lookup("java:comp/env");
    return mobileDataContext;
  }

  private static InitialContext getInitialContext() throws NamingException
  {
    if (Objects.isNull(initialContext))
      initialContext = new InitialContext();
    return initialContext;
  }
}
