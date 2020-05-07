package cn.hand.tech.ble;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import cn.hand.tech.bean.HDSendDataModel;
import cn.hand.tech.ble.bleUtil.BluetoothUtil;
import cn.hand.tech.ble.bleUtil.CRC16;
import cn.hand.tech.log.DLog;
import cn.hand.tech.ui.setting.bean.GuJianBean;

/**
 * Created by hand-hxz on 2018-07-05.
 */
/*---------------------------------- 蓝牙发送数据 -----------------------------*/
public class HDBLESend {
    private static int m;
    public final static  UUID SER_UUID = UUID.fromString("0000fff0-0000-1000-8000-00805f9b34fb");
    public final static  UUID SPP_UUID = UUID.fromString("0000fff1-0000-1000-8000-00805f9b34fb");

    //清零命令
    public static void SendClearZero(BluetoothGattCharacteristic characteristic, BluetoothGatt bluetoothGatt) {

        //byte[] byteCommand = {0x7B,0xA5,0x00,0x00,0x00,0x00,0x01,0x01,0x18,0x84,0x00,0x00,0x00,0x00,0x00,0x00,0xB7,0x5A};

        //        String topStr="7B A5 0000000001011884000000000000B75A";
        //        System.out.println(topStr);
        //        byte[] value_array=BluetoothUtil.hex2Bytes(topStr);
        //        System.out.println(Arrays.toString(value_array));
        //        byte[] value = new byte[20];
        //        value[0] = (byte) 0x00;
        //        byte[] value_byte = hex2Bytes(topStr);
        //        characteristic.setValue(value_byte);


        //        final StringBuilder stringBuilder = new StringBuilder(value_array.length);
        //        for(byte byteChar : value_array) {
        //            stringBuilder.append(String.format("%02X", byteChar));
        //        }
        //        //                ToastUtil.showToast(mainActivity,stringBuilder.toString());
        //
        //        System.out.println(stringBuilder.toString());
        //        String value=stringBuilder.toString();


        String order = "7B A5 0000000001011884000000000000B75A";
        byte[] send_data = BluetoothUtil.hex2Bytes(order);
        char[] crc_data = CRC16.hexStringToByteArray(order);
        int crcData = CRC16.calcCrc16(crc_data, 0, crc_data.length - 4);
        byte[] crc_int_byte = CRC16.IntToByteArray(crcData);//1 0
        send_data[14] = crc_int_byte[1];
        send_data[15] = crc_int_byte[0];
        characteristic.setValue(send_data);
        bluetoothGatt.writeCharacteristic(characteristic);
    }
    //重启主机
    public static boolean ReloadSYS(BluetoothGattCharacteristic characteristic, BluetoothGatt bluetoothGatt) {

        String order = "7BA5 00000000 01 01 0000 00000000 0000 B75A";
        byte[] send_data = BluetoothUtil.hex2Bytes(order);
        char[] crc_data = CRC16.hexStringToByteArray(order);
        int crcData = CRC16.calcCrc16(crc_data, 0, crc_data.length - 4);
        byte[] crc_int_byte = CRC16.IntToByteArray(crcData);//1 0
        send_data[14] = crc_int_byte[1];
        send_data[15] = crc_int_byte[0];
        characteristic.setValue(send_data);
        boolean isWrite=bluetoothGatt.writeCharacteristic(characteristic);
        return isWrite;
    }
    //恢复出厂设置
    public static boolean doRenturnBack(BluetoothGattCharacteristic characteristic, BluetoothGatt bluetoothGatt) {

        String order = "7BA5 00000000 01 01 1C84 00000000 0000 B75A";
        byte[] send_data = BluetoothUtil.hex2Bytes(order);
        char[] crc_data = CRC16.hexStringToByteArray(order);
        int crcData = CRC16.calcCrc16(crc_data, 0, crc_data.length - 4);
        byte[] crc_int_byte = CRC16.IntToByteArray(crcData);//1 0
        send_data[14] = crc_int_byte[1];
        send_data[15] = crc_int_byte[0];
        characteristic.setValue(send_data);
        boolean isSuccess=bluetoothGatt.writeCharacteristic(characteristic);
        return isSuccess;
    }
    /*重量设置*/
    public static void SendWeightConfiguration(BluetoothGattCharacteristic characteristic, BluetoothGatt bluetoothGatt, double weightPredict) {
        //  Byte byteCommand[18] = {0x7B,0xA5,0x00,0x00,0x00,0x00,0x01,0x01,0x22,0x84,0x00,0x00,0x00,0x00,0x00,0x00,0xB7,0x5A};
        String order = "7BA50000000001012284000000000000B75A";
        byte[] send_data = BluetoothUtil.hex2Bytes(order);
        float m1 = (float) weightPredict;
        byte[] a1 = float2byte(m1);
        for (int i = 0; i < 4; i++) {
            send_data[10 + i] = a1[i];

        }
        final StringBuilder stringBuilder = new StringBuilder(send_data.length);
        for (byte byteChar : send_data) {
            stringBuilder.append(String.format("%02X", byteChar));
        }
        //                ToastUtil.showToast(mainActivity,stringBuilder.toString());
        //Log.e("CRC16",stringBuilder.toString());
        String value = stringBuilder.toString();
        //System.out.println(stringBuilder.toString());
        char[] crc_data = CRC16.hexStringToByteArray(value);
        int crcData = CRC16.calcCrc16(crc_data, 0, crc_data.length - 4);
        byte[] crc_int_byte = CRC16.IntToByteArray(crcData);
        send_data[14] = crc_int_byte[1];
        send_data[15] = crc_int_byte[0];

        for (int j=0;j<send_data.length;j++){
            //            LogUtil.e("send_data==第"+m+"次=="+send_data[j]);
        }
        m++;
        if (m==16){
            m=0;
        }
        characteristic.setValue(send_data);
        bluetoothGatt.writeCharacteristic(characteristic);


    }

