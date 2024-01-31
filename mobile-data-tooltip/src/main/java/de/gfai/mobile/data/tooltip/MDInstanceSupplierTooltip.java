/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.gfai.mobile.data.tooltip;

import de.gfai.cafm.geom.scene.properties.InstanceSupplier;
import de.gfai.core.tooltip.Tooltip;
import de.gfai.infocable.database.IfcaDatabase;
import de.gfai.infocable.geom.app.model.text.TextDbObject;
import de.gfai.infocable.model.catalog.Pin;
import de.gfai.infocable.model.connect.Ppv;
import de.gfai.infocable.model.instance.Instance;
import de.gfai.infocable.model.instance.SlotInstance;
import de.gfai.infocable.model.instance.port.PortInstance;
import de.gfai.infocable.modules.signalpath.model.SwgNode;
import java.util.Objects;
import java.util.function.Supplier;

public class MDInstanceSupplierTooltip implements Tooltip<InstanceSupplier<?>>
{
  private final IfcaDatabase databaseSupplier;
  private final Supplier<String> symbolNameSupplier;

  public MDInstanceSupplierTooltip(IfcaDatabase dbSupplier, Supplier<String> symNameSupplier)
  {
    databaseSupplier = dbSupplier;
    symbolNameSupplier = symNameSupplier;
  }

  @Override
  public String getTooltipText(InstanceSupplier<?> t)
  {
    Object object = t.getInstance();

    if (object instanceof Instance)
      return getInstanceTooltip((Instance) object);

    if (object instanceof PortInstance)
      return getPortInstanceTooltip((PortInstance) object);

    if (object instanceof Pin)
      return getPinTooltip((Pin) object);

    if (object instanceof SlotInstance)
      return getSlotInstanceTooltip((SlotInstance) object);

    if (object instanceof Ppv)
      return getPpvTooltip((Ppv) object);

    if (object instanceof SwgNode)
      return getSwgNodeTooltip((SwgNode) object);

    if (object instanceof TextDbObject)
      return getTextObjectTooltip((TextDbObject) object);

    return Objects.toString(object);
  }

  private String getInstanceTooltip(Instance instance)
  {
    MDInstanceTooltip instanceTooltip = new MDInstanceTooltip(databaseSupplier, symbolNameSupplier.get());
    return instanceTooltip.getTooltipText(instance);
  }

  private String getPortInstanceTooltip(PortInstance portInstance)
  {
    MDPortInstanceTooltip portInstanceTooltip = new MDPortInstanceTooltip(databaseSupplier, symbolNameSupplier.get());
    return portInstanceTooltip.getTooltipText(portInstance);
  }

  private String getPinTooltip(Pin pin)
  {
    MDPinTooltip pinTooltip = new MDPinTooltip(databaseSupplier);
    return pinTooltip.getTooltipText(pin);
  }

  private String getSlotInstanceTooltip(SlotInstance slotInstance)
  {
    MDSlotInstanceTooltip slotInstanceTooltip = new MDSlotInstanceTooltip(databaseSupplier);
    return slotInstanceTooltip.getTooltipText(slotInstance);
  }

  private String getPpvTooltip(Ppv ppv)
  {
    MDPpvTooltip ppvTooltip = new MDPpvTooltip(databaseSupplier);
    return ppvTooltip.getTooltipText(ppv);
  }

  private String getSwgNodeTooltip(SwgNode swgNode)
  {
    MDInstanceTooltip instanceTooltip = new MDInstanceTooltip(databaseSupplier, symbolNameSupplier.get());
    return instanceTooltip.getTooltipText(swgNode.getInstance());
  }

  private String getTextObjectTooltip(TextDbObject textObject)
  {
    MDTextObjectTooltip textElementTooltip = new MDTextObjectTooltip(databaseSupplier);
    return textElementTooltip.getTooltipText(textObject);
  }
}
