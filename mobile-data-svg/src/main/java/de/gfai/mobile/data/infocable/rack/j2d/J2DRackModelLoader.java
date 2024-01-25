/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package de.gfai.mobile.data.infocable.rack.j2d;

import de.gfai.cafm.geom.j2d.model.J2DGroup;
import de.gfai.cafm.geom.j2d.model.J2DNode;
import de.gfai.core.util.function.Invalidatable;
import de.gfai.infocable.database.IfcaDatabase;
import de.gfai.infocable.database.IfcaDatabaseCompound;
import de.gfai.infocable.geom.app.options.database.rack.RackViewOptions;
import de.gfai.infocable.geom.j2d.db.rack.RackViewDatabaseMethods;
import de.gfai.infocable.geom.j2d.model.node.instance.J2DInstance;
import de.gfai.infocable.geom.j2d.model.rack.J2DRackTransformGroup;
import de.gfai.infocable.geom.j2d.model.rack.J2DRackViewModel;
import de.gfai.infocable.geom.j2d.view.rack.J2DArrangeRacksUtil;
import de.gfai.infocable.model.core.NetObject;
import de.gfai.infocable.model.instance.Instance;
import de.gfai.mobile.data.infocable.SVGTextControl;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class J2DRackModelLoader implements IfcaDatabaseCompound
{
  private final IfcaDatabase ifcaDatabase;
  private final SVGTextControl svgTextControl;

  public J2DRackModelLoader(IfcaDatabase ifcaDatabase, SVGTextControl svgTextControl)
  {
    this.ifcaDatabase = Objects.requireNonNull(ifcaDatabase);
    this.svgTextControl = svgTextControl;
  }

  public J2DRackModel loadRackModel(boolean ppvVisibility, boolean instanceTextObjectVisibility, boolean instanceTextPortVisibility,
                                    boolean freeTextObjectVisibility, boolean freeTextPortVisibility,
                                    boolean ruleTextObjectVisibility, boolean ruleTextPortVisibility, long... ktiIds) throws SQLException, IOException, CloneNotSupportedException, InterruptedException
  {
    J2DRackModel j2DRackModel = new J2DRackModel(getDatabase(), ktiIds);
    List<J2DRackTransformGroup> j2DRackTransformGroups = collectRackTransformGroups(j2DRackModel);
    loadRacks(j2DRackModel.getRackGroup(), j2DRackTransformGroups);
    loadTextSymbols(j2DRackTransformGroups, instanceTextObjectVisibility, instanceTextPortVisibility,
                    freeTextObjectVisibility, freeTextPortVisibility,
                    ruleTextObjectVisibility, ruleTextPortVisibility);
    loadVisiblePpvs(j2DRackModel, ppvVisibility);
    return j2DRackModel;
  }

  private List<J2DRackTransformGroup> collectRackTransformGroups(J2DRackViewModel j2DRackViewModel)
  {
    return Arrays.stream(j2DRackViewModel.getRackGroup().getSceneGraphChildren())
                 .filter(J2DRackTransformGroup.class::isInstance)
                 .map(J2DRackTransformGroup.class::cast)
                 .collect(Collectors.toList());
  }

  private void loadRacks(J2DGroup j2DRackGroup, List<J2DRackTransformGroup> j2DRackTransformGroups) throws SQLException, IOException, CloneNotSupportedException, InterruptedException
  {
    for (int i = 0; i < j2DRackTransformGroups.size(); ++i)
    {
      J2DInstance j2DRackInstance = loadRackJ2DInstance(j2DRackTransformGroups.get(i));

      if (i > 0)
        J2DArrangeRacksUtil.arrangeRacks(j2DRackGroup, Collections.emptyMap());

      loadInstallations(j2DRackInstance);
    }
  }

  private J2DInstance loadRackJ2DInstance(J2DRackTransformGroup j2DRackTransformGroup) throws SQLException, IOException, CloneNotSupportedException, InterruptedException
  {
    RackViewDatabaseMethods rackViewDatabaseMethods = RackViewDatabaseMethods.getInstance(getDatabase());
    Instance rackInstance = (Instance) j2DRackTransformGroup.getUserData();
    J2DInstance j2DRackInstance = rackViewDatabaseMethods.loadRackSymbol(rackInstance);
    j2DRackTransformGroup.addChild(j2DRackInstance);
    return j2DRackInstance;
  }

  private void loadInstallations(J2DInstance j2DRackInstance) throws SQLException, IOException, CloneNotSupportedException, InterruptedException
  {
    RackViewDatabaseMethods rackViewDatabaseMethods = RackViewDatabaseMethods.getInstance(getDatabase());
    Map<NetObject, J2DInstance> loadedInstallations = rackViewDatabaseMethods.loadInstallations(j2DRackInstance, RackViewOptions.getInstance());
    loadPorts(loadedInstallations);
    loadPins(loadedInstallations);

    loadedInstallations.values()
                       .parallelStream()
                       .forEach(this::invalidate);
  }

  private void invalidate(J2DNode j2DNode)
  {
    if (j2DNode instanceof Invalidatable)
      ((Invalidatable) j2DNode).invalidate();

    if (j2DNode instanceof J2DGroup
        && ((J2DGroup) j2DNode).countChildren() > 0)
    {
      Arrays.stream(((J2DGroup) j2DNode).getSceneGraphChildren())
            .parallel()
            .forEach(this::invalidate);
    }
  }

  private void loadPorts(Map<NetObject, J2DInstance> loadedInstallations) throws SQLException, IOException, CloneNotSupportedException, InterruptedException
  {
    RackViewDatabaseMethods rackViewDatabaseMethods = RackViewDatabaseMethods.getInstance(getDatabase());
    rackViewDatabaseMethods.loadPorts(loadedInstallations, RackViewOptions.getInstance());
  }

  private void loadPins(Map<NetObject, J2DInstance> loadedInstallations) throws SQLException, IOException
  {
    RackViewDatabaseMethods rackViewDatabaseMethods = RackViewDatabaseMethods.getInstance(getDatabase());
    rackViewDatabaseMethods.loadPins(loadedInstallations, RackViewOptions.getInstance());
  }

  private void loadTextSymbols(List<J2DRackTransformGroup> j2DRackTransformGroups,
                               boolean instanceTextObjectVisibility, boolean instanceTextPortVisibility,
                               boolean freeTextObjectVisibility, boolean freeTextPortVisibility,
                               boolean ruleTextObjectVisibility, boolean ruleTextPortVisibility) throws SQLException, InterruptedException
  {
    J2DTextLoader j2DTextLoader = new J2DTextLoader(ifcaDatabase, j2DRackTransformGroups, svgTextControl);
    j2DTextLoader.loadTextSymbols(instanceTextObjectVisibility, instanceTextPortVisibility,
                                  freeTextObjectVisibility, freeTextPortVisibility,
                                  ruleTextObjectVisibility, ruleTextPortVisibility);
  }

  private void loadVisiblePpvs(J2DRackModel j2DRackModel, boolean ppvVisibility) throws SQLException
  {
    J2DPpvLoader j2DPpvLoader = new J2DPpvLoader(j2DRackModel);
    j2DPpvLoader.loadVisiblePpvs(ppvVisibility);
  }

  @Override
  public IfcaDatabase getDatabase()
  {
    return ifcaDatabase;
  }
}
