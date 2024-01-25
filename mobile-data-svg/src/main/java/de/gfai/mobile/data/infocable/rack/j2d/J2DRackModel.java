/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package de.gfai.mobile.data.infocable.rack.j2d;

import de.gfai.cafm.geom.j2d.model.J2DGroup;
import de.gfai.infocable.database.IfcaDatabase;
import de.gfai.infocable.database.methods.instance.InstanceMethods;
import de.gfai.infocable.geom.j2d.model.rack.J2DRackSceneGraphRoot;
import de.gfai.infocable.geom.j2d.model.rack.J2DRackTransformGroup;
import de.gfai.infocable.geom.j2d.model.rack.J2DRackViewModel;
import de.gfai.infocable.model.instance.Instance;
import java.sql.SQLException;

public class J2DRackModel extends J2DRackViewModel
{
  public J2DRackModel(IfcaDatabase ifcaDatabase, long... ktiIds) throws SQLException
  {
    super();
    j2DRackViewSvgModelInit(ifcaDatabase, ktiIds);
  }

  private void j2DRackViewSvgModelInit(IfcaDatabase ifcaDatabase, long... ktiIds) throws SQLException
  {
    setDatabase(ifcaDatabase);
    addJ2DRackTransformGroups(ktiIds);
  }

  private void addJ2DRackTransformGroups(long... ktiIds) throws SQLException
  {
    InstanceMethods instanceMethods = InstanceMethods.getInstance(getDatabase());
    J2DGroup rackGroup = getRackGroup();

    for (long ktiId : ktiIds)
      rackGroup.addChild(createJ2DRackTransformGroup(instanceMethods.getInstanceById(ktiId)));
  }

  private J2DRackTransformGroup createJ2DRackTransformGroup(Instance rackInstance)
  {
    J2DRackTransformGroup j2DTransformGroup = new J2DRackTransformGroup();
    j2DTransformGroup.setUserData(rackInstance);
    return j2DTransformGroup;
  }

  @Override
  public J2DRackSceneGraphRoot getSceneRoot()
  {
    return (J2DRackSceneGraphRoot) super.getSceneRoot();
  }
}
