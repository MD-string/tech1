package cn.hand.tech.ui.setting;

import java.util.List;

import cn.hand.tech.ui.setting.bean.CarNumberInfo;
import cn.hand.tech.ui.setting.bean.GuJianBean;
import cn.hand.tech.ui.weight.bean.UserResultBean;

/**
 * 动态接口定义
 */
public interface IUpdateView {
	void doSuccess(GuJianBean model);
	void doError(String msg);

	void doLogin(UserResultBean bean);
	void doLoginFail(String bean);

	void doLOG(String bean);
	void doLogFail(String bean);

	void doBinList(List<GuJianBean> bean);
	void doBinListFail(String bean);

	void doCompanySuccess(CarNumberInfo companyResult);
	void doCompanyError(String msg);
}
