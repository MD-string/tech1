package cn.hand.tech.ble;

import android.content.Intent;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;

import cn.hand.tech.BApplication;
import cn.hand.tech.bean.HDModelData;
import cn.hand.tech.bean.HDSendDataModel;
import cn.hand.tech.ble.bean.GPRSbean;
import cn.hand.tech.ble.bean.GPSTimebean;
import cn.hand.tech.ble.bean.GPSbean;
import cn.hand.tech.ble.bleUtil.BleConstant;
import cn.hand.tech.common.ACache;
import cn.hand.tech.log.DLog;
import cn.hand.tech.utils.Tools;


/*---------------------------------- 蓝牙返回数据处理 -----------------------------*/
public class HandlerBleData {

    private static final String TAG = "BluetoothLeService";
    private  static int nDataCount, nLengthPerPackage;
    public static final int BUFFERCOUNT = 4096;
    private static int[] pBuffer = new int[4096];
    private static int nBufferTail = 0;
    private static int nBufferHead = 0;
    public static int[] nABuffer = new int[1542];
    private static boolean isWeightConfiguration = false;
    /**
     * 处理设备返回的数据  放入数组int[] pBuffer
     *
     * @param
     */

    public static Boolean addToBuffer(int nValue) {
        int nTail = 0;
        nTail = (nBufferTail + 1);
        if (nTail == BUFFERCOUNT) {
            nTail = 0;
        }
        if (nTail == nBufferHead) {
            return false;
        }
        pBuffer[nBufferTail] = nValue;
        nBufferTail = nTail;
        return true;
    }


    /**
     * 处理设备返回的数据
     *
     */
    public static void analyzeData() {
        int tmpNum = 0;
        if (nBufferHead == nBufferTail) {
            return;
        }
        if (getBufferSize() > 8) {
            //解码开始
            tmpNum = nBufferHead + 1;
            if (tmpNum == BUFFERCOUNT) {
                tmpNum = 0;
            }
            //判断包头，不是包头全部抛弃，直到前两个元素是包头

            while ((pBuffer[nBufferHead] != 0x7B) || (pBuffer[tmpNum] != 0xA5)) {
                //System.out.println(pBuffer[nBufferHead]);
                //System.out.println(pBuffer[tmpNum]);
                addBufferHead(); //因为不是，抛弃该点
                tmpNum = nBufferHead + 1;
                if (tmpNum == BUFFERCOUNT) {
                    tmpNum = 0;
                }
                if (nBufferHead == nBufferTail) { //每一次都判断下加完后是不是队列为空
                    DLog.e(TAG,"无数据NO data" + getBufferSize() + "");
                    return;
                }
            }
            nDataCount = ((nBufferHead + 7) < BUFFERCOUNT) ? (pBuffer[nBufferHead + 7]) : (pBuffer[nBufferHead + 7 - BUFFERCOUNT]);
            //LogUtil.e("============长度--"+nDataCount+"");
            //6个byte位一组  nDataCount*6
            if(nDataCount >255){
                DLog.e(TAG,"数据长度错误：" +nDataCount + "");
            }else {
                //LogUtil.e("============长度--"+nDataCount+"");
                //6个byte位一组  nDataCount*6
                nLengthPerPackage = 12 + nDataCount * 6;
                if (getBufferSize() < nLengthPerPackage) {
                    // LogUtil.e("数据不够");
                    return;
                } else {
                    unpackData();
                }
            }

        }

    }

