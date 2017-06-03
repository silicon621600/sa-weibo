package cn.hit.sa.bootstrap;


import cn.hit.sa.activemq.ActiveMQUtils;
import cn.hit.sa.component.WeiboChangeEvent;
import cn.hit.sa.dao.WeiboOpLogDao;
import cn.hit.sa.entity.WeiboOpLog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;

/**
 * Created by guwei on 17/6/2.
 */
public class LogServerMain {
    private static final Logger log = LoggerFactory.getLogger(LogServerMain.class);
    private static ActiveMQUtils activeMQUtils = ActiveMQUtils.getInstance();
    private static WeiboOpLogDao weiboOpLogDao = WeiboOpLogDao.getInstance();
    private static Gson gson = new GsonBuilder().create();

    public static void main(String[] args) {
        Connection conn = activeMQUtils.getConnection();

        javax.jms.Connection connection = ActiveMQUtils.getInstance().getConnection();
        // 获取操作连接
        try {
            Session session = connection.createSession(Boolean.TRUE,
                    Session.AUTO_ACKNOWLEDGE);
            // Destination ：消息的目的地;消息发送给谁.
            // 获取session注意参数值xingbo.xu-queue是一个服务器的queue，须在在ActiveMq的console配置
            Destination destination = session.createQueue("WeiboOpQueue");


            MessageConsumer consumer = session.createConsumer(destination);

            while (true) {
                //设置接收者接收消息的时间，为了便于测试，这里谁定为100s
                //ObjectMessage message = (ObjectMessage) consumer.receive(100000);
                TextMessage message = (TextMessage) consumer.receive();
                if (null != message) {
                    WeiboChangeEvent event = (WeiboChangeEvent) gson.fromJson(message.getText(), WeiboChangeEvent.class);
                    log.info("接收到WeiboChangeEvent对象:" + event);
                    weiboOpLogDao.addLog(new WeiboOpLog(event.getType(), event.getTime(), event.getUserId(), event.getWeibo().getId()));
                }
            }

        } catch (JMSException e) {
            e.printStackTrace();
            log.error("日志服务器执行错误:",e);
        } finally {
            activeMQUtils.closeConn();
        }
    }
}
