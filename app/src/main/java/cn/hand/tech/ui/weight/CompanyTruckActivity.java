package cn.hand.tech.ui.weight;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.hand.tech.R;
import cn.hand.tech.common.ACache;
import cn.hand.tech.ui.weight.adapter.CompanyTruckAdapter;
import cn.hand.tech.ui.weight.bean.CompanyBean;
import cn.hand.tech.ui.weight.bean.CompanyResultBean;
import cn.hand.tech.ui.weight.bean.CompanyTruckGroupBean;
import cn.hand.tech.ui.weight.bean.TruckChildBean;
import cn.hand.tech.utils.ToastUtil;
import cn.hand.tech.utils.Tools;


/**
 * A simple {@link Fragment} subclass.
 * describe:公司车辆
 */
public class CompanyTruckActivity extends Activity implements View.OnClickListener,ICompyanView {


    private ExpandableListView list_1;
    private static final String TAG = "WeightFragment";
    private ACache acache;
    private CompanyTruckAdapter madapter;
    private TextView mTvBack;
    private CompanyTruckActivity mContext;
    private CompanyResultBean companyResult;
    private List<CompanyTruckGroupBean> mGroupList=new ArrayList<>();
    private ImageView tv_search;

    public static void start(Context context){
        Intent i=new Intent(context,CompanyTruckActivity.class);
        context.startActivity(i);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=this;
        setContentView(R.layout.activity_company_truck);
        acache= ACache.get(mContext,TAG);
        companyResult=(CompanyResultBean)acache.getAsObject("company_truck");
        findViews();
    }

    protected void findViews() {
        List<CompanyBean> listBean=companyResult.getResult();
        mGroupList=new ArrayList<>();
        if(listBean !=null && listBean.size() >0){
            for(int i=0;i<listBean.size();i++){
                CompanyBean bean=listBean.get(i);
                String parentId=bean.getParentId();
                String parentName=bean.getParentName();
                String id=bean.getId();
                if(Tools.isEmpty(parentId) && Tools.isEmpty(parentName)){
                    CompanyTruckGroupBean cbean=new CompanyTruckGroupBean();
                    cbean.setName(bean.getCompanyName());
                    cbean.setId(id);
                    List<TruckChildBean> tlist=new ArrayList<>();
                    for(int j=listBean.size()-1;j>=0;j--){ //分公司
                        CompanyBean beanj=listBean.get(j);
                        String parentIdj=beanj.getParentId();
                        if(parentIdj.equals(id)){
                            TruckChildBean tbean=new TruckChildBean();
                            tbean.setName(beanj.getCompanyName());
                            tbean.setChildId(beanj.getId());
                            tlist.add(tbean);
                        }

                    }
                    cbean.setChildren(tlist);
                    mGroupList.add(cbean);
                }
            }
        }
        acache.put("truck_list",(Serializable) mGroupList);
        mTvBack = (TextView) findViewById(R.id.tv_back);
        mTvBack.setVisibility(View.VISIBLE);
        mTvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tv_search=(ImageView)findViewById(R.id.tv_search);
        tv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchTruckActivity.start(mContext);
                finish();
            }
        });

        list_1=(ExpandableListView)findViewById(R.id.list_1);
        madapter=new CompanyTruckAdapter(mContext,mGroupList);
        list_1.setGroupIndicator(null);//不使用系统提供的展开和收起的图标  左边有个下的图标
        list_1.setAdapter(madapter);
        int groupCount = list_1.getCount()-1;//减去头部
        for (int i=0; i<groupCount; i++)
        {
            list_1.expandGroup(i);
        }
    }




    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //返回
            case R.id.tv_back:
                finish();
                break;
        }
    }
    public void  showTips(String tip){
        ToastUtil.getInstance().showCenterMessage(mContext,tip);
    }


    @Override
    public void doSuccess(CompanyResultBean bean) {

    }

    @Override
    public void doError(String str) {

    }

    @Override
    public void inputSuccess(String str) {

    }
}
