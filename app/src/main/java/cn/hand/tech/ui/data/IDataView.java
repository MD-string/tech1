package cn.hand.tech.ui.data;

/**
 * 动态接口定义
 */
public interface IDataView {
	void loginError(String msg);

	void loginSuccess(float[] kValueData, String K_values, float[] bValueData, String B_values);
}
