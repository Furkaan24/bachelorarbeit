/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package de.gfai.mobile.data.servlet.param;

import de.gfai.cafm.geom.j2d.model.properties.Graphics2DPaintable;
import de.gfai.cafm.geom.scene.properties.InstanceSupplier;
import de.gfai.infocable.database.IfcaDatabase;
import de.gfai.infocable.geom.j2d.model.core.J2DIfcaSymbol;
import de.gfai.infocable.model.core.IfcaDatabaseObject;
import de.gfai.j2d.J2DDXFDocument;
import de.gfai.mobile.data.context.MobileDataContext;
import de.gfai.mobile.data.database.IfcaDatabaseHolder;
import de.gfai.mobile.data.infocable.SVGControl;
import de.gfai.mobile.data.svg.element.SVGDefs;
import de.gfai.mobile.data.tooltip.MDInstanceSupplierTooltip;
import java.util.Objects;
import java.util.Optional;
import javax.naming.NamingException;
import org.apache.batik.anim.dom.SVGDOMImplementation;
import org.apache.batik.util.SVGConstants;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Element;

/**
 *
 * @author lost
 */
public class RackSVGControl implements SVGControl
{
  private static final String ON_CLICK_JS = "info(evt)";

  private final String webAppURL;
  private final String onClickJavaScript;

  public RackSVGControl(String webAppURL)
  {
    this.webAppURL = webAppURL;
    this.onClickJavaScript = String.format("function %s{"
                                           + " var url='%s/info?type=' + evt.currentTarget.getAttribute('class') + '&id=' + evt.currentTarget.id;"
                                           + " window.open(url);"
                                           + "}", ON_CLICK_JS, webAppURL);
  }

  @Override
  public String getAppName() throws NamingException
  {
    String appName = MobileDataContext.getAppName();
    return MobileDataContext.getAppName();
  }

  @Override
  public IfcaDatabase getDatabase()
  {
    return IfcaDatabaseHolder.getIfcaDatabase();
  }

  @Override
  public void initDefsElement(SVGDefs svgDefsElement)
  {
    appendOnClickJavaScript(svgDefsElement);
    appendSvgPanJavaScript(webAppURL, svgDefsElement);
  }

  private void appendOnClickJavaScript(SVGDefs svgDefsElement)
  {
    Element defsElement = svgDefsElement.getDefsElement();
    Element scriptElement = svgDefsElement.getSVGDocument().createElementNS(SVGDOMImplementation.SVG_NAMESPACE_URI, "script");
    CDATASection cdataSection = svgDefsElement.getSVGDocument().createCDATASection(onClickJavaScript);
    scriptElement.setAttributeNS(null, "type", SVGConstants.SVG_SCRIPT_TYPE_JAVASCRIPT);
    scriptElement.appendChild(cdataSection);
    defsElement.appendChild(scriptElement);
  }

  private void appendSvgPanJavaScript(String webAppURL, SVGDefs svgDefsElement)
  {
    Element defsElement = svgDefsElement.getDefsElement();
    Element scriptElement = svgDefsElement.getSVGDocument().createElementNS(SVGDOMImplementation.SVG_NAMESPACE_URI, "script");
    String svgpanURL = webAppURL + "js/svgpan.js";
    scriptElement.setAttributeNS(null, "type", SVGConstants.SVG_SCRIPT_TYPE_JAVASCRIPT);
    scriptElement.setAttributeNS(null, "xlink:href", svgpanURL);
    defsElement.appendChild(scriptElement);
  }

  @Override
  public String getOnClickAttributeValue(Element svgElement, Graphics2DPaintable graphics2DPaintable)
  {
    appendDatabaseLink(svgElement, graphics2DPaintable);
    return ON_CLICK_JS;
  }

  private void appendDatabaseLink(Element svgElement, Graphics2DPaintable graphics2DPaintable)
  {
    svgElement.setAttributeNS(null, "id", Objects.toString(getId(graphics2DPaintable)));
    svgElement.setAttributeNS(null, "class", getClassName(graphics2DPaintable));
  }

  private long getId(Graphics2DPaintable graphics2DPaintable)
  {
    if (graphics2DPaintable instanceof InstanceSupplier<?>)
    {
      Object instance = ((InstanceSupplier) graphics2DPaintable).getInstance();

      if (instance instanceof IfcaDatabaseObject)
        return ((IfcaDatabaseObject) instance).getId();
    }

    return 0l;
  }

  private String getClassName(Graphics2DPaintable graphics2DPaintable)
  {
    if (graphics2DPaintable instanceof InstanceSupplier<?>)
      return ((InstanceSupplier) graphics2DPaintable).getInstance().getClass().getSimpleName();

    return Optional.ofNullable(graphics2DPaintable)
                   .map(g2DPaintable -> g2DPaintable.getClass().getSimpleName())
                   .orElse("null");
  }

  @Override
  public String getTitleTextContent(Graphics2DPaintable graphics2DPaintable)
  {
    if (graphics2DPaintable instanceof InstanceSupplier<?>)
    {
      MDInstanceSupplierTooltip instanceSupplierTooltip = new MDInstanceSupplierTooltip(getDatabase(), () -> getSymbolName(graphics2DPaintable));
      return instanceSupplierTooltip.getTooltipText((InstanceSupplier<?>) graphics2DPaintable);
    }

    return Objects.toString(graphics2DPaintable);
  }

  private String getSymbolName(Graphics2DPaintable graphics2DPaintable)
  {
    return Optional.ofNullable(graphics2DPaintable)
                   .filter(g2DPaintable -> g2DPaintable instanceof J2DIfcaSymbol<?>)
                   .map(g2DPaintable -> ((J2DIfcaSymbol<?>) g2DPaintable).getJ2DDXFDocument())
                   .filter(Objects::nonNull)
                   .map(J2DDXFDocument::getDXFCellName)
                   .orElse(null);
  }
}
