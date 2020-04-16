package cn.hand.tech.ui.setting;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.hand.tech.BApplication;
import cn.hand.tech.R;
import cn.hand.tech.common.ACache;
import cn.hand.tech.utils.ToastUtil;
import cn.hand.tech.utils.Tools;

/*
 *IP地址设定
 */
public class IPAddressAct extends Activity {
    private Context context;
    private LinearLayout ll_back;
    private TextView tv_para_title;
    private EditText et_date,et_fa;
    private Button bt_save_mv;
    private ACache acache;
    private LinearLayout ll_jin,ll_dun;
    private ImageView img_jin,img_t;
    private boolean isChange;
    private RelativeLayout rl_adr_setting;
    private EditText et_ip;
    private Button bt_save;

    public static void start(Context context) {
        Intent intent = new Intent(context, IPAddressAct.class);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=this;
        setContentView(R.layout.activity_ip_set);
        acache= ACache.get(BApplication.mContext,"IPAddressAct");
        initView();
    }
    private void initView() {
        tv_para_title=(TextView)findViewById(R.id.tv_para_title);
        tv_para_title.setText("地址设置");
        ll_back=(LinearLayout)findViewById(R.id.ll_back);
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ll_jin=(LinearLayout)findViewById(R.id.ll_jin);
        img_jin=(ImageView)findViewById(R.id.img_jin);
        ll_dun=(LinearLayout)findViewById(R.id.ll_dun);
        img_t=(ImageView)findViewById(R.id.img_t);

        rl_adr_setting=(RelativeLayout)findViewById(R.id.rl_adr_setting);
        rl_adr_setting.setVisibility(View.GONE);
        et_ip=(EditText)findViewById(R.id.et_ip);
        bt_save=(Button)findViewById(R.id.bt_save);

        String unit= acache.getAsString("http_url");
        if("0".equals(unit)){
            img_t.setVisibility(View.VISIBLE);
            img_jin.setVisibility(View.GONE);
            rl_adr_setting.setVisibility(View.VISIBLE);
        }else{
            img_jin.setVisibility(View.VISIBLE);
            img_t.setVisibility(View.GONE);
            rl_adr_setting.setVisibility(View.GONE);
        }

        String ipAdr= acache.getAsString("pro_ip");
        if(!Tools.isEmpty(ipAdr)){
            et_ip.setText(ipAdr);
        }else{
            et_ip.setText("");
        }

        ll_jin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isChange=true;
                img_jin.setVisibility(View.VISIBLE);
                img_t.setVisibility(View.GONE);
                String str="1";
                acache.put("http_url",str);
                rl_adr_setting.setVisibility(View.GONE);
            }
        });
        ll_dun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isChange=true;
                img_jin.setVisibility(View.GONE);
                img_t.setVisibility(View.VISIBLE);
                String str1="0";
                acache.put("http_url",str1);
                rl_adr_setting.setVisibility(View.VISIBLE);
            }
        });
        bt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String str=et_ip.getText().toString();
                if(Tools.isEmpty(str)){
                    showTips("地址为空");
                    return;
                }
                acache.put("pro_ip",str);
                showTips("保存成功");

            }
        });

    }

    public void  showTips(String tip){
        ToastUtil.getInstance().showCenterMessage(context,tip);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
