package cn.hit.sa;

import cn.hit.sa.component.WeiboChangeEvent;
import cn.hit.sa.component.WeiboComponent;
import cn.hit.sa.dao.CountDao;
import cn.hit.sa.entity.Weibo;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by guwei on 17/5/30.
 */
public class WeiboTest {

    private static final Logger log = LoggerFactory.getLogger(WeiboTest.class);

    @Before
    public void before() {
        Utils.cleanEnv();
    }

    @After
    public void after() {
        Utils.closeConn();
    }

    private void checkWeiboOpLog(int userId, int type, Weibo weibo) {
        //当前使用日志服务器形式 可能有延迟的
        /*
        WeiboOpLogDao weiboOpLogDao = WeiboOpLogDao.getInstance();
        //检查操作日志
        int lastLogId = weiboOpLogDao.getLastInsertLogId();
        log.info("log表上次插入记录id为"+lastLogId);
        WeiboOpLog weiboOpLog = weiboOpLogDao.readLog(lastLogId);
        Assert.assertNotNull(weiboOpLog);
        log.info("检查该对象 weiboOplog:"+weiboOpLog);
        Assert.assertEquals(userId,weiboOpLog.getUserId());
        Assert.assertEquals(weibo.getId(),weiboOpLog.getWeiboId());
        Assert.assertEquals(type,weiboOpLog.getType());
        */
    }

    /**
     * 测试微博的增删改查
     */
    @Test
    public void simpleTest() {
        WeiboComponent weiboComponent = WeiboComponent.getInstance();


        //初始化一个微博对象
        Weibo weibo = new Weibo();
        weibo.setContent("test");
        weibo.setPubtime(System.currentTimeMillis());
        weibo.setAuthor(1);

        //添加微博
        weiboComponent.addWeibo(1, weibo);
        log.info("用户1 添加微博:" + weibo);

        checkWeiboOpLog(1, WeiboChangeEvent.ADD, weibo);

        //查看微博(记数构件)
        Weibo weibo1 = weiboComponent.readWeibo(2, weibo.getId());
        log.info("用户2读取id=" + weibo.getId() + "微博:" + weibo1);

        //检查微博对象
        Assert.assertNotNull("检查用户2读取的微博", weibo1);
        Assert.assertEquals("检查用户2读取的微博", weibo, weibo1);

        //检查读取次数
        CountDao countDao = CountDao.getInstance();
        Assert.assertEquals("检查读取次数", 1, countDao.getCount(weibo.getId()));


        checkWeiboOpLog(2, WeiboChangeEvent.READ, weibo);


        //修改微博(日志构件)
        weibo.setContent("test after updated");
        weiboComponent.updateWeibo(1, weibo);
        log.info("用户1修改之前的微博(只改内容字段) " + weibo);


        checkWeiboOpLog(1, WeiboChangeEvent.UPDATE, weibo);


        //再次查看微博(记数构件)
        weibo1 = weiboComponent.readWeibo(2, weibo.getId());
        log.info("用户2读取id=" + weibo.getId() + "微博:" + weibo1);

        //检查微博对象
        Assert.assertNotNull("检查用户2读取的微博", weibo1);
        Assert.assertEquals("检查用户2读取的微博", weibo, weibo1);

        //检查读取次数
        Assert.assertEquals("检查读取次数", 2, countDao.getCount(weibo.getId()));


        //刪除微博
        weiboComponent.deleteWeibo(1, weibo.getId());
        log.info("用户1删除id=" + weibo.getId() + "微博");

        checkWeiboOpLog(1, WeiboChangeEvent.DELETE, weibo);

    }
}
