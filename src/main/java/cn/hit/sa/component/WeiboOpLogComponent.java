package cn.hit.sa.component;

import cn.hit.sa.activemq.ActiveMQUtils;
import cn.hit.sa.dao.WeiboOpLogDao;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;
import java.util.Observable;
import java.util.Observer;

/**
 * 日志构件
 * 用于微博记录操作日志
 *
 * @author Mandy
 * @version 0.0.1
 */
public class WeiboOpLogComponent implements Observer {


    private static final Logger log = LoggerFactory.getLogger(WeiboOpLogComponent.class);

    private static WeiboOpLogComponent instance;
    private WeiboOpLogDao weiboOpLogDao = WeiboOpLogDao.getInstance();
    private Gson gson = new GsonBuilder().create();


    private WeiboOpLogComponent() {
    }

    public static synchronized WeiboOpLogComponent getInstance() {
        if (instance == null) {
            instance = new WeiboOpLogComponent();
        }
        return instance;
    }

    @Override
    public void update(Observable o, Object arg) {
        // TODO Auto-generated method stub
        //WeiboComponent ws = (WeiboComponent)o;
        WeiboChangeEvent event = (WeiboChangeEvent) arg;
        //weiboOpLogDao.addLog(new WeiboOpLog(event.getType(),event.getTime(),event.getUserId(),event.getWeibo().getId()));
        sendMessage(event);
    }

    public void sendMessage(WeiboChangeEvent event) {
        javax.jms.Connection connection = ActiveMQUtils.getInstance().getConnection();
        // 获取操作连接
        try {
            Session session = connection.createSession(Boolean.TRUE,
                    Session.AUTO_ACKNOWLEDGE);
            // Destination ：消息的目的地;消息发送给谁.
            // 获取session注意参数值xingbo.xu-queue是一个服务器的queue，须在在ActiveMq的console配置
            Destination destination = session.createQueue("WeiboOpQueue");
            // 得到消息生成者【发送者】
            MessageProducer producer = session.createProducer(destination);
            // 设置不持久化，此处学习，实际根据项目决定
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
            // 构造消息，此处写死，项目就是参数，或者方法获取

            //ObjectMessage message = session.createObjectMessage(event);
            //直接使用ObjectMessage 需要配置一些安全设置 http://activemq.apache.org/objectmessage.html
            //还是用json得了

            TextMessage message = session.createTextMessage(gson.toJson(event));
            producer.send(message);
            session.commit();
        } catch (JMSException e) {
            log.error("发送消息到ActiveMQ错误",e);
            e.printStackTrace();
        }
    }

}
