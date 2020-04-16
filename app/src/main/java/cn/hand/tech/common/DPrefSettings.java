package cn.hand.tech.common;

/**
 * @date 2015-10-20
 */
public enum DPrefSettings {

	/**是否第一次使用*/
    SETTINGS_FIRST_USE("com.and.bingo.app_first_use" , Boolean.TRUE),
    /**检查是否需要自动登录*/
    SETTINGS_REGIST_AUTO("com.and.bingo.app_account" , ""),
    /**登陆者的信息*/
    SETTINGS_LOGIN_INFO("com.and.bingo.app_login_info" , ""),
    /**是否使用回车键发送消息*/
    SETTINGS_ENABLE_ENTER_KEY("com.and.bingo.app_sendmessage_by_enterkey" , Boolean.TRUE),
    /**聊天键盘的高度*/
    SETTINGS_KEYBORD_HEIGHT("com.and.bingo.app_keybord_height" , 0),
    /**新消息声音*/
    SETTINGS_NEW_MSG_SOUND("com.and.bingo.app_new_msg_sound" , true),
    /**新消息震动*/
    SETTINGS_NEW_MSG_SHAKE("com.and.bingo.app_new_msg_shake" , true),
    /**图片缓存路径*/
    SETTINGS_CROPIMAGE_OUTPUTPATH("com.and.bingo.app_cropimage_outputpath" , "");



    private final String mId;
    private final Object mDefaultValue;

    private DPrefSettings(String id, Object defaultValue) {
        this.mId = id;
        this.mDefaultValue = defaultValue;
    }

    public String getId() {
        return this.mId;
    }

    public Object getDefaultValue() {
        return this.mDefaultValue;
    }

    public static DPrefSettings fromId(String id) {
        DPrefSettings[] values = values();
        int cc = values.length;
        for (int i = 0; i < cc; i++) {
            if (values[i].mId == id) {
                return values[i];
            }
        }
        return null;
    }
}
