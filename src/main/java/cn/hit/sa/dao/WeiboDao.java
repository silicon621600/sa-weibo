package cn.hit.sa.dao;

import cn.hit.sa.entity.Weibo;
import com.mysql.jdbc.PreparedStatement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by guwei on 17/5/30.
 */
public class WeiboDao {


    private static final Logger log = LoggerFactory.getLogger(WeiboDao.class);
    
    private static  WeiboDao instance;
    private WeiboDao() {}
    public  static synchronized WeiboDao getInstance(){
        if (instance==null){
            instance= new WeiboDao();
        }
        return instance;
    }
    
    

    public void addWeibo(Weibo weibo){
        //加入数据库
        log.debug("-**-添加微博-**-");
        Connection conn = DBUtil.getConn();
        String sql = "insert into weibo(content,pubtime,author) values(?,?,?)";
        PreparedStatement pstmt;
        try {
            pstmt = (PreparedStatement) conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, weibo.getContent());
            pstmt.setLong(2, weibo.getPubtime());
            pstmt.setInt(3, weibo.getAuthor());
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            rs.next();
            int auto_id = rs.getInt(1);
            weibo.setId(auto_id);
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Weibo readWeibo(int id){
        //查询指定ID的微博
        log.debug("-**-查看微博-**-");
        Connection conn = DBUtil.getConn();
        String sql = "select * from weibo where id = ?";
        PreparedStatement pstmt;
        try {
            pstmt = (PreparedStatement)conn.prepareStatement(sql);
            pstmt.setInt(1,id);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                Weibo wb = new Weibo();
                wb.setId(rs.getInt(1));
                wb.setContent(rs.getString(2));
                wb.setPubtime(rs.getLong(3));
                wb.setAuthor(rs.getInt(4));
                return wb;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void deleteWeibo(int id){
        //删除指定ID的微博
        log.debug("-**-删除微博-**-");
        Connection conn = DBUtil.getConn();
        String sql = "delete from weibo where id=?";
        PreparedStatement pstmt;
        try {
            pstmt = (PreparedStatement) conn.prepareStatement(sql);
            pstmt.setInt(1,id);
            pstmt.executeUpdate();
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




    public void updateWeibo(Weibo newWeibo){
        //更新微博
        log.debug("-**-更新微博-**-");
        Connection conn = (Connection)DBUtil.getConn();
        String sql = "update weibo set content=? where id=?";
        PreparedStatement pstmt;
        try {
            pstmt = (PreparedStatement) conn.prepareStatement(sql);
            pstmt.setString(1,newWeibo.getContent());
            pstmt.setInt(2,newWeibo.getId());
            pstmt.executeUpdate();
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
