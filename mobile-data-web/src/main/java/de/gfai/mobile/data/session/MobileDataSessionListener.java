package de.gfai.mobile.data.session;

import de.gfai.core.app.CommandLine;
import de.gfai.core.app.Version;
import de.gfai.mobile.data.context.MobileDataContext;
import de.gfai.mobile.data.database.IfcaDatabaseHolder;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;
import javax.naming.NamingException;

public class MobileDataSessionListener implements HttpSessionListener
{
  @Override
  public void sessionCreated(HttpSessionEvent httpSessionEvent)
  {
    try
    { // reine Dummies
      if (!CommandLine.existsCommandLine())
        CommandLine.createCurrentCommandLine(MobileDataContext.getCommandLineArgs(), null);

      if (Objects.isNull(Version.getCurrentVersion()))
        Version.createCurrentVersion(new Version(MobileDataContext.getAppName(), MobileDataContext.getVersion()));
    }
    catch (NamingException ex)
    {
    }
  }

  @Override
  public void sessionDestroyed(HttpSessionEvent httpSessionEvent)
  {
    IfcaDatabaseHolder.setIfcaDatabase(null);
    DriverManager.drivers()
                 .forEach(driver -> deregisterOracleDriver(httpSessionEvent, driver));
  }

  private void deregisterOracleDriver(HttpSessionEvent httpSessionEvent, Driver driver)
  {
    try
    {
      DriverManager.deregisterDriver(driver);
    }
    catch (SQLException ex)
    {
      httpSessionEvent.getSession().getServletContext().log("", ex);
    }
  }
}
