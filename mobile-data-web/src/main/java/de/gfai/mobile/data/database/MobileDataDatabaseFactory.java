/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package de.gfai.mobile.data.database;

import de.gfai.core.database.Database;
import de.gfai.core.database.DatabaseInfo;
import de.gfai.core.database.DatabaseListener;
import de.gfai.mobile.data.context.MobileDataContext;
import java.sql.SQLException;
import java.util.Base64;
import java.util.Objects;
import javax.naming.NamingException;

/**
 *
 * @author lost
 */
class MobileDataDatabaseFactory implements DatabaseListener
{
  private final String userName;
  private final String passwd;
  private String conectionErrorMessage;

  MobileDataDatabaseFactory(String authorization)
  {
    Base64.Decoder decoder = Base64.getDecoder();
    byte[] mydata = decoder.decode(authorization.substring(6));
    String login = new String(mydata);
    int index = login.indexOf(":");
    this.userName = login.substring(0, index).toUpperCase().trim();
    this.passwd = login.substring(index+1);
  }

  MobileDataDatabase createMobileDataDatabase() throws SQLException, NamingException
  {
    MobileDataDatabase mobileDataDatabase = new MobileDataDatabase();
    mobileDataDatabase.addDatabaseListener(this);
    mobileDataDatabase.connect(createDatabaseInfo());
    mobileDataDatabase.removeDatabaseListener(this);

    if (Objects.nonNull(conectionErrorMessage))
      throw new SQLException(conectionErrorMessage);

    return mobileDataDatabase;
  }

  @Override
  public void databaseConnectingError(Database database, String message)
  {
    conectionErrorMessage = message;
  }

  private DatabaseInfo createDatabaseInfo() throws NamingException
  {
    return new DatabaseInfo(MobileDataContext.getOracleDriverClassName(), getURL(), userName, passwd);
  }

  private String getURL() throws NamingException
  {
    return String.format("jdbc:oracle:thin:@//%s:%s/%s", MobileDataContext.getDatabaseServer(),
                                                         MobileDataContext.getDatabasePort(),
                                                         MobileDataContext.getDatabaseSID());
  }
}
