package cn.hit.sa.memcached;

import com.whalin.MemCached.*;

/**
 * Created by guwei on 17/5/31.
 */
public class MemcachedUtils {
    // create a static client as most installs only need
    // a single instance
    public static final MemCachedClient mcc = new MemCachedClient();

    // set up connection pool once at class load
    static {

        // server list and weights
        String[] servers =
                {
                        //"server1.mydomain.com:1624",
                        //"server2.mydomain.com:1624",
                        "120.24.42.28:1624"
                };

        Integer[] weights = { /*3, 3,*/ 2 };

        // grab an instance of our connection pool
        SockIOPool pool = SockIOPool.getInstance();

        // set the servers and the weights
        pool.setServers( servers );
        pool.setWeights( weights );

        // set some basic pool settings
        // 5 initial, 5 min, and 250 max conns
        // and set the max idle time for a conn
        // to 6 hours
        pool.setInitConn( 5 );
        pool.setMinConn( 5 );
        pool.setMaxConn( 250 );
        pool.setMaxIdle( 1000 * 60 * 60 * 6 );

        // set the sleep for the maint thread
        // it will wake up every x seconds and
        // maintain the pool size
        pool.setMaintSleep( 30 );

        // set some TCP settings
        // disable nagle
        // set the read timeout to 3 secs
        // and don't set a connect timeout
        pool.setNagle( false );
        pool.setSocketTO( 3000 );
        pool.setSocketConnectTO( 0 );

        // initialize the connection pool
        pool.initialize();


        // lets set some compression on for the client
        // compress anything larger than 64k
       // mcc.setCompressEnable( true );
       // mcc.setCompressThreshold( 64 * 1024 );
    }

    // from here on down, you can call any of the client calls
//    public static void examples() {
//        mcc.set( "foo", "This is a test String" );
//        String bar = (String) mcc.get( "foo" );
//        mcc.flushAll()
//    }
}