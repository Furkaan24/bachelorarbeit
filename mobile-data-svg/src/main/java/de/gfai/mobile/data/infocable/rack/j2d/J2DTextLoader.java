/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package de.gfai.mobile.data.infocable.rack.j2d;

import de.gfai.infocable.database.IfcaDatabase;
import de.gfai.infocable.database.IfcaDatabaseCompound;
import de.gfai.infocable.database.methods.catalog.StructureMethods;
import de.gfai.infocable.geom.app.model.loader.text.TextSymbolLoader;
import de.gfai.infocable.geom.app.options.model.PortInstanceTextScale;
import de.gfai.infocable.geom.app.options.model.PortInstanceTextType;
import de.gfai.infocable.geom.app.options.model.structure.rack.RackFreeTextStructureVisibility;
import de.gfai.infocable.geom.app.options.model.structure.rack.RackInstanceTextStructureVisibility;
import de.gfai.infocable.geom.app.options.model.structure.rack.RackRuleTextStructureVisibility;
import de.gfai.infocable.geom.app.options.util.port.PortTextSymbolControl;
import de.gfai.infocable.geom.j2d.model.rack.J2DRackTransformGroup;
import de.gfai.infocable.model.catalog.Structure;
import de.gfai.infocable.model.factory.CatalogFactory;
import de.gfai.mobile.data.infocable.SVGTextControl;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

class J2DTextLoader implements IfcaDatabaseCompound
{
  private final IfcaDatabase ifcaDatabase;
  private final List<J2DRackTransformGroup> j2DRackTransformGroups;
  private final SVGTextControl svgTextControl;

  J2DTextLoader(IfcaDatabase ifcaDatabase, List<J2DRackTransformGroup> j2DRackTransformGroups, SVGTextControl svgTextControl)
  {
    this.ifcaDatabase = ifcaDatabase;
    this.j2DRackTransformGroups = j2DRackTransformGroups;
    this.svgTextControl = svgTextControl;
  }

  void loadTextSymbols(boolean instanceTextVisibility, boolean instanceTextPortVisibility,
                       boolean freeTextObjectVisibility, boolean freeTextPortVisibility,
                       boolean ruleTextObjectVisibility, boolean ruleTextPortVisibility) throws SQLException, InterruptedException
  {
    initRackTextOptions(instanceTextVisibility, instanceTextPortVisibility,
                        freeTextObjectVisibility, freeTextPortVisibility, ruleTextObjectVisibility, ruleTextPortVisibility);
    textSymbolLoader();
  }

  private void initRackTextOptions(boolean instanceTextObjectVisibility, boolean instanceTextPortVisibility,
                                   boolean freeTextObjectVisibility, boolean freTextPortVisibility,
                                   boolean ruleTextObjectVisibility, boolean ruleTextPortVisibility) throws SQLException
  {
    initInstanceTextVisibility(instanceTextObjectVisibility, instanceTextPortVisibility);
    initFreeTextVisibility(freeTextObjectVisibility, freTextPortVisibility);
    initRuleTextVisibility(ruleTextObjectVisibility, ruleTextPortVisibility);
  }

  private void initInstanceTextVisibility(boolean instanceTextObjectVisibility, boolean instanceTextPortVisibility) throws SQLException
  {
    Structure structure = getStructure(svgTextControl.getInstanceTextStructure());
    RackInstanceTextStructureVisibility.getInstance().setVisible(instanceTextObjectVisibility && Objects.nonNull(structure));

    if (RackInstanceTextStructureVisibility.getInstance().isVisible())
      RackInstanceTextStructureVisibility.getInstance().addStructure(structure);

    RackFreeTextStructureVisibility.getInstance().setShowPorts(instanceTextPortVisibility && RackInstanceTextStructureVisibility.getInstance().isVisible());

    if (!RackFreeTextStructureVisibility.getInstance().isShowPorts())
      PortInstanceTextType.NONE.setSelected(true);
    else
    {
      Optional.ofNullable(svgTextControl.getPortInstanceTextType())
              .orElse(PortInstanceTextType.NONE)
              .setSelected(true);

      PortInstanceTextScale.getInstance().setScale(Math.max(.1, svgTextControl.getPortInstanceTextScale()));
    }
  }

  private void initFreeTextVisibility(boolean freeTextObjectVisibility, boolean freTextPortVisibility) throws SQLException
  {
    Structure structure = getStructure(svgTextControl.getFreeTextStructure());
    RackFreeTextStructureVisibility.getInstance().setVisible(freeTextObjectVisibility && Objects.nonNull(structure));

    if (RackFreeTextStructureVisibility.getInstance().isVisible())
      RackFreeTextStructureVisibility.getInstance().addStructure(structure);

    RackFreeTextStructureVisibility.getInstance().setShowPorts(freTextPortVisibility && RackFreeTextStructureVisibility.getInstance().isVisible()
                                                               && svgTextControl.isShowPortFreeText());
  }

  private void initRuleTextVisibility(boolean ruleTextObjectVisibility, boolean ruleTextPortVisibility) throws SQLException
  {
    Structure structure = getStructure(svgTextControl.getRuleTextStructure());
    RackRuleTextStructureVisibility.getInstance().setVisible(ruleTextObjectVisibility && Objects.nonNull(structure));

    if (RackRuleTextStructureVisibility.getInstance().isVisible())
      RackRuleTextStructureVisibility.getInstance().addStructure(structure);

    RackRuleTextStructureVisibility.getInstance().setShowPorts(ruleTextPortVisibility && RackRuleTextStructureVisibility.getInstance().isVisible()
                                                               && svgTextControl.isShowPortRuleText());
  }

  private Structure getStructure(String strukturChild) throws SQLException
  {
    Structure structure = structures().stream()
                                      .filter(st -> Objects.equals(st.getName(), strukturChild))
                                      .findFirst()
                                      .orElse(null);
    if (Objects.isNull(structure))
    {
      StructureMethods structureMethods = StructureMethods.getInstance(getDatabase());
      structure = structureMethods.getStructures().stream()
                                                  .filter(st -> Objects.equals(st.getName(), strukturChild))
                                                  .findFirst()
                                                  .orElse(null);
      if (Objects.nonNull(structure))
        CatalogFactory.getFactory(getDatabase(), CatalogFactory.class).putStructure(structure);
    }

    return structure;
  }

  private Collection<Structure> structures()
  {
    return CatalogFactory.getFactory(getDatabase(), CatalogFactory.class).structures();
  }

  private void textSymbolLoader() throws InterruptedException, SQLException
  {
    TextSymbolLoader textSymbolLoader = new TextSymbolLoader(getDatabase());

    for (J2DRackTransformGroup j2DRackTransformGroup : j2DRackTransformGroups)
    {
      textSymbolLoader.loadTextSymbols(j2DRackTransformGroup);
      PortTextSymbolControl.getInstance().updateTextSymbols(j2DRackTransformGroup);
    }
  }

  @Override
  public IfcaDatabase getDatabase()
  {
    return ifcaDatabase;
  }
}
