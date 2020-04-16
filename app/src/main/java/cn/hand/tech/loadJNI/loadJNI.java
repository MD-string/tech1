package cn.hand.tech.loadJNI;

/**
 * Created by hxz on 2018-07-04.
 */

public class loadJNI {

    static {
        System.loadLibrary("JniLibrary");
    }

    public static native double getWeight(double[] Sensor_data,double[] x,double[] y, double clear_flag, CanshuBean bean);

}

