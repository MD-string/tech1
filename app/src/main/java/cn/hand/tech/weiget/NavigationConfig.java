package cn.hand.tech.weiget;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import cn.hand.tech.R;


/**
 * 导航条的资源配置，标题、左边图标、右边图标
 * 
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface NavigationConfig {

	int leftIconId() default -1;
	
	int rightIconId() default -1;

	/**
	 * resource id
	 * 
	 * @return
	 */
	int titleId() default -1;

	/**
	 * 普通String
	 */
	String titleValue() default "";
	
	String tabTitle1Value() default "";
	
	String tabTitle2Value() default "";
	
	boolean isShow() default true;
	
	int showNavType() default 0;
	
	boolean showLeft() default true;
	
	boolean showMsgIcon() default true;
	
	int showLeftIcon() default -1;
	
	int showLeftTextId() default -1;
	
	String showLeftText() default "";
	
	boolean showRight() default false;
	
	int showRightIcon() default -1;
	
	int showRightTextId() default -1;
	
	String showRightText() default "";
	
	int leftTextColor() default -1;
	int rightTextColor() default -1;
	
	int backGroundResId() default R.color.white;
	
	boolean isOverlay() default false;//覆盖在activity上面
	
}
