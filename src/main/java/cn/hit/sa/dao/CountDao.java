package cn.hit.sa.dao;

import com.mysql.jdbc.PreparedStatement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by guwei on 17/5/30.
 */
public class CountDao {

    private static final Logger log = LoggerFactory.getLogger(CountDao.class);

    private static CountDao instance;
    private CountDao() {}
    public static synchronized CountDao getInstance(){
        if (instance==null){
            instance= new CountDao();
        }
        return instance;
    }

    public long getCount(int weiboId){
        long ret=-1;
        Connection conn = DBUtil.getConn();
        String sql = "select `count` from `count` where weiboId=?";
        PreparedStatement pstmt;
        try {
            pstmt = (PreparedStatement) conn.prepareStatement(sql);
            pstmt.setInt(1,weiboId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()){
                ret = rs.getLong(1);
            }
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public void inc(int weiboId){
        log.info("-**-微博计数加1-**-");
        Connection conn = DBUtil.getConn();
        String sql = "update `count` set count=count+1 where weiboId=?";
        PreparedStatement pstmt;
        try {
            pstmt = (PreparedStatement) conn.prepareStatement(sql);
            pstmt.setInt(1,weiboId);
            pstmt.executeUpdate();
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void add(int weiboId){
        Connection conn = DBUtil.getConn();
        String sql = "INSERT into `count`(weiboId,count) values (?,0) ";
        PreparedStatement pstmt;
        try {
            pstmt = (PreparedStatement) conn.prepareStatement(sql);
            pstmt.setInt(1,weiboId);
            pstmt.executeUpdate();
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
