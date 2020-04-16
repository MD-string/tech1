package cn.hand.tech.weiget;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import cn.hand.tech.R;
import cn.hand.tech.utils.CommonUtils;


/**
 * 加载提醒对话框
 */
public class MyCustomDialog extends ProgressDialog
{
    private  String str;

    public MyCustomDialog(Context context)
    {
        super(context);
    }

    public MyCustomDialog(Context context, int theme,String txStr)
    {
        super(context, theme);
        this.str=txStr;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        init(getContext(),str);
    }

    private void init(Context context,String str)
    {
        //设置不可取消，点击其他区域不能取消，实际中可以抽出去封装供外包设置
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        View view = View.inflate(context, R.layout.load_dialog, null);
        TextView tv_load_dialog=(TextView)view.findViewById(R.id.tv_load_dialog);
        tv_load_dialog.setText(str);
        setContentView(view);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = CommonUtils.dip2px(context, 120);
        params.height =  CommonUtils.dip2px(context, 110);
        getWindow().setAttributes(params);
    }

    @Override
    public void show()
    {
        super.show();
    }
}
