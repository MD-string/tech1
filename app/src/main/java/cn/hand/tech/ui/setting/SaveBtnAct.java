package cn.hand.tech.ui.setting;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.hand.tech.R;
import cn.hand.tech.common.ACache;

/*
 *保存 时 手动输入重量是否开启
 */
public class SaveBtnAct extends Activity {
    private Context context;
    private LinearLayout ll_back;
    private TextView tv_para_title;
    private CheckBox cb_1;
    private ACache acache;

    public static void start(Context context) {
        Intent intent = new Intent(context, SaveBtnAct.class);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=this;
        setContentView(R.layout.activity_save_switch);
        acache= ACache.get(context,"WeightFragment");
        initView();
    }
    private void initView() {
        tv_para_title=(TextView)findViewById(R.id.tv_para_title);
        tv_para_title.setText("保存开关");
        ll_back=(LinearLayout)findViewById(R.id.ll_back);
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        cb_1=(CheckBox)findViewById(R.id.cb_1);
        String str=acache.getAsString("save_switch");
        if("1".equals(str)){
            cb_1.setChecked(true);
        }else{
            cb_1.setChecked(false);
        }
        cb_1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    acache.put("save_switch","1");
                }else{
                    acache.put("save_switch","0");
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
