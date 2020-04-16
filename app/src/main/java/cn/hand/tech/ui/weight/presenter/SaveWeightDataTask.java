package cn.hand.tech.ui.weight.presenter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.hand.tech.R;
import cn.hand.tech.bean.HDModelData;
import cn.hand.tech.bean.WeightData;
import cn.hand.tech.bean.WeightDataBean;
import cn.hand.tech.ble.bleUtil.BleConstant;
import cn.hand.tech.common.ConsTantsCode;
import cn.hand.tech.common.UrlConstant;
import cn.hand.tech.dao.WeightDao;
import cn.hand.tech.log.DLog;
import cn.hand.tech.net.HttpHandler;
import cn.hand.tech.net.NetCallBack;
import cn.hand.tech.utils.DialogUtil;
import cn.hand.tech.utils.SPUtil;
import cn.hand.tech.utils.ToastUtil;

/**
 * Created by hand-hitech2 on 2018-03-13.
 */

public class SaveWeightDataTask {
    private static Handler mHandler;
    private static Context mContext;
    private static final String TAG = "WeightFragment";
    private static SaveWeightDataTask instance = null;

    public SaveWeightDataTask() {
    }

    public static SaveWeightDataTask getInstance(Context context, Handler handler) {
        if (instance == null) {
            instance = new SaveWeightDataTask();
        }
        mContext = context;
        mHandler = handler;
        return instance;
    }

