package cn.hand.tech.ui.setting;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.hand.tech.R;
import cn.hand.tech.ble.bleUtil.BleConstant;
import cn.hand.tech.common.ACache;

/*
 *重量单位
 */
public class UnitSettingAct extends Activity {
    private Context context;
    private LinearLayout ll_back;
    private LinearLayout ll_jin,ll_dun;
    private ImageView img_jin,img_t;
    private ACache acache;
    private boolean isChange =false;
    private TextView tv_para_title;

    public static void start(Context context) {
        Intent intent = new Intent(context, UnitSettingAct.class);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=this;
        setContentView(R.layout.activity_unit_setting);
        acache= ACache.get(context,"WeightFragment");
        initView();
    }
    private void initView() {
        tv_para_title=(TextView)findViewById(R.id.tv_para_title);
        tv_para_title.setText("重量单位");
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
         String unit= acache.getAsString("weight_unit");
         if("吨".equals(unit)){
             img_t.setVisibility(View.VISIBLE);
             img_jin.setVisibility(View.GONE);
         }else{
             img_jin.setVisibility(View.VISIBLE);
             img_t.setVisibility(View.GONE);
         }

        ll_jin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isChange=true;
                img_jin.setVisibility(View.VISIBLE);
                img_t.setVisibility(View.GONE);
                String str="公斤";
                acache.put("weight_unit",str);
            }
        });
        ll_dun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isChange=true;
                img_jin.setVisibility(View.GONE);
                img_t.setVisibility(View.VISIBLE);
                String str1="吨";
                acache.put("weight_unit",str1);
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        if(isChange){
            isChange=false;
            Intent i=new Intent(BleConstant.ACTION_UNIT_CHANGE);
            sendBroadcast(i);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
