package de.gfai.mobile.data.info;

import de.gfai.infocable.database.IfcaDatabase;
import de.gfai.infocable.database.methods.connect.PpvMethods;
import de.gfai.infocable.model.connect.Ppv;
import de.gfai.infocable.model.instance.Instance;
import de.gfai.infocable.model.instance.port.PortInstance;
import de.gfai.mobile.data.info.report.NetObjectReport;
import de.gfai.mobile.data.info.report.PredicateIfcaObjects;
import de.gfai.mobile.data.info.report.Schrankliste;
import java.sql.SQLException;
import java.util.Objects;

/**
 *
 * @author lost
 */
public class ObjectInfo implements PredicateIfcaObjects
{
  private static final String SMARTPHONESCALE = "<meta name='viewport' content='width=device-width, initial-scale=1'>";

  private final IfcaDatabase ifcaDatabase;

  private Schrankliste schrankliste;
  private NetObjectReport instanceReport;
  private NetObjectReport portInstanceNetObjectReport;

  public ObjectInfo(IfcaDatabase ifcaDatabase)
  {
    super();
    this.ifcaDatabase = Objects.requireNonNull(ifcaDatabase);
  }

  public byte[] htmlExport(String className, long id) throws Throwable
  {
    if (isRackClass(className))
      return getSchrankliste().htmlExport(id);

    if (isPortInstance(className))
      return getPortInstanceNetObjectReport().htmlExport(id);

    if (isPpvClass(className))
      return htmlExport(Instance.class.getSimpleName(), getCablingFromPpvId(id).getId());

    if (isTextDbObjectClass(className))
      return null;

    if (Objects.nonNull(className)
        && !className.isEmpty())
    {
      return getInstanceReport().htmlExport(id);
    }

    return null;
  }

  public byte[] pdfExport(String className, long id) throws Throwable
  {
    if (isRackClass(className))
      return getSchrankliste().pdfExport(id);

    if (isPortInstance(className))
      return getPortInstanceNetObjectReport().pdfExport(id);

    if (isPpvClass(className))
      return pdfExport(Instance.class.getSimpleName(), getCablingFromPpvId(id).getId());

    if (isTextDbObjectClass(className))
      return null;

    if (Objects.nonNull(className)
        && !className.isEmpty())
    {
      return getInstanceReport().pdfExport(id);
    }

    return null;
  }


  private Schrankliste getSchrankliste()
  {
    if (Objects.isNull(schrankliste))
      schrankliste = new Schrankliste(ifcaDatabase);
    return schrankliste;
  }

  private NetObjectReport getPortInstanceNetObjectReport()
  {
    if (Objects.isNull(portInstanceNetObjectReport))
      portInstanceNetObjectReport = new NetObjectReport(ifcaDatabase, PortInstance.class.getSimpleName());
    return portInstanceNetObjectReport;
  }

  private NetObjectReport getInstanceReport()
  {
    if (Objects.isNull(instanceReport))
      instanceReport = new NetObjectReport(ifcaDatabase, Instance.class.getSimpleName());
    return instanceReport;
  }

  private Instance getCablingFromPpvId(long ppvId) throws SQLException
  {
    PpvMethods ppvMethods = PpvMethods.getInstance(ifcaDatabase);
    Ppv ppv = ppvMethods.getPpvFromPpvId(ppvId);
    return ppvMethods.getCablingFromPpv(ppv);
  }

}
