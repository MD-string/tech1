package cn.hand.tech.bean;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * 登录账号
 *  @date 2016.12.5
 */
public class Account implements Serializable {

	private static final long serialVersionUID = 133333312L;

	public final static String _ID = "id";
	public final static String _LOGIN_NAME = "loginName";
	public final static String _PASSWORD = "password";
	public final static String _IS_SAVE = "isSave";

	private String id;
	private String loginName;
	private String password;
	private String isSave;

	public Account() {
		super();
	}

	public Account(String loginName) {
		super();
		this.loginName = loginName;
	}

	public Account(String loginName, String password, String isSave) {
		super();
		this.loginName = loginName;
		this.password = password;
		this.isSave = isSave;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getIsSave() {
		return isSave;
	}

	public void setIsSave(String isSave) {
		this.isSave = isSave;
	}

	@Override
	public boolean equals(Object o) {
		Account account = (Account) o;
		return account.getLoginName().equals(this.getLoginName());
	}

	@Override
	public String toString() {
		return new Gson().toJson(this);
	}

	public static Account from(String registAccount) {
		Account a =new Gson().fromJson(registAccount, Account.class);
		return a;
	}

}
