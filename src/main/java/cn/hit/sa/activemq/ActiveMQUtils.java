package cn.hit.sa.activemq;

import cn.hit.sa.component.WeiboChangeEvent;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.jms.Connection;
import java.sql.*;

/**
 * Created by guwei on 17/6/2.
 */
public class ActiveMQUtils {

    private ActiveMQUtils(){}
    private static ActiveMQUtils instance;
    public static synchronized ActiveMQUtils getInstance(){
        if (instance==null){
            instance=new ActiveMQUtils();
        }
        return instance;
    }
    private Connection connection = null;

    public Connection getConnection() {
        if (connection!=null) return connection;
        // ConnectionFactory ：连接工厂，JMS 用它创建连接
        ConnectionFactory connectionFactory;
        // Connection ：JMS 客户端到JMS Provider 的连接
        connectionFactory = new ActiveMQConnectionFactory(
                ActiveMQConnection.DEFAULT_USER,
                ActiveMQConnection.DEFAULT_PASSWORD,
                "tcp://120.24.42.28:61616");
        try {
            // 构造从工厂得到连接对象
            connection = connectionFactory.createConnection();
            // 启动
            connection.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  connection;
    }

    public void closeConn(){
        if (connection!=null){
            try {
                connection.close();
            } catch (JMSException e) {
                e.printStackTrace();
            }finally {
                connection=null;
            }
        }
    }

}