    public static void unpackData() {
        //解码
        //LogUtil.e("------开始解码------");
        //1、把数据转移到一个新对列，然后出队
        for (int i = 0; i < nLengthPerPackage; i++) {
            nABuffer[i] = pBuffer[nBufferHead];
            //保存数据
            addBufferHead();
        }
        final HDModelData uMode = new HDModelData();
        HDSendDataModel sMode = new HDSendDataModel();
        GPSbean gpsbean=new GPSbean();
        GPRSbean rsBean=new GPRSbean();
        GPSTimebean timbean=new GPSTimebean();
        uMode.nID = (nABuffer[5] << 24) | (nABuffer[4] << 16) | (nABuffer[3] << 8) | (nABuffer[2]);
        // LogUtil.e("uMode.nID ===" + uMode.nID);
        boolean bZeroAction = false;
        boolean bSetPara = false;
        for (int i = 0; i < nDataCount; i++) {
            int low = nABuffer[8 + 6 * i]; //低字节在前     命令码
            int high = nABuffer[8 + 6 * i + 1] & 0x3F; //低字节在前
            int nOrder = (high << 8) | low;
            //NSLog(@"order = %d",nOrder);
            byte w1[] = new byte[4];
            w1[0] = (byte) nABuffer[8 + 6 * i + 2];
            w1[1] = (byte) nABuffer[8 + 6 * i + 3];
            w1[2] = (byte) nABuffer[8 + 6 * i + 4];
            w1[3] = (byte) nABuffer[8 + 6 * i + 5];


            float ww = ArryToFloat(w1, 0);
            //NSLog(@"nOrder:%d   --- ww = %f",nOrder,ww);
            switch (nOrder) {
                case BleConstant.HD_AD_SAMPLING_1:
                    uMode.ad1 = ww;
                    break;
                case BleConstant.HD_AD_SAMPLING_2:
                    uMode.ad2 = ww;
                    break;
                case BleConstant.HD_AD_SAMPLING_3:
                    uMode.ad3 = ww;
                    break;
                case BleConstant.HD_AD_SAMPLING_4:
                    uMode.ad4 = ww;
                    break;
                case BleConstant.HD_AD_SAMPLING_5:
                    uMode.ad5 = ww;
                    break;
                case BleConstant.HD_AD_SAMPLING_6:
                    uMode.ad6 = ww;
                    break;
                case BleConstant.HD_AD_SAMPLING_7:
                    uMode.ad7 = ww;
                    break;
                case BleConstant.HD_AD_SAMPLING_8:
                    uMode.ad8 = ww;
                    break;
                case BleConstant.HD_AD_SAMPLING_9:
                    uMode.ad9 = ww;
                    break;
                case BleConstant.HD_AD_SAMPLING_10:
                    uMode.ad10 = ww;
                    break;
                case BleConstant.HD_AD_SAMPLING_11:
                    uMode.ad11 = ww;
                    break;
                case BleConstant.HD_AD_SAMPLING_12:
                    uMode.ad12 = ww;
                    break;
                case BleConstant.HD_AD_SAMPLING_13:
                    uMode.ad13 = ww;
                    break;
                case BleConstant.HD_AD_SAMPLING_14:
                    uMode.ad14 = ww;
                    break;
                case BleConstant.HD_AD_SAMPLING_15:
                    uMode.ad15 = ww;
                    break;
                case BleConstant.HD_AD_SAMPLING_16:
                    uMode.ad16 = ww;
                    break;
                case BleConstant.HD_AD_SAMPLING_17:
                    uMode.ad17 = ww;
                    break;
                case BleConstant.HD_AD_SAMPLING_18:
                    uMode.ad18 = ww;
                    break;
                case BleConstant.HD_AD_SAMPLING_19:
                    uMode.ad19 = ww;
                    break;
                case BleConstant.HD_AD_SAMPLING_20:
                    uMode.ad20 = ww;
                    break;
                case BleConstant.HD_AD_SAMPLING_21:
                    uMode.ad21 = ww;
                    break;
                case BleConstant.HD_AD_SAMPLING_22:
                    uMode.ad22 = ww;
                    break;
                case BleConstant.HD_AD_SAMPLING_23:
                    uMode.ad23 = ww;
                    break;
                case BleConstant.HD_AD_SAMPLING_24:
                    uMode.ad24 = ww;
                    break;
                case BleConstant.HD_AD_SAMPLING_25:
                    uMode.ad25 = ww;
                    break;
                case BleConstant.HD_AD_SAMPLING_26:
                    uMode.ad26 = ww;
                    break;
                case BleConstant.HD_AD_SAMPLING_27:
                    uMode.ad27 = ww;
                    break;
                case BleConstant.HD_AD_SAMPLING_28:
                    uMode.ad28 = ww;
                    break;
                case BleConstant.HD_AD_SAMPLING_29:
                    uMode.ad29 = ww;
                    break;
                case BleConstant.HD_AD_SAMPLING_30:
                    uMode.ad30 = ww;
                    break;
                case BleConstant.HD_AD_SAMPLING_31:
                    uMode.ad31 = ww;
                    break;
                case BleConstant.HD_AD_SAMPLING_32:
                    uMode.ad32 = ww;
                    break;
                case BleConstant.HD_AD_SAMPLING_33:
                    uMode.ad33 = ww;
                    break;
                case BleConstant.HD_AD_SAMPLING_34:
                    uMode.ad34 = ww;
                    break;
                case BleConstant.HD_AD_SAMPLING_35:
                    uMode.ad35 = ww;
                    break;
                case BleConstant.HD_AD_SAMPLING_36:
                    uMode.ad36 = ww;
                    break;
                case BleConstant.HD_AD_SAMPLING_37:
                    uMode.ad37 = ww;
                    break;
                case BleConstant.HD_AD_SAMPLING_38:
                    uMode.ad38 = ww;
                    break;
                case BleConstant.HD_AD_SAMPLING_39:
                    uMode.ad39 = ww;
                    break;
                case BleConstant.HD_AD_SAMPLING_40:
                    uMode.ad40 = ww;
                    break;
                case BleConstant.HD_AD_SAMPLING_41:
                    uMode.ad41 = ww;
                    break;
                case BleConstant.HD_AD_SAMPLING_42:
                    uMode.ad42 = ww;
                    break;
                case BleConstant.HD_AD_SAMPLING_43:
                    uMode.ad43 = ww;
                    break;
                case BleConstant.HD_AD_SAMPLING_44:
                    uMode.ad44 = ww;
                    break;
                case BleConstant.HD_AD_SAMPLING_45:
                    uMode.ad45 = ww;
                    break;
                case BleConstant.HD_AD_SAMPLING_46:
                    uMode.ad46 = ww;
                    break;
                case BleConstant.HD_AD_SAMPLING_47:
                    uMode.ad47 = ww;
                    break;
                case BleConstant.HD_AD_SAMPLING_48:
                    uMode.ad48 = ww;
                    break;
                case BleConstant.HD_AD_SAMPLING_49:
                    uMode.ad49 = ww;
                    break;
                case BleConstant.HD_AD_SAMPLING_50:
                    uMode.ad50 = ww;
                    break;
                case BleConstant.HD_AD_SAMPLING_51:
                    uMode.ad51 = ww;
                    break;
                case BleConstant.HD_AD_SAMPLING_52:
                    uMode.ad52 = ww;
                    break;
                case BleConstant.HD_AD_SAMPLING_53:
                    uMode.ad53 = ww;
                    break;
                case BleConstant.HD_AD_SAMPLING_54:
                    uMode.ad54 = ww;
                    break;
                case BleConstant.HD_AD_SAMPLING_55:
                    uMode.ad55 = ww;
                    break;
                case BleConstant.HD_AD_SAMPLING_56:
                    uMode.ad56 = ww;
                    break;
                case BleConstant.HD_AD_SAMPLING_57:
                    uMode.ad57 = ww;
                    break;
                case BleConstant.HD_AD_SAMPLING_58:
                    uMode.ad58 = ww;
                    break;
                case BleConstant.HD_AD_SAMPLING_59:
                    uMode.ad59 = ww;
                    break;
                case BleConstant.HD_AD_SAMPLING_60:
                    uMode.ad60 = ww;
                    break;
                case BleConstant.HD_AD_SAMPLING_61:
                    uMode.ad61 = ww;
                    break;
                case BleConstant.HD_AD_SAMPLING_62:
                    uMode.ad62 = ww;
                    break;
                case BleConstant.HD_AD_SAMPLING_63:
                    uMode.ad63 = ww;
                    break;
                case BleConstant.HD_AD_SAMPLING_64:
                    uMode.ad64 = ww;
                    break;

                case BleConstant.HD_AD_ZERO_1:
                    uMode.adZero1 = ww;
                    break;
                case BleConstant.HD_AD_ZERO_2:
                    uMode.adZero2 = ww;
                    break;
                case BleConstant.HD_AD_ZERO_3:
                    uMode.adZero3 = ww;
                    break;
                case BleConstant.HD_AD_ZERO_4:
                    uMode.adZero4 = ww;
                    break;
                case BleConstant.HD_AD_ZERO_5:
                    uMode.adZero5 = ww;
                    break;
                case BleConstant.HD_AD_ZERO_6:
                    uMode.adZero6 = ww;
                    break;
                case BleConstant.HD_AD_ZERO_7:
                    uMode.adZero7 = ww;
                    break;
                case BleConstant.HD_AD_ZERO_8:
                    uMode.adZero8 = ww;
                    break;
                case BleConstant.HD_AD_ZERO_9:
                    uMode.adZero9 = ww;
                    break;
                case BleConstant.HD_AD_ZERO_10:
                    uMode.adZero10 = ww;
                    break;
                case BleConstant.HD_AD_ZERO_11:
                    uMode.adZero11 = ww;
                    break;
                case BleConstant.HD_AD_ZERO_12:
                    uMode.adZero12 = ww;
                    break;
                case BleConstant.HD_AD_ZERO_13:
                    uMode.adZero13 = ww;
                    break;
                case BleConstant.HD_AD_ZERO_14:
                    uMode.adZero14 = ww;
                    break;
                case BleConstant.HD_AD_ZERO_15:
                    uMode.adZero15 = ww;
                    break;
                case BleConstant.HD_AD_ZERO_16:
                    uMode.adZero16 = ww;
                    break;


                case BleConstant.HD_AD_ZERO_17:
                    uMode.adZero17 = ww;
                    break;
                case BleConstant.HD_AD_ZERO_18:
                    uMode.adZero18 = ww;
                    break;
                case BleConstant.HD_AD_ZERO_19:
                    uMode.adZero19 = ww;
                    break;
                case BleConstant.HD_AD_ZERO_20:
                    uMode.adZero20 = ww;
                    break;
                case BleConstant.HD_AD_ZERO_21:
                    uMode.adZero21 = ww;
                    break;
                case BleConstant.HD_AD_ZERO_22:
                    uMode.adZero22 = ww;
                    break;
                case BleConstant.HD_AD_ZERO_23:
                    uMode.adZero23 = ww;
                    break;
                case BleConstant.HD_AD_ZERO_24:
                    uMode.adZero24 = ww;
                    break;
                case BleConstant.HD_AD_ZERO_25:
                    uMode.adZero25 = ww;
                    break;
                case BleConstant.HD_AD_ZERO_26:
                    uMode.adZero26 = ww;
                    break;
                case BleConstant.HD_AD_ZERO_27:
                    uMode.adZero27 = ww;
                    break;
                case BleConstant.HD_AD_ZERO_28:
                    uMode.adZero28 = ww;
                    break;
                case BleConstant.HD_AD_ZERO_29:
                    uMode.adZero29 = ww;
                    break;
                case BleConstant.HD_AD_ZERO_30:
                    uMode.adZero30 = ww;
                    break;
                case BleConstant.HD_AD_ZERO_31:
                    uMode.adZero31 = ww;
                    break;
                case BleConstant.HD_AD_ZERO_32:
                    uMode.adZero32 = ww;
                    break;
                case BleConstant.HD_AD_ZERO_33:
                    uMode.adZero33 = ww;
                    break;
                case BleConstant.HD_AD_ZERO_34:
                    uMode.adZero34 = ww;
                    break;
                case BleConstant.HD_AD_ZERO_35:
                    uMode.adZero35 = ww;
                    break;
                case BleConstant.HD_AD_ZERO_36:
                    uMode.adZero36 = ww;
                    break;
                case BleConstant.HD_AD_ZERO_37:
                    uMode.adZero37 = ww;
                    break;
                case BleConstant.HD_AD_ZERO_38:
                    uMode.adZero38 = ww;
                    break;
                case BleConstant.HD_AD_ZERO_39:
                    uMode.adZero39 = ww;
                    break;
                case BleConstant.HD_AD_ZERO_40:
                    uMode.adZero40 = ww;
                    break;
                case BleConstant.HD_AD_ZERO_41:
                    uMode.adZero41 = ww;
                    break;
                case BleConstant.HD_AD_ZERO_42:
                    uMode.adZero42 = ww;
                    break;
                case BleConstant.HD_AD_ZERO_43:
                    uMode.adZero43 = ww;
                    break;
                case BleConstant.HD_AD_ZERO_44:
                    uMode.adZero44 = ww;
                    break;
                case BleConstant.HD_AD_ZERO_45:
                    uMode.adZero45 = ww;
                    break;
                case BleConstant.HD_AD_ZERO_46:
                    uMode.adZero46 = ww;
                    break;
                case BleConstant.HD_AD_ZERO_47:
                    uMode.adZero47 = ww;
                    break;
                case BleConstant.HD_AD_ZERO_48:
                    uMode.adZero48 = ww;
                    break;
                case BleConstant.HD_AD_ZERO_49:
                    uMode.adZero49 = ww;
                    break;
                case BleConstant.HD_AD_ZERO_50:
                    uMode.adZero50 = ww;
                    break;
                case BleConstant.HD_AD_ZERO_51:
                    uMode.adZero51 = ww;
                    break;
                case BleConstant.HD_AD_ZERO_52:
                    uMode.adZero52 = ww;
                    break;
                case BleConstant.HD_AD_ZERO_53:
                    uMode.adZero53 = ww;
                    break;
                case BleConstant.HD_AD_ZERO_54:
                    uMode.adZero54 = ww;
                    break;
                case BleConstant.HD_AD_ZERO_55:
                    uMode.adZero55 = ww;
                    break;
                case BleConstant.HD_AD_ZERO_56:
                    uMode.adZero56 = ww;
                    break;
                case BleConstant.HD_AD_ZERO_57:
                    uMode.adZero57 = ww;
                    break;
                case BleConstant.HD_AD_ZERO_58:
                    uMode.adZero58 = ww;
                    break;
                case BleConstant.HD_AD_ZERO_59:
                    uMode.adZero59 = ww;
                    break;
                case BleConstant.HD_AD_ZERO_60:
                    uMode.adZero60 = ww;
                    break;
                case BleConstant.HD_AD_ZERO_61:
                    uMode.adZero61 = ww;
                    break;
                case BleConstant.HD_AD_ZERO_62:
                    uMode.adZero62 = ww;
                    break;
                case BleConstant.HD_AD_ZERO_63:
                    uMode.adZero63 = ww;
                    break;
                case BleConstant.HD_AD_ZERO_64:
                    uMode.adZero64 = ww;
                    break;

                case BleConstant.HD_AD_PARA_1:
                    sMode.mmv1 = ww;
                    bSetPara = true;
                    break;
                case BleConstant.HD_AD_PARA_2:
                    sMode.mmv2 = ww;
                    bSetPara = true;
                    break;
                case BleConstant.HD_AD_PARA_3:
                    sMode.mmv3 = ww;
                    bSetPara = true;
                    break;
                case BleConstant.HD_AD_PARA_4:
                    sMode.mmv4 = ww;
                    bSetPara = true;
                    break;
                case BleConstant.HD_AD_PARA_5:
                    sMode.mmv5 = ww;
                    bSetPara = true;
                    break;
                case BleConstant.HD_AD_PARA_6:
                    sMode.mmv6 = ww;
                    bSetPara = true;
                    break;
                case BleConstant.HD_AD_PARA_7:
                    sMode.mmv7 = ww;
                    bSetPara = true;
                    break;
                case BleConstant.HD_AD_PARA_8:
                    sMode.mmv8 = ww;
                    bSetPara = true;
                    break;
                case BleConstant.HD_AD_PARA_9:
                    sMode.mmv9 = ww;
                    bSetPara = true;
                    break;
                case BleConstant.HD_AD_PARA_10:
                    sMode.mmv10 = ww;
                    bSetPara = true;
                    break;
                case BleConstant.HD_AD_PARA_11:
                    sMode.mmv11 = ww;
                    bSetPara = true;
                    break;
                case BleConstant.HD_AD_PARA_12:
                    sMode.mmv12 = ww;
                    bSetPara = true;
                    break;
                case BleConstant.HD_AD_PARA_13:
                    sMode.mmv13 = ww;
                    bSetPara = true;
                    break;
                case BleConstant.HD_AD_PARA_14:
                    sMode.mmv14 = ww;
                    bSetPara = true;
                    break;
                case BleConstant.HD_AD_PARA_15:
                    sMode.mmv15 = ww;
                    bSetPara = true;
                    break;
                case BleConstant.HD_AD_PARA_16:
                    sMode.mmv16 = ww;
                    bSetPara = true;
                    break;
                case BleConstant.HD_AD_PARA_17:
                    sMode.mmv17 = ww;
                    bSetPara = true;
                    break;
                case BleConstant.HD_AD_PARA_18:
                    sMode.mmv18 = ww;
                    bSetPara = true;
                    break;
                case BleConstant.HD_AD_PARA_19:
                    sMode.mmv19 = ww;
                    bSetPara = true;
                    break;
                case BleConstant.HD_AD_PARA_20:
                    sMode.mmv20 = ww;
                    bSetPara = true;
                    break;
                case BleConstant.HD_AD_PARA_21:
                    sMode.mmv21 = ww;
                    bSetPara = true;
                    break;
                case BleConstant.HD_AD_PARA_22:
                    sMode.mmv22 = ww;
                    bSetPara = true;
                    break;
                case BleConstant.HD_AD_PARA_23:
                    sMode.mmv23 = ww;
                    bSetPara = true;
                    break;
                case BleConstant.HD_AD_PARA_24:
                    sMode.mmv24 = ww;
                    bSetPara = true;
                    break;
                case BleConstant.HD_AD_PARA_25:
                    sMode.mmv25 = ww;
                    bSetPara = true;
                    break;
                case BleConstant.HD_AD_PARA_26:
                    sMode.mmv26 = ww;
                    bSetPara = true;
                    break;
                case BleConstant.HD_AD_PARA_27:
                    sMode.mmv27 = ww;
                    bSetPara = true;
                    break;
                case BleConstant.HD_AD_PARA_28:
                    sMode.mmv28 = ww;
                    bSetPara = true;
                    break;
                case BleConstant.HD_AD_PARA_29:
                    sMode.mmv29 = ww;
                    bSetPara = true;
                    break;
                case BleConstant.HD_AD_PARA_30:
                    sMode.mmv30 = ww;
                    bSetPara = true;
                    break;
                case BleConstant.HD_AD_PARA_31:
                    sMode.mmv31 = ww;
                    bSetPara = true;
                    break;
                case BleConstant.HD_AD_PARA_32:
                    sMode.mmv32 = ww;
                    bSetPara = true;
                    break;
                case BleConstant.HD_AD_PARA_33:
                    sMode.mmv33 = ww;
                    bSetPara = true;
                    break;
                case BleConstant.HD_AD_PARA_34:
                    sMode.mmv34 = ww;
                    bSetPara = true;
                    break;
                case BleConstant.HD_AD_PARA_35:
                    sMode.mmv35 = ww;
                    bSetPara = true;
                    break;
                case BleConstant.HD_AD_PARA_36:
                    sMode.mmv36 = ww;
                    bSetPara = true;
                    break;
                case BleConstant.HD_AD_PARA_37:
                    sMode.mmv37 = ww;
                    bSetPara = true;
                    break;
                case BleConstant.HD_AD_PARA_38:
                    sMode.mmv38 = ww;
                    bSetPara = true;
                    break;
                case BleConstant.HD_AD_PARA_39:
                    sMode.mmv39 = ww;
                    bSetPara = true;
                    break;
                case BleConstant.HD_AD_PARA_40:
                    sMode.mmv40 = ww;
                    bSetPara = true;
                    break;
                case BleConstant.HD_AD_PARA_41:
                    sMode.mmv41 = ww;
                    bSetPara = true;
                    break;
                case BleConstant.HD_AD_PARA_42:
                    sMode.mmv42 = ww;
                    bSetPara = true;
                    break;
                case BleConstant.HD_AD_PARA_43:
                    sMode.mmv43 = ww;
                    bSetPara = true;
                    break;
                case BleConstant.HD_AD_PARA_44:
                    sMode.mmv44 = ww;
                    bSetPara = true;
                    break;
                case BleConstant.HD_AD_PARA_45:
                    sMode.mmv45 = ww;
                    bSetPara = true;
                    break;
                case BleConstant.HD_AD_PARA_46:
                    sMode.mmv46 = ww;
                    bSetPara = true;
                    break;
                case BleConstant.HD_AD_PARA_47:
                    sMode.mmv47 = ww;
                    bSetPara = true;
                    break;
                case BleConstant.HD_AD_PARA_48:
                    sMode.mmv48 = ww;
                    bSetPara = true;
                    break;
                case BleConstant.HD_AD_PARA_49:
                    sMode.mmv49 = ww;
                    bSetPara = true;
                    break;
                case BleConstant.HD_AD_PARA_50:
                    sMode.mmv50 = ww;
                    bSetPara = true;
                    break;
                case BleConstant.HD_AD_PARA_51:
                    sMode.mmv51 = ww;
                    bSetPara = true;
                    break;
                case BleConstant.HD_AD_PARA_52:
                    sMode.mmv52 = ww;
                    bSetPara = true;
                    break;
                case BleConstant.HD_AD_PARA_53:
                    sMode.mmv53 = ww;
                    bSetPara = true;
                    break;
                case BleConstant.HD_AD_PARA_54:
                    sMode.mmv54 = ww;
                    bSetPara = true;
                    break;
                case BleConstant.HD_AD_PARA_55:
                    sMode.mmv55 = ww;
                    bSetPara = true;
                    break;
                case BleConstant.HD_AD_PARA_56:
                    sMode.mmv56 = ww;
                    bSetPara = true;
                    break;
                case BleConstant.HD_AD_PARA_57:
                    sMode.mmv57 = ww;
                    bSetPara = true;
                    break;
                case BleConstant.HD_AD_PARA_58:
                    sMode.mmv58 = ww;
                    bSetPara = true;
                    break;
                case BleConstant.HD_AD_PARA_59:
                    sMode.mmv59 = ww;
                    bSetPara = true;
                    break;
                case BleConstant.HD_AD_PARA_60:
                    sMode.mmv60 = ww;
                    bSetPara = true;
                    break;
                case BleConstant.HD_AD_PARA_61:
                    sMode.mmv61 = ww;
                    bSetPara = true;
                    break;
                case BleConstant.HD_AD_PARA_62:
                    sMode.mmv62 = ww;
                    bSetPara = true;
                    break;
                case BleConstant.HD_AD_PARA_63:
                    sMode.mmv63 = ww;
                    bSetPara = true;
                    break;
                case BleConstant.HD_AD_PARA_64:
                    sMode.mmv64 = ww;
                    bSetPara = true;
                    break;
                case BleConstant.HD_WEIGHT_1:
                    uMode.weight1 = ww;
                    break;
                case BleConstant.HD_WEIGHT_2:
                    uMode.weight2 = ww;
                    break;
                case BleConstant.HD_WEIGHT_3:
                    uMode.weight3 = ww;
                    break;
                case BleConstant.HD_WEIGHT_4:
                    uMode.weight4 = ww;
                    break;
                case BleConstant.HD_WEIGHT_5:
                    uMode.weight5 = ww;
                    break;
                case BleConstant.HD_WEIGHT_6:
                    uMode.weight6 = ww;
                    break;
                case BleConstant.HD_WEIGHT_7:
                    uMode.weight7 = ww;
                    break;
                case BleConstant.HD_WEIGHT_8:
                    uMode.weight8 = ww;
                    break;
                case BleConstant.HD_WEIGHT:
                    uMode.weight = ww;
                    break;
                case BleConstant.HD_BOARD_ID:
                    //int32_t nID = (nABuffer[8+6*i+5]<<24)|(nABuffer[8+6*i+4]<<16)|(nABuffer[8+6*i+3]<<8)|(nABuffer[8+6*i+2]);
                    break;
                case BleConstant.HD_STABLE_PARA: //1027 锂电池电压
                    uMode.lVoltage=ww;

                    //uMode.weight =[self dealFloat:nABuffer[8+6*i+2] and2:nABuffer[8+6*i+3] and3:nABuffer[8+6*i+4] and4:nABuffer[8+6*i+5]];
                    break;
                case BleConstant.HD_HAND_BREAK:
                    uMode.nBreak = nABuffer[8 + 6 * i + 2];
                    break;
                case BleConstant.HD_STABLE:           // 1029
                    uMode.nStable = nABuffer[8 + 6 * i + 2];
                    break;
                case BleConstant.HD_VOLTAGE:  //1030
                    uMode.nVoltage = ww;
                    float dianya=ww;
                    ACache cache=ACache.get(BApplication.mContext,"ble");
                    String dianCache= cache.getAsString("ble_fa");
                    if(!Tools.isEmpty(dianCache)&&  "1".equals(dianCache)){
                        Intent dintent = new Intent(BleConstant.ACTION_AUTO_DIANYA);
                        dintent.putExtra("li_volate", String.valueOf(dianya));
                        BApplication.mContext.sendBroadcast(dintent);
                    }
                    break;
                case BleConstant.HD_WEIGHT_TIME:  //1031
                    long weiDate= (nABuffer[8 + 6 * i + 5] << 24) | (nABuffer[8 + 6 * i + 4] << 16) | (nABuffer[8 + 6 * i + 3] << 8) | (nABuffer[8 + 6 * i + 2]);
                    uMode.weightTime = (nABuffer[8 + 6 * i + 5] << 24) | (nABuffer[8 + 6 * i + 4] << 16) | (nABuffer[8 + 6 * i + 3] << 8) | (nABuffer[8 + 6 * i + 2]);
                    timbean.setweightTime(weiDate+"");
                    break;
                case BleConstant.HD_GPS_TIME:
                    int gpsDate= (nABuffer[8 + 6 * i + 5] << 24) | (nABuffer[8 + 6 * i + 4] << 16) | (nABuffer[8 + 6 * i + 3] << 8) | (nABuffer[8 + 6 * i + 2]);
                    uMode.gpsTime = (nABuffer[8 + 6 * i + 5] << 24) | (nABuffer[8 + 6 * i + 4] << 16) | (nABuffer[8 + 6 * i + 3] << 8) | (nABuffer[8 + 6 * i + 2]);
                    timbean.setgpsTime(gpsDate+"");

                    Intent intentG = new Intent(BleConstant.ACTION_BIN_GPS_TIME);
                    intentG.putExtra("gps_time", (Serializable) timbean);
                    BApplication.mContext.sendBroadcast(intentG);
                    break;
                case BleConstant.HD_LONGTITUDE:  //1033
                    uMode.nLongtitude = ww;//经度
                    break;
                case BleConstant.HD_LATITUDE:  //1034
                    uMode.nLatitude = ww;// 纬度
                    break;
                case BleConstant.HD_AZIMUTH:  //向角  smg  1035
                    uMode.nAzimuth = ww;
                    break;
                case BleConstant.HD_TRUCK_SPEED:  //1036
                    uMode.nSpeed = ww;//速度
                    break;
                case BleConstant.HD_HARD_WARE:    //1037  硬件版本号
                    DLog.e(TAG,"硬件版本号");
                    int h1=nABuffer[8 + 6 * i + 2];
                    int h2=nABuffer[8 + 6 * i + 3] ;
                    int h3=nABuffer[8 + 6 * i + 4] ;
                    int h4=nABuffer[8 + 6 * i + 5];
                    String hware=h1+"."+h2+"."+h3+"."+h4;
                    Intent intent = new Intent(BleConstant.ACTION_READ_HARD_SOFT_SUSSCESS);
                    intent.putExtra("hard_ware", hware);
                    BApplication.mContext.sendBroadcast(intent);
                    break;
                case BleConstant.HD_BOOTLOAD_SOFT_WARE:  //1038
                    break;

                case BleConstant.HD_APPLICATION_SOFT_WARE:  //1039
                    DLog.e(TAG,"固件版本号");
                    int s1=nABuffer[8 + 6 * i + 2];
                    int s2=nABuffer[8 + 6 * i + 3] ;
                    int s3=nABuffer[8 + 6 * i + 4] ;
                    int s4=nABuffer[8 + 6 * i + 5] ;
                    String sware=s1+"."+s2+"."+s3+"."+s4;
                    Intent intents = new Intent(BleConstant.ACTION_READ_SOFT_SUSSCESS);
                    intents.putExtra("soft_ware", sware);
                    BApplication.mContext.sendBroadcast(intents);
                    break;
                case BleConstant.HD_SOFT_WARE_STATUS: //1040
                    break;
                case BleConstant.HD_HARD_WARE_STATUS:   //1041 //硬件状态 Bit[11]:GPS天线状态  1byte=8bit
                    //
                    //                    byte aa=(byte)nABuffer[8 + 6 * i + 3];
                    //                    byte[] bb=getByteArray(aa);
                    //                    int gps11=bb[3];
                    //                    gpsbean.setAirWireStatus(gps11+"");
                    break;
                case BleConstant.HD_SENSOR_DIAGNOSE:    //1042 //传感器状态Bit[0~15], 每个比特位表示一路传感器数据更新状态。比特值： -	0：传感器异常。 -	1：传感器正常
                    byte a1=(byte)nABuffer[8 + 6 * i + 2];
                    byte a2=(byte)nABuffer[8 + 6 * i + 3];
                    DLog.e(TAG,"传感器状态="+a1+","+a2);
                    String b1=getBit(a1);
                    String b2=getBit(a2);
                    String chuanganqi=b1+b2;
                    Intent cintent = new Intent(BleConstant.ACTION_CHUAN_GAN_QI);
                    cintent.putExtra("senor_status", chuanganqi);
                    BApplication.mContext.sendBroadcast(cintent);
                    break;

                case BleConstant.HD_CIRCUIT_DIAGNOSE:   //1043   //采集器状态  Byte[0~4] 每字节表示一路采集器状态。 -	0值表示异常； -	非零值表示采集器通道数，状态正常。
                    int c1=nABuffer[8 + 6 * i + 2];
                    int c2=nABuffer[8 + 6 * i + 3] ;
                    int c3=nABuffer[8 + 6 * i + 4] ;
                    int c4=nABuffer[8 + 6 * i + 5] ;
                    String str=c1+";"+c2;
                    DLog.e(TAG,"采集器状态="+c1+","+c2+","+c3+","+c4);
                    Intent jintent = new Intent(BleConstant.ACTION_CAI_JI_QI);
                    jintent.putExtra("colection_status", str);
                    BApplication.mContext.sendBroadcast(jintent);
                    break;
                case BleConstant.HD_LOAD_DISCHARGE_STATUS:  //读命令  0-无货物 1-有货物 2-装货 3-卸货  1044 //测量状态
                    uMode.nLoadStatus = nABuffer[8 + 6 * i + 2];
                    break;
                case BleConstant.HD_RUNNING_NUMBER:   //1045 //项目流水号
                    uMode.nRunningNumber = (nABuffer[8 + 6 * i + 5] << 24) | (nABuffer[8 + 6 * i + 4] << 16) | (nABuffer[8 + 6 * i + 3] << 8) | (nABuffer[8 + 6 * i + 2]);
                    break;
                case BleConstant.HD_SENSOR_ID_LIST:   //1046
                    break;
                case BleConstant.HD_SENSOR_ID_READ_OR_WRITE:  //1047
                    break;
                case BleConstant.HD_ZERO_ACTION:               //清零指令
                    DLog.e(TAG,"接收到清零指令");
                    bZeroAction = true;
                    break;

                case BleConstant.HD_GPS_RSSI:  //1054  GPRSrssi 信号强度值0~31<15认为信号强度弱
                    float gpsRssi = (nABuffer[8 + 6 * i + 5] << 24) | (nABuffer[8 + 6 * i + 4] << 16) | (nABuffer[8 + 6 * i + 3] << 8) | (nABuffer[8 + 6 * i + 2]);
                    DLog.e(TAG,"藍牙GPRSrssi 信号强度值="+gpsRssi);

                    Intent rssiintent = new Intent(BleConstant.ACTION_GPRSSIR);
                    rssiintent.putExtra("gps_rssi",  String.valueOf(gpsRssi));
                    BApplication.mContext.sendBroadcast(rssiintent);

                    break;
                case BleConstant.HD_GPRS_STATUS:  //1233 GPRS网络状态
                    float gprs = (nABuffer[8 + 6 * i + 5] << 24) | (nABuffer[8 + 6 * i + 4] << 16) | (nABuffer[8 + 6 * i + 3] << 8) | (nABuffer[8 + 6 * i + 2]);
                    rsBean.setNetStatus(String.valueOf(gprs));

                    Intent gprsintent = new Intent(BleConstant.ACTION_GPRS);
                    gprsintent.putExtra("gprs_status", (Serializable) rsBean);
                    BApplication.mContext.sendBroadcast(gprsintent);
                    break;
                case BleConstant.HD_GPS_STATUS:  //1053  取值为GPS可视卫星数量
                    float gps = (nABuffer[8 + 6 * i + 5] << 24) | (nABuffer[8 + 6 * i + 4] << 16) | (nABuffer[8 + 6 * i + 3] << 8) | (nABuffer[8 + 6 * i + 2]);
                    gpsbean.setStarsNumber(String.valueOf(gps));
                    DLog.e(TAG,"藍牙GPS可视卫星数量="+gps);
                    break;
                case BleConstant.HD_LOCATION_GPS:  //1232 0：无定位，1：已定位
                    float loactionStatus = (nABuffer[8 + 6 * i + 5] << 24) | (nABuffer[8 + 6 * i + 4] << 16) | (nABuffer[8 + 6 * i + 3] << 8) | (nABuffer[8 + 6 * i + 2]);
                    String location=String.valueOf(loactionStatus);
                    gpsbean.setLocationStatus(String.valueOf(location));
                    Intent loacintent = new Intent(BleConstant.ACTION_GPS_CHECK_LOC);
                    loacintent.putExtra("gps_location", (Serializable)gpsbean);
                    BApplication.mContext.sendBroadcast(loacintent);
//                    DLog.e(TAG,"藍牙GPS定位狀態="+loactionStatus);
                    break;

                case BleConstant.HD_NET_LINE:  //1239 天线状态
                    float line = (nABuffer[8 + 6 * i + 5] << 24) | (nABuffer[8 + 6 * i + 4] << 16) | (nABuffer[8 + 6 * i + 3] << 8) | (nABuffer[8 + 6 * i + 2]);
                    //                    gpsbean.setAirWireStatus(String.valueOf(line));

                    Intent gpsintent = new Intent(BleConstant.ACTION_GPS_CHECK);
                    gpsintent.putExtra("check_gps",  String.valueOf(line));
                    BApplication.mContext.sendBroadcast(gpsintent);
                    DLog.e(TAG,"藍牙 天线状态="+line);
                    break;
                case BleConstant.HD_WEIGHT_CONFIGURATION_ACTION:
                    isWeightConfiguration = true;
                    break;
                case BleConstant.HD_READY_BIN: //1280 z准备升级 准备成功
                    Intent readyintent = new Intent(BleConstant.ACTION_UPDATE_BIN_SUCCESS);
                    BApplication.mContext.sendBroadcast(readyintent);
                    break;
                case BleConstant.HD_START_BIN: //1281 开始升级 上传包 应答返回
                    ACache   mcache= ACache.get(BApplication.mContext,"BIN");
                    String tag=mcache.getAsString("bin_upper");
                    if(!"1".equals(tag)){
                        return;
                    }
                    float binNumber = (nABuffer[8 + 6 * i + 5] << 24) | (nABuffer[8 + 6 * i + 4] << 16) | (nABuffer[8 + 6 * i + 3] << 8) | (nABuffer[8 + 6 * i + 2]);
                    Intent startintent = new Intent(BleConstant.ACTION_ING_BIN_SUSSESS);
                    startintent.putExtra("bin_number",String.valueOf(binNumber));
                    BApplication.mContext.sendBroadcast(startintent);
                    break;
                case BleConstant.HD_FINISH_BIN: //1282  升级完成
                    float binover = (nABuffer[8 + 6 * i + 5] << 24) | (nABuffer[8 + 6 * i + 4] << 16) | (nABuffer[8 + 6 * i + 3] << 8) | (nABuffer[8 + 6 * i + 2]);
                    DLog.e(TAG,"升级完成返回值："+binover);
                    Intent overtintent = new Intent(BleConstant.ACTION_BIN_OVER_STATUS);
                    overtintent.putExtra("over_bstatus",String.valueOf(binover));
                    BApplication.mContext.sendBroadcast(overtintent);
                    break;
                default:
                    break;
            }
        }

        if (bZeroAction) {
            //清零指令
            DLog.e(TAG,"接收到清零指令");
            Intent intent = new Intent(BleConstant.ACTION_BLE_CLEAR_ZERO);
            BApplication.mContext.sendBroadcast(intent);

        } else if (bSetPara) {
            DLog.e(TAG,"接收到参数信息1");
            /*在查看数据页dataFragment计算系数时触发、接收*/
            Intent intent = new Intent(BleConstant.ACTION_SEND_DATA);
            intent.putExtra("data", sMode);
            BApplication.mContext.sendBroadcast(intent);
        } else if (isWeightConfiguration) {
            isWeightConfiguration = false;
            //LogUtil.e("接收到重量配置");
        } else {

            //            Intent intent = new Intent(BleConstant.ACTION_BLE_HANDLER_DATA);
            //            Bundle bundle = new Bundle();
            //            bundle.putSerializable("ModelData", uMode);
            //            intent.putExtras(bundle);
            //            BApplication.mContext.sendBroadcast(intent);

            //            DLog.e(TAG,"setp3:HandlerBleData发送eventbus。");

            EventBus.getDefault().post(uMode);

            //
            //            new Thread("ble_msg") {
            //                @Override
            //                public void run() {
            //                    try {
            //                        EventBus.getDefault().post(uMode);
            //                    } catch (Exception e) {
            //                        e.printStackTrace();
            //                    }
            //
            //                }
            //            }.start();
        }

    }


