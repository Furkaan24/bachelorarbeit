/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package de.gfai.mobile.data.svg.element;

import de.gfai.mobile.data.svg.SVGDocumentWrapper;
import java.util.Objects;
import org.apache.batik.anim.dom.SVGDOMImplementation;
import org.w3c.dom.Element;

public interface SVGDefs extends SVGDocumentWrapper
{
  default Element getDefsElement()
  {
    Element defsElement = getChildElement("defs");

    if (Objects.nonNull(defsElement))
      return defsElement;

    return createDefsElement();
  }

  default Element createDefsElement()
  {
    Element defsElement = getSVGDocument().createElementNS(SVGDOMImplementation.SVG_NAMESPACE_URI, "defs");
    getDocumentRoot().appendChild(defsElement);
    return defsElement;
  }

}
