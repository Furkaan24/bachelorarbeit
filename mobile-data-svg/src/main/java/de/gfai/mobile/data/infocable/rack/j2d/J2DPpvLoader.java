package de.gfai.mobile.data.infocable.rack.j2d;

import de.gfai.cafm.geom.j2d.model.J2DGroup;
import de.gfai.cafm.geom.j2d.model.J2DNode;
import de.gfai.infocable.database.IfcaDatabase;
import de.gfai.infocable.database.methods.catalog.PortMethods;
import de.gfai.infocable.database.methods.connect.PpvMethods;
import de.gfai.infocable.geom.j2d.model.node.J2DPPV;
import de.gfai.infocable.geom.j2d.model.node.J2DPortInstance;
import de.gfai.infocable.geom.j2d.model.node.instance.J2DInstance;
import de.gfai.infocable.geom.util.IfcaSceneObject;
import de.gfai.infocable.geom.util.IfcaSceneObjectCollector;
import de.gfai.infocable.geom.util.NodeCollectorUtil;
import de.gfai.infocable.geom.util.SceneObjectWizard;
import de.gfai.infocable.model.connect.Ppv;
import de.gfai.infocable.model.factory.ConnectFactory;
import de.gfai.infocable.model.instance.Instance;
import de.gfai.infocable.model.instance.port.PortInstance;
import de.gfai.infocable.model.records.PortOccupy;
import de.gfai.infocable.model.records.PpvType;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

final class J2DPpvLoader
{
  private static final Predicate<J2DPortInstance> PATCH = pg -> Stream.of(PortOccupy.CONN_CONN, PortOccupy.CONN_FREE)
                                                                      .anyMatch(pg.getInstance().getPortOccupy()::equals);
  private static final int ORACLE_MAX_ID_COUNT = 1000;

  private final J2DRackModel j2DRackModel;

   J2DPpvLoader(J2DRackModel j2DRackModel)
  {
    this.j2DRackModel = j2DRackModel;
  }

  void loadVisiblePpvs(boolean showPatches) throws SQLException
  {
    J2DGroup ppvGroup = getPPVGroup();
    List<J2DPortInstance> j2DPortInstance = getJ2DPortInstances();
    Collection<J2DPortInstance> subList;

    ppvGroup.clear();

    for (int i = 0; i < j2DPortInstance.size(); i += ORACLE_MAX_ID_COUNT)
    {
      subList = j2DPortInstance.subList(i, Math.min(j2DPortInstance.size(), i + ORACLE_MAX_ID_COUNT));
      addPatches(ppvGroup, subList, j2DPortInstance, showPatches);
    }
  }

  private void addPatches(J2DGroup ppvGroup, Collection<J2DPortInstance> j2DPortInstances,
                          Collection<J2DPortInstance> allJ2DPortInstances, boolean showPatches) throws SQLException
  {
    IfcaDatabase db = j2DRackModel.getDatabase();
    ConnectFactory factory = ConnectFactory.getFactory(db, ConnectFactory.class);
    PortMethods portMethods = PortMethods.getInstance(db);
    String sql = "select PPV_ID, PPV_NAME, PPV_TYP, "
               + "       POI_ID_1, PT_ID_1, PR_ID_1,"
               + "       POI_ID_2, PT_ID_2, PR_ID_2 "
               + "from INFOCABLE.PPV~ "
               + "where POI_ID_1 in (%s) "
               + "union "
               + "select PPV_ID, PPV_NAME, PPV_TYP, "
               + "       POI_ID_1, PT_ID_1, PR_ID_1,"
               + "       POI_ID_2, PT_ID_2, PR_ID_2 "
               + "from INFOCABLE.PPV~ "
               + "where POI_ID_2 in (%s)";
    String szPoiIds = getSzPoiIds(j2DPortInstances);

    sql = String.format(db.prepareSql(sql), szPoiIds, szPoiIds);

    try(Statement s =  db.createStatement(); ResultSet rs = s.executeQuery(sql))
    {
      J2DPortInstance j2DPoi1;
      J2DPortInstance j2DPoi2;
      Ppv ppv;

      while(rs.next())
      {
        j2DPoi1 = getJ2DPortInstance(rs.getLong(4), allJ2DPortInstances);
        j2DPoi2 = getJ2DPortInstance(rs.getLong(7), allJ2DPortInstances);

        if (Objects.nonNull(j2DPoi1) && Objects.nonNull(j2DPoi2))
        {
          ppv = factory.createPpv(rs.getLong(1), rs.getString(2), PpvType.getPpvType(rs.getString(3).charAt(0)),
                                  j2DPoi1.getInstance(),
                                  portMethods.getPortType(rs.getLong(5)),
                                  portMethods.getProtocol(rs.getLong(6)),
                                  j2DPoi2.getInstance(),
                                  portMethods.getPortType(rs.getLong(8)),
                                  portMethods.getProtocol(rs.getLong(9)),
                                  null);
          PpvMethods.getInstance(db).getCablingFromPpv(ppv);
          ppvGroup.addChild(createJ2DPPV(ppv, j2DPoi1, j2DPoi2, showPatches));
        }
      }
    }
  }

