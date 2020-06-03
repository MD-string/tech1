package cn.hand.tech.mail;

import android.app.Activity;
import android.net.Uri;

import java.io.File;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import cn.hand.tech.BApplication;
import cn.hand.tech.R;
import cn.hand.tech.log.DLog;
import cn.hand.tech.utils.FileUtils;
import cn.hand.tech.utils.ZipUtil;

/**
 * Created by Administrator on 2017/4/10.
 * * 当使用第三方登录邮箱时需要有邮箱的授权码，且要开启POP3/SMTP/IMAP:服务
 */

public class SendMailUtil {

    public static String APP_PATH = "hande/main";
    public  static String filepath= BApplication.mContext.getCacheDir().getPath() + "/" + APP_PATH;

    //需要该邮箱打开权限 才能发邮件（去该邮箱设置里面去开启POP3/IMAP功能）
    //qq
    private static final String HOST = "smtp.qq.com";
    private static final String PORT = "587";
    private static final String FROM_ADD = "365049173@qq.com";
    private static final String FROM_PSW = "yniqafxvwrtbbija";//许多邮箱使用smtp服务登录第三方软件需要使用授权码而不是密码

    //    private static final String[] TO_MORE= {"pengjunjun@hand-hitech.com", "liuyang@hand-hitech.com"};

    private static final String[] TO_MORE= {"huxinzhao@hand-hitech.com", "2942701982@qq.com"};
    //    //163
    //    private static final String HOST = "smtp.163.com";
    //    private static final String PORT = "465"; //或者465  994
    //    private static final String FROM_ADD = "teprinciple@163.com";
    //    private static final String FROM_PSW = "teprinciple163";
    ////    private static final String TO_ADD = "2584770373@qq.com";


    public static void send(final File file,String toAdd){
        final MailInfo mailInfo = creatMail(toAdd);
        final MailSender sms = new MailSender();
        new Thread(new Runnable() {
            @Override
            public void run() {
                //                sms.sendFileMail(mailInfo,file);
            }
        }).start();
    }

    public static void sendMore(final String[] filename,String subjcet,String content,String[] sender){
        final MailInfo mailInfo = creatMail(subjcet,content,sender);
        final MailSender sms = new MailSender();
        new Thread(new Runnable() {
            @Override
            public void run() {
                sms.sendFileMail(mailInfo,filename);
            }
        }).start();
    }

    public static void send(String toAdd){
        final MailInfo mailInfo = creatMail(toAdd);
        final MailSender sms = new MailSender();
        new Thread(new Runnable() {
            @Override
            public void run() {
                sms.sendTextMail(mailInfo);
            }
        }).start();
    }

    @NonNull
    private static MailInfo creatMail(String toAdd) {
        final MailInfo mailInfo = new MailInfo();
        mailInfo.setMailServerHost(HOST);
        mailInfo.setMailServerPort(PORT);
        mailInfo.setValidate(true);
        mailInfo.setUserName(FROM_ADD); // 你的邮箱地址
        mailInfo.setPassword(FROM_PSW);// 您的邮箱密码
        mailInfo.setFromAddress(FROM_ADD); // 发送的邮箱
        mailInfo.setToAddress(toAdd); // 发到哪个邮件去
        mailInfo.setSubject("Hello"); // 邮件主题
        mailInfo.setContent("Android 测试"); // 邮件文本
        return mailInfo;
    }

    @NonNull
    private static MailInfo creatMail(String subjcet, String content,String[] sender) {
        final MailInfo mailInfo = new MailInfo();
        mailInfo.setMailServerHost(HOST);
        mailInfo.setMailServerPort(PORT);
        mailInfo.setValidate(true);
        mailInfo.setUserName(FROM_ADD); // 你的邮箱地址
        mailInfo.setPassword(FROM_PSW);// 您的邮箱密码
        mailInfo.setFromAddress(FROM_ADD); // 发送的邮箱
        //        mailInfo.setToAddress(toAdd); // 发到哪个邮件去
        mailInfo.setFromMore(sender);
        mailInfo.setSubject("标定数据-" +subjcet); // 邮件主题
        mailInfo.setContent(content); // 邮件文本
        return mailInfo;
    }

    //发送多附件
    public static void sendFileMail(Activity context,String name, String content, String[] sender, String loc) {
        //多个附件文件上传
        try{
            String dirPath = filepath + "/" + "pic_csv" + "/";
            File mZipFile = new File(dirPath);
            File[] files = mZipFile.listFiles();
            if (files != null) {
                int len = files.length;
                ArrayList<Uri> uris = new ArrayList();
                for (int i = 0; i < len; i++) {
                    File file = files[i];
                    if(file.getName().contains("csv")){
                        String newpath= file.getAbsolutePath().replace(".csv",loc+".csv");
                        FileUtils.copyFile(file.getAbsolutePath(),newpath);//复制一份文件
                        Uri uriForFile = FileProvider.getUriForFile(context, context.getResources().getString(R.string.authorities),new File(newpath));
                        uris.add(uriForFile);
                    }
                    DLog.e("SaveMVVData", "发送附件地址=" + file.getAbsolutePath());
                }
                String filepaht = filepath + "/" + "zip" + "/";
                File mZipFile1 = new File(filepaht);
                if (!mZipFile1.exists()) {
                    mZipFile1.mkdirs();
                }
                String zipPath=filepaht+name+".zip";

                ZipUtil.zip(dirPath,zipPath);//压缩成zip

                File zip = new File(zipPath);
                if (zip.isFile()){
                    String[] filenames = new String[1];
                    filenames[0]=zipPath+","+zip.getName();
                    SendMailUtil.sendMore(filenames, name, content,sender);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            DLog.d("SaveMVVData","发送邮件异常");
        }

    }
}
