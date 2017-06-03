package cn.hit.sa.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {

    private static final Logger log = LoggerFactory.getLogger(DBUtil.class);
    private static DBUtil instance = null;
    private Connection conn = null;
    private String driver = "com.mysql.jdbc.Driver";
    private String url = "jdbc:mysql://120.24.42.28:8066/weibo_db?";
    private String username = "sa_weibo";
    private String password = "123";
    private DBUtil() {
    }

    public static synchronized DBUtil getInstance() {
        if (instance == null) {
            instance = new DBUtil();
        }
        return instance;
    }

    public Connection getConn() {
        if (conn != null) return conn;
        try {
            Class.forName(driver); //classLoader,加载对应驱动  
            conn = (Connection) DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            log.error("ClassNotFoundException",e);
        } catch (SQLException e) {
            e.printStackTrace();
            log.error("打开连接错误",e);
        }
        return conn;
    }

    public void closeConn() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
                log.error("关闭连接错误",e);
            } finally {
                conn = null;
            }
        }
    }

}