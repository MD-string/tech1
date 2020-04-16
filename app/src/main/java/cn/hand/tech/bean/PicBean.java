package cn.hand.tech.bean;

import java.io.Serializable;

public class PicBean  implements Serializable{
	
	private static final long serialVersionUID = -6107372610789875682L;
	private String id;
	private String url;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
