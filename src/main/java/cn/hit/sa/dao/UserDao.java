package cn.hit.sa.dao;

import cn.hit.sa.entity.User;
import com.mysql.jdbc.PreparedStatement;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by guwei on 17/5/30.
 */
public class UserDao {


    private static UserDao instance;
    private UserDao() {}
    public static synchronized UserDao getInstance(){
        if (instance==null){
            instance= new UserDao();
        }
        return instance;
    }

    //添加用户
    public void addUser(User user){
        Connection conn = DBUtil.getConn();
        int i = 0;
        String sql = "insert into user (username,email,telephone,password) values(?,?,?,?)";
        PreparedStatement pstmt;
        try {
            pstmt = (PreparedStatement) conn.prepareStatement(sql);
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getTelephone());
            pstmt.setString(4, user.getPassword());
            i = pstmt.executeUpdate();
            System.out.println(i);
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
