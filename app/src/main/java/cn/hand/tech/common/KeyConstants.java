package cn.hand.tech.common;


/**
 * 常量类
 *
 * @date 2015.9.20
 */
public final class KeyConstants {

	//清空访问足迹接口 

	public final static String ACTION_CLEAR_WHO_SEE_ME = "cn.hand.tech.ACTION_CLEAR_WHO_SEE_ME";

	//更新头像
	public final static String ACTION_UPDATE_IMG = "cn.hand.tech.ACTION_UPDATE_IMG";
	//更新姓名
	public final static String ACTION_UPDATE_NAME = "cn.hand.tech.ACTION_UPDATE_NAME";
	//更新年龄
	public final static String ACTION_UPDATE_AGE = "cn.hand.tech.ACTION_UPDATE_AGE";

	//相册 添加图片
	public final static String ACTION_ADD_PHOTO_PRIVATE = "cn.hand.tech.ACTION_ADD_PHOTO_PRIVATE";
	//相册 添加图片 设置私密照
	public final static String ACTION_SET_PHOTO_PRIVATE = "cn.hand.tech.ACTION_SET_PHOTO_PRIVATE";
	//相册 添加图片 删除照片
	public final static String ACTION_DELETE_PHOTO = "cn.hand.tech.ACTION_DELETE_PHOTO";
	//相册 送礼  点赞
	/**
	 * 回执ReceiptReceived
	 */
	public static final String BROADCAST_PATH = "cn.hand.tech.broadcast_path";

	/**
	 * 打开相册来之那个页面的标示
	 */
	public static final String IMAGE_FROM_FLAG = "image_from_flag";

	/**
	 * 主页面tab切换通知
	 */
	public static final String ACTION_MAIN_TAB_SWITCH = "cn.hand.tech.ACTION_MAIN_TAB_SWITCH";

	/**
	 * 主页面tab切换参数
	 * 表示切换到那个tab
	 */
	public static final String KEY_MAIN_TAB_SWITCH_NUM = "KEY_MAIN_TAB_SWITCH_NUM";

	/**
	 * 叮叮的custid
	 */
	public static final String DDJ_CUST_ID = "ddjun";
	/**
	 * 用户登录成功
	 */
	public static final String USER_LOGIN_SUCEESS = "cn.hand.tech.USER_LOGIN_SUCEESS";

	/**
	 * 用户登录成功
	 */
	public static final String USER_LOGIN_AGAIN = "cn.hand.tech.USER_LOGIN_AGAIN";
	/**
	 * 用户退出登录
	 */
	public static final String USER_LOGIN_OUT = "cn.hand.tech.USER_LOGIN_OUT";

	/**
	 * 在别的地方登录
	 */
	public static final String ACTION_OFFLINE_NOTIFICATION = "cn.hand.tech.ACTION_OFFLINE_NOTIFICATION";

	/**
	 * 抢购通知
	 */
	public static final String NOTICE_PANIC_BUYING = " panic_buying ";
	/**
	 * 降价提醒
	 */
	public static final String NOTICE_CUT_PRICE = "cut_price";

}
