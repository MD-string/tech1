package cn.hand.tech.ui.setting;

import java.util.List;

import cn.hand.tech.ui.setting.bean.OnLineTruckBean;
import cn.hand.tech.ui.setting.bean.RepairModel;
import cn.hand.tech.ui.weight.bean.CompanyResultBean;
import cn.hand.tech.ui.weight.bean.InstallerInfo;
import cn.hand.tech.ui.weight.bean.UserResultBean;

/**
 * 动态接口定义
 */
public interface IRepairView {
	void doSuccess(List<RepairModel> model);
	void doError(String msg);

	void doLogin(UserResultBean bean);
	void doLoginFail(String bean);

	void sendSuccess(String msg);
	void sendError(String msg);

	void sendPersonSuccess(List<InstallerInfo> msg);
	void sendPersonError(String msg);
	void doCompanySuccess(CompanyResultBean companyResult);
	void doCompanyError(String msg);

	void doAllSuccess(List<RepairModel> model);
	void doAllError(String msg);


	void doOnlineSuccess(List<OnLineTruckBean> model);
}
