package cn.hand.tech.ui.weight;

import cn.hand.tech.ui.weight.bean.CompanyResultBean;

/**
 * 动态接口定义
 */
public interface IBasicView {
	void doSuccess(String msg);
	void doCheckAPISuccess(String msg,String msg1);
	void doError(String msg);

	void  doCompanySuccess(CompanyResultBean bean);

	void  doEnterCar(String msg);

	void onlineSuccess(String msg);
	void onlineFail(String msg);

}
