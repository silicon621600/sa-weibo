package cn.hit.sa.dao;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.mysql.jdbc.PreparedStatement;  
  
public class DBUtil {

    private DBUtil(){
    }



    private static DBUtil instance = null;
    public static synchronized DBUtil getInstance(){
        if (instance==null){
            instance = new DBUtil();
        }
        return instance;
    }


    private Connection conn=null;
    private String driver = "com.mysql.jdbc.Driver";
    private String url = "jdbc:mysql://120.24.42.28:8066/weibo_db?";
    private String username = "sa_weibo";
    private String password = "123";
    public  Connection getConn() {
        if (conn!=null) return conn;
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

    public void closeConn(){
        if (conn!=null){
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }finally {
                conn=null;
            }
        }
    }

}