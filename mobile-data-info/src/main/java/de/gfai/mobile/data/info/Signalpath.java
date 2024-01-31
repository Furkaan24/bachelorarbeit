package de.gfai.mobile.data.info;

import de.gfai.infocable.database.IfcaDatabase;
import de.gfai.infocable.database.methods.connect.SignalPathMethods;
import de.gfai.infocable.database.methods.instance.port.PortInstanceMethods;
import de.gfai.infocable.model.connect.SignalPath;
import de.gfai.infocable.model.instance.port.PortInstance;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Objects;

public class Signalpath
{
  private final IfcaDatabase ifcaDatabase;

  private SignalPathMethods signalPathMethods;
  private PortInstanceMethods portInstanceMethods;

  public Signalpath(IfcaDatabase ifcaDatabase)
  {
    super();
    this.ifcaDatabase = Objects.requireNonNull(ifcaDatabase);
  }

  private SignalPathMethods getSignalPathMethods()
  {
    if((Objects.isNull(signalPathMethods)))
      signalPathMethods = SignalPathMethods.getInstance(ifcaDatabase);
    return signalPathMethods;
  }

  private PortInstanceMethods getPortInstanceMethods()
  {
    if((Objects.isNull(portInstanceMethods)))
      portInstanceMethods = PortInstanceMethods.getInstance(ifcaDatabase);
    return portInstanceMethods;
  }

  public Collection<String> getSignalPathNames(PortInstance portInstance) throws SQLException
  {
    if (Objects.isNull(signalPathMethods))
      signalPathMethods = getSignalPathMethods();
    return  signalPathMethods.getSignalPathNames(portInstance);
  }

  public Collection<SignalPath> getSignalPathsFromPortInstance(PortInstance portInstance) throws SQLException
  {
    if (Objects.isNull(signalPathMethods))
      signalPathMethods = getSignalPathMethods();
    return  signalPathMethods.getSignalPathsFromPortInstance(portInstance);
  }

}
