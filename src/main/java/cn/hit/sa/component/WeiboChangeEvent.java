package cn.hit.sa.component;

import cn.hit.sa.entity.Weibo;

/**
 * 事件的语义 是  [userId]的用户 在[time]时刻 对[weibo]对象表示的微博进行了 [type]类型的操作
 */
public class WeiboChangeEvent {
	/*
	 * 不同改变类型的常量
	 */
	public static final int ADD = 0;
	public static final int READ = 1;
	public static final int UPDATE = 2;
	public static final int DELETE = 3;
	
	private int type;
	private Weibo weibo;
	private int userId;
	private long time;

	public WeiboChangeEvent() {
	}

	public WeiboChangeEvent(int type, Weibo weibo, int userId) {
		this.type = type;
		this.weibo = weibo;
		this.userId = userId;
		this.time = System.currentTimeMillis();
	}

	public void setType(int type){
		type = type;
	}
	
	public int getType(){
		return type;
	}

	public Weibo getWeibo() {
		return weibo;
	}

	public void setWeibo(Weibo weibo) {
		this.weibo = weibo;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	@Override
	public String toString() {
		return "WeiboChangeEvent{" +
				"type=" + type +
				", weibo=" + weibo +
				", userId=" + userId +
				", time=" + time +
				'}';
	}
}
