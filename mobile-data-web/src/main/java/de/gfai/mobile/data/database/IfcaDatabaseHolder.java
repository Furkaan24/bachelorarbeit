package de.gfai.mobile.data.database;

import de.gfai.cafm.geom.scene.entity.SceneType;
import de.gfai.infocable.database.IfcaDatabase;
import de.gfai.infocable.database.methods.ProjectMethods;
import de.gfai.infocable.geom.app.options.database.text.RackTextOptions;
import de.gfai.infocable.model.Planning;
import de.gfai.infocable.model.Project;
import de.gfai.mobile.data.context.MobileDataContext;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.naming.NamingException;

public class IfcaDatabaseHolder
{
  private static IfcaDatabase ifcaDatabase;
  private static RackTextOptions rackTextOptions;

  private IfcaDatabaseHolder()
  {
  }

  public static boolean isConnected()
  {
    return Optional.ofNullable(ifcaDatabase)
                   .filter(IfcaDatabase::isConnected)
                   .isPresent();
  }

  public static IfcaDatabase getIfcaDatabase()
  {
    return ifcaDatabase;
  }

  public static void setIfcaDatabase(IfcaDatabase ifcaDatabase)
  {
    if (Objects.nonNull(IfcaDatabaseHolder.ifcaDatabase))
    {
      saveRackTextOptions();
      IfcaDatabaseHolder.ifcaDatabase.disconnect();
    }

    IfcaDatabaseHolder.ifcaDatabase = ifcaDatabase;
  }

  private static void saveRackTextOptions()
  {
    if (Objects.nonNull(rackTextOptions)
        && isConnected())
    {
      try
      {
        rackTextOptions.saveOptions(ifcaDatabase, MobileDataContext.getAppName(), SceneType.Rack.name());
        rackTextOptions = null;
      }
      catch (NamingException ex)
      {
      }
    }
  }

  public static RackTextOptions getRackTextOptions()
  {
    if (Objects.isNull(rackTextOptions)
        && isConnected())
    {
      try
      {
        rackTextOptions = new RackTextOptions(MobileDataContext.getAppName(), SceneType.Rack);
      }
      catch (NamingException ex)
      {
      }
    }

    return rackTextOptions;
  }


  public static List<Project> getProjects() throws SQLException
  {
    ProjectMethods projectMethods = ProjectMethods.getInstance(ifcaDatabase);
    return projectMethods.getProjects();
  }

  public static List<Planning> getPlannings(Project project) throws SQLException
  {
    ProjectMethods projectMethods = ProjectMethods.getInstance(ifcaDatabase);
    return projectMethods.getPlannings(project);
  }

}
