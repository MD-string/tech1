package cn.hand.tech.ui.weight;

import cn.hand.tech.ui.weight.bean.CompanyResultBean;

/**
 * 动态接口定义
 */
public interface ICompyanView {
   void  doError(String str);
   void  inputSuccess(String str);
   void doSuccess(CompanyResultBean bean);
}
