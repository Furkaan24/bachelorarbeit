/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.gfai.mobile.data.tooltip;

import de.gfai.core.tooltip.Tooltip;
import de.gfai.infocable.database.IfcaDatabase;
import de.gfai.infocable.database.IfcaDatabaseCompound;
import de.gfai.infocable.database.methods.flag.FlagMethods;
import de.gfai.infocable.model.catalog.Structure;
import de.gfai.infocable.model.core.NetObject;
import de.gfai.infocable.model.status.BaseStatus;
import de.gfai.infocable.model.status.Status;
import de.gfai.infocable.ui.util.text.IfcaStringConverter;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author lost
 */
abstract class MDAbstractTooltip<T> implements Tooltip<T>, IfcaDatabaseCompound
{
  final static String LINE_BREAK = "\n";
  final static String TAB = "  ";
  final static int MAX_DESCRIPTION = 80;

  private final IfcaDatabase ifcaDatabase;

  protected MDAbstractTooltip(IfcaDatabase ifcaDatabase)
  {
    this.ifcaDatabase = ifcaDatabase;
  }

  protected String getStructureText(Structure structure)
  {
    return IfcaStringConverter.getText(structure)
                              .orElse(structure.getLabel());
  }

  protected String getStatusText(NetObject netObject)
  {
    Collection<Status> statusCollection = getStatusCollection(netObject);

    if (!statusCollection.isEmpty())
    {
      return "Status:"
             + LINE_BREAK
             + statusCollection.stream()
                               .map(status -> TAB + status.getName() + LINE_BREAK)
                               .reduce((t, u) -> t + u)
                               .get();
    }

    return "";
  }

  private Collection<Status> getStatusCollection(NetObject netObject)
  {
    try
    {
      FlagMethods flagMethods = FlagMethods.getInstance(getDatabase());
      return flagMethods.getStatus(netObject, BaseStatus.values());
    }
    catch (SQLException ex)
    {
      Logger.getLogger(getClass().getName()).log(Level.SEVERE, ex.getLocalizedMessage(), ex);
    }

    return Collections.emptyList();
  }

  @Override
  public IfcaDatabase getDatabase()
  {
    return ifcaDatabase;
  }

}
