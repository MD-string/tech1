package cn.hand.tech.ui.setting;

import java.util.List;

import cn.hand.tech.ui.setting.bean.RepairModel;

/**
 * 动态接口定义
 */
public interface INeedRepairView {
   void  doSuccess(List<RepairModel> bean);
   void  doError(String str);
   void  inputSuccess(String str);
}