  private String getSzPoiIds(Collection<J2DPortInstance> j2DPortInstances)
  {
    return j2DPortInstances.stream()
                           .map(j2DPortInstance -> j2DPortInstance.getInstance().getId())
                           .collect(Collectors.toList())
                           .toString()
                           .replaceAll("\\[|\\]", "");
  }

  private J2DPPV createJ2DPPV(Ppv ppv, J2DPortInstance j2DPoi1, J2DPortInstance j2DPoi2, boolean show)
  {
    J2DPPV j2dppv = new J2DPPV(ppv, j2DPoi1, j2DPoi2);
    j2dppv.setVisible(show);
    return j2dppv;
  }

  public void removeRackPatches(Instance rackInstance)
  {
    Optional.ofNullable(getRackPatches(rackInstance))
            .filter(ppvs -> !ppvs.isEmpty())
            .ifPresent(this::remove);
  }

  public Collection<Ppv> getRackPatches(Instance rackInstance)
  {
    Collection<PortInstance> portInstances = getVisibleRackPortInstances(rackInstance);
    return NodeCollectorUtil.collectFromRoot(getPPVGroup(), Ppv.class::isInstance, true)
                            .stream()
                            .map(SceneObjectWizard::getPpv)
                            .filter(predicatePpv(portInstances))
                            .collect(Collectors.toList());
  }

  public Predicate<Ppv> predicatePpv(Collection<PortInstance> portInstances)
  {
    return ppv -> portInstances.stream()
                               .anyMatch(predicatePpvPortInstance(ppv));
  }

  public Predicate<PortInstance> predicatePpvPortInstance(Ppv ppv)
  {
    return portInstance -> Objects.equals(ppv.getPortInstance1(), portInstance)
                        || Objects.equals(ppv.getPortInstance2(), portInstance);
  }

  public Collection<PortInstance> getVisibleRackPortInstances(Instance rackInstance)
  {
    return Optional.ofNullable(getRackJ2DInstance(rackInstance))
                   .map(j2DRackInstance -> {
                     return NodeCollectorUtil.collectFromRoot(j2DRackInstance, PortInstance.class::isInstance, true)
                                             .stream()
                                             .map(SceneObjectWizard::getPortInstance)
                                             .collect(Collectors.toList());
                   })
                   .orElse(Collections.emptyList());
  }

  public J2DInstance getRackJ2DInstance(Instance rackInstance)
  {
    return NodeCollectorUtil.collectIfcaNodes(getRackGroup(), rackInstance)
                            .stream()
                            .filter(J2DInstance.class::isInstance)
                            .findFirst()
                            .map(J2DInstance.class::cast)
                            .orElse(null);
  }

  public void remove(Collection<Ppv> ppvs)
  {
    Collection<J2DNode> removeNodes = new ArrayList<>(2);

    for (J2DNode ppvGroup : getPPVGroup())
    {
      if (ppvGroup instanceof J2DPPV)
      {
        if (ppvs.stream()
                .anyMatch(ppv -> ppv.getId() == ((J2DPPV) ppvGroup).getInstance().getId()))
        {
          removeNodes.add(ppvGroup);
        }
      }
    }

    removeNodes.forEach(j2DNode -> j2DNode.removeFromSceneGraphParent());
  }

  public void add(Collection<Ppv> ppvs) throws InterruptedException
  {
    J2DGroup ppvGroup = getPPVGroup();
    Collection<J2DPortInstance> j2DPortInstance = getJ2DPortInstances();

    ppvs.forEach(ppv -> {
      J2DPortInstance j2DPortInstance1 = getJ2DPortInstance(ppv.getPortInstance1().getId(), j2DPortInstance);
      J2DPortInstance j2DPortInstance2 = getJ2DPortInstance(ppv.getPortInstance2().getId(), j2DPortInstance);
      ppvGroup.addChild(new J2DPPV(ppv, j2DPortInstance1, j2DPortInstance2));
    });
  }

  private J2DGroup getPPVGroup()
  {
    return j2DRackModel.getPatchGroup();
  }

  private List<J2DPortInstance> getJ2DPortInstances()
  {
    IfcaSceneObjectCollector j2DNodeCollector = new IfcaSceneObjectCollector(IfcaSceneObject.PORT_INSTANCE);
    j2DNodeCollector.collect(getRackGroup());
    return j2DNodeCollector.getPortSceneObjects()
                           .stream()
                           .map(J2DPortInstance.class::cast)
                           .filter(PATCH)
                           .collect(Collectors.toList());
  }

  private J2DGroup getRackGroup()
  {
    return j2DRackModel.getRackGroup();
  }

  private J2DPortInstance getJ2DPortInstance(long poiId, Collection<J2DPortInstance> j2DPortInstances)
  {
    return j2DPortInstances.stream()
                           .filter(j2DPortInstance -> poiId ==  j2DPortInstance.getInstance().getId())
                           .findFirst()
                           .orElse(null);
  }
}


