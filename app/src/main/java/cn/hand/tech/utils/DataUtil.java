package cn.hand.tech.utils;


import java.util.HashMap;
import java.util.List;

import cn.hand.tech.ui.weight.bean.CarInfo;

public class DataUtil {


    //. 循环list中的所有元素然后删除重复
    public static List<CarInfo> removeDuplicate(List<CarInfo>  list, List<CarInfo>  addlist)  {
        try{
            for  ( int  i  =   0 ; i  <  addlist.size()  ; i ++ )  {
                boolean issame=false;
                CarInfo info=addlist.get(i);
                for  ( int  j  =  0 ; j  <list.size(); j ++)  {
                    if  ((list.get(j).getDeviceId()).equalsIgnoreCase(info.getDeviceId()))  {//说明相同
                        issame=true;
                        String ver1=list.get(j).getVersion();
                        String ver2=info.getVersion();
                        if(!ver1.equalsIgnoreCase(ver2)){ //版本号不同  说明有更新
                            list.remove(j);
                            info.setNew(true);
                            list.add(info);
                        }
                    }
                }
                if(!issame){
                    info.setNew(true);
                    list.add(info);
                }
            }
            return list;
        }catch (Exception e){
            e.printStackTrace();
            return list;
        }

    }

    //江西厂区地址  鱼峰厂区地址
    public  static  HashMap<String, LatLng> getFatoryAddress(){
        HashMap<String, LatLng> adMap=new HashMap<>();
        adMap.put("信丰厂区",new LatLng(114.8931803, 25.4516317));
        adMap.put("龙南厂区",new LatLng(114.795931, 24.8482056));
        adMap.put("潭东厂区",new LatLng(114.8926595, 25.4512207));
        adMap.put("赣江厂区",new LatLng(115.9580892, 28.8349813));
        adMap.put("庐山厂区",new LatLng(116.1228353, 29.6041382));
        adMap.put("分宜厂区",new LatLng(114.7508138, 27.8610311));
        adMap.put("进贤厂区",new LatLng(116.4459635, 28.3013957));
        adMap.put("南昌厂区",new LatLng(116.4458822, 28.3012573));
        adMap.put("建阳厂区",new LatLng(117.3803548, 28.626884));
        adMap.put("弋阳厂区  ",new LatLng(117.3804687, 28.6271606));

        adMap.put("鱼峰股份",new LatLng(109.2451009,24.369633));

        adMap.put("信丰物资车",new LatLng( 114.8892578,25.4559163));
        adMap.put("弋阳物资车",new LatLng(117.3782389,28.6255046));
        adMap.put("分宜物资车",new LatLng( 114.7479492,27.8543945));


        adMap.put("遵义赛德",new LatLng(106.977395,27.606479 ));
        adMap.put("正安瑞溪",new LatLng(  107.427905,28.605809));
        adMap.put("习水赛德",new LatLng(106.269269,28.317398));
        adMap.put("遵义恒聚",new LatLng( 106.81077,27.448901));
        adMap.put("沿河水泥",new LatLng(108.46681,28.492822));
        adMap.put("印江水泥",new LatLng(  108.468121,27.996552));
        adMap.put("松桃水泥",new LatLng(108.950164,28.000011));
        adMap.put("玉屏水泥",new LatLng( 109.135437,27.473135));
        adMap.put("秀山水泥",new LatLng(109.073591,28.385668));
        adMap.put("思南水泥 ",new LatLng(108.234185,27.934547));
        return  adMap;

    }

    public static String  dohandlerDistance(LatLng car,LatLng me){

        GPS map=GPSConverterUtils.gcj02_To_Bd09(me.getLat(),me.getLon());
        double mlon=map.getLon();
        double mlat=map.getLat();
        String str=distance(car.getLon(),car.getLat(),mlon,mlat)+"";
        return str;
    }


    /**
     * 地球半径，单位：公里/千米
     */
    private static final double EARTH_RADIUS = 6378.137;

    /**
     * 经纬度转化成弧度
     * @param d  经度/纬度
     * @return  经纬度转化成的弧度
     */
    private static double radian(double d) {
        return d * Math.PI / 180.0;
    }

    /**
     * 返回两个地理坐标之间的距离
     * @param firsLongitude 第一个坐标的经度
     * @param firstLatitude 第一个坐标的纬度
     * @param secondLongitude 第二个坐标的经度
     * @param secondLatitude  第二个坐标的纬度
     * @return 两个坐标之间的距离，单位：公里/千米
     */
    public static double distance(double firsLongitude,double firstLatitude,double secondLongitude,double secondLatitude)
    {
        double firstRadianLongitude = radian(firsLongitude);
        double firstRadianLatitude = radian(firstLatitude);
        double secondRadianLongitude = radian(secondLongitude);
        double secondRadianLatitude = radian(secondLatitude);

        double a = firstRadianLatitude - secondRadianLatitude;
        double b = firstRadianLongitude - secondRadianLongitude;
        double cal = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(firstRadianLatitude) * Math.cos(secondRadianLatitude)
                * Math.pow(Math.sin(b / 2), 2)));
        cal = cal * EARTH_RADIUS;

        return Math.round(cal * 10000d) / 10000d;

    }



}
