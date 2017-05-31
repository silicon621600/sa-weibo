package cn.hit.sa.dao;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.mysql.jdbc.PreparedStatement;  
  
public class DBUtil { 
    public static Connection getConn() {  
        
        String driver = "com.mysql.jdbc.Driver";  
        String url = "jdbc:mysql://120.24.42.28:8066/weibo_db";
        String username = "sa_weibo";
        String password = "123";
        Connection conn = null;  
        try {  
            Class.forName(driver); //classLoader,加载对应驱动  
            conn = (Connection) DriverManager.getConnection(url, username, password);  
        } catch (ClassNotFoundException e) {  
            e.printStackTrace();  
        } catch (SQLException e) {  
            e.printStackTrace();  
        }  
        return conn;  
    }

}