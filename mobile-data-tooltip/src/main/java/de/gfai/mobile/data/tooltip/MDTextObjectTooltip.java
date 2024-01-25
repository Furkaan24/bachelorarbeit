/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.gfai.mobile.data.tooltip;

import de.gfai.core.util.debug.DebugUtil;
import de.gfai.infocable.database.IfcaDatabase;
import de.gfai.infocable.database.methods.text.rule.TextRegelMethods;
import de.gfai.infocable.geom.app.model.text.TextDbObject;
import de.gfai.infocable.geom.app.model.text.free.InstanceFreeTextDbObject;
import de.gfai.infocable.geom.app.model.text.free.PortFreeTextDbObject;
import de.gfai.infocable.geom.app.model.text.instance.InstanceTextDbObject;
import de.gfai.infocable.geom.app.model.text.instance.PortInstanceTextDbObject;
import de.gfai.infocable.geom.app.model.text.rule.RuleTextDbObject;
import de.gfai.infocable.geom.app.options.model.PortInstanceTextType;
import de.gfai.infocable.model.instance.port.PortInstance;
import de.gfai.infocable.model.text.TextRegel;
import de.gfai.infocable.ui.util.text.IfcaModelSupport;
import de.gfai.oda.core.exception.OdErrorLogger;
import java.sql.SQLException;

/**
 *
 * @author lost
 */
class MDTextObjectTooltip extends MDAbstractTooltip<TextDbObject>
{
  MDTextObjectTooltip(IfcaDatabase ifcaDatabase)
  {
    super(ifcaDatabase);
  }

  @Override
  public String getTooltipText(TextDbObject textElement)
  {
    return getTextObjectTooltip(textElement);
  }

  private String getTextObjectTooltip(TextDbObject textDbObject)
  {
    if (textDbObject instanceof InstanceTextDbObject)
      return getInstanceTextObjectTooltip((InstanceTextDbObject) textDbObject)
             + getDebugInfo(textDbObject);

    if (textDbObject instanceof PortInstanceTextDbObject)
      return getPortInstanceTextObjectTooltip((PortInstanceTextDbObject) textDbObject)
             + getDebugInfo(textDbObject);

    if (textDbObject instanceof InstanceFreeTextDbObject)
      return getInstanceFreeTextObjectTooltip((InstanceFreeTextDbObject) textDbObject)
             + getDebugInfo(textDbObject);

    if (textDbObject instanceof PortFreeTextDbObject)
      return getPortFreeTextObjectTooltip((PortFreeTextDbObject) textDbObject)
             + getDebugInfo(textDbObject);

    if (textDbObject instanceof RuleTextDbObject)
      return getRuleTextObjectTooltip((RuleTextDbObject) textDbObject)
             + getDebugInfo(textDbObject);

    return String.format("%s: %s", getClassName(textDbObject.getClass()), textDbObject.getText())
           + getDebugInfo(textDbObject);
  }

  private String getInstanceTextObjectTooltip(InstanceTextDbObject instanceTextObject)
  {
    return String.format("%s: %s", getClassName(InstanceTextDbObject.class), instanceTextObject.getText());
  }

  private String getPortInstanceTextObjectTooltip(PortInstanceTextDbObject portInstanceTextObject)
  {
    String type = PortInstanceTextType.getSelected().toString();
    return String.format("%s[%s], %s", getClassName(portInstanceTextObject.getClass()), type, portInstanceTextObject.getText());
  }

  private String getInstanceFreeTextObjectTooltip(InstanceFreeTextDbObject instanceFreeTextObject)
  {
    String tooltip;

    if (instanceFreeTextObject.isInstanceText())
      tooltip = String.format("%s: %s", getClassName(instanceFreeTextObject.getClass()), instanceFreeTextObject.getText());
    else
      tooltip = String.format("%s: %s", getClassName(instanceFreeTextObject.getClass()), instanceFreeTextObject.getText());

    if (DebugUtil.isDebug())
      tooltip += LINE_BREAK + String.format("TEXT_KTI.TXT_ID: %d", instanceFreeTextObject.getId());

    return tooltip;
  }

  private String getPortFreeTextObjectTooltip(PortFreeTextDbObject portFreeTextObject)
  {
    String tooltip;

    if (portFreeTextObject.isInstanceText())
    {
      String type = PortInstanceTextType.getSelected().toString();
      tooltip = String.format("%s[%s]: %s", getClassName(portFreeTextObject.getClass()), type, portFreeTextObject.getText());
    }
    else
      tooltip = String.format("%s: %s", getClassName(portFreeTextObject.getClass()), portFreeTextObject.getText());

    if (DebugUtil.isDebug())
      tooltip += LINE_BREAK + String.format("TEXT_POI.TXT_ID: %d", portFreeTextObject.getId());

    return tooltip;
  }

  private String getRuleTextObjectTooltip(RuleTextDbObject ruleTextObject)
  {
    long regId = ruleTextObject.getRuleTextSymbolGroup().getRegId();
    TextRegel textRule = getTextRuleById(regId);

    String tooltip = String.format("%s: %s", getClassName(RuleTextDbObject.class), ruleTextObject.getText())
                   + LINE_BREAK
                   + String.format("%s/%d", textRule.getName(), ruleTextObject.getRuleAttribute().getPosition());

    if (DebugUtil.isDebug())
    {
      if (ruleTextObject.getTextNetObject() instanceof PortInstance)
      {
        tooltip += LINE_BREAK + String.format("TEXT_POI_BASE.TXT_ID: %d", ruleTextObject.getId())
                 + LINE_BREAK + String.format("TEXT_POI_BASE.REG_ID: %d", regId);
      }
      else
      {
        tooltip += LINE_BREAK + String.format("TEXT_KTI_BASE.TXT_ID: %d", ruleTextObject.getId())
                 + LINE_BREAK + String.format("TEXT_KTI_BASE.REG_ID: %d", regId);
      }
    }

    return tooltip;
  }

  private TextRegel getTextRuleById(long regId)
  {
    TextRegelMethods textRegelMethods = TextRegelMethods.getInstance(getDatabase());

    try
    {
      return textRegelMethods.getTextRegel(regId);
    }
    catch (SQLException ex)
    {
      OdErrorLogger.logThrowable(ex);
    }

    return new TextRegel(regId, "Rule not defined!");
  }

  private <T extends TextDbObject> String getClassName(Class<T> textElementClass)
  {
    return IfcaModelSupport.getIfcaModelSupport(textElementClass).toString();
  }

  private String getDebugInfo(TextDbObject textDbObject)
  {
    if (DebugUtil.isDebug())
    {
      return LINE_BREAK
             + String.format("%s: %d", IfcaModelSupport.getIfcaModelSupport(textDbObject.getTextNetObject().getClass()),
                                       textDbObject.getTextNetObject().getId());
    }

    return "";
  }
}
