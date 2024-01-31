package de.gfai.mobile.data.infocable.rack.svg;

import de.gfai.cafm.geom.j2d.model.J2DGroup;
import de.gfai.cafm.geom.j2d.model.J2DNode;
import de.gfai.cafm.geom.j2d.model.properties.Graphics2DPaintable;
import de.gfai.infocable.geom.app.view.text.TextSymbol;
import de.gfai.infocable.geom.j2d.model.core.J2DIfca;
import de.gfai.infocable.geom.j2d.model.rack.J2DRackSceneGraphRoot;
import de.gfai.mobile.data.infocable.SVGControl;
import de.gfai.mobile.data.infocable.factory.PredicateDXFSymbolReference;
import de.gfai.mobile.data.infocable.factory.SVGIfcaElementFactory;
import de.gfai.mobile.data.svg.element.SVGViewport;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGDocument;

public class SVGRackDocumentLoader implements PredicateDXFSymbolReference
{
  private static final Predicate<J2DNode> PREDICATE_SYMBOL_NODE = j2DNode -> j2DNode instanceof Graphics2DPaintable
                                                                             && (j2DNode instanceof J2DIfca<?>
                                                                                 || j2DNode instanceof TextSymbol);
  private final SVGControl svgControl;

  private final SVGViewport svgViewportElementFactory;
  private final SVGIfcaElementFactory svgSymbolElementFactory;

  private final J2DRackSceneGraphRoot j2DRackSceneGraphRoot;
  private final Rectangle2D modelBounds2D;
  private final double dX;
  private final double dY;

  public SVGRackDocumentLoader(SVGDocument svgDocument, SVGControl svgControl, J2DRackSceneGraphRoot j2DRackSceneGraphRoot)
  {
    this.svgControl = svgControl;
    this.svgViewportElementFactory = () -> svgDocument;
    this.svgSymbolElementFactory = new SVGIfcaElementFactory(svgDocument, svgControl);

    this.j2DRackSceneGraphRoot = j2DRackSceneGraphRoot;
    this.modelBounds2D = j2DRackSceneGraphRoot.getBounds2D(true);
    this.dX = Math.abs(modelBounds2D.getMinX());
    this.dY = Math.abs(modelBounds2D.getMaxY());
  }

  public void loadSVGDocument()
  {
    Element viewPortElement = svgViewportElementFactory.createViewportElement(modelBounds2D, svgControl.getBackgroundColor());
    appendChildren(j2DRackSceneGraphRoot, viewPortElement);
  }

  private void appendChildren(J2DGroup j2DRackGroup, Element viewPortElement)
  {
    List<J2DNode> children = getChildren(j2DRackGroup);

    for (int i = 0; i < children.size(); ++i)
    {
      J2DNode childNode = children.get(i);

      if (PREDICATE_SYMBOL_NODE.test(childNode))
      {
        Optional.ofNullable(svgSymbolElementFactory.createSymbol((Graphics2DPaintable) childNode))
                .ifPresent(childElement -> appendChild(viewPortElement, childNode, childElement));
      }

      if (childNode instanceof J2DGroup)
        children.addAll(getChildren((J2DGroup) childNode));
    }
  }

  private void appendChild(Element viewPortElement, Graphics2DPaintable graphics2DPaintable, Element svgElement)
  {
    if (graphics2DPaintable instanceof J2DNode)
      transform(svgElement, getAffineTransform((J2DNode) graphics2DPaintable));

    viewPortElement.appendChild(svgElement);
  }

  private AffineTransform getParentAffineTransform(J2DNode j2DNode)
  {
    return Optional.ofNullable(j2DNode.getSceneGraphParent())
                   .map(J2DNode::getAffineTransform)
                   .orElseGet(() -> j2DNode.getAffineTransform());
  }

  private AffineTransform getAffineTransform(J2DNode j2DNode)
  {
    if (isDXFSymbolReference(j2DNode)) // 99.99%
      return j2DNode.getAffineTransform();

    return getParentAffineTransform(j2DNode);
  }

  private void transform(Element svgElement, AffineTransform tx)
  {
    double[] flatmatrix = new double[6];

    tx.getMatrix(flatmatrix);

    if (flatmatrix[5] == 0)
      flatmatrix[5] = dY;
    else
      flatmatrix[5] = dY - flatmatrix[5];

    svgElement.setAttributeNS(null, "transform", "matrix(" + flatmatrix[0] + " "
                                                           + -flatmatrix[1] + " "
                                                           + flatmatrix[2] + " "
                                                           + -flatmatrix[3] + " "
                                                           + (flatmatrix[4] + dX) + " " // X-Koordinate
                                                           + flatmatrix[5] + ")"); // Y-Koordinate
  }

  private List<J2DNode> getChildren(J2DGroup j2DRackGroup)
  {
    return IntStream.range(0, j2DRackGroup.countChildren())
                    .mapToObj(j2DRackGroup::getChild)
                    .collect(Collectors.toCollection(ArrayList<J2DNode>::new));
  }
}
