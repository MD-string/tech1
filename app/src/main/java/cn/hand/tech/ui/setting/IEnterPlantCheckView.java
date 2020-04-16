package cn.hand.tech.ui.setting;

import cn.hand.tech.ui.setting.bean.AutoCheckFrgBean;
import cn.hand.tech.ui.setting.bean.CarNumberInfo;
import cn.hand.tech.ui.weight.bean.UserResultBean;

/**
 * 动态接口定义
 */
public interface IEnterPlantCheckView {
	void doLogin(UserResultBean bean);
	void doLoginFail(String bean);

	void doInfo(CarNumberInfo companyResult);
	void doError(String msg);
	void doFragoryFinish(AutoCheckFrgBean companyResult);
	void doFragoryError(String msg);
}