    public void SaveData(HDModelData saveData, String truckNum, String realWeight) {
        final ProgressDialog dialog = new ProgressDialog(mContext);
        DialogUtil.setProgressDialog(dialog, mContext.getResources().getString(R.string.save_ing));
        Gson gson = new Gson();
        final WeightDataBean weightData;
        weightData = new WeightDataBean();
        String deviceId = String.valueOf(saveData.nID);
      /*  long time = saveData.weightTime;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(time*1000);

      String uploadDate = formatter.format(curDate);*/
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        String uploadDate = formatter.format(date);

        DLog.e(TAG,"时间curDate==="+uploadDate);
        String mLocation = String.valueOf(SPUtil.get(mContext, "save_location", "0"));
        weightData.setLocation(mLocation);
        weightData.setDeviceId(deviceId);
        weightData.setCarNumber(truckNum);
        weightData.setWeightFromReal(realWeight);
        weightData.setUploadDate(uploadDate);//"2018-03-13 16:20:10"
        weightData.setHandBrakeHardwareStatus(String.valueOf(saveData.nBreak));
        weightData.setWeightFromDevice(String.valueOf(saveData.weight));
        weightData.setPackageNum(String.valueOf(saveData.nRunningNumber));
        weightData.setSpeed(String.valueOf(saveData.nSpeed));
        weightData.setX(String.valueOf(saveData.nLongtitude));
        weightData.setY(String.valueOf(saveData.nLatitude));
        long gpsTime = saveData.gpsTime;
        Date gpsDate = new Date(gpsTime*1000);
        String gpsUploadDate = formatter.format(gpsDate);
        weightData.setGpsUploadDate(gpsUploadDate);

        weightData.setShipmentStatus(String.valueOf(saveData.nLoadStatus));
        weightData.setAch1(String.valueOf(saveData.ad1));
        weightData.setAch2(String.valueOf(saveData.ad2));
        weightData.setAch3(String.valueOf(saveData.ad3));
        weightData.setAch4(String.valueOf(saveData.ad4));
        weightData.setAch5(String.valueOf(saveData.ad5));
        weightData.setAch6(String.valueOf(saveData.ad6));
        weightData.setAch7(String.valueOf(saveData.ad7));
        weightData.setAch8(String.valueOf(saveData.ad8));
        weightData.setAch9(String.valueOf(saveData.ad9));
        weightData.setAch10(String.valueOf(saveData.ad10));
        weightData.setAch11(String.valueOf(saveData.ad11));
        weightData.setAch12(String.valueOf(saveData.ad12));
        weightData.setAch13(String.valueOf(saveData.ad13));
        weightData.setAch14(String.valueOf(saveData.ad14));
        weightData.setAch15(String.valueOf(saveData.ad15));
        weightData.setAch16(String.valueOf(saveData.ad16));
        weightData.setSch1(String.valueOf(saveData.adZero1));
        weightData.setSch2(String.valueOf(saveData.adZero2));
        weightData.setSch3(String.valueOf(saveData.adZero3));
        weightData.setSch4(String.valueOf(saveData.adZero4));
        weightData.setSch5(String.valueOf(saveData.adZero5));
        weightData.setSch6(String.valueOf(saveData.adZero6));
        weightData.setSch7(String.valueOf(saveData.adZero7));
        weightData.setSch8(String.valueOf(saveData.adZero8));
        weightData.setSch9(String.valueOf(saveData.adZero9));
        weightData.setSch10(String.valueOf(saveData.adZero10));
        weightData.setSch11(String.valueOf(saveData.adZero11));
        weightData.setSch12(String.valueOf(saveData.adZero12));
        weightData.setSch13(String.valueOf(saveData.adZero13));
        weightData.setSch14(String.valueOf(saveData.adZero14));
        weightData.setSch15(String.valueOf(saveData.adZero15));
        weightData.setSch16(String.valueOf(saveData.adZero16));
        weightData.setStableStatus(String.valueOf(saveData.nStable));
        weightData.setDeviceVoltage(String.valueOf(saveData.nVoltage));
        //        List< WeightDataBean> weightDataBeanList=new ArrayList<>();
        //        weightDataBeanList.add(weightData);
        //
        //        String jsonData = gson.toJson(weightDataBeanList);
        //        String url = UrlConstant.HttpUrl.SAVA_DATA;
        //        DLog.e(TAG,"保存数据請求URL==" + url + jsonData);

        dialog.dismiss();
        showTips("数据保存成功");
        weightData.setTag("0");
        WeightDao.getInstance(mContext).updateOrInsert(weightData);//上传成功后 储存在本地
        Intent i=new Intent(BleConstant.ACTION_SAVE_SUCCESS);
        mContext.sendBroadcast(i);

        //        HttpHandler.getInstance().postSave(url, jsonData, new NetCallBack() {
        //            @Override
        //            public void onSuccess(String result, String executeStatus) {
        //                dialog.dismiss();
        //                DLog.e(TAG,"保存数据返回的response==" + result.toString());
        //
        //                  mHandler.sendEmptyMessage(1001);
        //
        //                showTips("数据保存成功");
        //                weightData.setTag("0");
        //                WeightDao.getInstance(mContext).updateOrInsert(weightData);//上传成功后 储存在本地
        //
        //                Intent i=new Intent(BleConstant.ACTION_SAVE_SUCCESS);
        //                mContext.sendBroadcast(i);
        //
        //            }
        //            @Override
        //            public void onException(String errorCode, String errorMsg) {
        //                dialog.dismiss();
        //                mHandler.sendEmptyMessage(1002);
        //                DLog.e(TAG,"请求服务器异常=="+errorMsg);
        //                showTips("服务器连接失败");
        //            }
        //            @Override
        //            public void onError(Exception e) {
        //                dialog.dismiss();
        //                mHandler.sendEmptyMessage(1002);
        //                DLog.e(TAG,"请求服务器异常=="+e.getMessage().toString());
        //                showTips("服务器连接失败");
        //
        //            }
        //        },true);

    }

