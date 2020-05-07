package cn.hand.tech.ui.setting;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.hand.tech.R;
import cn.hand.tech.common.ACache;
import cn.hand.tech.ui.setting.adapter.CompanyTruckForRepairAdapter;
import cn.hand.tech.ui.weight.bean.CompanyTruckGroupBean;
import cn.hand.tech.ui.weight.bean.TruckChildBean;
import cn.hand.tech.utils.CommonUtils;
import cn.hand.tech.utils.PinyinUtils;
import cn.hand.tech.utils.ToastUtil;
import cn.hand.tech.utils.Tools;


/**
 * A simple {@link Fragment} subclass.
 * describe:搜索
 */
public class SearchTruckForRepairActivity extends Activity implements View.OnClickListener {


    private ExpandableListView list_1;
    private EditText et_search;
    private TextView tv_cancle,tv_search;
    private ACache acache;
    private List<CompanyTruckGroupBean> mGroupList;
    private List<CompanyTruckGroupBean> mlist=new ArrayList<>();
    private CompanyTruckForRepairAdapter madapter;
    private Context mContext;
    public static void start(Context context){
        Intent i=new Intent(context, SearchTruckForRepairActivity.class);
        context.startActivity(i);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=this;
        setContentView(R.layout.fragment_search_truck);
        acache= ACache.get(mContext, CommonUtils.TAG);
        findViews();
        setListeners();
    }

