package cn.hand.tech.ui.setting;

import cn.hand.tech.ui.weight.bean.CompanyResultBean;
import cn.hand.tech.ui.weight.bean.UserResultBean;

/**
 * 动态接口定义
 */
public interface ICompanyListView {

	void doLogin(UserResultBean bean);
	void doLoginFail(String bean);

	void sendSuccess(String msg);
	void sendError(String msg);
	void doCompanySuccess(CompanyResultBean companyResult);
	void doCompanyError(String msg);
}
