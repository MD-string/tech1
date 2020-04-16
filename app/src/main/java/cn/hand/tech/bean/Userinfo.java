package cn.hand.tech.bean;

import java.io.Serializable;

import com.google.gson.Gson;
/*
 * 
 * 用户个人信息
 */
public class Userinfo implements Serializable {

	private static final long serialVersionUID = 13435345345L;
	private String  Icon;         //用户小头像（aes 加密） 
	private String  Voicesign;//用户语音签名 
	private String  Txtsign;  //文字签名
	
	private String  Nickname;   //用户呢称 
	private String  Age;  //用户年龄 Sex 
	private String  Sex;//用户性别 1：女，2：男 
	
	private String  Job;//用户职业 
	private String  Rate;//语音资费 
	private String  City;//用户所在城市 
	
	private String  Talent;//才华值 
	private String  Charm;//魅力值 
	private String  Money;//财富值 
	private String  Follow;//关注数 
	private String  Fans;//粉丝数 
	private String  Isauth;//是否认证 
	
	private String  Isfollow;//是否关注（1：关注，0：未关注） 
	private String  Isvip;//用户特权 
	private String  Level;//用户等级 
	private  String  Userid;//userid
	private String  Height;
	private String  Birthday;
		
	public String getUserid() {
		return Userid;
	}
	public void setUserid(String userid) {
		Userid = userid;
	}
	public String getIcon() {
		return Icon;
	}
	public void setIcon(String icon) {
		Icon = icon;
	}
	public String getVoicesign() {
		return Voicesign;
	}
	public void setVoicesign(String voicesign) {
		Voicesign = voicesign;
	}
	public String getTxtsign() {
		return Txtsign;
	}
	public void setTxtsign(String txtsign) {
		Txtsign = txtsign;
	}
	public String getNickname() {
		return Nickname;
	}
	public void setNickname(String nickname) {
		Nickname = nickname;
	}
	public String getAge() {
		return Age;
	}
	public void setAge(String age) {
		Age = age;
	}
	public String getSex() {
		return Sex;
	}
	public void setSex(String sex) {
		Sex = sex;
	}
	public String getJob() {
		return Job;
	}
	public void setJob(String job) {
		Job = job;
	}
	public String getRate() {
		return Rate;
	}
	public void setRate(String rate) {
		Rate = rate;
	}
	public String getCity() {
		return City;
	}
	public void setCity(String city) {
		City = city;
	}
	public String getTalent() {
		return Talent;
	}
	public void setTalent(String talent) {
		Talent = talent;
	}
	public String getCharm() {
		return Charm;
	}
	public void setCharm(String charm) {
		Charm = charm;
	}
	public String getMoney() {
		return Money;
	}
	public void setMoney(String money) {
		Money = money;
	}
	public String getFollow() {
		return Follow;
	}
	public void setFollow(String follow) {
		Follow = follow;
	}
	public String getFans() {
		return Fans;
	}
	public void setFans(String fans) {
		Fans = fans;
	}
	public String getIsauth() {
		return Isauth;
	}
	public void setIsauth(String isauth) {
		Isauth = isauth;
	}
	public String getIsfollow() {
		return Isfollow;
	}
	public void setIsfollow(String isfollow) {
		Isfollow = isfollow;
	}
	public String getIsvip() {
		return Isvip;
	}
	public void setIsvip(String isvip) {
		Isvip = isvip;
	}
	public String getLevel() {
		return Level;
	}
	public void setLevel(String level) {
		Level = level;
	}
	public String getHeight() {
		return Height;
	}
	public void setHeight(String height) {
		Height = height;
	}
	public String getBirthday() {
		return Birthday;
	}
	public void setBirthday(String birthday) {
		Birthday = birthday;
	}
	
}
