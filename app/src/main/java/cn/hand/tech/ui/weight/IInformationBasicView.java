package cn.hand.tech.ui.weight;

import java.util.List;

import cn.hand.tech.ui.weight.bean.InstallerInfo;

/**
 * 动态接口定义
 */
public interface IInformationBasicView {
	void doSuccess(  List<InstallerInfo> info );
	void doError(String msg);

	void sendSuccess(String msg);

	void loadSuccess(String msg);
	void loadFail(String msg);

	void uploadRadioSuccess(String msg);
	void uploadFail(String msg);
}
