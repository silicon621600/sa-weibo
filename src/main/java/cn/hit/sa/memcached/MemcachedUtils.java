package cn.hit.sa.memcached;

import cn.hit.sa.entity.Count;
import cn.hit.sa.entity.Weibo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.whalin.MemCached.MemCachedClient;
import com.whalin.MemCached.SockIOPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by guwei on 17/5/31.
 */
public class MemcachedUtils {

    private static final Logger log = LoggerFactory.getLogger(MemcachedUtils.class);
    // create a static client as most installs only need
    // a single instance
    private static final MemCachedClient mcc = new MemCachedClient();
    /**
     * 存到Memcached的东西全用json字符串的形式,方便调试.
     */
    private static Gson gson = new GsonBuilder().create();

    // from here on down, you can call any of the client calls
//    public static void examples() {
//        mcc.set( "foo", "This is a test String" );
//        String bar = (String) mcc.get( "foo" );
//        mcc.flushAll()
//    }
    private static Type listType = new TypeToken<ArrayList<Count>>() {
    }.getType();

    // set up connection pool once at class load
    static {

        // server list and weights
        String[] servers =
                {
                        //"server1.mydomain.com:1624",
                        //"server2.mydomain.com:1624",
                        "120.24.42.28:1624"
                };

        Integer[] weights = { /*3, 3,*/ 2};

        // grab an instance of our connection pool
        SockIOPool pool = SockIOPool.getInstance();

        // set the servers and the weights
        pool.setServers(servers);
        pool.setWeights(weights);

        // set some basic pool settings
        // 5 initial, 5 min, and 250 max conns
        // and set the max idle time for a conn
        // to 6 hours
        pool.setInitConn(5);
        pool.setMinConn(5);
        pool.setMaxConn(250);
        pool.setMaxIdle(1000 * 60 * 60 * 6);

        // set the sleep for the maint thread
        // it will wake up every x seconds and
        // maintain the pool size
        pool.setMaintSleep(30);

        // set some TCP settings
        // disable nagle
        // set the read timeout to 3 secs
        // and don't set a connect timeout
        pool.setNagle(false);
        pool.setSocketTO(3000);
        pool.setSocketConnectTO(0);

        // initialize the connection pool
        pool.initialize();


        // lets set some compression on for the client
        // compress anything larger than 64k
        // mcc.setCompressEnable( true );
        // mcc.setCompressThreshold( 64 * 1024 );
    }

    public static void setTop100(String key, List<Count> list) {
        String str = gson.toJson(list);
        mcc.set(key, str);
        log.debug("setTop100 :" + str);
    }

    public static List<Count> getTop100(String key) {
        String str = (String) mcc.get(key);
        if (str == null) {
            return new ArrayList<>();
        }
        List<Count> ret = new ArrayList<>();
        try {
            ret = gson.fromJson(str, listType);
        } catch (Exception e) {
            log.error("json转换错误",e);
            e.printStackTrace();
        }
        return ret;
    }

    public static void setWeibo(String key, Weibo weibo) {
        String str = gson.toJson(weibo);
        mcc.set(key, str, new Date(1800 * 1000));
    }

    public static void replaceWeibo(String key, Weibo weibo) {
        String str = gson.toJson(weibo);
        mcc.replace(key, str, new Date(1800 * 1000));
    }

    public static Weibo getWeibo(String key) {
        String str = (String) mcc.get(key);
        Weibo ret = null;
        try {
            ret = gson.fromJson(str, Weibo.class);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("json转换错误",e);
        }
        return ret;
    }

    public static boolean flushAll() {
        return mcc.flushAll();
    }
}
