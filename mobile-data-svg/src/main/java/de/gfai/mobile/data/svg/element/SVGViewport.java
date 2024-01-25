/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package de.gfai.mobile.data.svg.element;

import de.gfai.mobile.data.svg.SVGDocumentWrapper;
import java.awt.geom.Rectangle2D;
import org.apache.batik.anim.dom.SVGDOMImplementation;
import org.w3c.dom.Element;

public interface SVGViewport extends SVGDocumentWrapper
{
  static final double DEFAULT_WIDTH = 1000.0;
  static final double DEFAULT_HEIGHT = 1000.0;

  default Element createViewportElement(Rectangle2D bounds2D, BackgroundColor backgroundColor)
  {
    Element viewportElement = getSVGDocument().createElementNS(SVGDOMImplementation.SVG_NAMESPACE_URI, "g");
    double scale = Math.min(getHeight(DEFAULT_HEIGHT)/bounds2D.getHeight(), getWidth(DEFAULT_WIDTH)/bounds2D.getWidth());
    viewportElement.setAttributeNS(null, "id", "viewport");
    viewportElement.setAttributeNS(null, "transform", "scale(" + scale + ")");
    getDocumentRoot().appendChild(viewportElement);
    return viewportElement;
  }
}
