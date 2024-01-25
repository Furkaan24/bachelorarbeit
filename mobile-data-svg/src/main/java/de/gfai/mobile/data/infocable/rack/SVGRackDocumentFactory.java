/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package de.gfai.mobile.data.infocable.rack;

import de.gfai.infocable.database.IfcaDatabase;
import de.gfai.mobile.data.infocable.SVGControl;
import de.gfai.mobile.data.infocable.rack.j2d.J2DRackModel;
import de.gfai.mobile.data.infocable.rack.j2d.J2DRackModelLoader;
import de.gfai.mobile.data.infocable.rack.svg.SVGRackDocumentLoader;
import de.gfai.mobile.data.svg.element.BackgroundColor;
import de.gfai.mobile.data.svg.element.SVGStyle;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;
import org.apache.batik.anim.dom.SVGDOMImplementation;
import org.w3c.dom.svg.SVGDocument;

public class SVGRackDocumentFactory
{
  private final SVGControl svgControl;
  private SVGDocument svgDocument;
  private IfcaDatabase ifcaDatabase;

  public SVGRackDocumentFactory(SVGControl svgControl)
  {
    this.svgControl = svgControl;
  }

  public SVGDocument loadSVGRackDocument(boolean ppvVisibility, boolean isInstanceTextObject, boolean isInstanceTextPort,
                                         boolean isFreeTextObject, boolean isFreeTextPort,
                                         boolean isRuleTextObject, boolean isRuleTextPort, long... ktiIds) throws SQLException, IOException, CloneNotSupportedException, InterruptedException
  {
    ifcaDatabase = svgControl.getDatabase();
    if(ifcaDatabase != null){
    J2DRackModelLoader j2DRackViewSvgModelLoader = new J2DRackModelLoader(ifcaDatabase , svgControl);
    J2DRackModel j2DRackModel = j2DRackViewSvgModelLoader.loadRackModel(ppvVisibility, isInstanceTextObject, isInstanceTextPort,
                                                     isFreeTextObject, isFreeTextPort,
                                                     isRuleTextObject, isRuleTextPort, ktiIds);
    SVGRackDocumentLoader svgRackDocumentLoader = new SVGRackDocumentLoader(getSVGDocument(), svgControl, j2DRackModel.getSceneRoot());
    setBackgroundColor(svgControl.getBackgroundColor());
    svgRackDocumentLoader.loadSVGDocument();
    }
    return getSVGDocument();
  }

  public void setBackgroundColor(BackgroundColor backgroundColor)
  {
    getSVGDocument().getDocumentElement().setAttributeNS("null", "style", "background:" + backgroundColor.name());
  }

  public SVGDocument getSVGDocument()
  {
    if (Objects.isNull(svgDocument))
      svgDocument = createSVGDocument();
    return svgDocument;
  }

  private SVGDocument createSVGDocument()
  {
    SVGDocument svgDoc = (SVGDocument) SVGDOMImplementation.getDOMImplementation().createDocument(SVGDOMImplementation.SVG_NAMESPACE_URI, "svg", null);
    SVGStyle svgStyleElement = () -> svgDoc;
    svgStyleElement.createStyleElement();
    svgControl.initDefsElement(() -> svgDoc);
    return svgDoc;
  }
}
