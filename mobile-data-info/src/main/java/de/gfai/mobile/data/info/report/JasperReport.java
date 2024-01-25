/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package de.gfai.mobile.data.info.report;

import de.gfai.core.app.reports.ReportParameter;
import de.gfai.infocable.database.IfcaDatabaseCompound;
import de.gfai.infocable.database.methods.instance.InstanceMethods;
import de.gfai.infocable.database.methods.instance.port.PortInstanceMethods;
import de.gfai.infocable.model.core.NetObject;
import de.gfai.infocable.model.instance.Instance;
import de.gfai.infocable.model.instance.port.PortInstance;
import de.gfai.infocable.modules.reports.ui.ReportOption;
import de.gfai.jasper.resources.util.ListReportUtil;
import de.gfai.jasper.resources.util.ReportUtil;
import java.io.ByteArrayOutputStream;
import java.sql.SQLException;
import java.util.Objects;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;







/**
 *
 * @author lost
 */
public interface JasperReport extends IfcaDatabaseCompound
{
  default byte[] htmlExport(long id) throws Throwable
  {

    JasperPrint jasperPrint = createJasperPrint(id);

    if (Objects.nonNull(jasperPrint))
    {
      try (ByteArrayOutputStream out = new ByteArrayOutputStream())
      {
        HtmlExporter htmlExporter = new HtmlExporter();
        htmlExporter.setExporterOutput(new SimpleHtmlExporterOutput(out));
        htmlExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        htmlExporter.exportReport();
        return out.toByteArray();

        }

      }
    return null;
  }

  default byte[] pdfExport(long id) throws Throwable
  {
    JasperPrint jasperPrint = createJasperPrint(id);

    if (Objects.nonNull(jasperPrint))
    {
      try (ByteArrayOutputStream out = new ByteArrayOutputStream())
      {
        JRPdfExporter jrPdfExporter = new JRPdfExporter();
        jrPdfExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(out));
        jrPdfExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        jrPdfExporter.exportReport();
        return out.toByteArray();

      }

    }
    return null;

  }



  JasperPrint createJasperPrint(long id) throws Throwable;

  default ReportParameter<?>[] getReportParameters(ReportParameter<?> reportParameter, ReportOption<?>... reportOptions)
  {

    return ListReportUtil.addReportParameter(reportParameter, reportOptions);
  }

  ReportUtil getReportUtil();

  default NetObject getNetObject(long id, String className) throws SQLException
  {
    if (Objects.compare(className, PortInstance.class.getSimpleName(), String.CASE_INSENSITIVE_ORDER) == 0)
      return getPortInstance(id);

    return getInstance(id);
  }

  default PortInstance getPortInstance(long poiId) throws SQLException
  {
    PortInstanceMethods portInstanceMethods = PortInstanceMethods.getInstance(getDatabase());
    return portInstanceMethods.getPortInstance(poiId);
  }

  default Instance getInstance(long ktiId) throws SQLException
  {
    InstanceMethods instanceMethods = InstanceMethods.getInstance(getDatabase());
    return instanceMethods.getInstanceById(ktiId);
  }

}


