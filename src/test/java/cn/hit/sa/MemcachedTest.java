package cn.hit.sa;

import cn.hit.sa.entity.Weibo;
import cn.hit.sa.memcached.MemcachedUtils;
import org.junit.*;

/**
 * Created by guwei on 17/5/31.
 */
public class MemcachedTest {

    @Before
    public  void before(){
        Utils.cleanEnv();
    }
    @After
    public  void after(){
        Utils.closeConn();
    }

    @Test
    public void testWeiboObject(){
        Weibo weibo = new Weibo();
        weibo.setId(1);
        weibo.setAuthor(1);
        weibo.setContent("test");
        weibo.setPubtime(312312);
        MemcachedUtils.setWeibo("xxx",weibo);
        Weibo weibo1 = MemcachedUtils.getWeibo("xxx");
        Assert.assertEquals(weibo,weibo1);

        MemcachedUtils.flushAll();
        weibo1 = MemcachedUtils.getWeibo("xxx");
        Assert.assertNull(weibo1);
    }
}
