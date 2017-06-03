package cn.hit.sa.dao;

import cn.hit.sa.entity.Count;
import com.mysql.jdbc.PreparedStatement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by guwei on 17/5/30.
 */
public class CountDao {

    private static final Logger log = LoggerFactory.getLogger(CountDao.class);

    private static CountDao instance;
    private DBUtil dbUtil = DBUtil.getInstance();

    private CountDao() {
    }

    public static synchronized CountDao getInstance() {
        if (instance == null) {
            instance = new CountDao();
        }
        return instance;
    }

    public long getCount(int weiboId) {
        long ret = -1;
        Connection conn = dbUtil.getConn();
        String sql = "select `count` from `count` where weiboId=?";
        PreparedStatement pstmt;
        try {
            pstmt = (PreparedStatement) conn.prepareStatement(sql);
            pstmt.setInt(1, weiboId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                ret = rs.getLong(1);
            }
            pstmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
            log.error("SQLException",e);
        }
        return ret;
    }

    public void inc(int weiboId) {
        log.info("-**-微博计数加1-**-");
        Connection conn = dbUtil.getConn();
        String sql = "update `count` set count=count+1 where weiboId=?";
        PreparedStatement pstmt;
        try {
            pstmt = (PreparedStatement) conn.prepareStatement(sql);
            pstmt.setInt(1, weiboId);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            log.error("SQLException",e);
        }
    }


    public void delete(int weiboId) {
        Connection conn = dbUtil.getConn();
        String sql = "delete from `count` where weiboId=?";
        PreparedStatement pstmt;
        try {
            pstmt = (PreparedStatement) conn.prepareStatement(sql);
            pstmt.setInt(1, weiboId);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            log.error("SQLException",e);
        }
    }

    public void add(int weiboId) {
        Connection conn = dbUtil.getConn();
        String sql = "INSERT into `count`(weiboId,count) values (?,0) ";
        PreparedStatement pstmt;
        try {
            pstmt = (PreparedStatement) conn.prepareStatement(sql);
            pstmt.setInt(1, weiboId);
            pstmt.executeUpdate();
            pstmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
            log.error("SQLException",e);
        }
    }

    public List<Count> getTop(int num) {
        Connection conn = dbUtil.getConn();
        String sql = "SELECT `count` as c,`weiboId` as w FROM weibo_db.`count` order by  c desc,w asc limit ?";
        //mycat 的排序实际上 是先从节点取 再排序 ,注意sql顺序问题, count必须在前 否则不起作用
        PreparedStatement pstmt;
        List<Count> ret = new ArrayList<>();
        try {
            pstmt = (PreparedStatement) conn.prepareStatement(sql);
            pstmt.setInt(1, num);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Count count = new Count();
                count.setCount(rs.getLong("c"));
                count.setWeiboId(rs.getInt("w"));
                ret.add(count);
            }
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            log.error("SQLException",e);
        }
        return ret;
    }


}
