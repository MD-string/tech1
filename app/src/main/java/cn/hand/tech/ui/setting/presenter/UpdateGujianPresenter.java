package cn.hand.tech.ui.setting.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.hand.tech.R;
import cn.hand.tech.common.ACache;
import cn.hand.tech.common.UrlConstant;
import cn.hand.tech.log.DLog;
import cn.hand.tech.net.HttpHandler;
import cn.hand.tech.net.NetCallBack;
import cn.hand.tech.ui.setting.IUpdateView;
import cn.hand.tech.ui.setting.bean.CarNumberInfo;
import cn.hand.tech.ui.setting.bean.GuJianBean;
import cn.hand.tech.ui.weight.bean.UserResultBean;
import cn.hand.tech.utils.CommonKitUtil;
import cn.hand.tech.utils.LogUtil;
import cn.hand.tech.utils.Tools;
import cn.hand.tech.weiget.MyCustomDialog;

/*
 * 固件升级数据 获取
 */
public class UpdateGujianPresenter {
    private final ACache acache;
    private Context mContext;
    private static final String TAG = "AddRepairPresenter";
    private IUpdateView mView;
    MyCustomDialog dialog;
    public static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    public UpdateGujianPresenter(Context context, IUpdateView view) {
        mContext = context;
        mView = view;
        acache= ACache.get(context,"WeightFragment");
    }


