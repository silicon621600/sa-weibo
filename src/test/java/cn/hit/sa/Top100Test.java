package cn.hit.sa;

import cn.hit.sa.component.CountComponent;
import cn.hit.sa.component.WeiboComponent;
import cn.hit.sa.dao.CountDao;
import cn.hit.sa.entity.Count;
import cn.hit.sa.entity.Weibo;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Created by guwei on 17/5/31.
 */
public class Top100Test {

    private static final Logger log = LoggerFactory.getLogger(Top100Test.class);

    @Before
    public void before() {
        Utils.cleanEnv();
    }

    @After
    public void after() {
        Utils.closeConn();
    }

    private Object chooseK(Set set, int k) {
        int i = 0;
        for (Object obj : set) {
            if (i == k)
                return obj;
            i++;
        }
        return null;
    }

    @Test
    public void simpleTest() {
        //构造微博
        int num = 350;
        Set<Integer> set = new HashSet<>();
        //随机操作
        Random random = new Random(0);
        WeiboComponent weiboComponent = WeiboComponent.getInstance();
        CountComponent countComponent = CountComponent.getInstance();
        CountDao countDao = CountDao.getInstance();
        for (int i = 0; i < num; i++) {
            int x = random.nextInt();
            if (x < 0) x = -x;
            x = x % 4;
            if (i < 100) x = 0;
            if (x == 0) {
                //添加微博
                Weibo weibo = new Weibo();
                weibo.setAuthor(i);
                weibo.setPubtime(i);
                weibo.setContent("It is No." + i + " weibo");
                weiboComponent.addWeibo(x, weibo);
                set.add(weibo.getId());
                log.debug("添加id=" + weibo.getId() + "的微博");
            } else {
                int y = random.nextInt();
                if (y < 0) y = -y;
                if (set.size() == 0) continue;
                y = y % set.size();
                Integer weiboId = (Integer) chooseK(set, y);
                if (weiboId == null) continue;
                if (x == 1) {
                    //阅读
                    weiboComponent.readWeibo(x, weiboId);
                    log.debug("阅读id=" + weiboId + "的微博");
                } else if (x == 2) {
                    //修改微博
                    Weibo weibo = new Weibo();
                    weibo.setAuthor(i);
                    weibo.setPubtime(i);
                    weibo.setContent("It is No." + i + " weibo");
                    weibo.setId(weiboId);
                    weiboComponent.updateWeibo(x, weibo);
                    log.debug("更新id=" + weiboId + "的微博");
                } else if (x == 3) {
                    //删除微博
                    weiboComponent.deleteWeibo(x, weiboId);
                    set.remove(weiboId);
                    log.debug("删除id=" + weiboId + "的微博");
                }
            }
            //通过中间件取的应该和从数据库里取的是一样的
            long t1 = System.currentTimeMillis();
            List<Weibo> list1 = countComponent.getTop100Weibo();
            long t2 = System.currentTimeMillis();
            List<Count> list2 = countDao.getTop(100);
            long t3 = System.currentTimeMillis();
            log.info("从封装的memcached方法中取:" + (t2 - t1) + " ms 从数据库里直接取:" + (t3 - t2) + " ms");
            Assert.assertEquals(list1.size(), list2.size());
            for (int j = 0; j < list1.size(); j++) {
                Assert.assertEquals(list1.get(j).getId(), list2.get(j).getWeiboId());
            }
        }
    }
}
