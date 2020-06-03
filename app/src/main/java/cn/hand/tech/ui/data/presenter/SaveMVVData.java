package cn.hand.tech.ui.data.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import androidx.core.content.FileProvider;
import cn.hand.tech.BApplication;
import cn.hand.tech.R;
import cn.hand.tech.bean.WeightDataBean;
import cn.hand.tech.common.ConsTantsCode;
import cn.hand.tech.log.DLog;
import cn.hand.tech.ui.data.bean.LocalDataTimeModel;
import cn.hand.tech.utils.ZipUtil;

/**
 * Created by hand-hitech2 on 2018-01-17.
 *通过第三方发送邮件
 * 发送邮件
 */

public class SaveMVVData {
    public static String APP_PATH = "hande/main";
    public  static String filepath= BApplication.mContext.getCacheDir().getPath() + "/" + APP_PATH;
    public static void saveLocalData(Context context, List<LocalDataTimeModel> listLocalData, String name) {
        //将原来存储的文件的清除掉
        String dirPath =filepath + "/" + "tech3_csv" + "/";
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
        //存储文件
        for (int j = 0; j < listLocalData.size(); j++) {
            List<WeightDataBean> listData = listLocalData.get(j).getWeightDataBeanList();
            String name1 = listLocalData.get(j).getLocalDataTime();
            File file2 = new File(dirPath, name1 + "_Loc.csv");
            DLog.e("SaveMVVData","保存发送文件名==" + file2.getAbsolutePath());
            try {
                OutputStream outputStream1 = new FileOutputStream(file2);
                String str = "number,time,mv/v1,mv/v2,mv/v3,mv/v4,mv/v5,mv/v6,mv/v7,mv/v8,mv/v9,mv/v10,mv/v11,mv/v12,mv/v13,mv/v14,mv/v15,mv/v16,weight,originalWeight,location\n";//1,1,1,1,2\n2,2,2,2,3\n";
                outputStream1.write(str.getBytes("UTF-8"));//避免解析出现乱码
                int size = listData.size();
                for (int i = 0; i < size; i++) {
                    String ss = "";
                    WeightDataBean item = listData.get(size-i-1);
                    int number=i+1;
                    float mvvV1 = getMvvValue(item.getAch1(), item.getSch1());
                    float mvvV2 = getMvvValue(item.getAch2(), item.getSch2());
                    float mvvV3 = getMvvValue(item.getAch3(), item.getSch3());
                    float mvvV4 = getMvvValue(item.getAch4(), item.getSch4());
                    float mvvV5 = getMvvValue(item.getAch5(), item.getSch5());
                    float mvvV6 = getMvvValue(item.getAch6(), item.getSch6());
                    float mvvV7 = getMvvValue(item.getAch7(), item.getSch7());
                    float mvvV8 = getMvvValue(item.getAch8(), item.getSch8());
                    float mvvV9 = getMvvValue(item.getAch9(), item.getSch9());
                    float mvvV10 = getMvvValue(item.getAch10(), item.getSch10());
                    float mvvV11 = getMvvValue(item.getAch11(), item.getSch11());
                    float mvvV12 = getMvvValue(item.getAch12(), item.getSch12());
                    float mvvV13 = getMvvValue(item.getAch13(), item.getSch13());
                    float mvvV14 = getMvvValue(item.getAch14(), item.getSch14());
                    float mvvV15 = getMvvValue(item.getAch15(), item.getSch15());
                    float mvvV16 = getMvvValue(item.getAch16(), item.getSch16());
                    String realWeight=item.getWeightFromReal();
                    String orgWeight=item.getWeightFromDevice();

                    ss = ss + number + "," + item.getUploadDate() + ","+mvvV1+ "," + mvvV2 + "," + mvvV3 + "," + mvvV4+ "," + mvvV5+ "," + mvvV6 + "," + mvvV7 + "," + mvvV8 + "," + mvvV9 + "," + mvvV10 + "," +mvvV11+ "," + mvvV12 + "," + mvvV13+ "," + mvvV14 + "," + mvvV15 + "," + mvvV16 + "," + realWeight + "," +orgWeight + "," + item.getLocation() + "\n";
                    outputStream1.write(ss.getBytes());
                }

                outputStream1.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

    private static float getMvvValue(String ad, String zero) {
        float mAD = Float.parseFloat(ad);
        float mzero = Float.parseFloat(zero);
        float x = (mAD - mzero) / 5;
        java.text.DecimalFormat myformat = new java.text.DecimalFormat("0.000");
        String str = myformat.format(x);
        float mvvValue = Float.parseFloat(str);
        return mvvValue;
    }



    //邮箱发送
    public static void sendXX(Activity context, String name,String loc) {
        //多个附件文件上传
        try{
            String dirPath = filepath + "/" + "tech3_csv" + "/";
            File mZipFile = new File(dirPath);
            File[] files = mZipFile.listFiles();
            if (files != null) {
                ArrayList<Uri> uris = new ArrayList();
                for (int i = 0; i < files.length; i++) {
                    File file = files[i];
                    String newpath= file.getAbsolutePath().replace(".csv",loc+".csv");
                    copyFile(file.getAbsolutePath(),newpath);//复制一份文件
                    DLog.e("SaveMVVData", "发送附件地址=" + file.getAbsolutePath());
                    Uri uriForFile = FileProvider.getUriForFile(context, context.getResources().getString(R.string.authorities),new File(newpath));
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
                    intent.putExtra(Intent.EXTRA_TEXT, "请输入车牌号码:");//多附件发送执行这句会有string不能转化为list的警告；
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

    /**
     * 复制单个文件
     * @param oldPath String 原文件路径 如：c:/fqf.txt
     * @param newPath String 复制后路径 如：f:/fqf.txt
     * @return boolean
     */
    public static void copyFile(String oldPath, String newPath) {
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) { //文件存在时
                InputStream inStream = new FileInputStream(oldPath); //读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                int length;
                while ( (byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; //字节数 文件大小
                    System.out.println(bytesum);
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
                oldfile.delete();//删除原文件
            }
        }
        catch (Exception e) {
            System.out.println("复制单个文件操作出错");
            e.printStackTrace();

        }

    }
}


