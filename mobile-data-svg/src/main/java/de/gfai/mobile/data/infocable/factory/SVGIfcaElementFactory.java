package de.gfai.mobile.data.infocable.factory;

import de.gfai.cafm.geom.j2d.model.properties.Graphics2DPaintable;
import de.gfai.cafm.geom.j2d.view.J2DView;
import de.gfai.infocable.geom.j2d.model.core.J2DIfcaSymbol;
import de.gfai.infocable.geom.j2d.model.node.J2DPortInstance;
import de.gfai.j2d.J2DDXFDocument;
import de.gfai.mobile.data.infocable.SVGControl;
import de.gfai.mobile.data.svg.element.SVGDefs;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.apache.batik.anim.dom.SVGDOMImplementation;
import org.apache.batik.constants.XMLConstants;
import org.apache.batik.svggen.SVGGraphics2D;
import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGDocument;

public class SVGIfcaElementFactory implements PredicateDXFSymbolReference
{
  private final SVGDocument svgDocument;
  private final SVGControl svgControl;
  private final ArrayList<String> dxfCellNames = new ArrayList<>(2);

  public SVGIfcaElementFactory(SVGDocument svgDocument, SVGControl svgControl)
  {
    this.svgDocument = svgDocument;
    this.svgControl = svgControl;
  }

  public Element createSymbol(Graphics2DPaintable graphics2DPaintable)
  {
    Element element;

    if (isDXFSymbolReference(graphics2DPaintable))
      element = createDXFSymbolReference((J2DIfcaSymbol<?>) graphics2DPaintable);
    else
      element = createSVGElement(graphics2DPaintable);

    svgControl.appendTitleElement(svgDocument, element, graphics2DPaintable);
    svgControl.onClick(element, graphics2DPaintable);

    return element;
  }

  private Element createDXFSymbolReference(J2DIfcaSymbol<?> j2DIfcaSymbol)
  {
    Element referenceElement = svgDocument.createElementNS(SVGDOMImplementation.SVG_NAMESPACE_URI, "use");
    J2DDXFDocument j2DDXFDocument = j2DIfcaSymbol.getJ2DDXFDocument();

    if (!dxfCellNames.contains(j2DDXFDocument.getDXFCellName()))
    {
      Element symbolElement = createCellSymbolElement(j2DDXFDocument);

      dxfCellNames.add(j2DDXFDocument.getDXFCellName());
      symbolElement.setAttributeNS(null, "display", "block");

      if (j2DIfcaSymbol instanceof J2DPortInstance)
        removeAttributes(symbolElement, "stroke", "fill");
    }

    if (j2DIfcaSymbol instanceof J2DPortInstance)
      PortOccupyColorName.setPoiBelegtColor(referenceElement, ((J2DPortInstance) j2DIfcaSymbol).getInstance(), svgControl.getBackgroundColor());

    referenceElement.setAttributeNS(XMLConstants.XLINK_NAMESPACE_URI, "xlink:href", "#" + j2DDXFDocument.getDXFCellName());

    return referenceElement;
  }

  private Element createCellSymbolElement(J2DDXFDocument j2DDXFDocument)
  {
    Element svgSymbolElement = createSVGElement(j2DDXFDocument);
    SVGDefs svgDefsElement = () -> svgDocument;
    svgSymbolElement.setAttributeNS(null, "id", j2DDXFDocument.getDXFCellName());
    svgDefsElement.getDefsElement().appendChild(svgSymbolElement);
    return svgSymbolElement;
  }

  private Element createSVGElement(Graphics2DPaintable graphics2DPaintable)
  {
    SVGGraphics2D svgGraphics2D = new SVGGraphics2D(svgDocument);
    svgGraphics2D.setRenderingHint(J2DView.KEY_J2DVIEW_PAINTCHILDREN, false);
    graphics2DPaintable.paint(svgGraphics2D);
    return svgGraphics2D.getTopLevelGroup(true);
  }

  private void removeAttributes(Element element, String... attributeNames)
  {
    List<Element> elements = Stream.of(element)
                                   .filter(Objects::nonNull)
                                   .collect(Collectors.toCollection(ArrayList<Element>::new));

    for (int i = 0; i < elements.size(); ++i)
    {
      if (!(element instanceof org.apache.batik.dom.GenericText))
      {
        for (String attributeName : attributeNames)
          elements.get(i).removeAttributeNS(null, attributeName);

        if (elements.get(i).hasChildNodes())
        {
           IntStream.range(0, elements.get(i).getChildNodes().getLength())
                    .mapToObj(elements.get(i).getChildNodes()::item)
                    .forEach(node -> elements.add((Element) node));
        }
      }
    }
  }
}
