/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.gfai.mobile.data.tooltip;

import de.gfai.core.util.debug.DebugUtil;
import de.gfai.infocable.database.IfcaDatabase;
import de.gfai.infocable.model.instance.Instance;
import java.util.Optional;

/**
 *
 * @author lost
 */
class MDInstanceTooltip extends MDAbstractTooltip<Instance>
{
  private final Optional<String> optionalSymbolName;

  MDInstanceTooltip(IfcaDatabase databaseSupplier, String symName)
  {
    super(databaseSupplier);
    optionalSymbolName = Optional.ofNullable(symName);
  }

  @Override
  public String getTooltipText(Instance instance)
  {
    return instance.getName() + LINE_BREAK
         + getDescription(instance)
         + instance.getCatalogObject().getName() + LINE_BREAK
         + getStructureText(instance.getCatalogObject().getStructure()) + LINE_BREAK
         + getSymbolName()
         + getStatusText(instance)
         + getDebugInfo(instance);
  }

  static String getDescription(Instance instance)
  {
     String description = Optional.ofNullable(instance.getDescription())
                                  .map(String::trim)
                                  .orElse("");

     if (description.isEmpty())
       return description;

    if (description.length() > MAX_DESCRIPTION)
      description = description.substring(0, MAX_DESCRIPTION-3) + "...";

    return description + LINE_BREAK;
  }

  static String getDebugInfo(Instance instance)
  {
    if (DebugUtil.isDebug())
      return String.format("KTI_ID[%d]\tKT_ID[%d]", instance.getId(), instance.getCatalogObject().getId());
    return "";
  }

  private String getSymbolName()
  {
    if (DebugUtil.isDebug())
      return optionalSymbolName.map(symbolName -> symbolName + LINE_BREAK)
                               .orElse("");
    return "";
  }
}
