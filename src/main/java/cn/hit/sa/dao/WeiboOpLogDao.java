package cn.hit.sa.dao;

import cn.hit.sa.entity.Weibo;
import cn.hit.sa.entity.WeiboOpLog;
import com.mysql.jdbc.PreparedStatement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by guwei on 17/5/30.
 */
public class WeiboOpLogDao {


    private static final Logger log = LoggerFactory.getLogger(WeiboOpLogDao.class);
    private static WeiboOpLogDao instance;
    private DBUtil dbUtil = DBUtil.getInstance();

    private WeiboOpLogDao() {
    }

    public static synchronized WeiboOpLogDao getInstance() {
        if (instance == null) {
            instance = new WeiboOpLogDao();
        }
        return instance;
    }

    public void addLog(WeiboOpLog weiboOpLog) {
        Connection conn = dbUtil.getConn();
        String sql = "insert into log(type, time, userId,weiboId) values(?,?,?,?)";
        PreparedStatement pstmt;
        try {
            pstmt = (PreparedStatement) conn.prepareStatement(sql);
            pstmt.setInt(1, weiboOpLog.getType());
            pstmt.setLong(2, weiboOpLog.getTime());
            pstmt.setInt(3, weiboOpLog.getUserId());
            pstmt.setInt(4, weiboOpLog.getWeiboId());
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            log.error("SQLException",e);
        }
    }

    public WeiboOpLog readLog(int id) {
        Connection conn = dbUtil.getConn();
        WeiboOpLog ret = null;
        String sql = "select * from log where id=?"; //mysql函数 last_insert_id()
        PreparedStatement pstmt;
        try {
            pstmt = (PreparedStatement) conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                ret = new WeiboOpLog();
                ret.setId(rs.getInt("id"));
                ret.setTime(rs.getLong("time"));
                ret.setType(rs.getInt("type"));
                ret.setUserId(rs.getInt("userId"));
                ret.setWeiboId(rs.getInt("weiboId"));
            }
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            log.error("SQLException",e);
        }
        return ret;
    }

    public int getLastInsertLogId() {
        Connection conn = dbUtil.getConn();
        int ret = -1;
        String sql = "select max(id) as last_id from log"; //mysql函数 last_insert_id()
        PreparedStatement pstmt;
        try {
            pstmt = (PreparedStatement) conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                ret = rs.getInt(1);
            }
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            log.error("SQLException",e);
        }
        return ret;
    }
}
