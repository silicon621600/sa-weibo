package cn.hit.sa.component;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Observable;
import java.util.Observer;

import cn.hit.sa.dao.WeiboOpLogDao;
import cn.hit.sa.entity.Weibo;
import cn.hit.sa.entity.WeiboOpLog;
import com.mysql.jdbc.PreparedStatement;

import cn.hit.sa.dao.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日志构件
 * 用于微博记录操作日志
 * 
 * @author Mandy
 * @version 0.0.1
 * 
 */
public class WeiboOpLogComponent implements Observer{


	private static final Logger log = LoggerFactory.getLogger(WeiboOpLogComponent.class);
	
	private static WeiboOpLogComponent instance;
	private WeiboOpLogComponent() {}
	public static synchronized WeiboOpLogComponent getInstance(){
		if (instance==null){
			instance= new WeiboOpLogComponent();
		}
		return instance;
	}


	private WeiboOpLogDao weiboOpLogDao = WeiboOpLogDao.getInstance();

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		//WeiboComponent ws = (WeiboComponent)o;
		WeiboChangeEvent event = (WeiboChangeEvent)arg;
		weiboOpLogDao.addLog(new WeiboOpLog(event.getType(),event.getTime(),event.getUserId(),event.getWeibo().getId()));
	}
	

}
