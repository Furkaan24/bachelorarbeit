package de.gfai.mobile.data.infocable;

import de.gfai.infocable.geom.app.options.model.PortInstanceTextScale;
import de.gfai.infocable.geom.app.options.model.PortInstanceTextType;
import de.gfai.infocable.model.constants.StructureConstants;
import javax.naming.NamingException;

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
