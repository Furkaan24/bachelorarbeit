package de.gfai.mobile.data.database;

import de.gfai.infocable.model.Planning;
import de.gfai.infocable.model.PlvTyp;
import de.gfai.infocable.model.Project;
import de.gfai.mobile.data.context.MobileDataContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.Objects;
import javax.naming.NamingException;

public class Authorization
{
  private Authorization()
  {
  }

  public static Authorization getInstance()
  {
    return AuthorizationHolder.INSTANCE;
  }

  private static class AuthorizationHolder
  {
    private static final Authorization INSTANCE = new Authorization();

    private AuthorizationHolder()
    {
    }
  }

  /**
   * @param httpServletRequest
   * @param httpServletResponse
   * @throws Exception
   */
  public void connect(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Throwable
  {
    DatabaseSession databaseSession = (DatabaseSession) httpServletRequest.getSession().getAttribute(DatabaseSession.SESSION_ATTRIBUTE);

    if (databaseSession == DatabaseSession.Login)
      authorization(httpServletRequest, httpServletResponse);
    else if (databaseSession != DatabaseSession.Connected)
    {
      if (Objects.isNull(httpServletRequest.getHeader("AUTHORIZATION")))
        authorization(httpServletRequest, httpServletResponse);
      else
        connect(httpServletRequest);
    }
  }

  private void connect(HttpServletRequest httpServletRequest) throws ServletException
  {
    try
    {
      MobileDataDatabaseFactory mobileDataDatabaseFactory = new MobileDataDatabaseFactory(httpServletRequest.getHeader("AUTHORIZATION"));
      IfcaDatabaseHolder.setIfcaDatabase(mobileDataDatabaseFactory.createMobileDataDatabase());
      httpServletRequest.getSession().setAttribute(DatabaseSession.SESSION_ATTRIBUTE, DatabaseSession.Connected);
      settings();
    }
    catch (SQLException | NamingException | RuntimeException ex)
    {
      httpServletRequest.getSession().setAttribute(DatabaseSession.SESSION_ATTRIBUTE, DatabaseSession.Login);
      throw new ServletException(ex);
    }
  }

  /**
   *
   * @deprecated sollte über eine eigen Seite zur Auswahl von Projekt und Planung erfolgen!<br>
   * Z.B. über ifcaDatabase.jsp
   * @see jsp/ifcaDatabase.jsp
   */
  @Deprecated
  private void settings() throws NamingException, SQLException
  {
    long proId = MobileDataContext.getProId();
    Project project = IfcaDatabaseHolder.getProjects()
                                        .stream()
                                        .filter(pro -> pro.getId() == proId)
                                        .findFirst()
                                        .orElse(null);
    if (Objects.nonNull(project))
    {
      PlvTyp plvTyp = PlvTyp.valueOf(MobileDataContext.getPlvTyp());
      Planning planning = IfcaDatabaseHolder.getPlannings(project).stream()
                                                                  .filter(plv -> plv.getPlvTyp() == plvTyp)
                                                                  .findFirst()
                                                                  .orElse(null);
      IfcaDatabaseHolder.getIfcaDatabase().setProject(project);
      IfcaDatabaseHolder.getIfcaDatabase().setPlanning(planning);
    }
  }

  private void authorization(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws NamingException
  {
    httpServletRequest.getSession().removeAttribute(DatabaseSession.SESSION_ATTRIBUTE);
    httpServletResponse.setHeader("WWW-Authenticate", "BASIC realm=\"" + MobileDataContext.getAppName() + "\"");
    httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
  }
}


