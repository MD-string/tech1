package cn.hand.tech.bean;

import com.google.gson.Gson;

import java.io.Serializable;


/**
 * 登录用户的信息
 */
public class LoginInfo implements Serializable{

	private static final long serialVersionUID = 1L;
	private String Userid;            //用户ID
	private String Token;			 //token串，用于每次通讯使用
	private String Unicode;	//所有图片AES加密码key与iv， eg:bingo#    61d5377545b4f345 
	private Userinfo Userinfo;  //个人信息
	private Icon  Icon;//tab 图标
	
	

	public String getUserid() {
		return Userid;
	}

	public void setUserid(String userid) {
		Userid = userid;
	}

	public String getToken() {
		return Token;
	}

	public void setToken(String token) {
		Token = token;
	}

	public String getUnicode() {
		return Unicode;
	}

	public void setUnicode(String unicode) {
		Unicode = unicode;
	}

	public Userinfo getUserinfo() {
		return Userinfo;
	}

	public void setUserinfo(Userinfo userinfo) {
		Userinfo = userinfo;
	}


	public Icon getIcon() {
		return Icon;
	}

	public void setIcon(Icon icon) {
		Icon = icon;
	}

	@Override
	public String toString() {
		return new Gson().toJson(this);
	}

	public static LoginInfo from(String registAccount) {
		return new Gson().fromJson(registAccount, LoginInfo.class);
	}

}