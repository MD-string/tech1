package cn.hand.tech.ui.weight;

import android.bluetooth.BluetoothDevice;

import java.util.List;

import cn.hand.tech.ui.weight.bean.CarInfo;
import cn.hand.tech.ui.weight.bean.UserResultBean;

/**
 * 动态接口定义
 */
public interface IChannelView {
	void loginError(String msg);

	void loginSuccess(List<CarInfo> carInfo);
	void downloadSuccess(BluetoothDevice dev);
	void downloadError(BluetoothDevice dev);

	void doLogin(UserResultBean bean);
	void doLoginFail(String bean);

	void onlineSuccess(String msg);
	void onlineFail(String msg);
}
