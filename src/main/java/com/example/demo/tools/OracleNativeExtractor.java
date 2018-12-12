package com.example.demo.tools;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;

import oracle.jdbc.driver.OracleConnection;


import com.mchange.v2.c3p0.C3P0ProxyConnection;

/**
 * Based upon Spring Frameworks C3P0NativeJdbcExtractor by Juergen Hoeller.
 * 
 * @author Juergen Hoeller (Changes by Sloan Seaman)
 * @since 1.1.5
 * @see com.mchange.v2.c3p0.C3P0ProxyConnection#rawConnectionOperation
 */
public class OracleNativeExtractor {

    public static Connection getRawConnection(Connection con) {
        return con;
    }

    /**
     * Retrieve the Connection via C3P0's <code>rawConnectionOperation</code> API, using the
     * <code>getRawConnection</code> as callback to get access to the raw Connection (which is otherwise not directly
     * supported by C3P0).
     * 
     * @see #getRawConnection
     */
    public Connection getNativeConnection(Connection con) throws SQLException {
        if (con instanceof OracleConnection) {
            return con;
        } else if (con instanceof C3P0ProxyConnection) {
            C3P0ProxyConnection cpCon = (C3P0ProxyConnection) con;
            try {
                Method rawConnMethod = getClass().getMethod("getRawConnection", new Class[] { Connection.class });
                return (Connection) cpCon.rawConnectionOperation(rawConnMethod, null,
                    new Object[] { C3P0ProxyConnection.RAW_CONNECTION });
            } catch (SQLException ex) {
                throw ex;
            } catch (Exception ex) {
                throw new SQLException("Error in reflection:" + ex.getMessage());
            }
        } else {
            Connection conTmp = con.getMetaData().getConnection();
            // if (conTmp instanceof
            // JTAConnection_weblogic_jdbc_wrapper_XAConnection_oracle_jdbc_driver_LogicalConnection) {
            return conTmp;
            // }
        }

        // throw new SQLException("Could not find Native Connection of type OracleConnection");
    }

}