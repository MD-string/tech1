package cn.hand.tech.ui.weight.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.core.content.FileProvider;
import cn.hand.tech.BApplication;
import cn.hand.tech.R;
import cn.hand.tech.common.ConsTantsCode;
import cn.hand.tech.log.DLog;
import cn.hand.tech.ui.weight.bean.XlsData;
import cn.hand.tech.ui.weight.bean.XlsDataTimeModel;
import cn.hand.tech.utils.ExcelUtils;
import cn.hand.tech.utils.Tools;
import cn.hand.tech.utils.ZipUtil;

/**
 * Created by hand-hitech2 on 2018-01-17.
 *通过第三方发送邮件
 * 发送邮件
 */

public class DoExcelData {
    public static String APP_PATH = "hande/main";
    public  static String filepath= BApplication.mContext.getCacheDir().getPath() + "/" + APP_PATH;
    private static String[] title = { "序号","名称","信息","补充"};
    private static ArrayList<ArrayList<String>> recordList;
    public static void saveLocalData(Context context, List<XlsDataTimeModel> listLocalData, String name) {
        //将原来存储的文件的清除掉
        String dirPath =filepath + "/" + "stock_xls" + "/";
        File mZipFile = new File(dirPath);
        if (!mZipFile.exists()) {
            mZipFile.mkdirs();
        }
        File[] files = mZipFile.listFiles();
        if (files != null && files.length >0) {
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                file.delete();
            }
        }
        ExcelUtils.initExcel(dirPath + name+"_hd.xls", title,name+"_hd");
        String fileName = dirPath + name+"_hd.xls";
        ExcelUtils.writeObjListToExcel(getRecordData(listLocalData), fileName, context);

    }

    /**
     * 将数据集合 转化成ArrayList<ArrayList<String>>
     * @return
     */
    private static ArrayList<ArrayList<String>> getRecordData(List<XlsDataTimeModel> listLocalData) {
        recordList = new ArrayList<ArrayList<String>>();

        XlsDataTimeModel modle = listLocalData.get(0);
        List<XlsData>  list=modle.getWeightDataBeanList();
        for (int i = 0; i <list.size(); i++) {
            XlsData bean=list.get(i);
            ArrayList<String> beanList = new ArrayList<String>();
            beanList.add((i+1)+"");
            beanList.add(bean.getName());
            String v1=bean.getV1();
            if(!Tools.isEmpty(v1)){

                beanList.add(v1);
            }else{
                beanList.add("");
            }
            String v2=bean.getV2();
            if(!Tools.isEmpty(v2)){
                beanList.add(v2);
            }else{
                beanList.add("");
            }
            recordList.add(beanList);
        }
        return recordList;
    }


    //邮箱发送
    public static void sendXX(Activity context, String name) {
        //多个附件文件上传
        try{
            String dirPath = filepath + "/" + "stock_xls" + "/";
            File mZipFile = new File(dirPath);
            File[] files = mZipFile.listFiles();
            if (files != null) {
                File file2 = new File(dirPath,  name+"_hd.xls");

                Uri uriForFile = FileProvider.getUriForFile(context, context.getResources().getString(R.string.authorities),file2);
                if(uriForFile !=null ){
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    String[] tos = {"pengjunjun@hand-hitech.com", "liuyang@hand-hitech.com"};
                    // 收件人
                    intent.putExtra(Intent.EXTRA_EMAIL, tos);

                    intent.putExtra(Intent.EXTRA_SUBJECT, name);
                    //                    intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris1);
                    intent.putExtra(Intent.EXTRA_STREAM, uriForFile);
                    intent.putExtra(Intent.EXTRA_TEXT, "物料清单");//多附件发送执行这句会有string不能转化为list的警告；
                    //                    intent.setType("multipart/form-data"); //其他的均使用流当做二进制数据来发送
                    intent.setType("application/octet-stream");
                    Intent.createChooser(intent, "Choose Email Client");
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivityForResult(intent, ConsTantsCode.REQUEST_CODE_EMAIL);
                }
            }
        }catch (Exception e){
            //            Intent intent = new Intent(Intent.ACTION_SEND);
            //            context.startActivityForResult(intent, ConsTantsCode.REQUEST_CODE_EMAIL);
            e.printStackTrace();
            DLog.d("SaveMVVData","发送邮件异常");
        }

    }




    //邮箱发送
    public static void sendZip(Activity context, String name) {
        //多个附件文件上传
        try{
            String dirPath = filepath + "/" + "stock_xls" + "/";
            File mZipFile = new File(dirPath);
            File[] files = mZipFile.listFiles();
            if (files != null) {
                ArrayList<Uri> uris = new ArrayList();
                for (int i = 0; i < files.length; i++) {
                    File file = files[i];
                    DLog.e("SaveMVVData", "发送附件地址=" + file.getAbsolutePath());
                    Uri uriForFile = FileProvider.getUriForFile(context, context.getResources().getString(R.string.authorities),file);
                    uris.add(uriForFile);
                }
                String filepaht = filepath + "/" + "zip" + "/";
                File mZipFile1 = new File(filepaht);
                if (!mZipFile1.exists()) {
                    mZipFile1.mkdirs();
                }
                String zipPath=filepaht+name+".zip";
                ZipUtil.zip(dirPath,zipPath);//压缩成zip
                File mZipFile2 = new File(zipPath);
                if(mZipFile2 !=null ){
                    Uri uriForFile = FileProvider.getUriForFile(context, context.getResources().getString(R.string.authorities),mZipFile2);
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    String[] tos = {"pengjunjun@hand-hitech.com", "liuyang@hand-hitech.com"};
                    // 收件人
                    intent.putExtra(Intent.EXTRA_EMAIL, tos);

                    intent.putExtra(Intent.EXTRA_SUBJECT, name);
                    //                    intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris1);
                    intent.putExtra(Intent.EXTRA_STREAM, uriForFile);
                    intent.putExtra(Intent.EXTRA_TEXT, "录车");//多附件发送执行这句会有string不能转化为list的警告；
                    intent.setType("application/octet-stream"); //其他的均使用流当做二进制数据来发送
                    Intent.createChooser(intent, "Choose Email Client");
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivityForResult(intent, ConsTantsCode.REQUEST_CODE_EMAIL);
                }
            }
        }catch (Exception e){
            //            Intent intent = new Intent(Intent.ACTION_SEND);
            //            context.startActivityForResult(intent, ConsTantsCode.REQUEST_CODE_EMAIL);
            e.printStackTrace();
            DLog.d("SaveMVVData","发送邮件异常");
        }

    }


}


