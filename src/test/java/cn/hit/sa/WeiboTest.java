package cn.hit.sa;

import cn.hit.sa.component.CountComponent;
import cn.hit.sa.component.WeiboChangeEvent;
import cn.hit.sa.component.WeiboComponent;
import cn.hit.sa.component.WeiboOpLogComponent;
import cn.hit.sa.dao.CountDao;
import cn.hit.sa.dao.DBUtil;
import cn.hit.sa.dao.WeiboOpLogDao;
import cn.hit.sa.entity.Weibo;
import cn.hit.sa.entity.WeiboOpLog;
import org.junit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by guwei on 17/5/30.
 */
public class WeiboTest {

    private static final Logger log = LoggerFactory.getLogger(WeiboTest.class);

    @Before
    public  void before(){
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
    }


    private void checkWeiboOpLog(int userId,int type,Weibo weibo){
        WeiboOpLogDao weiboOpLogDao = WeiboOpLogDao.getInstance();
        //检查操作日志
        int lastLogId = weiboOpLogDao.getLastInsertLogId();
        log.info("log表上次插入记录id为"+lastLogId);
        WeiboOpLog weiboOpLog = weiboOpLogDao.readLog(lastLogId);
        log.info("检查该对象 weiboOplog:"+weiboOpLog);
        Assert.assertEquals(userId,weiboOpLog.getUserId());
        Assert.assertEquals(weibo.getId(),weiboOpLog.getWeiboId());
        Assert.assertEquals(type,weiboOpLog.getType());
    }

    /**
     * 测试微博的增删改查
     */
    @Test
    public void simpleTest(){
        WeiboComponent weiboComponent = WeiboComponent.getInstance();



        //初始化一个微博对象
        Weibo weibo = new Weibo();
        weibo.setContent("test");
        weibo.setPubtime(System.currentTimeMillis());
        weibo.setAuthor(1);

        //添加微博
        weiboComponent.addWeibo(1,weibo);
        log.info("用户1 添加微博:"+weibo);

        checkWeiboOpLog(1,WeiboChangeEvent.ADD,weibo);

        //查看微博(记数构件)
        Weibo weibo1=weiboComponent.readWeibo(2,weibo.getId());
        log.info("用户2读取id="+weibo.getId()+"微博:"+weibo1);

        //检查微博对象
        Assert.assertNotNull("检查用户2读取的微博",weibo1);
        Assert.assertEquals("检查用户2读取的微博",weibo,weibo1);

        //检查读取次数
        CountDao countDao = CountDao.getInstance();
        Assert.assertEquals("检查读取次数",1,countDao.getCount(weibo.getId()));


        checkWeiboOpLog(2,WeiboChangeEvent.READ,weibo);


        //修改微博(日志构件)
        weibo.setContent("test after updated");
        weiboComponent.updateWeibo(1,weibo);
        log.info("用户1修改之前的微博(只改内容字段) "+weibo);


        checkWeiboOpLog(1,WeiboChangeEvent.UPDATE,weibo);




        //再次查看微博(记数构件)
        weibo1=weiboComponent.readWeibo(2,weibo.getId());
        log.info("用户2读取id="+weibo.getId()+"微博:"+weibo1);

        //检查微博对象
        Assert.assertNotNull("检查用户2读取的微博",weibo1);
        Assert.assertEquals("检查用户2读取的微博",weibo,weibo1);

        //检查读取次数
        Assert.assertEquals("检查读取次数",2,countDao.getCount(weibo.getId()));


        //刪除微博
        weiboComponent.deleteWeibo(1,weibo.getId());
        log.info("用户1删除id="+weibo.getId()+"微博");

        checkWeiboOpLog(1,WeiboChangeEvent.DELETE,weibo);

    }
}
