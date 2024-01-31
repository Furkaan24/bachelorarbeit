package de.gfai.mobile.data.info.report;

import de.gfai.core.app.reports.ReportParameter;
import de.gfai.infocable.database.IfcaDatabase;
import de.gfai.infocable.model.core.NetObject;
import de.gfai.jasper.factory.ReportFactory;
import de.gfai.jasper.resources.parameter.IfcaModelReportParameter;
import de.gfai.jasper.resources.util.NetObjectInfoReportUtil;
import de.gfai.jasper.resources.util.ReportUtil;
import java.util.Map;
import java.util.Objects;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRXmlDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import org.w3c.dom.NodeList;

public class NetObjectReport implements JasperReport
{
  private final IfcaDatabase ifcaDatabase;
  private final String className;

  private NetObjectInfoReportUtil netObjectInfoReportUtil;

  public NetObjectReport(IfcaDatabase ifcaDatabase, String className)
  {
    this.ifcaDatabase = ifcaDatabase;
    this.className = className;
  }

  @Override
  public JasperPrint createJasperPrint(long id) throws Throwable
  {
    NetObject netObject = getNetObject(id, className);
    String reportFileName = getReportUtil().getReportFileName(netObject).orElse(null);

    if (Objects.nonNull(reportFileName))
    {
      JasperDesign jasperDesign = ReportFactory.getInstance().getJasperDesign(reportFileName);
      JRXmlDataSource jRXmlDataSource = ReportFactory.getInstance().loadJRXmlDataSource(reportFileName);
      printInfo(jRXmlDataSource);
      return getReportUtil().createJasperPrintFromDesign(jasperDesign, getReportParameters(netObject, reportFileName));
    }

    return null;
  }

  private void printInfo(JRXmlDataSource jRXmlDataSource)
  {
    NodeList nodeList = jRXmlDataSource.getDocument().getChildNodes();
    printNodeListInfo(nodeList, 0);
  }

  private void printNodeListInfo(NodeList nodeList, int level)
  {
    for (int i = 0; i < nodeList.getLength(); ++i)
    {

      for (int j = 0; j < level; ++j)
        System.err.print("  ");
      System.err.println(nodeList.item(i).getClass().getSimpleName() + " "
                         + nodeList.item(i).getNodeName() + " "
                         + nodeList.item(i).getNodeType() + " "
                         + nodeList.item(i).getNodeValue());

      printNodeListInfo(nodeList.item(i).getChildNodes(), level+1);
    }
  }



  private void printInfoJasperDesign(JasperDesign jasperDesign)
  {
    // ??? ---------------------------------------------
    System.err.println("\nParameter ------------------- ");
    jasperDesign.getParametersList()
                .forEach(jrParameter -> System.out.println(jrParameter.getClass().getSimpleName() + "\t" + jrParameter.getName() + "\t" + jrParameter.getNestedTypeName()));

    System.err.println("\nFields ---------------------- ");
    jasperDesign.getFieldsList().forEach(jrField -> {
      System.err.println(jrField.getClass().getSimpleName() + "\t" + jrField.getValueClassName() + "\t" + jrField.getName());
    });
  }

  private Map<String, Object> getReportParameters(NetObject netObject, String reportFileName)
  {
    ReportParameter<Long> reportParameter = IfcaModelReportParameter.getOptionalReportParameter(netObject).get();
    return getReportUtil().addStandardReportParameter(reportFileName, reportParameter);
  }

  @Override
  public ReportUtil getReportUtil()
  {
    if (Objects.isNull(netObjectInfoReportUtil))
      netObjectInfoReportUtil = new NetObjectInfoReportUtil(getDatabase());
    return netObjectInfoReportUtil;
  }

  @Override
  public IfcaDatabase getDatabase()
  {
    return ifcaDatabase;
  }
}