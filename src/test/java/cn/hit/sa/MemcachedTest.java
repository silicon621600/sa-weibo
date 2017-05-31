package cn.hit.sa;

import cn.hit.sa.entity.Weibo;
import cn.hit.sa.memcached.MemcachedUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Created by guwei on 17/5/31.
 */
public class MemcachedTest {

    @Before
    public  void before(){
        Utils.cleanEnv();
    }

    @Test
    public void testWeiboObject(){
        Weibo weibo = new Weibo();
        weibo.setId(1);
        weibo.setAuthor(1);
        weibo.setContent("test");
        weibo.setPubtime(312312);
        MemcachedUtils.mcc.set("xxx",weibo);
        Weibo weibo1 = (Weibo)MemcachedUtils.mcc.get("xxx");
        Assert.assertEquals(weibo,weibo1);
    }
}