    public void SaveData(List<WeightDataBean> listWeightData) {
        List<WeightData> listWeight = new ArrayList<>();
        for (int i = 0; i < listWeightData.size(); i++) {
            WeightDataBean weightDataBean = listWeightData.get(i);
            WeightData model = new WeightData();

            model.setUploadDate(weightDataBean.getUploadDate());
            model.setDeviceId(weightDataBean.getDeviceId());
            model.setCarNumber(weightDataBean.getCarNumber());
            model.setWeightFromDevice(weightDataBean.getWeightFromDevice());
            model.setWeightFromReal(weightDataBean.getWeightFromReal());
            model.setHandBrakeHardwareStatus(weightDataBean.getHandBrakeHardwareStatus());
            model.setPackageNum(weightDataBean.getPackageNum());
            model.setSpeed(weightDataBean.getSpeed());
            model.setX(weightDataBean.getX());
            model.setY(weightDataBean.getY());
            model.setGpsUploadDate(weightDataBean.getGpsUploadDate());
            model.setStableStatus(weightDataBean.getStableStatus());
            model.setDeviceVoltage(weightDataBean.getDeviceVoltage());
            model.setShipmentStatus(weightDataBean.getShipmentStatus());
            model.setAch1(weightDataBean.getAch1());
            model.setAch2(weightDataBean.getAch2());
            model.setAch3(weightDataBean.getAch3());
            model.setAch4(weightDataBean.getAch4());
            model.setAch5(weightDataBean.getAch5());
            model.setAch6(weightDataBean.getAch6());
            model.setAch7(weightDataBean.getAch7());
            model.setAch8(weightDataBean.getAch8());
            model.setAch9(weightDataBean.getAch9());
            model.setAch10(weightDataBean.getAch10());
            model.setAch11(weightDataBean.getAch11());
            model.setAch12(weightDataBean.getAch12());
            model.setAch13(weightDataBean.getAch13());
            model.setAch14(weightDataBean.getAch14());
            model.setAch15(weightDataBean.getAch15());
            model.setAch16(weightDataBean.getAch16());
            model.setSch1(weightDataBean.getSch1());
            model.setSch2(weightDataBean.getSch2());
            model.setSch3(weightDataBean.getSch3());
            model.setSch4(weightDataBean.getSch4());
            model.setSch5(weightDataBean.getSch5());
            model.setSch6(weightDataBean.getSch6());
            model.setSch7(weightDataBean.getSch7());
            model.setSch8(weightDataBean.getSch8());
            model.setSch9(weightDataBean.getSch9());
            model.setSch10(weightDataBean.getSch10());
            model.setSch11(weightDataBean.getSch11());
            model.setSch12(weightDataBean.getSch12());
            model.setSch13(weightDataBean.getSch13());
            model.setSch14(weightDataBean.getSch14());
            model.setSch15(weightDataBean.getSch15());
            model.setSch16(weightDataBean.getSch16());
            listWeight.add(model);
        }

        Gson gson = new Gson();
        String jsonData = gson.toJson(listWeight);
        String url = UrlConstant.HttpUrl.SAVA_DATA;
        //  LogUtil.e("保存数据請求URL==" + url + jsonData);
        HttpHandler.getInstance().postSave(url, jsonData, new NetCallBack() {
            @Override
            public void onError(Exception e) {
                mHandler.sendEmptyMessage(ConsTantsCode.REQUEST_CODE_FAIL);
                DLog.e(TAG,"请求服务器异常==" + e.getMessage().toString());
                showTips("服务器连接失败");
            }

            @Override
            public void onException(String errorCode, String errorMsg) {
                mHandler.sendEmptyMessage(ConsTantsCode.REQUEST_CODE_FAIL);
                showTips(errorMsg);
            }

            @Override //>responsebody:{"result":[],"message":"数据提交成功，共提交[17]条","token":""}
            public void onSuccess(String result, String executeStatus) {
                DLog.e(TAG,"保存数据返回的response==" + result.toString());
                mHandler.sendMessage(mHandler.obtainMessage(ConsTantsCode.REQUEST_CODE_SUCCESS, "数据提交成功"));
            }
        },true);

    }

    public void  showTips(String tip){
        ToastUtil.getInstance().showCenterMessage(mContext,tip);
    }
}
