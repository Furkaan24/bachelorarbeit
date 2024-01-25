/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
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





/**
 *
 * @author lost
 */
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


/*
  public static String[] extractLinesFromJRXML_Java(String filePath, int startLine, int endLine)+
  {
            List<String> jrxmlLines = new ArrayList<>();

            try (BufferedReader reader = new BufferedReader(new FileReader(filePath)))
            {
                String line;
                int currentLine = 0;

                while ((line = reader.readLine()) != null)
                {
                    currentLine++;
                    if (currentLine >= startLine && currentLine <= endLine)
                    {
                        jrxmlLines.add(line);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return jrxmlLines.toArray(new String[0]);
  }

   public void someMethod()
   {

        String jrxmlFilePath = "D:\\git\\dev_8_0\\gfaisoft\\java\\jasper\\src\\main\\java\\de\\gfai\\jasper\\resources\\reports\\info_instance\\instance.jrxml";
        int startLine = 100;
        int endLine = 200;

        String[] extractedLines = extractLinesFromJRXML(jrxmlFilePath, startLine, endLine);


    }

  public String[] extractLinesFromJRXML(String jrxmlFilePath, int startLine, int endLine)
  {
    try
    {
      //  die JRXML-Datei in ein JasperDesign-Objekt Kompilieren
      JasperDesign jasperDesign = JRXmlLoader.load(jrxmlFilePath);

      String modifiedJRXMLFilePath = "D:\\git\\dev_8_0\\gfaisoft\\java\\jasper\\src\\main\\java\\de\\gfai\\jasper\\resources\\reports\\info_instance\\instance.jrxml";
      JRXmlWriter.writeReport(jasperDesign, modifiedJRXMLFilePath, "UTF-8");

      // Rückgabe des extrahierten Inhalts als String-Array oder null, wenn nichts gefunden wurde
      return new String[]
      {
      };
    }
    catch (Exception e)
    {
      e.printStackTrace();
      return null;
    }
  }

  public void someMethod()
  {
    //Instanz erstellen


    String jrxmlFilePath = "D:\\git\\dev_8_0\\gfaisoft\\java\\jasper\\src\\main\\java\\de\\gfai\\jasper\\resources\\reports\\info_instance\\instance.jrxml";
    int startLine = 1909;
    int endLine = 2295;

    String[] extractedLines = extractLinesFromJRXML(jrxmlFilePath, startLine, endLine);

  }*/