    protected void findViews() {
        mGroupList=(List<CompanyTruckGroupBean>)acache.getAsObject("truck_list");//缓存

        et_search=(EditText)findViewById(R.id.et_search);
        tv_cancle=(TextView)findViewById(R.id.tv_cancle);
        tv_cancle.setVisibility(View.VISIBLE);
        tv_search=(TextView)findViewById(R.id.tv_search);
        tv_search.setVisibility(View.GONE);
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String str=s.toString();
                if(str !=null && !str.equals("")){
                    tv_cancle.setVisibility(View.GONE);
                    tv_search.setVisibility(View.VISIBLE);
                }else{
                    tv_cancle.setVisibility(View.VISIBLE);
                    tv_search.setVisibility(View.GONE);
                }

                if(!Tools.isEmpty(s.toString())){
                    List<CompanyTruckGroupBean>list= doSearch(s.toString());
                    if(list !=null && list.size() >0){
                        madapter.updateListView(list);
                        //遍历所有group,将所有项设置成默认展开
                        int groupCount =list.size();
                        for (int i=0; i<groupCount; i++)
                        {
                            list_1.expandGroup(i);
                        }

                    }else{
                        List<CompanyTruckGroupBean>list1=new ArrayList<>();
                        madapter.updateListView(list1);
                    }
                }else{
                    List<CompanyTruckGroupBean>list2=new ArrayList<>();
                    madapter.updateListView(list2);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                String str1=s.toString();
                if(str1 !=null && !str1.equals("")){
                    tv_cancle.setVisibility(View.GONE);
                    tv_search.setVisibility(View.VISIBLE);
                }else{
                    tv_cancle.setVisibility(View.VISIBLE);
                    tv_search.setVisibility(View.GONE);
                }
            }
        });
        list_1=(ExpandableListView)findViewById(R.id.list_1);
        madapter=new CompanyTruckForRepairAdapter(mContext,mlist);
        list_1.setGroupIndicator(null);//不使用系统提供的展开和收起的图标  左边有个下的图标
        list_1.setAdapter(madapter);
        int groupCount =mlist.size();

        for (int i=0; i<groupCount; i++) {

            list_1.expandGroup(i);

        }
        list_1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyBoard(mContext,et_search);
                return false;
            }
        });
    }

    /**
     * 获取焦点,并隐藏或显示软键盘
     */
    public static void hideKeyBoard(Context context, View view) {
        /*隐藏或显示软键盘*/
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);


    }


    protected void setListeners() {
        tv_cancle.setOnClickListener(this);
        tv_search.setOnClickListener(this);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //取消
            case R.id.tv_cancle:
                finish();
                break;
            //搜索
            case R.id.tv_search:
                String etStr=et_search.getText().toString().trim();
                if(!Tools.isEmpty(etStr)){
                    List<CompanyTruckGroupBean>list= doSearch(etStr);
                    if(list !=null && list.size() >0){
                        madapter.updateListView(list);
                        //遍历所有group,将所有项设置成默认展开
                        int groupCount =list.size();
                        for (int i=0; i<groupCount; i++)
                        {
                            list_1.expandGroup(i);
                        }

                    }else{
                        List<CompanyTruckGroupBean>list3=new ArrayList<>();
                        madapter.updateListView(list3);
                        showTips("搜索不到数据");
                    }
                }else{
                    List<CompanyTruckGroupBean>list4=new ArrayList<>();
                    madapter.updateListView(list4);
                    showTips("搜索关键字为空");
                }
                break;
        }
    }
    public void  showTips(String tip){
        ToastUtil.getInstance().showCenterMessage(mContext,tip);
    }

    //搜索匹配首字母
    public List<CompanyTruckGroupBean> doSearch(String etstr){
         List<CompanyTruckGroupBean> list=new ArrayList<>();
        boolean isChinese= PinyinUtils.isContainChinese(etstr);
        if(isChinese){
            String str=etstr;//获取第一个字首字母
            for(int i=0;i<mGroupList.size();i++){
                boolean ishave=false;
                CompanyTruckGroupBean  comybean=mGroupList.get(i);
                String companyName=comybean.getName();//公司名称
                if(companyName.contains(etstr)){
                    list.add(comybean);
                }else{
                    CompanyTruckGroupBean b=new CompanyTruckGroupBean();
                    List<TruckChildBean> tlist=  mGroupList.get(i).getChildren(); //车辆名称
                    List<TruckChildBean> chlist= new ArrayList<>();
                    if(tlist !=null && tlist.size() > 0){
                        for(int j=0;j<tlist.size();j++){
                            TruckChildBean bean=tlist.get(j);
                            String Chname=bean.getName();
                            if(Chname.contains(str)){
                                chlist.add(bean);
                                ishave=true;
                            }

                        }
                    }
                    if(ishave){
                        b.setTruckNumber(mGroupList.get(i).getTruckNumber());
                        b.setOnlineNumber(mGroupList.get(i).getOnlineNumber());
                        b.setName(mGroupList.get(i).getName());
                        b.setChildren(chlist);
                        list.add(b);
                    }
                }
            }
        }else{
            String str=etstr;//
            for(int i=0;i<mGroupList.size();i++){
                boolean ishave=false;
                CompanyTruckGroupBean b=new CompanyTruckGroupBean();
                List<TruckChildBean> tlist=  mGroupList.get(i).getChildren();
                List<TruckChildBean> chlist= new ArrayList<>();
                if(tlist !=null && tlist.size() > 0){
                    for(int j=0;j<tlist.size();j++){
                        TruckChildBean bean=tlist.get(j);
                        String Chname=bean.getName();
                        String Enname=PinyinUtils.converfirst(Chname)+Chname;//获取第一个字首字母
                        if(Enname.contains(str)){
                            chlist.add(bean);
                            ishave=true;
                        }

                    }
                }
                if(ishave){
                    b.setTruckNumber(mGroupList.get(i).getTruckNumber());
                    b.setOnlineNumber(mGroupList.get(i).getOnlineNumber());
                    b.setName(mGroupList.get(i).getName());
                    b.setChildren(chlist);
                    list.add(b);
                }
            }
        }
         return list;

    }
    /**
     * 提取英文的首字母，非英文字母用#代替。
     *
     * @param str
     * @return
     */
    private String getAlpha(String str) {
        String  sortStr = str.trim().substring(0, 1).toUpperCase();
        // 正则表达式，判断首字母是否是英文字母
        if (sortStr.matches("[A-Z]")) {
            return sortStr;
        } else {
            return "#";
        }
    }

}