    //设置DEVid
    public static void readDeviceID(BluetoothGattCharacteristic characteristic, BluetoothGatt bluetoothGatt) {
        //byte[] byteCommand = {0x7B,0xA5,0x00,0x00,0x00,0x00,0x01,0x01,0x0E,0x84,0x00,0x00,0x00,0x00,0x00,0x00,0xB7,0x5A};
        String order = "7BA5 00000000 01 01 020400000000 0000B75A";  //84 写 04读
        byte[] send_data = BluetoothUtil.hex2Bytes(order);
        char[] crc_data = CRC16.hexStringToByteArray(order);
        int crcData = CRC16.calcCrc16(crc_data, 0, crc_data.length - 4);
        byte[] crc_int_byte = CRC16.IntToByteArray(crcData);//1 0
        send_data[14] = crc_int_byte[1];
        send_data[15] = crc_int_byte[0];
        characteristic.setValue(send_data);
        boolean istr=bluetoothGatt.writeCharacteristic(characteristic);

        try{
            while (!istr){
                Thread.sleep(20);

                characteristic.setValue(send_data);
                boolean issuccess2=bluetoothGatt.writeCharacteristic(characteristic);
                DLog.e("readDeviceID","readDeviceID:" + "====》issuccess1: "+issuccess2);
                if(issuccess2){
                    istr=true;
                }
            }

            //解决BUG
            String order2 = "7BA5 00000000 01 01 020400000000 0000B75A";
            byte[] send_data2 = BluetoothUtil.hex2Bytes(order2);
            char[] crc_data2 = CRC16.hexStringToByteArray(order2);
            int crcData2 = CRC16.calcCrc16(crc_data2, 0, crc_data2.length - 4);
            byte[] crc_int_byte2 = CRC16.IntToByteArray(crcData2);//1 0
            send_data2[14] = crc_int_byte2[1];
            send_data2[15] = crc_int_byte2[0];
            characteristic.setValue(send_data2);
            boolean istrue = bluetoothGatt.writeCharacteristic(characteristic);
            while(!istrue){
                Thread.sleep(20);
                characteristic.setValue(send_data2);
                boolean istrue1 = bluetoothGatt.writeCharacteristic(characteristic);
                if(istrue1){
                    istrue=true;
                }
            }
            //            Thread.sleep(50);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    //版本号
    public static void ReadVerCode(BluetoothGattCharacteristic characteristic, BluetoothGatt bluetoothGatt,int id) {
        //byte[] byteCommand = {0x7B,0xA5,0x00,0x00,0x00,0x00,0x01,0x01,0x0F,0x84,0x00,0x00,0x00,0x00,0x00,0x00,0xB7,0x5A};
//        String order = "7B A5 000000000101 0F0400000000 0000B75A";
//        byte[] send_data = BluetoothUtil.hex2Bytes(order);
//
//        char[] crc_data = CRC16.hexStringToByteArray(order);
//        int crcData = CRC16.calcCrc16(crc_data, 0, crc_data.length - 4);
//        byte[] crc_int_byte = CRC16.IntToByteArray(crcData);//1 0
//        send_data[14] = crc_int_byte[1];
//        send_data[15] = crc_int_byte[0];
//        characteristic.setValue(send_data);
//        bluetoothGatt.writeCharacteristic(characteristic);

        try {
            String order = "7B A5 000000000101 0F0400000000 0000B75A";
            byte[] send_data = BluetoothUtil.hex2Bytes(order);
            char[] crc_data = CRC16.hexStringToByteArray(order);
            int crcData = CRC16.calcCrc16(crc_data, 0, crc_data.length - 4);
            byte[] crc_int_byte = CRC16.IntToByteArray(crcData);//1 0
            send_data[14] = crc_int_byte[1];
            send_data[15] = crc_int_byte[0];
            characteristic.setValue(send_data);
            boolean istrue = bluetoothGatt.writeCharacteristic(characteristic);
            while(!istrue){
                Thread.sleep(20);
                characteristic.setValue(send_data);
                boolean istrue1 = bluetoothGatt.writeCharacteristic(characteristic);
                if(istrue1){
                    istrue=true;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    //硬件版本号
    public static void ReadHardSotf(BluetoothGattCharacteristic characteristic, BluetoothGatt bluetoothGatt,int id) {
        //byte[] byteCommand = {0x7B,0xA5,0x00,0x00,0x00,0x00,0x01,0x01,0x0D,0x84,0x00,0x00,0x00,0x00,0x00,0x00,0xB7,0x5A};
//        String order = "7B A5 000000000101 0D0400000000 0000B75A";
//        byte[] send_data = BluetoothUtil.hex2Bytes(order);
//        char[] crc_data = CRC16.hexStringToByteArray(order);
//        int crcData = CRC16.calcCrc16(crc_data, 0, crc_data.length - 4);
//        byte[] crc_int_byte = CRC16.IntToByteArray(crcData);//1 0
//        send_data[14] = crc_int_byte[1];
//        send_data[15] = crc_int_byte[0];
//        characteristic.setValue(send_data);
//        bluetoothGatt.writeCharacteristic(characteristic);
        try {
            String order2 = "7B A5 000000000101 0D0400000000 0000B75A";
            byte[] send_data2 = BluetoothUtil.hex2Bytes(order2);
            char[] crc_data2 = CRC16.hexStringToByteArray(order2);
            int crcData2 = CRC16.calcCrc16(crc_data2, 0, crc_data2.length - 4);
            byte[] crc_int_byte2 = CRC16.IntToByteArray(crcData2);//1 0
            send_data2[14] = crc_int_byte2[1];
            send_data2[15] = crc_int_byte2[0];
            characteristic.setValue(send_data2);
            boolean istrue = bluetoothGatt.writeCharacteristic(characteristic);
            while(!istrue){
                Thread.sleep(20);
                characteristic.setValue(send_data2);
                boolean istrue1 = bluetoothGatt.writeCharacteristic(characteristic);
                if(istrue1){
                    istrue=true;
                }
            }
            //                    mWriteCharacteristic(characteristic,bluetoothGatt,send_data2);
        }catch (Exception e){
            e.printStackTrace();
        }
    }



    public static void SendReadPara(BluetoothGattCharacteristic characteristic, BluetoothGatt bluetoothGatt) {
        //Byte byteCommand[108] = {0x7B,0xA5,0x00,0x00,0x00,0x00,0x01,0x10, 0x01,0x02,0x00,0x00,0x00,0x00,  0x02,0x02,0x00,0x00,0x00,0x00,  0x03,0x02,0x00,0x00,0x00,0x00,  0x04,0x02,0x00,0x00,0x00,0x00,  0x05,0x02,0x00,0x00,0x00,0x00,  0x06,0x02,0x00,0x00,0x00,0x00,  0x07,0x02,0x00,0x00,0x00,0x00,  0x08,0x02,0x00,0x00,0x00,0x00,  0x09,0x02,0x00,0x00,0x00,0x00,  0x0A,0x02,0x00,0x00,0x00,0x00,  0x0B,0x02,0x00,0x00,0x00,0x00,  0x0C,0x02,0x00,0x00,0x00,0x00,  0x0D,0x02,0x00,0x00,0x00,0x00,  0x0E,0x02,0x00,0x00,0x00,0x00,  0x0F,0x02,0x00,0x00,0x00,0x00,  0x10,0x02,0x00,0x00,0x00,0x00,  0x00,0x00,0xB7,0x5A};

        String order = "7BA5 000000000110 010200000000  020200000000  030200000000  040200000000  050200000000  060200000000  070200000000  080200000000  090200000000  0A0200000000  0B0200000000  0C0200000000  0D0200000000  0E0200000000  0F0200000000  100200000000  0000B75A";

        byte[] send_data = BluetoothUtil.hex2Bytes(order);
        char[] crc_data = CRC16.hexStringToByteArray(order);
        int crcData = CRC16.calcCrc16(crc_data, 0, crc_data.length - 4);
        byte[] crc_int_byte = CRC16.IntToByteArray(crcData);//1 0
        send_data[104] = crc_int_byte[1];
        send_data[105] = crc_int_byte[0];
        for (int i = 0; i < 6; i++) {
            byte[] b = new byte[18];
            for (int j = 0; j < 18; j++) {
                b[j] = send_data[18 * i + j];
            }
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println(Arrays.toString(b));
            characteristic.setValue(b);
            bluetoothGatt.writeCharacteristic(characteristic);
        }

        //        characteristic.setValue(send_data);
        //        bluetoothGatt.writeCharacteristic(characteristic);

    }


    public static void SendAutoCheck(BluetoothGattCharacteristic characteristic, BluetoothGatt bluetoothGatt,int id) {
        //Byte byteCommand[108] = {0x7B,0xA5,0x00,0x00,0x00,0x00,0x01,0x10, 0x01,0x02,0x00,0x00,0x00,0x00,  0x02,0x02,0x00,0x00,0x00,0x00,  0x03,0x02,0x00,0x00,0x00,0x00,  0x04,0x02,0x00,0x00,0x00,0x00,  0x05,0x02,0x00,0x00,0x00,0x00,  0x06,0x02,0x00,0x00,0x00,0x00,  0x07,0x02,0x00,0x00,0x00,0x00,  0x08,0x02,0x00,0x00,0x00,0x00,  0x09,0x02,0x00,0x00,0x00,0x00,  0x0A,0x02,0x00,0x00,0x00,0x00,  0x0B,0x02,0x00,0x00,0x00,0x00,  0x0C,0x02,0x00,0x00,0x00,0x00,  0x0D,0x02,0x00,0x00,0x00,0x00,  0x0E,0x02,0x00,0x00,0x00,0x00,  0x0F,0x02,0x00,0x00,0x00,0x00,  0x10,0x02,0x00,0x00,0x00,0x00,  0x00,0x00,0xB7,0x5A};

        String order = "7BA5 00000000010B 0F0400000000  0D0400000000  030400000000  060400000000  D70400000000  120400000000  130400000000  1D0400000000  D00400000000  1E0400000000  D10400000000   0000B75A";

        byte[] send_data = BluetoothUtil.hex2Bytes(order);
        char[] crc_data = CRC16.hexStringToByteArray(order);
        int crcData = CRC16.calcCrc16(crc_data, 0, crc_data.length - 4);
        byte[] crc_int_byte = CRC16.IntToByteArray(crcData);//1 0
        send_data[74] = crc_int_byte[1];
        send_data[75] = crc_int_byte[0];
        for (int i = 0; i < 4; i++) {
            if(i<3) {
                byte[] b = new byte[20];
                for (int j = 0; j < 20; j++) {
                    b[j] = send_data[20 * i + j];
                }
                System.out.println(Arrays.toString(b));
                characteristic.setValue(b);
                bluetoothGatt.writeCharacteristic(characteristic);
            }else{
                byte[] bb = new byte[18];
                for (int j = 0; j < 18; j++) {
                    bb [j] = send_data[20 * i + j];
                }
                System.out.println(Arrays.toString(bb));
                characteristic.setValue(bb);
                bluetoothGatt.writeCharacteristic(characteristic);
            }
            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }

        //        characteristic.setValue(send_data);
        //        bluetoothGatt.writeCharacteristic(characteristic);

    }
    //准备升级固件
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static void SendStartUpdate(BluetoothGattCharacteristic characteristic, BluetoothGatt bluetoothGatt, GuJianBean bean) {
        //Byte byteCommand[108] = {0x7B,0xA5,0x00,0x00,0x00,0x00,0x01,0x10, 0x01,0x02,0x00,0x00,0x00,0x00,  0x02,0x02,0x00,0x00,0x00,0x00,  0x03,0x02,0x00,0x00,0x00,0x00,  0x04,0x02,0x00,0x00,0x00,0x00,  0x05,0x02,0x00,0x00,0x00,0x00,  0x06,0x02,0x00,0x00,0x00,0x00,  0x07,0x02,0x00,0x00,0x00,0x00,  0x08,0x02,0x00,0x00,0x00,0x00,  0x09,0x02,0x00,0x00,0x00,0x00,  0x0A,0x02,0x00,0x00,0x00,0x00,  0x0B,0x02,0x00,0x00,0x00,0x00,  0x0C,0x02,0x00,0x00,0x00,0x00,  0x0D,0x02,0x00,0x00,0x00,0x00,  0x0E,0x02,0x00,0x00,0x00,0x00,  0x0F,0x02,0x00,0x00,0x00,0x00,  0x10,0x02,0x00,0x00,0x00,0x00,  0x00,0x00,0xB7,0x5A};

        double bao1=bean.getPageNumber(); //包数量

        DLog.e("SendStartUpdate","SendStartUpdate:" + "====》bao1: "+bao1);

        int m1 = (int) bao1;

        String order = "7BA5 00000000 04 01 0085 00000000  0000B75A";

        byte[] send_data = BluetoothUtil.hex2Bytes(order);
        byte[] a1 = CRC16.IntToByteArray(m1);
        send_data[10 + 0] = a1[0];
        send_data[10 + 1] = a1[1];
        send_data[10 + 2] = a1[2];
        send_data[10 + 3] = a1[3];

        final StringBuilder stringBuilder = new StringBuilder(send_data.length);
        for (byte byteChar : send_data) {
            stringBuilder.append(String.format("%02X", byteChar));
        }
        String value = stringBuilder.toString();
        DLog.e("SendStartUpdate","SendStartUpdate:" + "====》value: "+value);

        char[] crc_data = CRC16.hexStringToByteArray(value);
        int crcData = CRC16.calcCrc16(crc_data, 0, crc_data.length - 4);
        byte[] crc_int_byte = CRC16.IntToByteArray(crcData);//1 0

        send_data[14] = crc_int_byte[1];
        send_data[15] = crc_int_byte[0];
        characteristic.setValue(send_data);
        bluetoothGatt.writeCharacteristic(characteristic);

        //        characteristic.setValue(send_data);
        //        bluetoothGatt.writeCharacteristic(characteristic);

    }
    //开始写入固件数据
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static void SendupdateBin( BluetoothGattCharacteristic characteristic, BluetoothGatt bluetoothGatt,final GuJianBean bean,final double num) {

                String binStr=bean.getBinStr();
                DLog.e("SendupdateBin","SendupdateBin:" + "binStr:"+binStr);
                byte[] bin_by = BluetoothUtil.hex2Bytes(binStr);
                int size=bin_by.length;
                double bao=bean.getPageNumber();
                int xuhao=(int)num;
                DLog.e("SendupdateBin","SendupdateBin:" + "size:"+size+"====》xuhao: "+xuhao);

                int trueLenth=(int)(bao*1024d);
                int z=trueLenth-size;
                StringBuffer sub=new StringBuffer();
                sub.append(binStr);
                for (int k = 0; k < z; k++) {
                    sub.append("ff");
                }
                String bytestr=sub.toString();

                String nowStr=bytestr.substring(1024*xuhao*2,2*1024*(xuhao+1)); //每次1024數據
                int lenno=nowStr.length();
                DLog.e("SendupdateBin","SendupdateBin:" + "len:"+lenno+ "====》nowStr: "+nowStr);
                String headStr="7BA50000000004010185";

                int m1 = (int) num;

                String order=headStr+"00000000"+nowStr+"0000B75A";
                int len=order.length();
                DLog.e("SendupdateBin","SendupdateBin:" + "len:"+len+"====》order: "+order);

                byte[] send_data = BluetoothUtil.hex2Bytes(order);
                byte[] a1 = CRC16.IntToByteArray(m1);
                send_data[10 + 0] = a1[0];
                send_data[10 + 1] = a1[1];
                send_data[10 + 2] = a1[2];
                send_data[10 + 3] = a1[3];

                final StringBuilder stringBuilder = new StringBuilder(send_data.length);
                for (byte byteChar : send_data) {
                    stringBuilder.append(String.format("%02X", byteChar));
                }
                String value = stringBuilder.toString();
                DLog.e("SendStartUpdate","SendStartUpdate:" + "====》value: "+value);


                char[] crc_data = CRC16.hexStringToByteArray(value);
                int crcData = CRC16.calcCrc16(crc_data, 0, crc_data.length - 4);
                byte[] crc_int_byte = CRC16.IntToByteArray(crcData);//1 0
                send_data[1038] = crc_int_byte[1];
                send_data[1039] = crc_int_byte[0];
                int sizedata=send_data.length;
                System.out.println(Arrays.toString(send_data));
                DLog.e("SendupdateBin","SendupdateBin:" + "send_data:"+sizedata);
                //        for (int i = 0; i < 53; i++) {
                //            try {
                //                if(i<52) {
                //                    byte[] b = new byte[20];
                //                    for (int j = 0; j < 20; j++) {
                //                        b[j] = send_data[20 * i + j];
                //                    }
                //                    System.out.println(Arrays.toString(b));
                //                    characteristic.setValue(b);
                //                    bluetoothGatt.writeCharacteristic(characteristic);
                //                }else{
                //                    byte[] bb = new byte[4];
                //                    for (int j = 0; j < 4; j++) {
                //                        bb [j] = send_data[20 * i + j];
                //                    }
                //                    System.out.println(Arrays.toString(bb));
                //                    characteristic.setValue(bb);
                //                    bluetoothGatt.writeCharacteristic(characteristic);
                //                }
                //                    Thread.sleep(400);
                //            } catch (InterruptedException e) {
                //                e.printStackTrace();
                //            }
                //
                //
                //        }
                List<byte[]> mlist=splitSendData(send_data);
                for(int i=0;i<mlist.size();i++){
                    try {
                        byte[] bb= mlist.get(i);
                        System.out.println(Arrays.toString(bb));
                        //                        mWriteCharacteristic(characteristic,bluetoothGatt,bb);
                        characteristic.setValue(bb);
                        boolean issuccess=bluetoothGatt.writeCharacteristic(characteristic);

                        DLog.e("SendupdateBin","SendupdateBin:" + "====》issuccess0: "+issuccess);
                        while (!issuccess){
                            Thread.sleep(20);
                            byte[] bb1= mlist.get(i);
                            characteristic.setValue(bb1);
                            boolean issuccess1=bluetoothGatt.writeCharacteristic(characteristic);
                            DLog.e("SendupdateBin","SendupdateBin:" + "====》issuccess1: "+issuccess1);
                            if(issuccess1){
                                issuccess=true;
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                //解决 蓝牙硬件bUG
                try {
                    String order2 = "7B A5 000000000101 0D0400000000 0000B75A";
                    byte[] send_data2 = BluetoothUtil.hex2Bytes(order2);
                    char[] crc_data2 = CRC16.hexStringToByteArray(order2);
                    int crcData2 = CRC16.calcCrc16(crc_data2, 0, crc_data2.length - 4);
                    byte[] crc_int_byte2 = CRC16.IntToByteArray(crcData2);//1 0
                    send_data2[14] = crc_int_byte2[1];
                    send_data2[15] = crc_int_byte2[0];
                    characteristic.setValue(send_data2);
                    boolean istrue = bluetoothGatt.writeCharacteristic(characteristic);
                    while(!istrue){
                        Thread.sleep(20);
                        characteristic.setValue(send_data2);
                        boolean istrue1 = bluetoothGatt.writeCharacteristic(characteristic);
                        if(istrue1){
                            istrue=true;
                        }
                    }
                    //                    mWriteCharacteristic(characteristic,bluetoothGatt,send_data2);
                }catch (Exception e){
                    e.printStackTrace();
                }

    }
    //完成固件数据上传命令
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static void sendFinishBin(BluetoothGattCharacteristic characteristic, BluetoothGatt bluetoothGatt, GuJianBean bean) {
        //Byte byteCommand[108] = {0x7B,0xA5,0x00,0x00,0x00,0x00,0x01,0x10, 0x01,0x02,0x00,0x00,0x00,0x00,  0x02,0x02,0x00,0x00,0x00,0x00,  0x03,0x02,0x00,0x00,0x00,0x00,  0x04,0x02,0x00,0x00,0x00,0x00,  0x05,0x02,0x00,0x00,0x00,0x00,  0x06,0x02,0x00,0x00,0x00,0x00,  0x07,0x02,0x00,0x00,0x00,0x00,  0x08,0x02,0x00,0x00,0x00,0x00,  0x09,0x02,0x00,0x00,0x00,0x00,  0x0A,0x02,0x00,0x00,0x00,0x00,  0x0B,0x02,0x00,0x00,0x00,0x00,  0x0C,0x02,0x00,0x00,0x00,0x00,  0x0D,0x02,0x00,0x00,0x00,0x00,  0x0E,0x02,0x00,0x00,0x00,0x00,  0x0F,0x02,0x00,0x00,0x00,0x00,  0x10,0x02,0x00,0x00,0x00,0x00,  0x00,0x00,0xB7,0x5A};

        String order = "7BA5 00000000 04 01 0285 00000000  0000  B75A";
        byte[] send_data = BluetoothUtil.hex2Bytes(order);

        String binStr=bean.getBinStr();
        byte[] bin_by = BluetoothUtil.hex2Bytes(binStr);
        int size=bin_by.length;
        double bao=bean.getPageNumber();
        int trueLenth=(int)(bao*1024d);
        int z1=trueLenth-size;
        StringBuffer sub=new StringBuffer();
        sub.append(binStr);
        for (int k = 0; k < z1; k++) {
            sub.append("ff");
        }
        String bytestr=sub.toString();


        char[] bin_data = CRC16.hexStringToByteArray(bytestr);
        int bincrcData = CRC16.calcCrc16(bin_data, 0, bin_data.length - 0); //CRC16校验 bin包
        byte[] bin_crc16 = CRC16.IntToByteArray(bincrcData);//1 0
        send_data[10] = bin_crc16[1];
        send_data[11] = bin_crc16[0];
        send_data[12] = bin_crc16[3];
        send_data[13] = bin_crc16[2];
        System.out.println(Arrays.toString(send_data));

        final StringBuilder stringBuilder = new StringBuilder(send_data.length);
        for (byte byteChar : send_data) {
            stringBuilder.append(String.format("%02X", byteChar));
        }
        String value = stringBuilder.toString();


        char[] crc_data = CRC16.hexStringToByteArray(value);
        int crcData = CRC16.calcCrc16(crc_data, 0, crc_data.length - 4);
        byte[] crc_int_byte = CRC16.IntToByteArray(crcData);//1 0

        send_data[14] = crc_int_byte[1];
        send_data[15] = crc_int_byte[0];
        characteristic.setValue(send_data);
        boolean istr=bluetoothGatt.writeCharacteristic(characteristic);

        try{
            while (!istr){
                Thread.sleep(20);

                characteristic.setValue(send_data);
                boolean issuccess2=bluetoothGatt.writeCharacteristic(characteristic);
                DLog.e("SendupdateBin","SendupdateBin:" + "====》issuccess1: "+issuccess2);
                if(issuccess2){
                    istr=true;
                }
            }

            //解决BUG
            String order2 = "7B A5 000000000101 0D0400000000 0000B75A";
            byte[] send_data2 = BluetoothUtil.hex2Bytes(order2);
            char[] crc_data2 = CRC16.hexStringToByteArray(order2);
            int crcData2 = CRC16.calcCrc16(crc_data2, 0, crc_data2.length - 4);
            byte[] crc_int_byte2 = CRC16.IntToByteArray(crcData2);//1 0
            send_data2[14] = crc_int_byte2[1];
            send_data2[15] = crc_int_byte2[0];
            characteristic.setValue(send_data2);
            boolean istrue = bluetoothGatt.writeCharacteristic(characteristic);
            while(!istrue){
                Thread.sleep(20);
                characteristic.setValue(send_data2);
                boolean istrue1 = bluetoothGatt.writeCharacteristic(characteristic);
                if(istrue1){
                    istrue=true;
                }
            }
            //            Thread.sleep(50);
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    public static void SendWritePara(BluetoothGattCharacteristic characteristic, BluetoothGatt bluetoothGatt, HDSendDataModel para) {
        //Byte byteCommand[108] = {0x7B,0xA5,0x00,0x00,0x00,0x00,0x01,0x10, 0x01,0x02,0x00,0x00,0x00,0x00,  0x02,0x02,0x00,0x00,0x00,0x00,  0x03,0x02,0x00,0x00,0x00,0x00,  0x04,0x02,0x00,0x00,0x00,0x00,  0x05,0x02,0x00,0x00,0x00,0x00,  0x06,0x02,0x00,0x00,0x00,0x00,  0x07,0x02,0x00,0x00,0x00,0x00,  0x08,0x02,0x00,0x00,0x00,0x00,  0x09,0x02,0x00,0x00,0x00,0x00,  0x0A,0x02,0x00,0x00,0x00,0x00,  0x0B,0x02,0x00,0x00,0x00,0x00,  0x0C,0x02,0x00,0x00,0x00,0x00,  0x0D,0x02,0x00,0x00,0x00,0x00,  0x0E,0x02,0x00,0x00,0x00,0x00,  0x0F,0x02,0x00,0x00,0x00,0x00,  0x10,0x02,0x00,0x00,0x00,0x00,  0x00,0x00,0xB7,0x5A};

        String order = "7BA5 000000000110 018200000000  028200000000  038200000000  048200000000  058200000000  068200000000  078200000000  088200000000  098200000000  0A8200000000  0B8200000000  0C8200000000  0D8200000000  0E8200000000  0F8200000000  108200000000  0000B75A";

        byte[] send_data = BluetoothUtil.hex2Bytes(order);
        //System.out.println(Arrays.toString(send_data));

        float m1 = para.mmv1;
        byte[] a1 = float2byte(m1);
        for (int i = 0; i < 4; i++) {
            send_data[10 + i] = a1[i];
        }
        float m2 = para.mmv2;
        byte[] a2 = float2byte(m2);
        for (int i = 0; i < 4; i++) {
            send_data[16 + i] = a2[i];
        }

        float m3 = para.mmv3;
        byte[] a3 = float2byte(m3);
        for (int i = 0; i < 4; i++) {
            send_data[22 + i] = a3[i];
        }

        float m4 = para.mmv4;
        byte[] a4 = float2byte(m4);
        for (int i = 0; i < 4; i++) {
            send_data[28 + i] = a4[i];
        }

        float m5 = para.mmv5;
        byte[] a5 = float2byte(m5);
        for (int i = 0; i < 4; i++) {
            send_data[34 + i] = a5[i];
        }
        float m6 = para.mmv6;
        byte[] a6 = float2byte(m6);
        for (int i = 0; i < 4; i++) {
            send_data[40 + i] = a6[i];
        }

        float m7 = para.mmv7;
        byte[] a7 = float2byte(m7);
        for (int i = 0; i < 4; i++) {
            send_data[46 + i] = a7[i];
        }

        float m8 = para.mmv8;
        byte[] a8 = float2byte(m8);
        for (int i = 0; i < 4; i++) {
            send_data[52 + i] = a8[i];
        }

        float m9 = para.mmv9;
        byte[] a9 = float2byte(m9);
        for (int i = 0; i < 4; i++) {
            send_data[58 + i] = a9[i];
        }

        float m10 = para.mmv10;
        byte[] a10 = float2byte(m10);
        for (int i = 0; i < 4; i++) {
            send_data[64 + i] = a10[i];
        }

        float m11 = para.mmv11;
        byte[] a11 = float2byte(m11);
        for (int i = 0; i < 4; i++) {
            send_data[70 + i] = a11[i];
        }

        float m12 = para.mmv12;
        byte[] a12 = float2byte(m12);
        for (int i = 0; i < 4; i++) {
            send_data[76 + i] = a12[i];
        }

        float m13 = para.mmv13;
        byte[] a13 = float2byte(m13);
        for (int i = 0; i < 4; i++) {
            send_data[82 + i] = a13[i];
        }

        float m14 = para.mmv14;
        byte[] a14 = float2byte(m14);
        for (int i = 0; i < 4; i++) {
            send_data[88 + i] = a14[i];
        }

        float m15 = para.mmv15;
        byte[] a15 = float2byte(m15);
        for (int i = 0; i < 4; i++) {
            send_data[94 + i] = a15[i];
        }

        float m16 = para.mmv16;
        byte[] a16 = float2byte(m16);
        for (int i = 0; i < 4; i++) {
            send_data[100 + i] = a16[i];
        }

        //System.out.println(Arrays.toString(send_data));

        final StringBuilder stringBuilder = new StringBuilder(send_data.length);
        for (byte byteChar : send_data) {
            stringBuilder.append(String.format("%02X", byteChar));
        }
        //                ToastUtil.showToast(mainActivity,stringBuilder.toString());
        //Log.e("CRC16",stringBuilder.toString());
        String value = stringBuilder.toString();
        //System.out.println(stringBuilder.toString());
        char[] crc_data = CRC16.hexStringToByteArray(value);

        int crcData = CRC16.calcCrc16(crc_data, 0, crc_data.length - 4);
        byte[] crc_int_byte = CRC16.IntToByteArray(crcData);//1 0
        send_data[104] = crc_int_byte[1];
        send_data[105] = crc_int_byte[0];
        for (int i = 0; i < 6; i++) {
            byte[] b = new byte[18];
            for (int j = 0; j < 18; j++) {
                b[j] = send_data[18 * i + j];
            }
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //System.out.println(Arrays.toString(b));
            characteristic.setValue(b);
            bluetoothGatt.writeCharacteristic(characteristic);
        }


    }

    public static void SendWritePara_2(BluetoothGattCharacteristic characteristic, BluetoothGatt bluetoothGatt, HDSendDataModel para) {
        //Byte byteCommand[108] = {0x7B,0xA5,0x00,0x00,0x00,0x00,0x01,0x10, 0x01,0x02,0x00,0x00,0x00,0x00,  0x02,0x02,0x00,0x00,0x00,0x00,  0x03,0x02,0x00,0x00,0x00,0x00,  0x04,0x02,0x00,0x00,0x00,0x00,  0x05,0x02,0x00,0x00,0x00,0x00,  0x06,0x02,0x00,0x00,0x00,0x00,  0x07,0x02,0x00,0x00,0x00,0x00,  0x08,0x02,0x00,0x00,0x00,0x00,  0x09,0x02,0x00,0x00,0x00,0x00,  0x0A,0x02,0x00,0x00,0x00,0x00,  0x0B,0x02,0x00,0x00,0x00,0x00,  0x0C,0x02,0x00,0x00,0x00,0x00,  0x0D,0x02,0x00,0x00,0x00,0x00,  0x0E,0x02,0x00,0x00,0x00,0x00,  0x0F,0x02,0x00,0x00,0x00,0x00,  0x10,0x02,0x00,0x00,0x00,0x00,  0x00,0x00,0xB7,0x5A};

        String order = "7BA5 000000000102 018200000000  028200000000    0000B75A";

        byte[] send_data = BluetoothUtil.hex2Bytes(order);
        //System.out.println(Arrays.toString(send_data));

        float m1 = para.mmv1;
        byte[] a1 = float2byte(m1);
        for (int i = 0; i < 4; i++) {
            send_data[10 + i] = a1[i];
        }
        float m2 = para.mmv2;
        byte[] a2 = float2byte(m2);
        for (int i = 0; i < 4; i++) {
            send_data[16 + i] = a2[i];
        }


        //System.out.println(Arrays.toString(send_data));

        final StringBuilder stringBuilder = new StringBuilder(send_data.length);
        for (byte byteChar : send_data) {
            stringBuilder.append(String.format("%02X", byteChar));
        }
        //                ToastUtil.showToast(mainActivity,stringBuilder.toString());
        //Log.e("CRC16",stringBuilder.toString());
        String value = stringBuilder.toString();
        //System.out.println(stringBuilder.toString());
        char[] crc_data = CRC16.hexStringToByteArray(value);

        int crcData = CRC16.calcCrc16(crc_data, 0, crc_data.length - 4);
        byte[] crc_int_byte = CRC16.IntToByteArray(crcData);//1 0
        send_data[20] = crc_int_byte[1]; //104
        send_data[21] = crc_int_byte[0];   //105
        for (int i = 0; i < 2; i++) {
            if(i==0){
                byte[] b = new byte[18];
                for (int j = 0; j < 18; j++) {
                    b[j] = send_data[18 * i + j];
                }
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                characteristic.setValue(b);
                bluetoothGatt.writeCharacteristic(characteristic);
                DLog.e("SendWritePara_2","SendWritePara_2"+Arrays.toString(b));
            }else{
                byte[] b1 = new byte[6];
                for (int j = 0; j < 6; j++) {
                    b1[j] = send_data[18 * i + j];
                }
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                characteristic.setValue(b1);
                bluetoothGatt.writeCharacteristic(characteristic);
                DLog.e("SendWritePara_2","SendWritePara_2"+Arrays.toString(b1));
            }
        }


    }

    public static void SendWritePara_3(BluetoothGattCharacteristic characteristic, BluetoothGatt bluetoothGatt, HDSendDataModel para) {
        //Byte byteCommand[108] = {0x7B,0xA5,0x00,0x00,0x00,0x00,0x01,0x10, 0x01,0x02,0x00,0x00,0x00,0x00,  0x02,0x02,0x00,0x00,0x00,0x00,  0x03,0x02,0x00,0x00,0x00,0x00,  0x04,0x02,0x00,0x00,0x00,0x00,  0x05,0x02,0x00,0x00,0x00,0x00,  0x06,0x02,0x00,0x00,0x00,0x00,  0x07,0x02,0x00,0x00,0x00,0x00,  0x08,0x02,0x00,0x00,0x00,0x00,  0x09,0x02,0x00,0x00,0x00,0x00,  0x0A,0x02,0x00,0x00,0x00,0x00,  0x0B,0x02,0x00,0x00,0x00,0x00,  0x0C,0x02,0x00,0x00,0x00,0x00,  0x0D,0x02,0x00,0x00,0x00,0x00,  0x0E,0x02,0x00,0x00,0x00,0x00,  0x0F,0x02,0x00,0x00,0x00,0x00,  0x10,0x02,0x00,0x00,0x00,0x00,  0x00,0x00,0xB7,0x5A};

        String order = "7BA5 000000000103 018200000000  028200000000  038200000000   0000B75A";

        byte[] send_data = BluetoothUtil.hex2Bytes(order);
        //System.out.println(Arrays.toString(send_data));

        float m1 = para.mmv1;
        byte[] a1 = float2byte(m1);
        for (int i = 0; i < 4; i++) {
            send_data[10 + i] = a1[i];
        }
        float m2 = para.mmv2;
        byte[] a2 = float2byte(m2);
        for (int i = 0; i < 4; i++) {
            send_data[16 + i] = a2[i];
        }

        float m3 = para.mmv3;
        byte[] a3 = float2byte(m3);
        for (int i = 0; i < 4; i++) {
            send_data[22 + i] = a3[i];
        }

        //System.out.println(Arrays.toString(send_data));

        final StringBuilder stringBuilder = new StringBuilder(send_data.length);
        for (byte byteChar : send_data) {
            stringBuilder.append(String.format("%02X", byteChar));
        }
        //                ToastUtil.showToast(mainActivity,stringBuilder.toString());
        //Log.e("CRC16",stringBuilder.toString());
        String value = stringBuilder.toString();
        //System.out.println(stringBuilder.toString());
        char[] crc_data = CRC16.hexStringToByteArray(value);

        int crcData = CRC16.calcCrc16(crc_data, 0, crc_data.length - 4);
        byte[] crc_int_byte = CRC16.IntToByteArray(crcData);//1 0
        send_data[26] = crc_int_byte[1]; //104
        send_data[27] = crc_int_byte[0];   //105
        for (int i = 0; i < 2; i++) {
            if(i==0){
                byte[] b = new byte[18];
                for (int j = 0; j < 18; j++) {
                    b[j] = send_data[18 * i + j];
                }
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                characteristic.setValue(b);
                bluetoothGatt.writeCharacteristic(characteristic);
                DLog.e("SendWritePara_3","SendWritePara_3"+Arrays.toString(b));
            }else{
                byte[] b1 = new byte[12];
                for (int j = 0; j < 12; j++) {
                    b1[j] = send_data[18 * i + j];
                }
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                characteristic.setValue(b1);
                bluetoothGatt.writeCharacteristic(characteristic);
                DLog.e("SendWritePara_3","SendWritePara_3"+Arrays.toString(b1));
            }

        }


    }

    public static void SendWritePara_4(BluetoothGattCharacteristic characteristic, BluetoothGatt bluetoothGatt, HDSendDataModel para) {
        //Byte byteCommand[108] = {0x7B,0xA5,0x00,0x00,0x00,0x00,0x01,0x10, 0x01,0x02,0x00,0x00,0x00,0x00,  0x02,0x02,0x00,0x00,0x00,0x00,  0x03,0x02,0x00,0x00,0x00,0x00,  0x04,0x02,0x00,0x00,0x00,0x00,  0x05,0x02,0x00,0x00,0x00,0x00,  0x06,0x02,0x00,0x00,0x00,0x00,  0x07,0x02,0x00,0x00,0x00,0x00,  0x08,0x02,0x00,0x00,0x00,0x00,  0x09,0x02,0x00,0x00,0x00,0x00,  0x0A,0x02,0x00,0x00,0x00,0x00,  0x0B,0x02,0x00,0x00,0x00,0x00,  0x0C,0x02,0x00,0x00,0x00,0x00,  0x0D,0x02,0x00,0x00,0x00,0x00,  0x0E,0x02,0x00,0x00,0x00,0x00,  0x0F,0x02,0x00,0x00,0x00,0x00,  0x10,0x02,0x00,0x00,0x00,0x00,  0x00,0x00,0xB7,0x5A};

        String order = "7BA5 000000000104 018200000000  028200000000  038200000000  048200000000   0000B75A";

        byte[] send_data = BluetoothUtil.hex2Bytes(order);
        //System.out.println(Arrays.toString(send_data));

        float m1 = para.mmv1;
        byte[] a1 = float2byte(m1);
        for (int i = 0; i < 4; i++) {
            send_data[10 + i] = a1[i];
        }
        float m2 = para.mmv2;
        byte[] a2 = float2byte(m2);
        for (int i = 0; i < 4; i++) {
            send_data[16 + i] = a2[i];
        }

        float m3 = para.mmv3;
        byte[] a3 = float2byte(m3);
        for (int i = 0; i < 4; i++) {
            send_data[22 + i] = a3[i];
        }

        float m4 = para.mmv4;
        byte[] a4 = float2byte(m4);
        for (int i = 0; i < 4; i++) {
            send_data[28 + i] = a4[i];
        }


        //System.out.println(Arrays.toString(send_data));

        final StringBuilder stringBuilder = new StringBuilder(send_data.length);
        for (byte byteChar : send_data) {
            stringBuilder.append(String.format("%02X", byteChar));
        }
        //                ToastUtil.showToast(mainActivity,stringBuilder.toString());
        //Log.e("CRC16",stringBuilder.toString());
        String value = stringBuilder.toString();
        //System.out.println(stringBuilder.toString());
        char[] crc_data = CRC16.hexStringToByteArray(value);

        int crcData = CRC16.calcCrc16(crc_data, 0, crc_data.length - 4);
        byte[] crc_int_byte = CRC16.IntToByteArray(crcData);//1 0
        send_data[32] = crc_int_byte[1]; //104
        send_data[33] = crc_int_byte[0];   //105
        for (int i = 0; i < 2; i++) {
            byte[] b = new byte[18];
            for (int j = 0; j < 18; j++) {
                b[j] = send_data[18 * i + j];
            }
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //System.out.println(Arrays.toString(b));
            characteristic.setValue(b);
            bluetoothGatt.writeCharacteristic(characteristic);

            DLog.e("SendWritePara_4","SendWritePara_4"+Arrays.toString(b));
        }


    }

    //    +(void) SendClearZero:(CBPeripheral *)port andChar:(CBCharacteristic *)myChar
    //    {
    //        if((port != nil) && (myChar != nil))
    //        {
    //            Byte byteCommand[18] =
    //            int x = [CRCCaculate GetCRC16:byteCommand andLen:14 andStart:0];
    //            byteCommand[14] = (x)&0x00FF;
    //            byteCommand[15] = (x>>8)&0xFF;
    //            NSData *data = [[NSData alloc] initWithBytes:byteCommand length:18];
    //        [port writeValue:data forCharacteristic:myChar type:CBCharacteristicWriteWithResponse];
    //        }
    //    }

    public static byte[] hex2Bytes(String src) {
        src = src.replaceAll(" ", "");

        if (src.length() % 2 != 0) {
            return null;
        }

        byte[] res = new byte[src.length() / 2];
        char[] chs = src.toCharArray();
        for (int i = 0, c = 0; i < chs.length; i += 2, c++) {
            res[c] = (byte) (Integer.parseInt(new String(chs, i, 2), 16));
        }

        return res;
    }

    public static byte[] float2byte(float f) {

        // 把float转换为byte[]
        int fbit = Float.floatToIntBits(f);

        byte[] b = new byte[4];
        for (int i = 0; i < 4; i++) {
            b[i] = (byte) (fbit >> (24 - i * 8));
        }

        // 翻转数组
        int len = b.length;
        // 建立一个与源数组元素类型相同的数组
        byte[] dest = new byte[len];
        // 为了防止修改源数组，将源数组拷贝一份副本
        System.arraycopy(b, 0, dest, 0, len);
        byte temp;
        // 将顺位第i个与倒数第i个交换
        for (int i = 0; i < len / 2; ++i) {
            temp = dest[i];
            dest[i] = dest[len - i - 1];
            dest[len - i - 1] = temp;
        }

        return dest;

    }

    public static char[] getChars(byte[] bytes) {
        Charset cs = Charset.forName("UTF-8");
        ByteBuffer bb = ByteBuffer.allocate(bytes.length);
        bb.put(bytes);
        bb.flip();
        CharBuffer cb = cs.decode(bb);
        return cb.array();
    }

    public static void pda() {
        //        String topStr="7E 45 00 01 1000 00000000 00000000 00000000 00000000 0000";
        //        byte[] value_array=BluetoothUtil.hex2Bytes(topStr);
        //        int i=6;
        //        int j=0;
        //        //位置7-10
        //        float c1= Float.parseFloat(channel_1.getText().toString());
        //        byte[] c1_array=BluetoothUtil.float2byte(c1);
        //        value_array[i++]=c1_array[j++];
        //        value_array[i++]=c1_array[j++];
        //        value_array[i++]=c1_array[j++];
        //        value_array[i++]=c1_array[j++];
        //        //位置11-14
        //        float c2= Float.parseFloat(channel_2.getText().toString());
        //        byte[] c2_array=BluetoothUtil.float2byte(c2);
        //        j=0;
        //        value_array[i++]=c2_array[j++];
        //        value_array[i++]=c2_array[j++];
        //        value_array[i++]=c2_array[j++];
        //        value_array[i++]=c2_array[j++];
        //        //位置15-18
        //        float c3= Float.parseFloat(channel_3.getText().toString());
        //        byte[] c3_array=BluetoothUtil.float2byte(c3);
        //        j=0;
        //        value_array[i++]=c3_array[j++];
        //        value_array[i++]=c3_array[j++];
        //        value_array[i++]=c3_array[j++];
        //        value_array[i++]=c3_array[j++];
        //        //位置19-22
        //        float c4= Float.parseFloat(channel_4.getText().toString());
        //        byte[] c4_array=BluetoothUtil.float2byte(c4);
        //        j=0;
        //        value_array[i++]=c4_array[j++];
        //        value_array[i++]=c4_array[j++];
        //        value_array[i++]=c4_array[j++];
        //        value_array[i++]=c4_array[j++];
        //        //位置23-24
        //        final StringBuilder stringBuilder = new StringBuilder(value_array.length);
        //        for(byte byteChar : value_array) {
        //            stringBuilder.append(String.format("%02X", byteChar));
        //        }
        //        //                ToastUtil.showToast(mainActivity,stringBuilder.toString());
        //        Log.e("CRC16",stringBuilder.toString());
        //        String value=stringBuilder.toString();
        //        char[] data_byte=CRC16.hexStringToByteArray(value);
        //        //Log.e("insert",stringBuilder.toString());
        //        int crcData = CRC16.calcCrc16(data_byte,0,data_byte.length - 2);
        //        byte[] crc_bytes=CRC16.IntToByteArray(crcData);//1 0
        //        value=value.substring(0,value.length()-4)+ String.format("%02X", crc_bytes[1])+ String.format("%02X", crc_bytes[0]);
        //
        //        //ToastUtil.showToast(mainActivity,value);
        //        mainActivity.writeToBluetooth(value);

    }
    public static int size=20;
    //每次写入20个字节 把数据分解成20个字节一个部分一个部分
    public static List<byte[]> splitSendData(byte[] data) {
        if(data == null) return null;
        List<byte[]> datas = new ArrayList<byte[]>();
        if(data.length > size){
            int num = data.length % size== 0 ? data.length / size : data.length / size + 1;
            for(int i = 0; i < num; i ++) {
                if(data.length % size == 0){
                    byte[] d = new byte[size];
                    for(int j = 0; j < size && i * size + j < data.length ; j ++) {
                        d[j] = data[i * size + j];
                    }
                    datas.add(d);
                } else {
                    byte[] d;
                    if(i == num - 1) {
                        d = new byte[data.length - i * size];
                    } else {
                        d = new byte[size];
                    }
                    for(int j = 0; j < size && i * size + j < data.length ; j ++) {
                        d[j] = data[i * size + j];
                    }
                    datas.add(d);
                }
            }
        } else {
            datas.add(data);
        }
        return datas;
    }





    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static void mWriteCharacteristic(BluetoothGattCharacteristic characteristic,BluetoothGatt mBluetoothGatt, byte[] value){
        if (mBluetoothGatt == null) {
            return;
        }
        BluetoothGattService service = null;
        long enterTime = System.currentTimeMillis();
        while ((System.currentTimeMillis() - enterTime) < HONEY_CMD_TIMEOUT) {
            if(isDeviceBusy(mBluetoothGatt)){
                try {
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }else {
                break;
            }
        }
        try {
            //            service = mBluetoothGatt.getService(SER_UUID);
            //            BluetoothGattCharacteristic characteristic = service.getCharacteristic(SPP_UUID);
            characteristic.setValue(value);
            boolean status = mBluetoothGatt.writeCharacteristic(characteristic);
            DLog.e("potter123","status:"+status);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private static long HONEY_CMD_TIMEOUT = 2000;
    //判断设备 是否在busy状态
    public static boolean isDeviceBusy(BluetoothGatt mBluetoothGatt){
        boolean state = false;
        try {
            state = (boolean)readField(mBluetoothGatt,"mDeviceBusy");//反射获取 设备busy状态
            Log.e("potter123","isDeviceBusy:"+state);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return state;
    }
    public static Object readField(BluetoothGatt mBluetoothGatt, String name) throws IllegalAccessException, NoSuchFieldException {
        Field field = mBluetoothGatt.getClass().getDeclaredField(name);
        field.setAccessible(true);
        return field.get(mBluetoothGatt);
    }
}
