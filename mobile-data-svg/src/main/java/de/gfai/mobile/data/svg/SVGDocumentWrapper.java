package de.gfai.mobile.data.svg;

import java.util.Objects;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.svg.SVGDocument;

@FunctionalInterface
public interface SVGDocumentWrapper
{
  default void setWidth(double width)
  {
    getDocumentRoot().setAttributeNS(null, "width", Objects.toString(width));
  }

  default double getWidth(double defaultValue)
  {
    return getDoubleAttributeNS("width", defaultValue);
  }

  default void setHeight(double height)
  {
    getDocumentRoot().setAttributeNS(null, "height", Objects.toString(height));
  }

  default double getHeight(double defaultValue)
  {
    return getDoubleAttributeNS("height", defaultValue);
  }

  default double getDoubleAttributeNS(String localName, double defaultValue)
  {
    try
    {
      String szDouble = getDocumentRoot().getAttributeNS(null, localName);

      if (Objects.nonNull(szDouble)
          && szDouble.trim().length() > 0)
      {
        return Double.parseDouble(szDouble);
      }
    }
    catch (NullPointerException | NumberFormatException ex)
    {
    }

    return defaultValue;
  }

  default Element getChildElement(String localName)
  {
    NodeList nodeList = getDocumentRoot().getChildNodes();

    for (int i = 0; i < nodeList.getLength(); ++i)
    {
      if (Objects.equals(nodeList.item(i).getLocalName(), localName))
        return (Element) nodeList.item(i);
    }

    return null;
  }

  default Element getDocumentRoot()
  {
    return getSVGDocument().getDocumentElement();
  }

  SVGDocument getSVGDocument();
}
