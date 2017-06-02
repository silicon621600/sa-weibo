package cn.hit.sa.component;

import java.sql.SQLException;
import java.util.*;

import cn.hit.sa.dao.CountDao;
import cn.hit.sa.dao.WeiboDao;
import cn.hit.sa.dao.WeiboOpLogDao;
import cn.hit.sa.entity.Count;
import cn.hit.sa.entity.Weibo;
import cn.hit.sa.memcached.MemcachedUtils;
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
	private WeiboDao weiboDao = WeiboDao.getInstance();

	public final String TOP_100_WEIBO = "top100Weibo";

	@Override
	public synchronized void  update(Observable arg0, Object arg1) {
		WeiboChangeEvent event = (WeiboChangeEvent)arg1;
		//只处理读取事件
		System.out.println("countCompnent接收到 事件:"+event);
		int weiboId = 	event.getWeibo().getId();

		if (event.getType() == WeiboChangeEvent.READ){
			countDao.inc(weiboId);
			long a = countDao.getCount(weiboId);
			insertIntoList(new Count(weiboId,a));
		}else if (event.getType() == WeiboChangeEvent.ADD){
			countDao.add(weiboId);
			insertIntoList(new Count(weiboId,0));
		}else if (event.getType() == WeiboChangeEvent.DELETE){
			countDao.delete(weiboId);
			deleteFromList(weiboId);
		}else if (event.getType() == WeiboChangeEvent.UPDATE){
			MemcachedUtils.replaceWeibo("weibo_"+weiboId,event.getWeibo());
		}
	}

	private int compare(Count a,Count b){
		if (a.getCount()>b.getCount()) return 1;
		if (a.getCount()<b.getCount()) return -1;
		if (a.getWeiboId()<b.getWeiboId()) return 1;
		if (a.getWeiboId()>b.getWeiboId()) return -1;
		return 0;
	}

	/**
	 * 尝试加入队列
	 * @param c
     */
	private void insertIntoList(Count c){
		log.debug("尝试插入 :"+c);
		List<Count> top100WeiboList = getTop100WeiboList();
		if (top100WeiboList.isEmpty()){
			top100WeiboList.add(c); //队列为空直接添加
		}else{
			int k=-1;
			for (int i=0;i<top100WeiboList.size();i++){
				if (top100WeiboList.get(i).getWeiboId()==c.getWeiboId()){
					k=i;
					break;
				}
			}
			log.debug("first K:"+k);
			if (k!=-1){
				top100WeiboList.remove(k);//如果该微博已在队列中 那么先删除原有的.
				log.debug("删除原有的:"+c.getWeiboId());
			}
			k=-1;
			for (int i=top100WeiboList.size()-1;i>=0;i--){
				Count e = top100WeiboList.get(i);
				if (compare(e,c)>=0){
					k=i;
					break;
				}
			}
			log.debug("second K:"+k+" size:"+top100WeiboList.size());
			top100WeiboList.add(k+1,c);
			while (top100WeiboList.size()>100){
				top100WeiboList.remove(top100WeiboList.size()-1);
			}
		}
		MemcachedUtils.setTop100(TOP_100_WEIBO,top100WeiboList);
		//log.debug("memcached set "+TOP_100_WEIBO+" "+top100WeiboList.size());
	}
	private void deleteFromList(int weiboId){
		List<Count> top100WeiboList = getTop100WeiboList();
		int k=-1;
		for (int i=0;i<top100WeiboList.size();i++){
			if (top100WeiboList.get(i).getWeiboId()==weiboId){
				k=i;
				break;
			}
		}
		if (k!=-1){
			top100WeiboList.remove(k);
		}
		MemcachedUtils.setTop100(TOP_100_WEIBO,top100WeiboList);
		//log.debug("memcached set "+TOP_100_WEIBO+" "+top100WeiboList.size());
	}

	private List<Count> getTop100WeiboList(){
		List<Count> top100WeiboList = MemcachedUtils.getTop100(TOP_100_WEIBO); //一个有序的数组
		if (top100WeiboList==null || top100WeiboList.size()<100){
			top100WeiboList = countDao.getTop(100);
			log.debug("coutDao getTop "+TOP_100_WEIBO+" "+top100WeiboList.size());
		}
		return  top100WeiboList;
	}


	public List<Weibo> getTop100Weibo(){
		List<Weibo> ret= new ArrayList<>();
		List<Count> top100Weibo =  getTop100WeiboList();
		for (int i=0;i<top100Weibo.size();i++){
			int weiboId = top100Weibo.get(i).getWeiboId();
			Weibo weibo = MemcachedUtils.getWeibo("weibo_"+weiboId);
			if (weibo==null){
				weibo = weiboDao.readWeibo(weiboId);
				log.debug("weiboDao read :"+weiboId);
				MemcachedUtils.setWeibo("weibo_"+weiboId,weibo);
			}
			if (weibo!=null) {
				ret.add(weibo);
			}
		}
		return ret;
	}

}
