package cn.hand.tech.ui.setting;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import cn.hand.tech.R;
import cn.hand.tech.common.ACache;
import cn.hand.tech.common.ConsTantsCode;
import cn.hand.tech.log.DLog;
import cn.hand.tech.ui.setting.adapter.CompanyListAdapter;
import cn.hand.tech.ui.setting.presenter.GetCompanyListPresenter;
import cn.hand.tech.ui.weight.bean.CompanyBean;
import cn.hand.tech.ui.weight.bean.CompanyResultBean;
import cn.hand.tech.ui.weight.bean.CompanyTruckGroupBean;
import cn.hand.tech.ui.weight.bean.TruckChildBean;
import cn.hand.tech.ui.weight.bean.UserResultBean;
import cn.hand.tech.utils.Aes;
import cn.hand.tech.utils.CommonUtils;
import cn.hand.tech.utils.ToastUtil;
import cn.hand.tech.utils.Tools;


/**
 * A simple {@link Fragment} subclass.
 * describe:公司列表
 */
public class CompanyListActivity extends Activity implements View.OnClickListener,ICompanyListView {


    private ExpandableListView list_1;
    private ACache acache;
    private CompanyListAdapter madapter;
    private TextView mTvBack;
    private CompanyListActivity mContext;
    private List<CompanyTruckGroupBean> mGroupList=new ArrayList<>();
    private ImageView tv_search;
    private AlertDialog adddialog;
    private String psw;
    private GetCompanyListPresenter mpresenter;
    private String token;
    private CompanyResultBean companyResult;

    public static void start(Context context){
        Intent i=new Intent(context, CompanyListActivity.class);
        context.startActivity(i);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=this;
        setContentView(R.layout.activity_company_truck);
        acache= ACache.get(mContext, CommonUtils.TAG);
        mpresenter = new GetCompanyListPresenter(mContext, this);
        doAddTruck();
        findViews();
    }

    private void doAddTruck() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        View view = View.inflate(mContext, R.layout.dialog_for_login, null);
        builder.setView(view);
        builder.setCancelable(true);
        final EditText et_login_count = (EditText) view.findViewById(R.id.et_login_count);
        String uname = acache.getAsString("login_name");
        if (!Tools.isEmpty(uname)) {
            et_login_count.setText(uname);
            et_login_count.setSelection(uname.length());
        }
        final EditText et_login_password = (EditText) view.findViewById(R.id.et_login_password);
        String password=   acache.getAsString("log_psw");
        if (!Tools.isEmpty(password)) {
            et_login_password.setText(password);
        }
        TextView tv_cancle = (TextView) view.findViewById(R.id.tv_cancle);//取消
        tv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adddialog.cancel();
                finish();
            }
        });
        TextView tv_login = (TextView) view.findViewById(R.id.tv_login);//登录
        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String count = et_login_count.getText().toString().trim();
                psw = et_login_password.getText().toString().trim();
                if (Tools.isEmpty(count) || Tools.isEmpty(psw)) {
                    showTips("账号或密码不能为空");
                    return;
                }
                submit(count, psw);
            }
        });
        //取消或确定按钮监听事件处理
        adddialog = builder.create();
        adddialog.show();
    }

    //开始登录
    private void submit(String count, String password) {

        final String inputString = count + "#" + password;

        try {
            String encryStr = Aes.encrypt(inputString, ConsTantsCode.PASSWORD_STRING);
            HashMap<String, String> mapLogin = new HashMap<>();
            mapLogin.put("token", encryStr);
            mpresenter.postLogin(mapLogin);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    /**
     * 关闭进度对话框
     */
    private void closedialog() {
        if (adddialog != null) {
            adddialog.dismiss();
            adddialog = null;
        }
    }
    /*获取公司列表信息*/
    private void initCompanyList() {
        HashMap<String, String> mapParams = new HashMap<>();
        mapParams.put("token", token);
        mapParams.put("version", "2");
        mpresenter.getCompanyList(mapParams);

    }

    protected void findViews() {
        try{
            mTvBack = (TextView) findViewById(R.id.tv_back);
            mTvBack.setVisibility(View.VISIBLE);
            mTvBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            tv_search=(ImageView)findViewById(R.id.tv_search);
            tv_search.setVisibility(View.GONE);
            tv_search.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SearchTruckForRepairActivity.start(mContext);
                    finish();
                }
            });

            list_1=(ExpandableListView)findViewById(R.id.list_1);
            list_1.setChildDivider(mContext.getResources().getDrawable(R.color.white));
            madapter=new CompanyListAdapter(mContext,mGroupList);
            list_1.setGroupIndicator(null);//不使用系统提供的展开和收起的图标  左边有个下的图标
            list_1.setAdapter(madapter);
//            int groupCount = list_1.getCount()-1;//减去头部
//            for (int i=0; i<groupCount; i++)
//            {
//                list_1.expandGroup(i);
//            }
        }catch (Exception e){
            e.printStackTrace();
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

    @SuppressLint("HandlerLeak")
    Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if(companyResult !=null){
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
                                    List<String> slist= ( List<String>)acache.getAsObject("CompanyListActivity_company_list");
                                    if(slist !=null && slist.size() >0){
                                        for(int k=0;k<slist.size();k++){
                                            String str=slist.get(k);
                                            if(str.equals(id)){
                                                cbean.setSelectedOr(true);
                                            }
                                        }
                                    }
                                    cbean.setChildren(tlist);
                                    mGroupList.add(cbean);
                                }
                            }
                        }
                        acache.put("truck_list",(Serializable) mGroupList);

                        madapter.updateListView(mGroupList);
                    }else{
                        DLog.e("AddTruckActivity", "下载公司列表数据体为空");
                    }
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void doLogin(UserResultBean bean) {
        closedialog();
        showTips("登录成功");
        UserResultBean userBean=bean;
        token=bean.getToken();
        String uname=bean.getResult().getUserName();

        acache.put("login_name",uname);
        acache.put("login_token",token);
        acache.put("log_psw",psw);

        initCompanyList();
    }

    @Override
    public void doLoginFail(String bean) {

    }

    @Override
    public void sendSuccess(String msg) {

    }

    @Override
    public void sendError(String msg) {

    }

    @Override
    public void doCompanySuccess(CompanyResultBean bean) {
         companyResult = bean;
        DLog.e("AddTruckActivity", "下载公司列表成功");
        mhandler.sendEmptyMessage(1);

    }

    @Override
    public void doCompanyError(String msg) {

    }

    @Override
    protected void onStop() {
        super.onStop();
        doGetClickList();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    private  void doGetClickList(){
        if(mGroupList !=null && mGroupList.size() > 0){
            List<String> mlist=new ArrayList<>();
            for(int i=0;i<mGroupList.size();i++){
                boolean ischeck=mGroupList.get(i).isSelectedOr();
                String  conmpanyId=mGroupList.get(i).getId();
                List<TruckChildBean> tlist= mGroupList.get(i).getChildren();
                boolean ishave=false;
                for(int j=0;j<tlist.size();j++){
                    boolean ischild=  tlist.get(j).isSelectedOrChrend();
                    if(ischild){
                        ishave=true;
                        break;
                    }
                }

                if(ischeck || ishave){
                    mlist.add(conmpanyId);
                }
            }
            acache.put("CompanyListActivity_company_list",(Serializable)mlist);
            showTips("保存成功");
            DLog.e("CompanyListActivity", "///"+Arrays.asList(mlist));

        }
    }
}
