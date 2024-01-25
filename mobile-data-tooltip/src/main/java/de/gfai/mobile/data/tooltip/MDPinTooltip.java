/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.gfai.mobile.data.tooltip;

import de.gfai.core.util.debug.DebugUtil;
import de.gfai.infocable.database.IfcaDatabase;
import de.gfai.infocable.model.catalog.Pin;

/**
 *
 * @author lost
 */
class MDPinTooltip extends MDAbstractTooltip<Pin>
{
  MDPinTooltip(IfcaDatabase ifcaDatabase)
  {
    super(ifcaDatabase);
  }

  @Override
  public String getTooltipText(Pin pin)
  {
    return pin.getName() + LINE_BREAK
           + getDebugInfo(pin);
  }

  private String getDebugInfo(Pin pin)
  {
    if (DebugUtil.isDebug())
      return String.format("PN_ID[%d]", pin.getId()) + LINE_BREAK;
    return "";
  }
}

