package cn.hit.sa;

import cn.hit.sa.dao.DBUtil;
import cn.hit.sa.memcached.MemcachedUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by guwei on 17/5/31.
 */
public class Utils {
    private static final Logger log = LoggerFactory.getLogger(Utils.class);

    public static void cleanEnv(){
        log.info("清空数据库");
        Connection connection = DBUtil.getConn();
        try {
            Statement statement = connection.createStatement();
            statement.addBatch("delete from user");
            statement.addBatch("delete from log");
            statement.addBatch("delete from weibo");
            statement.addBatch("delete from count");
            int[] res = statement.executeBatch();
            log.info("删除user表记录"+res[0]+"条");
            log.info("删除weibo表记录"+res[1]+"条");
            log.info("删除log表记录"+res[2]+"条");
            log.info("删除count表记录"+res[2]+"条");
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        log.info("清空Memcached:"+MemcachedUtils.mcc.flushAll());
    }
}
