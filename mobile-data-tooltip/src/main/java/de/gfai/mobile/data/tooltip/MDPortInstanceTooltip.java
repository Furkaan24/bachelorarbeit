/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.gfai.mobile.data.tooltip;

import de.gfai.core.util.debug.DebugUtil;
import de.gfai.infocable.database.IfcaDatabase;
import de.gfai.infocable.database.methods.connect.PpvMethods;
import de.gfai.infocable.geom.app.view.res.TooltipResourceUtil;
import de.gfai.infocable.model.connect.Ppv;
import de.gfai.infocable.model.instance.port.PortInstance;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 *
 * @author lost
 */
class MDPortInstanceTooltip extends MDAbstractTooltip<PortInstance>
{
  private final Optional<String> optionalSymbolName;

  MDPortInstanceTooltip(IfcaDatabase ifcaDatabase, String symName)
  {
    super(ifcaDatabase);
    optionalSymbolName = Optional.ofNullable(symName);
  }

  @Override
  public String getTooltipText(PortInstance portInstance)
  {
    return portInstance.getName() + LINE_BREAK
         + getLogicalAddress(portInstance)
         + getPhysicalAddress(portInstance)
         + getPortName(portInstance)
         + getSymbolName()
         + getStatusText(portInstance)
         + getPpvTarget(portInstance)
         + getDebugInfo(portInstance);
  }

  private String getLogicalAddress(PortInstance portInstance)
  {
    return Optional.ofNullable(portInstance.getLogicalAddress())
                   .map(logicalAddress -> String.format("Log. Adr.[%s]", logicalAddress) + LINE_BREAK)
                   .orElse("");
  }

  private String getPhysicalAddress(PortInstance portInstance)
  {
    return Optional.ofNullable(portInstance.getPhysicalAddress())
                   .map(physicalAddress -> String.format("Phys. Adr.[%s]", physicalAddress) + LINE_BREAK)
                   .orElse("");
  }

  private String getPortName(PortInstance portInstance)
  {
    return Optional.ofNullable(portInstance.getPort())
                   .map(port -> port.getName() + LINE_BREAK)
                   .orElse("");
  }

  private String getSymbolName()
  {
    if (DebugUtil.isDebug())
      return optionalSymbolName.map(symbolName -> symbolName + LINE_BREAK)
                               .orElse("");
    return "";
  }

  private String getPpvTarget(PortInstance portInstance)
  {
    Collection<PortInstance> targetPorts = getTargetPorts(portInstance);

    if (!targetPorts.isEmpty())
      return (targetPorts.size()==1?TooltipResourceUtil.getString("74089173-5264-4593-acc4-e0914c825c1c"):
                                   TooltipResourceUtil.getString("f2bbb445-4946-41fa-88dd-52d6aedf1da9"))
             + ":"
             + LINE_BREAK
             + targetPorts.stream()
                          .findFirst()
                          .map(targetPort -> TAB + targetPort.getName() + LINE_BREAK)
                          .get();
    return "";
  }

  private String getDebugInfo(PortInstance portInstance)
  {
    if (DebugUtil.isDebug())
      return String.format("POI_ID[%d]%s", portInstance.getId(), getPortIdInfo(portInstance)) + LINE_BREAK;
    return "";
  }

  private String getPortIdInfo(PortInstance portInstance)
  {
    return Optional.ofNullable(portInstance.getPort())
                   .map(port -> String.format("\tPO_ID[%d]", port.getId()))
                   .orElse("");
  }

  private Collection<PortInstance> getTargetPorts(PortInstance portInstance)
  {
    try
    {
      Collection<Ppv> ppvs = PpvMethods.getInstance(getDatabase()).getPpvsFromPortInstances(portInstance);
      return ppvs.stream()
                 .map(ppv -> ppv.getPortInstance1().equals(portInstance)?ppv.getPortInstance2():ppv.getPortInstance1())
                 .collect(Collectors.toList());
    }
    catch (SQLException ex)
    {
      Logger.getLogger(getClass().getName()).log(Level.SEVERE, ex.getLocalizedMessage(), ex);
    }

    return Collections.EMPTY_LIST;
  }
}

