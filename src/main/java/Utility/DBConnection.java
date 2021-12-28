package Utility;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;
import java.sql.Connection;
import java.sql.SQLException;

public class DBConnection {
    private static DBConnection dbc=null;
    private Connection conn;

    private DBConnection() throws SQLException {
        DataSource datasource;
        PoolProperties p = new PoolProperties();
        p.setUrl("jdbc:mysql://localhost:3306/UnisaLIB?useLegacyDatetimeCode=false&serverTimezone=Europe/Rome");
        p.setDriverClassName("com.mysql.cj.jdbc.Driver");
        p.setUsername("root");
        p.setPassword("root");
        p.setMaxActive(100);
        p.setInitialSize(10);
        p.setMinIdle(10);
        p.setRemoveAbandonedTimeout(60);
        p.setRemoveAbandoned(true);
        datasource = new DataSource();
        datasource.setPoolProperties(p);
        conn= datasource.getConnection();
    }

    public static DBConnection getInstance() throws SQLException {
        if(dbc==null)
            synchronized(DBConnection.class) {
                if( dbc == null )
                    dbc = new DBConnection();
            }
        return dbc;
    }

    public Connection getConnection(){
        return conn;
    }
}
