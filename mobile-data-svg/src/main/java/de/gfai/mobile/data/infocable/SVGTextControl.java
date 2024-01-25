/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package de.gfai.mobile.data.infocable;

import de.gfai.infocable.geom.app.options.model.PortInstanceTextScale;
import de.gfai.infocable.geom.app.options.model.PortInstanceTextType;
import de.gfai.infocable.model.constants.StructureConstants;
import javax.naming.NamingException;

/**
 *
 * @author lost
 */
public interface SVGTextControl
{
  String getAppName() throws NamingException;

  default String getInstanceTextStructure()
  {
    return StructureConstants.ST_CATALOG_OBJECT;
  }

  default PortInstanceTextType getPortInstanceTextType()
  {
    return PortInstanceTextType.NAME;
  }

  default double getPortInstanceTextScale()
  {
    return PortInstanceTextScale.DEFAULT_SCALE;
  }

  default String getFreeTextStructure()
  {
    return StructureConstants.ST_CATALOG_OBJECT;
  }

  default boolean isShowPortFreeText()
  {
    return true;
  }

  default String getRuleTextStructure()
  {
    return StructureConstants.ST_CATALOG_OBJECT;
  }

  default boolean isShowPortRuleText()
  {
    return true;
  }
}
