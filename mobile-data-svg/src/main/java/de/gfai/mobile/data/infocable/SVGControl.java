/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package de.gfai.mobile.data.infocable;

import de.gfai.cafm.geom.j2d.model.properties.Graphics2DPaintable;
import de.gfai.infocable.database.IfcaDatabase;
import de.gfai.mobile.data.svg.element.BackgroundColor;
import de.gfai.mobile.data.svg.element.SVGDefs;
import java.util.Optional;
import org.apache.batik.anim.dom.SVGDOMImplementation;
import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGDocument;

public interface SVGControl extends SVGTextControl
{
  default BackgroundColor getBackgroundColor()
  {
    return BackgroundColor.black;
  }

  /**
   * Registrieren von JavaScripten z.B.
   * @param svgDefsElement
   */
  default void initDefsElement(SVGDefs svgDefsElement)
  {
  }

  /**
   * Eine Art tooltip
   * @param svgDocument
   * @param svgElement
   * @param graphics2DPaintable
   */
  default void appendTitleElement(SVGDocument svgDocument, Element svgElement, Graphics2DPaintable graphics2DPaintable)
  {
    Optional.ofNullable(getTitleTextContent(graphics2DPaintable))
            .ifPresent(textContent -> {
              Element titleElement = svgDocument.createElementNS(SVGDOMImplementation.SVG_NAMESPACE_URI, "title");
              titleElement.setTextContent(getTitleTextContent(graphics2DPaintable));
              svgElement.appendChild(titleElement);
            });
  }

  default String getTitleTextContent(Graphics2DPaintable graphics2DPaintable)
  {
    return null;
  }

  /**
   *
   * @param svgElement
   * @param graphics2DPaintable
   */
  default void onClick(Element svgElement, Graphics2DPaintable graphics2DPaintable)
  {
    Optional.ofNullable(getOnClickAttributeValue(svgElement, graphics2DPaintable))
            .ifPresent(attributeValue -> svgElement.setAttributeNS(null, "onclick", attributeValue));
  }

  default String getOnClickAttributeValue(Element svgElement, Graphics2DPaintable graphics2DPaintable)
  {
    return null;
  }

  IfcaDatabase getDatabase();

}
