/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.gfai.mobile.data.tooltip;

import de.gfai.core.util.debug.DebugUtil;
import de.gfai.infocable.database.IfcaDatabase;
import de.gfai.infocable.database.methods.connect.PpvMethods;
import de.gfai.infocable.database.methods.connect.SignalPathMethods;
import de.gfai.infocable.geom.app.view.res.TooltipResourceUtil;
import de.gfai.infocable.model.connect.Ppv;
import de.gfai.infocable.model.instance.Instance;
import de.gfai.oda.core.exception.OdErrorLogger;
import java.sql.SQLException;
import java.util.Optional;

/**
 *
 * @author lost
 */
class MDPpvTooltip extends MDAbstractTooltip<Ppv>
{
  MDPpvTooltip(IfcaDatabase ifcaDatabase)
  {
    super(ifcaDatabase);
  }

  @Override
  public String getTooltipText(Ppv ppv)
  {
    return String.format("%s -> %s", ppv.getPortInstance1().getName(), ppv.getPortInstance2().getName())
           + LINE_BREAK
           + getCableInstanceInfo(ppv)
           + getSignalPathFromPpv(ppv)
           + getStatusText(ppv)
           + getDebugInfo(ppv);
  }

  private String getCableInstanceInfo(Ppv ppv)
  {
    return Optional.ofNullable(getCablingFromPpv(ppv))
                   .map(Instance.class::cast)
                   .map(instance -> String.format("%s: %s [%s]", getStructureText(instance.getCatalogObject().getStructure()),
                                                                 instance.getName(), instance.getCatalogObject().getName())
                                    + LINE_BREAK)
                   .orElse("");
  }

  private Instance getCablingFromPpv(Ppv ppv)
  {
    try
    {
      PpvMethods ppvMethods = PpvMethods.getInstance(getDatabase());
      return ppvMethods.getCablingFromPpv(ppv);
    }
    catch (SQLException ex)
    {
      OdErrorLogger.logThrowable(ex);
    }

    return null;
  }

  private String getSignalPathFromPpv(Ppv ppv)
  {
    try
    {
      SignalPathMethods signalPathMethods = SignalPathMethods.getInstance(getDatabase());
      return String.format("%s: %s", TooltipResourceUtil.getString("064a9d11-32a8-4b03-ac6d-e7b79045ac8d"),
                                     signalPathMethods.getSignalPathFromPpv(ppv).getName())
             + LINE_BREAK;
    }
    catch (SQLException ex)
    {
      OdErrorLogger.logThrowable(ex);
    }

    return "";
  }

  private String getDebugInfo(Ppv ppv)
  {
    if (DebugUtil.isDebug())
      return String.format("PPV_ID[%d]\tPOI_ID_1[%d]\tPOI_ID_2[%d]",
                           ppv.getId(), ppv.getPortInstance1().getId(), ppv.getPortInstance2().getId()) + LINE_BREAK;
    return "";
  }
}
