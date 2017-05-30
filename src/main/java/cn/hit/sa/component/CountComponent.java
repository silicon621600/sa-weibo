package cn.hit.sa.component;

import java.sql.SQLException;
import java.util.Observable;
import java.util.Observer;

import cn.hit.sa.dao.CountDao;
import cn.hit.sa.dao.WeiboDao;
import cn.hit.sa.dao.WeiboOpLogDao;
import cn.hit.sa.entity.Weibo;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

import cn.hit.sa.dao.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 记数构件
 * 用于记录微博点击次数
 * 
 * @author Mandy
 * @version 0.0.1
 *
 */
public class CountComponent implements Observer{

	private static final Logger log = LoggerFactory.getLogger(CountComponent.class);

	private static CountComponent instance;
	private CountComponent() {}
	public static synchronized CountComponent getInstance(){
		if (instance==null){
			instance= new CountComponent();
		}
		return instance;
	}

	private CountDao countDao = CountDao.getInstance();



	@Override
	public void update(Observable arg0, Object arg1) {
		WeiboComponent weibo = (WeiboComponent)arg0;
		WeiboChangeEvent event = (WeiboChangeEvent)arg1;
		//只处理读取事件
		if (event.getType() == WeiboChangeEvent.READ){
			countDao.inc(event.getWeibo().getId());
		}else if (event.getType() == WeiboChangeEvent.ADD){
			countDao.add(event.getWeibo().getId());
		}
	}


}
