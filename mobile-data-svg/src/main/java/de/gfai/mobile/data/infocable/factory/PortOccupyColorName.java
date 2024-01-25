/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package de.gfai.mobile.data.infocable.factory;

import de.gfai.infocable.model.instance.port.PortInstance;
import de.gfai.infocable.model.records.PortOccupy;
import de.gfai.mobile.data.svg.element.BackgroundColor;
import java.util.Arrays;
import org.w3c.dom.Element;

enum PortOccupyColorName
{
  RED(PortOccupy.CONN_CONN, "rgb(255, 50, 0)"),

  MAGENTA(PortOccupy.CONN_FREE, "rgb(255, 0, 255)"),

  GREEN(PortOccupy.FREE_CONN, "rgb(150, 255, 0)"),

  GRAY(PortOccupy.FREE_FREE, "rgb(200, 190, 183)"),

  WHITE(null, "WHITE");

  private final PortOccupy portOccupy;
  private final String colorName;

  private PortOccupyColorName(PortOccupy portOccupy, String colorName)
  {
    this.portOccupy = portOccupy;
    this.colorName = colorName;
  }

  static void setPoiBelegtColor(Element svgElement, PortInstance portInstance, BackgroundColor backgroundColor)
  {
    PortOccupyColorName portOccupyColorName = getPortOccupyColorName(portInstance.getPortOccupy());
    svgElement.setAttributeNS(null, "stroke", portOccupyColorName.colorName);
    svgElement.setAttributeNS(null, "fill", backgroundColor.name());
    svgElement.setAttributeNS(null, "fill-opacity", "0");
  }

  private static PortOccupyColorName getPortOccupyColorName(PortOccupy portOccupy)
  {
    return Arrays.stream(values())
                 .filter(value -> value.portOccupy == portOccupy)
                 .findFirst()
                 .orElse(WHITE);
  }
}
