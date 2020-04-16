package cn.hand.tech.utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * Created by wcf on 2018/3/19 18:55.
 * Describe: TODO 断不为null的操作
 */
public class NotNull {

    public static boolean isNotNull(Integer i) {
        if (null != i) {
            return true;
        }
        return false;
    }

    public static boolean isNotNull(Double d) {
        if (null != d && 0 != d) {
            return true;
        }
        return false;
    }

    /**
     * 判断多个字符串是否有空值
     * @author baishuang.wu
     * @date 2014-12-3 上午11:25:39
     * @param strings
     * @return
     */
    public static boolean isNotNull(String... strings) {
        if(strings==null||strings.length==0)
            return false;

        for (int i = 0; i < strings.length; i++) {
            if (null == strings[i] || "".equals(strings[i])) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNotNull(Object object) {
        if (null != object && !"".equals(object)) {
            return true;
        }
        return false;
    }

    public static boolean isNotNull(List<?> t) {
        if (null != t && t.size() > 0) {
            return true;
        }
        return false;
    }

    public static boolean isNotNull(Map<?, ?> t) {
        if (null != t && t.size() > 0) {
            return true;
        }
        return false;
    }

    public static boolean isNotNull(Object[] objects) {
        if (null != objects && objects.length > 0) {
            return true;
        }
        return false;
    }

    public static boolean isNotNull(JSONArray jsonArray) {
        if (null != jsonArray && jsonArray.length() > 0) {
            return true;
        }
        return false;
    }

    public static boolean isNotNull(JSONObject jsonObject) {
        if (null != jsonObject && !"".equals(jsonObject)) {
            return true;
        }
        return false;
    }

    public static boolean isNotNullAndNaN(Object object) {
        if (isNotNull(object) && !object.toString().equals("NaN")) {
            return true;
        }
        return false;
    }

}
