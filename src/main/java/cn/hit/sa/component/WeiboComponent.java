package cn.hit.sa.component;

import cn.hit.sa.dao.WeiboDao;
import cn.hit.sa.entity.Weibo;

import java.util.Observable;
/**
 * 实现微博组件对象用于操作微博对象
 * 包括查看、修改、增加、删除等功能，
 * 每个功能都会构造WeiboChangeEvent对象发送给观察者
 * 1. 微博内容一旦改变，会通知“日志构件”记录操作日志。
 * 2. 微博被阅读一次，通知“记数构件”记录阅读次数。
 * 
 * @author Mandy
 * @version 0.0.1
 *
 */
public class WeiboComponent extends Observable{

	private static WeiboComponent instance;
	private WeiboComponent() {}
	public static synchronized WeiboComponent getInstance(){
		if (instance==null){
			instance= new WeiboComponent();
			instance.addObserver(CountComponent.getInstance());
			instance.addObserver(WeiboOpLogComponent.getInstance());
		}
		return instance;
	}
	
	private WeiboDao weoboDao = WeiboDao.getInstance();
	
	public void addWeibo(int userId,Weibo weibo){
		//添加微博
		weoboDao.addWeibo(weibo);
		eventExcute(new WeiboChangeEvent(WeiboChangeEvent.ADD,weibo,userId));
	}
	
	public Weibo readWeibo(int userId,int weiboID){
		//查看微博
		Weibo weibo = weoboDao.readWeibo(weiboID);
		Weibo t = new Weibo();
		t.setId(weiboID);
		eventExcute(new WeiboChangeEvent(WeiboChangeEvent.READ,t,userId));
		return weibo;
	}
		
	public void deleteWeibo(int userId,int weiboID){
		//删除微博
		weoboDao.deleteWeibo(weiboID);
		Weibo t = new Weibo();
		t.setId(weiboID);
		eventExcute(new WeiboChangeEvent(WeiboChangeEvent.DELETE,t,userId));
	}
	
	public void updateWeibo(int userId,Weibo newWeibo){
		//修改微博
		weoboDao.updateWeibo(newWeibo);
		eventExcute(new WeiboChangeEvent(WeiboChangeEvent.UPDATE,newWeibo,userId));
	}


	private void eventExcute(WeiboChangeEvent event){
		setChanged();
		//发送事件
		notifyObservers(event);
	}
}
