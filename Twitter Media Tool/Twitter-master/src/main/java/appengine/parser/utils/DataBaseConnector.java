package appengine.parser.utils;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Created by anand.kurapati on 09/12/17.
 */
public class DataBaseConnector {

    private static DSLContext create = null;

    public static DSLContext getDSLContext() {

        if (create == null) {
            Connection conn = null;

            String userName = "6txKRsiwk3";
            String password = "nPoqT54q3m";
            String url = "jdbc:mysql://remotemysql.com:3306/6txKRsiwk3?useSSL=false&useUnicode=true&characterEncoding=UTF-8&autoReconnect=true";
            try {
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                DriverManager.setLoginTimeout(10000);
                conn = DriverManager.getConnection(url, userName, password);
                create = DSL.using(conn, SQLDialect.MYSQL);
            } catch (Exception e) {
                 e.printStackTrace();
            }
        }
        return create;

    }
}
