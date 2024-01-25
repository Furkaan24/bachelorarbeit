/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gfai.mobile.data.patch;

import de.gfai.infocable.database.IfcaDatabase;
import de.gfai.infocable.model.connect.SignalPath;
import de.gfai.infocable.model.instance.Instance;
import de.gfai.infocable.model.instance.port.PortInstance;
import de.gfai.infocable.modules.connections.kabelauflegen.model.PortNameAction;
import de.gfai.infocable.modules.connections.patch.model.CatalogObjectPk;
import de.gfai.infocable.oracle.imports.cable.ImportCableImpl;
import de.gfai.infocable.oracle.imports.cable.ImportCableSupport;
import de.gfai.infocable.oracle.model.ImportStatus;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author buelbuel
 */
public class ImportCableMobile
{
  private final IfcaDatabase ifcaDatabase;

  private ImportCableSupport importCableSupport;
  private ImportCableImpl importCableImpl;

  public ImportCableMobile(IfcaDatabase ifcaDatabase) throws SQLException
  {
    this.ifcaDatabase = Objects.requireNonNull(ifcaDatabase);
  }

  private ImportCableSupport getImportCable()
  {
    if (importCableSupport == null)
    {
      importCableSupport = ImportCableSupport.getInstance(ifcaDatabase);
    }
        return importCableSupport;
  }

  public double getCableLengthKKabel(String poiId1List, String poiId2List, double additional) throws SQLException
  {
    if (importCableSupport == null)
    {
      importCableSupport = getImportCable();
    }
    return importCableSupport.getCableLengthKKabel(poiId1List, poiId2List, additional);
  }

  public String getCableListKKabel(String poiId1List, String poiId2List, double additional) throws SQLException
  {
    if (importCableSupport == null)
      importCableSupport = getImportCable();
    return importCableSupport.getCableListKKabel(poiId1List, poiId2List, additional);
  }

  public List<CatalogObjectPk> getKKList(String poiId1List, String poiId2List, double additional, boolean checkLength) throws SQLException
  {
    if (importCableSupport == null)
      importCableSupport = getImportCable();
    return importCableSupport.getKKList(poiId1List, poiId2List, additional,checkLength);
  }

  public ImportStatus connectCables (List<PortInstance> poi1, List<PortInstance> poi2, int cableClass, List<Instance> cables,
                                    SignalPath signalPath, boolean updateSwgBez, PortNameAction portNameAction)
  {
    if (importCableSupport == null)
      importCableSupport = getImportCable();
    return importCableSupport.connectCables(poi1, poi2, cableClass, cables, signalPath, updateSwgBez, portNameAction);
  }

  public int importCable (String poiId1List, String poiId2List, int cableClass, long ktId, long heId, long liId, String cableName,
                         double freiLae, int cCable, int cConnections, long algIdCore, long algIdWire, long ltId, long nzId, long statusId,
                         String tknIdList, long ktiId, double additionalAbs, double additionalRel, boolean virtual,
                         int portNameType, long sgId, String swgName) throws SQLException, ClassNotFoundException
  {
      if(importCableImpl == null)
        importCableImpl = new ImportCableImpl(ifcaDatabase.getDatabaseConnection().getConnection());
      int status = importCableImpl.importCable(poiId1List, poiId2List, cableClass, ktId, heId, liId, cableName,
                                      freiLae, cCable, cConnections, algIdCore, algIdWire, ltId, nzId,
                                      statusId, tknIdList, ktiId, additionalAbs, additionalRel, virtual,
                                      portNameType, sgId, swgName);
      return status;
  }


}
