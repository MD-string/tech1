package cn.hand.tech.ble.bleUtil;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.le.BluetoothLeScanner;
import android.os.DeadObjectException;

import java.lang.reflect.Method;
import java.util.Set;

import cn.hand.tech.log.DLog;

/**
 * Created by hand-hitech2 on 2018-04-02.
 */

public class BleUtils  {
    private static final String TAG = "BleUtils";
    //得到配对的设备列表，清除已配对的设备
    public static void  removePairDevice(BluetoothAdapter btAdapt){
        if(btAdapt!=null){

            Set<BluetoothDevice> bondedDevices = btAdapt.getBondedDevices();
            for(BluetoothDevice device : bondedDevices ){
                DLog.e(TAG,"移除配对的蓝牙设备=="+device.getAddress());
                try {
                    Method m = device.getClass().getMethod("removeBond", (Class[]) null);
                    m.invoke(device, (Object[]) null);
                } catch (Exception e) {
                    DLog.e(TAG,"移除配对异常=="+ e.getMessage());
                }
            }

        }

    }

    //反射来调用
    public static void cleanupScanner(BluetoothLeScanner scanner) {

        if (scanner!=null){
            try {
                Method m =scanner.getClass().getMethod("cleanup", (Class[]) null);
                DLog.e(TAG,"移除清楚扫描异常m=="+m);
                if (m== null) {
                    DLog.e(TAG,"移除清除扫描异常");
                    return;
                }
                m.invoke(scanner, (Object[]) null);
            } catch (Exception e) {
                DLog.e(TAG,"移除清楚扫描异常=="+e.getMessage());
            }
        }

    }
    public static  void refreshCache(BluetoothGatt gatt) throws DeadObjectException {

        try {
            BluetoothGatt localBluetoothGatt = gatt;
            Method localMethod = localBluetoothGatt.getClass().getMethod("refresh");
            DLog.e(TAG,"刷新设备localMethod==" + localMethod);
            if (localMethod == null) {
                return;
            }
            localMethod.invoke(localBluetoothGatt);
            DLog.e(TAG,"刷新蓝牙设备缓存成功" );
        } catch (Exception localException) {
            DLog.e(TAG,"当刷新设备时，An exception occured while refreshing device");
        }
    }


    protected String decodeMacAddress(byte[] scanRecord) {
        StringBuilder sb = new StringBuilder();
        for (int i = 14; i >= 9; i--) {
            if (i != 9) {
                String macBit = Integer.toHexString(scanRecord[i] & 0xff);
                if (macBit.length() <= 1) {
                    macBit = "0" + macBit;
                }
                sb.append(macBit);
                sb.append(":");
            } else {
                String macBit = Integer.toHexString(scanRecord[i] & 0xff);
                if (macBit.length() <= 1) {
                    macBit = "0" + macBit;
                }
                sb.append(macBit);
            }
        }

        return sb.toString().toUpperCase();
    }
    /**
     * 启用和禁用通知
     *
     * @param characteristic
     * @param enabled
     */
    public void setCharacteristicNotification(
            BluetoothGattCharacteristic characteristic, boolean enabled,BluetoothAdapter mBluetoothAdapter,BluetoothGatt mBluetoothGatt) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            DLog.e(TAG,"蓝牙适配器未初始化.");
            return;
        }
        mBluetoothGatt.setCharacteristicNotification(characteristic, enabled);
        BluetoothGattDescriptor descriptor = characteristic.getDescriptor(BleConstant.CONFIG_DESCRIPTOR_UUID);
        descriptor.setValue(enabled ? BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE :
                BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE);
        mBluetoothGatt.writeDescriptor(descriptor);
    }

}
