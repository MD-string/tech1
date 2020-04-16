package cn.hand.tech.ui.setting;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.hand.tech.R;

/*
 *机械臂系数
 */
public class ArmAct extends Activity {
    private Context context;
    private LinearLayout ll_back;
    private TextView tv_para_title;
    private CheckBox cb_1;
    private EditText et_num;
    private Button bt_save;

    public static void start(Context context) {
        Intent intent = new Intent(context, ArmAct.class);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=this;
        setContentView(R.layout.activity_arm_co);
        initView();
    }
    private void initView() {
        tv_para_title=(TextView)findViewById(R.id.tv_para_title);
        tv_para_title.setText("机械臂系数");
        ll_back=(LinearLayout)findViewById(R.id.ll_back);
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        cb_1=(CheckBox)findViewById(R.id.cb_1);
        et_num=(EditText)findViewById(R.id.et_num);
        et_num.setClickable(false);
        bt_save=(Button)findViewById(R.id.bt_save);//保存
        bt_save.setEnabled(false);
        bt_save.setBackgroundColor(getResources().getColor(R.color.hand_txt));
        cb_1.setChecked(false);
        cb_1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    et_num.setClickable(true);
                    bt_save.setEnabled(true);
                    et_num.performClick();
                    bt_save.setBackgroundColor(getResources().getColor(R.color.hand_blue));
                }
            }
        });
        bt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str=et_num.getText().toString().trim();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
