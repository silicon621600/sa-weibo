package cn.hit.sa.entity;

/**
 * Created by guwei on 17/5/30.
 */
public class WeiboOpLog {

    private int id;
    private int type;
    private long time;
    private int userId;
    private int weiboId;


    public WeiboOpLog(int type, long time, int userId,int weiboId) {
        this.type = type;
        this.time = time;
        this.userId = userId;
        this.weiboId = weiboId;
    }

    public WeiboOpLog() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getWeiboId() {
        return weiboId;
    }

    public void setWeiboId(int weiboId) {
        this.weiboId = weiboId;
    }

    @Override
    public String toString() {
        return "WeiboOpLog{" +
                "id=" + id +
                ", type=" + type +
                ", time=" + time +
                ", userId=" + userId +
                ", weiboId=" + weiboId +
                '}';
    }
}
