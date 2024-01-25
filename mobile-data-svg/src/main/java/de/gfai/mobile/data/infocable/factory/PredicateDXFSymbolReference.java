/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package de.gfai.mobile.data.infocable.factory;

import de.gfai.cafm.geom.j2d.model.properties.Graphics2DPaintable;
import de.gfai.infocable.geom.j2d.model.core.J2DIfcaSymbol;
import java.util.Objects;
import java.util.Optional;

public interface PredicateDXFSymbolReference
{
  default boolean isDXFSymbolReference(Graphics2DPaintable graphics2DPaintable)
  {
    return isJ2DIfcaSymbol(graphics2DPaintable)
           && hasDXFSymbol((J2DIfcaSymbol<?>) graphics2DPaintable);
  }

  default boolean isJ2DIfcaSymbol(Graphics2DPaintable graphics2DPaintable)
  {
    return graphics2DPaintable instanceof J2DIfcaSymbol<?>;
  }

  default boolean hasDXFSymbol(J2DIfcaSymbol<?> j2DIfcaSymbol)
  {
    return Optional.ofNullable(j2DIfcaSymbol.getJ2DDXFDocument())
                   .filter(j2DDXFDocument -> Objects.nonNull(j2DDXFDocument.getDXFDocument()))
                   .isPresent();
  }


}
