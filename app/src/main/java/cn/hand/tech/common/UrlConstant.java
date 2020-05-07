package cn.hand.tech.common;

import cn.hand.tech.BApplication;

public class UrlConstant {

	public static class HttpUrl {
		public static final String URL_SEARCH_CARINFO = "http://api.truckloading.cn/api/experimentserver/deviceinfo.html" ;
		/*保存数据，post*/
		public static final String SAVA_DATA = "http://api.truckloading.cn/api" + "/rawdataserver/rawdatafortest.html";

		public static final String SAVE_PARA = GET_URL()+"/calibserver/calculate.html";
		/*获取公司列表信息  POST*/
		public static final String COMPANY_LIST = GET_URL() + "/companyserver/companylist.html";
		/*添加或修改车辆信息接口接口功能录入或修改车辆信息 POST*/
		public static final String CAR_ADD_EDITOR = GET_URL() + "/carserver/addoredit.html";

		/*登录，post*/
		public static final String LOGIN = GET_URL() + "/loginserver/login.html";

		/*录车 检测该车是否已经录入*/
		public  static final String CHECK_ID = GET_URL() + "/carserver/checkDeviceIsExist.html";

		/*录车 */
		public static final String ENTER_CAR = GET_URL() + "/carserver/addoredit.html";

		/*获取安装人员信息 */
		public static final String GET_INSTALL = GET_URL() + "/userserver/userforinstall.html";

		/*上传安装人员信息 */
		public static final String SEND_INSTALLER = GET_URL() + "/deviceInstall/insertDeviceInstall.html";

		/*判断车辆是否在线 */
		public  static final String IS_ONLINE = GET_URL() + "/carserver/status.html";


		/*获取所有设备故障待维修记录 */
		public static final String GET_INFORMATION = GET_URL() + "/deviceRepaired/getDeviceRepairedRecords.html";

		/*上传故障待维修记录 */
		public static final String REPAIR_RECORD = GET_URL() + "/deviceRepaired/updateDeviceRepaired.html";
		/*获取该车辆列表实时信息  POST*/
		public static final String CAR_REAL_TIME_LIST = GET_URL() + "/carserver/realtimelist.html";

		/*添加设备维修记录 */
		public static final String ADD_REPAIR_RECODE = GET_URL() + "/deviceRepaired/addDeviceRepaired.html";

		/*根据车牌号获取车辆信息  */
		public static final String CHECK_CARNUMBER_EXIST = GET_URL() + "/carserver/getCarByCarNumber.html";

		/*根据车牌号获取车辆信息  */
		public static final String CHECK_BY_DEVICE_ID = GET_URL() + "/dataserver/getRealtimeByDeviceId.html";

		/*根据设备id获取传感器信息（传感器检测）  */
		public static final String GET_SENSOR_BY_DEVID = GET_URL() + "/deviceRepaired/getSensorByDeviceId.html";
		/*固件升级*/
		public static final String UPDATE_BIN_LIST = GET_URL() + "/bininfoserver/getBinInfoListByCompanyId.html";
		/*固件升级*/
		public static final String UPDATE_GU_JIAN = GET_URL() + "/bininfoserver/getBinInfoByDeviceId.html";
		/*上传日志*/
		public static final String URL_UPLOAD_LOG = GET_URL() + "/bininfoserver/addBinInfoUpgradeLog.html";

		/*是否进厂  POST*/
		public static final String AUTO_CHECK_INPUT_FRAGORY = GET_URL() + "/dataserver/realtime.html";
		/*是否进厂  POST*/
		public static final String NET_CHECK_DEVICE_EXSIT = GET_URL() + "/carserver/checkDeviceIsExist.html";


		/*上传写入系数日志*/
		public static final String URL_UPLOAD_WRITE_RATIO = GET_URL() + "/bininfoserver/addDeviceCoefLog.html";
	}
	public static String GET_URL() {
		String url = "";
		switch (BApplication.environment) {//192.168.10.43:8088  192.168.10.88:8088
			case 0:
				url = "http://192.168.10.43:8088/api";//测试环境
				break;
			case 1:
				url = "http://www.truckloading.cn/api";//生产环境
				break;
		}
		return url;
	}

}