    public static float getMvvValue(float ad, float zero) {
        float x = (ad - zero) / 5;
        java.text.DecimalFormat myformat = new java.text.DecimalFormat("0.000");
        String str = myformat.format(x);
        float mvvValue = Float.parseFloat(str);
        return mvvValue;
    }

    public static  void addBufferHead() {
        ++nBufferHead;
        if (nBufferHead == BUFFERCOUNT) {
            nBufferHead = 0;
        }
    }

    public static int getBufferSize() {
        int x = nBufferTail - nBufferHead;
        if (x < 0)
            return x + BUFFERCOUNT;
        else
            return x;
    }

    public static  float ArryToFloat(byte[] Array, int Pos) {
        int accum = 0;
        accum = Array[Pos + 0] & 0xFF;
        accum |= (long) (Array[Pos + 1] & 0xFF) << 8;
        accum |= (long) (Array[Pos + 2] & 0xFF) << 16;
        accum |= (long) (Array[Pos + 3] & 0xFF) << 24;
        return Float.intBitsToFloat(accum);
    }

    /**
     * 获取一个字节的bit数组
     *
     * @param value
     * @return
     */
    public static byte[] getByteArray(byte value) {
        byte[] byteArr = new byte[8]; //一个字节八位
        for (int i = 7; i > 0; i--) {
            byteArr[i] = (byte) (value & 1); //获取最低位
            value = (byte) (value >> 1); //每次右移一位
        }
        return byteArr;
    }

    /*
     *byte转bit
     */
    public static String getBit(byte by){
        StringBuffer sb = new StringBuffer();
        sb.append((by>>0)&0x1).append((by>>1)&0x1).append((by>>2)&0x1).append((by>>3)&0x1).append((by>>4)&0x1)
                .append((by>>5)&0x1)
                .append((by>>6)&0x1)
                .append((by>>7)&0x1);
        return sb.toString();
    }

}
