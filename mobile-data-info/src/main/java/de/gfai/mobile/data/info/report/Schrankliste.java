/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package de.gfai.mobile.data.info.report;

import de.gfai.core.app.reports.ReportParameter;
import de.gfai.infocable.database.IfcaDatabase;
import de.gfai.infocable.model.core.NetObject;
import de.gfai.infocable.model.instance.Instance;
import de.gfai.infocable.modules.reports.ui.ReportOption;
import de.gfai.infocable.modules.reports.ui.ReportOptionAdapter;
import de.gfai.jasper.resources.parameter.IfcaModelReportParameter;
import de.gfai.jasper.resources.util.RackInstallationReportUtil;
import de.gfai.jasper.resources.util.ReportUtil;
import java.util.Objects;
import net.sf.jasperreports.engine.JasperPrint;

/**
 *
 * @author lost
 */
public class Schrankliste implements JasperReport
{
  private static final ReportOption<String> REPORT_OPTION =  new ReportOptionAdapter<>("", null, "", "Komplett");

  private final IfcaDatabase ifcaDatabase;
  private RackInstallationReportUtil rackInstallationReportUtil;

  public Schrankliste(IfcaDatabase ifcaDatabase)
  {
    this.ifcaDatabase = ifcaDatabase;
  }

  @Override
  public JasperPrint createJasperPrint(long id) throws Throwable
  {
    NetObject netObject = getNetObject(id, Instance.class.getSimpleName());
    ReportParameter<Long> reportParameter = IfcaModelReportParameter.getOptionalReportParameter(netObject).get();
    ReportParameter<?>[] reportParameters = getReportParameters(reportParameter, REPORT_OPTION);
    return getReportUtil().createJasperPrintFromFile(netObject, reportParameters);
  }

  @Override
  public ReportUtil getReportUtil()
  {
    if (Objects.isNull(rackInstallationReportUtil))
      rackInstallationReportUtil = new RackInstallationReportUtil(getDatabase());
    return rackInstallationReportUtil;
  }

  @Override
  public IfcaDatabase getDatabase()
  {
    return ifcaDatabase;
  }
}

