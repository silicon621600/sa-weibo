package cn.hit.sa.entity;

import java.io.Serializable;

/**
 * Created by guwei on 17/5/30.
 */
public class Count implements Serializable {
    private int weiboId;
    private long count;

    public Count() {
    }

    public Count(int weiboId, long count) {
        this.weiboId = weiboId;
        this.count = count;
    }

    public int getWeiboId() {
        return weiboId;
    }

    public void setWeiboId(int weiboId) {
        this.weiboId = weiboId;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "Count{" +
                "weiboId=" + weiboId +
                ", count=" + count +
                '}';
    }
}