    //登录
    public void postLogin( final HashMap<String, String> map) {

        try {

            String url = UrlConstant.HttpUrl.LOGIN;
            DLog.e("EventListPresenter","登陸請求URL=="+url+map.toString());
            HttpHandler.getInstance().postBle(null,url, map, new NetCallBack() {
                @Override
                public void onSuccess(String result, String executeStatus) {
                    if (!TextUtils.isEmpty(result)) {
                        //						closeProgressDialog();
                        UserResultBean user = null;
                        try {
                            user = CommonKitUtil.parseJsonWithGson(result.toString(), UserResultBean.class);
                            String message = user.getMessage();
                            if (message.equals("success")) {
                                mView.doLogin(user);

                            } else {
                                mView.doLoginFail("登录失败");
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }
                @Override
                public void onException(String errorCode, String errorMsg) {
                    closeProgressDialog();
                    mView.doLoginFail(errorMsg);

                }
                @Override
                public void onError(Exception e) {
                    closeProgressDialog();
                    mView.doLoginFail("登录失败");
                }
            });

        } catch (Exception e1) {
            closeProgressDialog();
            mView.doLoginFail("登录失败");
            e1.printStackTrace();
        }
    }

    /**
     * 获取公司信息
     */
    public void getCompanyInfo( final HashMap<String, String> map) {
        try {
            String  url= UrlConstant.HttpUrl.CHECK_CARNUMBER_EXIST;
            LogUtil.d("根据车牌号获取车辆信息==" + url + map.toString());
            HttpHandler.getInstance().postBle(null,url, map, new NetCallBack() {
                @Override
                public void onSuccess(String result, String executeStatus) {
                    Gson gson = new Gson();
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(result.toString());
                        String message = jsonObject.getString("result");
                        if(!Tools.isEmpty(message) && message.length()> 0){
                            CarNumberInfo companyResult = CommonKitUtil.parseJsonWithGson(message.toString(), CarNumberInfo.class);
                            mView.doCompanySuccess(companyResult);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                @Override
                public void onException(String errorCode, String errorMsg) {
                    mView.doCompanyError(errorMsg);

                }
                @Override
                public void onError(Exception e) {
                    mView.doCompanyError("根据车牌号获取车辆信息失败");
                }
            });

        } catch (Exception e1) {
            mView.doCompanyError("根据车牌号获取车辆信息失败");
            e1.printStackTrace();
        }
    }


    /**
     * 固件BIN 列表
     */
    public void updateBinList( final HashMap<String, String> map) {
        try {

            String url = UrlConstant.HttpUrl.UPDATE_BIN_LIST;
            DLog.e("binList"," 固件BIN 列表URL=="+url+map.toString());
            HttpHandler.getInstance().postBleBIN(null,url, map, new NetCallBack() {
                @Override
                public void onSuccess(String result, String executeStatus) {
                    Gson gson = new Gson();
                    JSONObject jsonObject ;
                    try {
                        if(!Tools.isEmpty(result) && result.length()> 0 ){
                            jsonObject = new JSONObject(result);
                            JSONArray schoolInfoArray = jsonObject.getJSONArray("result");
                            List<GuJianBean> depts =new ArrayList<>();
                            for(int i=0;i<schoolInfoArray.length();i++){
                                GuJianBean bwan=new GuJianBean();
                                JSONObject gjon = schoolInfoArray.getJSONObject(i);
                                bwan.setBinName(gjon.getString("binName"));
                                bwan.setPacketTotal(gjon.getInt("packetTotal"));
                                bwan.setDescription(gjon.getString("description"));
                                bwan.setVersion(gjon.getString("version"));
                                bwan.setHmVersion(gjon.getString("hwVersion"));
                                bwan.setId(gjon.getString("id"));
                                depts.add(bwan);
                            }
                            mView.doBinList(depts);
                            DLog.e("binList"," 固件BIN =="+depts.size());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                @Override
                public void onException(String errorCode, String errorMsg) {
                    DLog.e("binList","固件BIN 列表URL=="+errorMsg);
                    mView.doBinListFail(errorMsg);

                }
                @Override
                public void onError(Exception e) {
                    mView.doBinListFail("固件BIN列表下载失败");
                }
            });

        } catch (Exception e1) {
            mView.doBinListFail("固件BIN列表下载失败");
            e1.printStackTrace();
        }
    }

    //下载固件
    public void downLoadText( final HashMap<String, String> map,String devid) {
        showProgressDialog("下载固件文件中...");
        new Thread("down_text") {
            @Override
            public void run() {
                try {
                    String url = UrlConstant.HttpUrl.UPDATE_GU_JIAN;
                    DLog.e("UpdateGujianPresenter","下载固件URL=="+url+map.toString());
                    HttpHandler.getInstance().postBle(null,url, map, new NetCallBack() {
                        @Override
                        public void onSuccess(String result, String executeStatus) {
                            DLog.e(TAG,"UpdateGujianPresenter:" + "====》result: "+result);
                            if (!TextUtils.isEmpty(result)) {
                                closeProgressDialog();
                                JSONObject jsonObject=null;
                                GuJianBean bean=null;
                                try {
                                    jsonObject= new JSONObject(result.toString());
                                    String  value=jsonObject.getString("result");
                                    bean = CommonKitUtil.parseJsonWithGson(value.toString(), GuJianBean.class);
                                    if (bean !=null) {
                                        mView.doSuccess(bean);

                                    } else {
                                        mView.doError("下载固件失败");
                                    }

                                } catch (Exception e) {
                                    mView.doError("下载固件失败");
                                    e.printStackTrace();
                                }
                            }

                        }
                        @Override
                        public void onException(String errorCode, String errorMsg) {
                            closeProgressDialog();
                            mView.doLoginFail(errorMsg);

                        }
                        @Override
                        public void onError(Exception e) {
                            closeProgressDialog();
                            mView.doError("下载固件失败");
                        }
                    });
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        }.start();
    }



    /**
     * 上传log日志
     */
    public void upLoadLog( final HashMap<String, String> map) {
        try {

            String url = UrlConstant.HttpUrl.URL_UPLOAD_LOG;
            DLog.e("upLoadLog","上传log日志URL=="+url+map.toString());
            HttpHandler.getInstance().postBle(null,url, map, new NetCallBack() {
                @Override
                public void onSuccess(String result, String executeStatus) {
                    try {
                        mView.doLOG("上传日志成功");
                        DLog.e("upLoadLog","上传log日志URL==成功");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                @Override
                public void onException(String errorCode, String errorMsg) {
                    DLog.e("upLoadLog","上传log日志URL=="+errorMsg);
                    closeProgressDialog();
                    mView.doLogFail(errorMsg);

                }
                @Override
                public void onError(Exception e) {
                    closeProgressDialog();
                    mView.doLogFail("登录失败");
                }
            });

        } catch (Exception e1) {
            closeProgressDialog();
            mView.doLogFail("登录失败");
            e1.printStackTrace();
        }
    }
    /**
     * 显示进度对话框
     */
    private void showProgressDialog(String text) {
        if(dialog !=null){
            dialog.cancel();
        }
        dialog = new MyCustomDialog(mContext, R.style.LoadDialog,text);
        dialog.show();
        new Thread("cancle_progressDialog") {
            @Override
            public void run() {
                try {
                    Thread.sleep(7000);
                    // cancel和dismiss方法本质都是一样的，都是从屏幕中删除Dialog,唯一的区别是
                    // 调用cancel方法会回调DialogInterface.OnCancelListener如果注册的话,dismiss方法不会回掉
                    if(dialog !=null ){
                        dialog.cancel();
                    }
                    // dialog.dismiss();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        }.start();
    }
    /**
     * 关闭进度对话框
     */
    private void closeProgressDialog() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }
}
