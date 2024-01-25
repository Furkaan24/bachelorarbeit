/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package de.gfai.mobile.data.svg.element;

import org.apache.batik.anim.dom.SVGDOMImplementation;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Element;

public interface SVGStyle extends SVGDefs
{
  default Element createStyleElement()
  {
    Element styleElement = getSVGDocument().createElementNS(SVGDOMImplementation.SVG_NAMESPACE_URI, "style");
    Element defsElement = getDefsElement();

    styleElement.setAttributeNS(null, "type", "text/css");
    styleElement.appendChild(createStyleCDATASection());

    if (defsElement.hasChildNodes())
      defsElement.insertBefore(styleElement, getDefsElement().getFirstChild());
    else
      defsElement.appendChild(styleElement);

    return styleElement;
  }

  default CDATASection createStyleCDATASection()
  {
    return getSVGDocument().createCDATASection("*{stroke-width: 120;}"
                                               + "text {stroke-width:0;}"
                                               + "*:hover{stroke:cyan;}"
                                               + "text:hover{fill:cyan;}"
                                               + "text{font-family: 'Arial';font-size: 10px;}"
                                               + "*/");
  }

}
