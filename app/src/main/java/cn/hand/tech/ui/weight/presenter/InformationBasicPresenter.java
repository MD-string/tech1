package cn.hand.tech.ui.weight.presenter;

import android.content.Context;
import android.graphics.Bitmap;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.hand.tech.common.ACache;
import cn.hand.tech.common.UrlConstant;
import cn.hand.tech.log.DLog;
import cn.hand.tech.net.HttpHandler;
import cn.hand.tech.net.NetCallBack;
import cn.hand.tech.ui.weight.IInformationBasicView;
import cn.hand.tech.ui.weight.bean.InstallerInfo;
import cn.hand.tech.utils.FilePathUtils;
import cn.hand.tech.utils.ImageUtil;
import cn.hand.tech.utils.Tools;

/*
 * 动态  数据处理
 */
public class InformationBasicPresenter {
    private final ACache acache;
    private Context mContext;
    private IInformationBasicView mView;

    public InformationBasicPresenter(Context context, IInformationBasicView view) {
        mContext = context;
        mView = view;
        acache= ACache.get(context,"WeightFragment");
    }


    /**
     *获取安装人员信息
     */
    public void getInformationBasic(String token,String id) {
        try {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("deviceId",id);
            map.put("token",token);

            HttpHandler.getInstance().postName(null, UrlConstant.HttpUrl.GET_INSTALL, map, new NetCallBack() {
                @Override
                public void onSuccess(String result, String executeStatus) {//
                    JSONObject jsonObject = null;
                    Gson gson = new Gson();
                    try {
                        //                        jsonObject = new JSONObject(result.toString());
                        //                        String message = jsonObject.getString("result");
                        if(!Tools.isEmpty(result) && result.length()> 0){
                            List<InstallerInfo> depts = gson.fromJson(result, new TypeToken<List<InstallerInfo>>(){}.getType());
                            mView.doSuccess(depts);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                @Override
                public void onException(String errorCode, String errorMsg) {
                    mView.doError("获取安装人员信息失败");

                }
                @Override
                public void onError(Exception e) {
                    mView.doError("获取安装人员信息失败");
                }
            });

        } catch (Exception e1) {
            mView.doError("获取安装人员信息失败");
            e1.printStackTrace();
        }
    }


    /**
     *上传安装人员信息
     */
    public void sendInstallInformation(HashMap<String, String> mapParams) {
        try {

            HttpHandler.getInstance().postBle(null, UrlConstant.HttpUrl.SEND_INSTALLER, mapParams, new NetCallBack() {
                @Override
                public void onSuccess(String result, String executeStatus) {//
                    Gson gson = new Gson();
                    try {
                        mView.sendSuccess(result);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                @Override
                public void onException(String errorCode, String errorMsg) {
                    mView.doError(errorMsg);

                }
                @Override
                public void onError(Exception e) {
                    mView.doError("上传安装人员信息失败");
                }
            });

        } catch (Exception e1) {
            mView.doError("上传安装人员信息失败");
            e1.printStackTrace();
        }
    }

//    /**
//     *判断车辆是否在线
//     */
//    public void checkCarOnline(HashMap<String, String> mapParams) {
//
//        try {
//            HttpHandler.getInstance().postBle(null, UrlConstant.getInstance(mContext).IS_ONLINE, mapParams, new NetCallBack() {
//                @Override
//                public void onSuccess(String result, String executeStatus) {//
//                    JSONObject jsonObject = null;
//                    String runStatus="0";
//                    try {
//                        if(!Tools.isEmpty(result) ){
//                            jsonObject = new JSONObject(result.toString());
//                            runStatus = jsonObject.getString("runStatus");
//                        }else{
//                            runStatus="0";
//                        }
//                        mView.onlineSuccess(runStatus);
//                    } catch (Exception e) {
//                        mView.onlineSuccess(runStatus);
//                        //                        e.printStackTrace();
//                    }
//
//                }
//                @Override
//                public void onException(String errorCode, String errorMsg) {
//                    mView.onlineFail("获取车辆在线状况信息失败");
//
//                }
//                @Override
//                public void onError(Exception e) {
//                    mView.onlineFail("获取车辆在线状况信息失败");
//                }
//            });
//
//        } catch (Exception e1) {
//            mView.onlineFail("获取车辆在线状况信息失败");
//            e1.printStackTrace();
//        }
//    }
    /**
     * 上传 人物头像
     */
    public void sendPersonalHeader(final String  path,String deviceId,String carNumber,String token,String companyId) {
        try {
//            Bitmap bitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), Uri.parse(path));

            Bitmap bitmap = ImageUtil.getImage(path,480,800); //此时返回 bm 为空
            // 计算图片缩放比例

            // 首先保存图片
            String dirPath = FilePathUtils.getRecvFilePath();
            File dirFile = new File(dirPath);
            if (!dirFile.exists()) {
                dirFile.mkdirs();
            }
            String fileName = System.currentTimeMillis() + ".jpg";
            final File file = new File(dirPath,fileName);
            if(!file.exists()){
                file.createNewFile();
            }

//            copyFile(path,file.getAbsolutePath());

            saveBitmap(bitmap,dirPath,fileName);

            Map<String, String> map = new HashMap<String, String>();
            map.put("file", file.getAbsolutePath());
            map.put("token", token);
            map.put("type", "2");
            map.put("isImage", "1");
            map.put("deviceId", deviceId);
            map.put("carNumber", carNumber);
            map.put("companyId", companyId);

            HttpHandler.getInstance().postMap(null,UrlConstant.HttpUrl.ENTER_CAR,map,new NetCallBack() {

                @Override
                public void onSuccess(String result, String executeStatus) {
                    DLog.d("PrivatePhotoGridActivity", "添加图片成功");
                    mView.loadSuccess(result);

                }
                @Override
                public void onException(String errorCode, String errorMsg) {
                    DLog.d("PrivatePhotoGridActivity", "添加图片失败"+errorMsg);
                    mView.loadFail("添加图片失败");
                }
                @Override
                public void onError(Exception e) {
                    DLog.d("PrivatePhotoGridActivity", "添加图片失败"+e.getMessage());
                    mView.loadFail("添加图片失败");
                }
            });
        } catch (Exception e1) {
            DLog.d("PrivatePhotoGridActivity", "添加图片失败");
            mView.loadFail("添加图片失败");
            e1.printStackTrace();
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
            long size=oldfile.length();
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
            DLog.d("infor","复制图片成功");
        }
        catch (Exception e) {
            System.out.println("复制单个文件操作出错");
            e.printStackTrace();

        }

    }

   // android中保存Bitmap图片到指定文件夹中的方法。

    /** 保存方法 */
    public void saveBitmap(Bitmap bm,String path,String name) {
        File f = new File(path, name);
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
