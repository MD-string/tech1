package cn.hand.tech.ui.setting;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import cn.hand.tech.R;
import cn.hand.tech.common.ACache;
import cn.hand.tech.ui.setting.adapter.DefWeightAdapter;
import cn.hand.tech.utils.ToastUtil;
import cn.hand.tech.utils.Tools;

/*
 *重量设置
 */
public class WeightSettingAct extends Activity implements View.OnClickListener{
    private Context context;
    private LinearLayout ll_back;
    private BroadcastReceiver receiver;
    private boolean bIsRead;
    private ACache acache;
    private TextView tv_para_title;
    private GridView gd_1;
    private TextView tv_close;
    private boolean isClose;
    private DefWeightAdapter madapter;
    private List<String> list;
    private Button bt_save,bt_low,bt_add;
    private List<String> defaultList;//默认

    public static void start(Context context) {
        Intent intent = new Intent(context, WeightSettingAct.class);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=this;
        setContentView(R.layout.activity_def_weight);
        acache= ACache.get(context,"WeightFragment");
        initView();
        registerBrodcat();
    }
    private void initView() {
        tv_para_title=(TextView)findViewById(R.id.tv_para_title);
        tv_para_title.setText("重量设置");
        ll_back=(LinearLayout)findViewById(R.id.ll_back);
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        bt_save=(Button)findViewById(R.id.bt_save);
        bt_save.setOnClickListener(this);
        bt_low=(Button)findViewById(R.id.bt_low);
        bt_low.setOnClickListener(this);
        bt_add=(Button)findViewById(R.id.bt_add);
        bt_add.setOnClickListener(this);

        list=(List<String>)acache.getAsObject("wei_list");
        defaultList =doDefaultList();//默认list
        if(list ==null  || list.size() <0){
            list=defaultList;
        }
        madapter=new DefWeightAdapter(context,list);
        tv_close=(TextView)findViewById(R.id.tv_close);//开启 关闭
        String str=acache.getAsString("open_close");
        if(!Tools.isEmpty(str) && str.equals("1")){
            isClose=true;
            tv_close.setText("开启");
            madapter.updateListView(list,false);
            bt_save.setBackgroundColor(getResources().getColor(R.color.lightGray1));
            bt_save.setEnabled(false);
            bt_low.setBackgroundColor(getResources().getColor(R.color.lightGray1));
            bt_low.setEnabled(false);
            bt_add.setBackgroundColor(getResources().getColor(R.color.lightGray1));
            bt_add.setEnabled(false);
        }else{
            isClose=false;
            tv_close.setText("关闭");
            madapter.updateListView(list,true);
            bt_save.setBackgroundColor(getResources().getColor(R.color.hand_blue));
            bt_save.setEnabled(true);
            bt_low.setBackgroundColor(getResources().getColor(R.color.hand_blue));
            bt_low.setEnabled(true);
            bt_add.setBackgroundColor(getResources().getColor(R.color.hand_blue));
            bt_add.setEnabled(true);
        }
        tv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isClose){
                    isClose=true;
                    tv_close.setText("开启");
                    madapter.updateListView(list,false);
                    bt_save.setBackgroundColor(getResources().getColor(R.color.lightGray1));
                    bt_save.setEnabled(false);
                    bt_low.setBackgroundColor(getResources().getColor(R.color.lightGray1));
                    bt_low.setEnabled(false);
                    bt_add.setBackgroundColor(getResources().getColor(R.color.lightGray1));
                    bt_add.setEnabled(false);
                    acache.put("open_close","1");
                }else{
                    isClose=false;
                    tv_close.setText("关闭");
                    madapter.updateListView(list,true);
                    bt_save.setBackgroundColor(getResources().getColor(R.color.hand_blue));
                    bt_save.setEnabled(true);
                    bt_low.setBackgroundColor(getResources().getColor(R.color.hand_blue));
                    bt_low.setEnabled(true);
                    bt_add.setBackgroundColor(getResources().getColor(R.color.hand_blue));
                    bt_add.setEnabled(true);
                    acache.put("open_close","2");
                }
            }
        });



        gd_1=(GridView)findViewById(R.id.gd_1);

        gd_1.setAdapter(madapter);
    }
    public void  showTips(String tip){
        ToastUtil.getInstance().showCenterMessage(context,tip);
    }

    /**
     * 注册广播
     */
    private void registerBrodcat() {
        receiver=new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                String action=intent.getAction();

            }

        };
        IntentFilter filter = new IntentFilter();
        registerReceiver(receiver, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
           this.unregisterReceiver(receiver);
            receiver = null;
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_save://保存
                acache.put("wei_list",(Serializable)list);
                showTips("保存成功");
                break;
            case R.id.bt_low://减少
                if(list !=null){
                    int size =list.size();
                    if( size>0){
                        list.remove(size-1);
                        madapter.updateListView(list,true);
                    }else{
                        list=new ArrayList<>();
                        madapter.updateListView(list,true);
                    }
                }else{
                    list=new ArrayList<>();
                    madapter.updateListView(list,true);
                }
                break;
            case R.id.bt_add://增加
                if(list !=null){
                    int size =list.size();
                    if( size >0){
                        int dex= size%28;//取余
                        list.add(defaultList.get(dex));
                        madapter.updateListView(list,true);
                    }else{
                        list.add(defaultList.get(0));
                        madapter.updateListView(list,true);
                    }
                }else{
                    list=new ArrayList<>();
                    madapter.updateListView(list,true);
                }
                break;
        }

    }
        //默认list  28
    public List<String> doDefaultList(){
        List<String>  list =new ArrayList<>();
        for(int i=0;i<28;i++){
            if(i<12){
                list.add(5+"");
            }else if( i>=22){
                list.add(5+"");
            }else{
                list.add(10+"");
            }
        }
        return  list;
    }
}
