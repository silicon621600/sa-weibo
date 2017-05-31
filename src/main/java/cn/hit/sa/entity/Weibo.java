package cn.hit.sa.entity;

import java.io.Serializable;

public class Weibo implements Serializable {
	private int id;
	private String content;
	private long pubtime;
	private int author;
	

	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public long getPubtime() {
		return pubtime;
	}

	public void setPubtime(long pubtime) {
		this.pubtime = pubtime;
	}

	public int getAuthor() {
		return author;
	}

	public void setAuthor(int author) {
		this.author = author;
	}


	@Override
	public String toString() {
		return "Weibo{" +
				"id=" + id +
				", content='" + content + '\'' +
				", pubtime=" + pubtime +
				", author=" + author +
				'}';
	}

	// count字段不比较
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Weibo weibo = (Weibo) o;

		if (id != weibo.id) return false;
		if (pubtime != weibo.pubtime) return false;
		if (author != weibo.author) return false;
		return !(content != null ? !content.equals(weibo.content) : weibo.content != null);

	}

	@Override
	public int hashCode() {
		int result = id;
		result = 31 * result + (content != null ? content.hashCode() : 0);
		result = 31 * result + (int) (pubtime ^ (pubtime >>> 32));
		result = 31 * result + author;
		return result;
	}
}
