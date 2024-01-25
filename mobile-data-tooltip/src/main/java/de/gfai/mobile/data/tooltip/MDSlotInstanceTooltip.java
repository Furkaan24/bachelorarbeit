/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.gfai.mobile.data.tooltip;

import de.gfai.core.util.debug.DebugUtil;
import de.gfai.infocable.database.IfcaDatabase;
import de.gfai.infocable.database.methods.catalog.SlotMethods;
import de.gfai.infocable.model.instance.SlotInstance;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lost
 */
class MDSlotInstanceTooltip extends MDAbstractTooltip<SlotInstance>
{
  MDSlotInstanceTooltip(IfcaDatabase databaseSupplier)
  {
    super(databaseSupplier);
  }

  @Override
  public String getTooltipText(SlotInstance slotInstance)
  {
    return slotInstance.getName() + LINE_BREAK
         + getMountingAngle(slotInstance)
         + getSlotTypes(slotInstance)
         + getStatusText(slotInstance)
         + getDebugInfo(slotInstance);
  }

  private String getMountingAngle(SlotInstance slotInstance)
  {
    if (slotInstance.getMountingAngle() != .0)
      return slotInstance.getMountingAngle() + "[°]" + LINE_BREAK;
    return "";
  }

  private String getSlotTypes(SlotInstance slotInstance)
  {
    try
    {
      SlotMethods slotMethods = SlotMethods.getInstance(getDatabase());
      return slotMethods.getSlotTypes(slotInstance.getSlot()).stream()
                        .map(slotType -> TAB + slotType.getName() + LINE_BREAK)
                        .reduce((t, u) -> t + u)
                        .get();
    }
    catch (SQLException ex)
    {
      Logger.getLogger(MDSlotInstanceTooltip.class.getName()).log(Level.SEVERE, null, ex);
    }

    return "";
  }

  static String getDebugInfo(SlotInstance slotInstance)
  {
    if (DebugUtil.isDebug())
      return String.format("SLI_ID[%d]\tSL_ID[%d]", slotInstance.getId(), slotInstance.getSlot().getId());
    return "";
  }
}
