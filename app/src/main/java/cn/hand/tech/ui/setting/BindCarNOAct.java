package cn.hand.tech.ui.setting;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import cn.hand.tech.R;
import cn.hand.tech.common.ACache;
import cn.hand.tech.ui.setting.adapter.BindCarAdapter;
import cn.hand.tech.ui.weight.bean.CarInfo;
import cn.hand.tech.utils.ToastUtil;
import cn.hand.tech.utils.Tools;

/*
 *绑定车牌
 */
public class BindCarNOAct extends Activity {
    private Context context;
    private LinearLayout ll_back;
    private TextView tv_para_title;
    private ListView list_1;
    private ACache acache;
    private BindCarAdapter madapter;
    private  List<CarInfo> clist;
    private boolean isConnected;
    private String truckNum;

    public static void start(Context context) {
        Intent intent = new Intent(context, BindCarNOAct.class);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=this;
        setContentView(R.layout.activity_bind_carno);
        acache= ACache.get(context,"WeightFragment");
        String isCon = acache.getAsString("is_connect");
        if ("2".equals(isCon)) {
            isConnected = true;
        } else {
            isConnected = false;
        }
        truckNum= acache.getAsString("car_num");
        initView();
    }
    private void initView() {
        tv_para_title=(TextView)findViewById(R.id.tv_para_title);
        tv_para_title.setText("绑定车牌");
        ll_back=(LinearLayout)findViewById(R.id.ll_back);
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView tv_dis=(TextView)findViewById(R.id.tv_dis);
        tv_dis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnected){
                    showTips("请先断开蓝牙");
                    return;
                }
                if(clist !=null && clist.size() > 0){
                    clist.clear();
                    madapter.updateListView(clist);
                    acache.put("carinfo_list",(Serializable)clist);
                    showTips("全部删除成功");
                }else{
                    showTips("数据为空");
                }
            }
        });

        list_1=(ListView)findViewById(R.id.list_1);

        clist=(List<CarInfo>) acache.getAsObject("carinfo_list");
        if(clist ==null || clist.size() <=0){
            clist=new ArrayList<>();
        }
        madapter=new BindCarAdapter(context,clist);
        madapter.setConnectClick(new BindCarAdapter.dobindCarNo() {
            @Override
            public void bindNo(String str, CarInfo item) {
                String  carNo=item.getCarNumber();
                if(isConnected && truckNum.equals(carNo)){
                    showTips("请先断开蓝牙");
                    return;
                }
                for(int i=0;i<clist.size();i++){
                    CarInfo info=clist.get(i);
                    String  id=info.getDeviceId();
                    if(!Tools.isEmpty(id)){
                        if (id.equals(item.getDeviceId())){
                            clist.remove(i);
                        }
                    }else{
                        clist.remove(i);
                    }

                }
                if(clist ==null || clist.size()<=0){
                    clist=new ArrayList<>();
                }
                acache.put("carinfo_list",(Serializable)clist);
                showTips("删除车牌成功");
                madapter.updateListView(clist);

            }
        });

        list_1.setAdapter(madapter);

    }
    public void  showTips(String tip){
        ToastUtil.getInstance().showCenterMessage(context,tip);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
