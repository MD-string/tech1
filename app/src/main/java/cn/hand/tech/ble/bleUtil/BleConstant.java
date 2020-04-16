package cn.hand.tech.ble.bleUtil;

import java.util.UUID;

public class BleConstant {

	public final static String ACTION_BLE_DEVICE_FOUND = "cn.hande.ble.BLE_DEVICE_FOUND";//发现设备
	public final static String ACTION_BLE_NONSUPPORT = "cn.hande.ble.ACTION_BLE_NONSUPPORT";//不支持蓝牙
	public final static String ACTION_BLE_HANDLER_DATA = "cn.hande.ble.ACTION_BLE_HANDLER_DATA";//接收通知数据
	public final static String ACTION_BLE_CLEAR_ZERO = "cn.hande.ble.ACTION_BLE_CLEAR_ZERO"; //清零

	public final static String ACTION_BLE_CONNECTED = "cn.hande.ble.ACTION_BLE_CONNECTED"; //连接成功
	public final static String ACTION_BLE_DISCONNECT = "cn.hande.ble.ACTION_BLE_DISCONNECT"; //连接断开
	public final static String ACTION_BLE_CONNECTION_EXCEPTION = "cn.hande.ble.ACTION_BLE_CONNECTION_EXCEPTION"; //连接异常
	public final static String ACTION_BLE_CONNECTION_RSSI = "cn.hande.ble.ACTION_BLE_CONNECTION_RSSI"; //连接信号

	public final static String ACTION_SAVE_SUCCESS = "cn.hande.ble.ACTION_SAVE_SUCCESS"; //数据保存成功

	public final static String ACTION_SEND_DATA = "cn.hande.ble.ACTION_SEND_DATA"; //数据保存成功

	public final static String ACTION_CHANNEL_CHANGE = "cn.hande.ble.ACTION_CHANNEL_CHANGE"; //通道选择
	public final static String ACTION_UNIT_CHANGE = "cn.hande.ble.ACTION_UNIT_CHANGE"; //重量单位

	public final static String ACTION_BLE_READ_NOTY = "cn.hande.ble.ACTION_BLE_READ_NOTY"; //重量单位

	public final static String ACTION_BLE_WRITE_COE = "cn.hande.ble.ACTION_BLE_WRITE_COE"; //重量单位

	public final static String ACTION_BLE_WRITE_KB = "cn.hande.ble.ACTION_BLE_WRITE_KB"; //LocalDataDetailActivity KB

	public final static String EXTRA_DEVICE = "extra_device";

	public final static UUID CONFIG_DESCRIPTOR_UUID = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");

	public final static String ACTION_BLE_START_SEARCH = "cn.hande.ble.ACTION_BLE_START_SEARCH"; //开始搜索蓝牙

	public final static String ACTION_BLE_STOP_SEARCH = "cn.hande.ble.ACTION_BLE_STOP_SEARCH"; //停止搜索蓝牙

	public final static String ACTION_BLE_CONNECT = "cn.hande.ble.ACTION_BLE_CONNECT"; //蓝牙连接

	public final static String ACTION_SEND_REPAIR_INFO_SUCCESS = "cn.hande.ble.ACTION_SEND_REPAIR_INFO_SUCCESS"; //蓝牙连接

	public final static String ACTION_RELOAD_SYS = "cn.hande.ble.ACTION_RELOAD_SYS"; //重启主机
	public final static String ACTION_RELOAD_SYS_SUCCESS = "cn.hande.ble.ACTION_RELOAD_SYS_SUCCESS"; //重启主机

	public final static String ACTION_RETURN_BACK = "cn.hande.ble.ACTION_RETURN_BACK"; //恢复出厂设置
	public final static String ACTION_RETURN_BACK_SUCCESS = "cn.hande.ble.ACTION_RETURN_BACK_SUCCESS"; //恢复出厂设置成功



	public final static String ACTION_READ_HARD_SOFT = "cn.hande.ble.ACTION_READ_HARD_SOFT"; //硬件版本号
	public final static String ACTION_READ_HARD_SOFT_SUSSCESS = "cn.hande.ble.ACTION_READ_HARD_SOFT_SUSSCESS"; //获取硬件版本号成功

	public final static String ACTION_READ_SOFT = "cn.hande.ble.ACTION_READ_SOFT"; //软件版本号
	public final static String ACTION_READ_SOFT_SUSSCESS = "cn.hande.ble.ACTION_READ_SOFT_SUSSCESS"; //获取软件版本号成功

	public final static String ACTION_ACUTO_CHECK = "cn.hande.ble.ACTION_ACUTO_CHECK"; //自动检测
    public final static String ACTION_AUTO_DIANYA = "cn.hande.ble.ACTION_AUTO_DIANYA"; //电压
	public final static String ACTION_GPRS = "cn.hande.ble.ACTION_GPRS"; //电压
	public final static String ACTION_GPRSSIR = "cn.hande.ble.ACTION_GPRSSIR"; //GPRSrssi
	public final static String ACTION_GPS_CHECK = "cn.hande.ble.ACTION_GPS_CHECK"; //GPS检测

    public final static String ACTION_GPS_CHECK_LOC = "cn.hande.ble.ACTION_GPS_CHECK_LOC"; //GPS检测狀態
    public final static String ACTION_GPS_CHECK_LINE = "cn.hande.ble.ACTION_GPS_CHECK_LINE"; //GPS检测狀態 天綫

	public final static String ACTION_CHUAN_GAN_QI = "cn.hande.ble.ACTION_CHUAN_GAN_QI"; //传感器检测
	public final static String ACTION_CAI_JI_QI = "cn.hande.ble.ACTION_CAI_JI_QI"; //采集器检测

	public final static String ACTION_READ_DEVICE_ID = "cn.hande.ble.ACTION_READ_DEVICE_ID"; //主动获取设备ID

    public final static String ACTION_START_UPDATE_BIN = "cn.hande.ble.ACTION_START_UPDATE_BIN"; //准备升级固件
	public final static String ACTION_UPDATE_BIN_SUCCESS = "cn.hande.ble.ACTION_UPDATE_BIN_SUCCESS"; //固件升级
	public final static String ACTION_UPDATE_BIN = "cn.hande.ble.ACTION_UPDATE_BIN"; //固件升级

	public final static String ACTION_ING_BIN_SUSSESS = "cn.hande.ble.ACTION_ING_BIN_SUSSESS"; //固件上传  成功应答

	public final static String ACTION_BIN_OVER = "cn.hande.ble.ACTION_BIN_OVER"; //固件上传完成 发送完成命令

	public final static String ACTION_BIN_OVER_STATUS = "cn.hande.ble.ACTION_BIN_OVER_STATUS"; //固件上传完成 发送完成命令应答
	public final static String ACTION_BIN_GPS_TIME = "cn.hande.ble.ACTION_BIN_GPS_TIME"; //固件上传完成 发送完成命令应答

	public static final int HD_AD_SAMPLING_1 = 1;
	public static final int HD_AD_SAMPLING_2 = 2;
	public static final int HD_AD_SAMPLING_3 = 3;
	public static final int HD_AD_SAMPLING_4 = 4;
	public static final int HD_AD_SAMPLING_5 = 5;
	public static final int HD_AD_SAMPLING_6 = 6;
	public static final int HD_AD_SAMPLING_7 = 7;
	public static final int HD_AD_SAMPLING_8 = 8;

	public static final int HD_AD_SAMPLING_9 = 9;
	public static final int HD_AD_SAMPLING_10 = 10;
	public static final int HD_AD_SAMPLING_11 = 11;
	public static final int HD_AD_SAMPLING_12 = 12;
	public static final int HD_AD_SAMPLING_13 = 13;
	public static final int HD_AD_SAMPLING_14 = 14;
	public static final int HD_AD_SAMPLING_15 = 15;
	public static final int HD_AD_SAMPLING_16 = 16;

	public static final int HD_AD_SAMPLING_17 = 17;
	public static final int HD_AD_SAMPLING_18 = 18;
	public static final int HD_AD_SAMPLING_19 = 19;
	public static final int HD_AD_SAMPLING_20 = 20;
	public static final int HD_AD_SAMPLING_21 = 21;
	public static final int HD_AD_SAMPLING_22 = 22;
	public static final int HD_AD_SAMPLING_23 = 23;
	public static final int HD_AD_SAMPLING_24 = 24;

	public static final int HD_AD_SAMPLING_25 = 25;
	public static final int HD_AD_SAMPLING_26 = 26;
	public static final int HD_AD_SAMPLING_27 = 27;
	public static final int HD_AD_SAMPLING_28 = 28;
	public static final int HD_AD_SAMPLING_29 = 29;
	public static final int HD_AD_SAMPLING_30 = 30;
	public static final int HD_AD_SAMPLING_31 = 31;
	public static final int HD_AD_SAMPLING_32 = 32;

	public static final int HD_AD_SAMPLING_33 = 33;
	public static final int HD_AD_SAMPLING_34 = 34;
	public static final int HD_AD_SAMPLING_35 = 35;
	public static final int HD_AD_SAMPLING_36 = 36;
	public static final int HD_AD_SAMPLING_37 = 37;
	public static final int HD_AD_SAMPLING_38 = 38;
	public static final int HD_AD_SAMPLING_39 = 39;
	public static final int HD_AD_SAMPLING_40 = 40;

	public static final int HD_AD_SAMPLING_41 = 41;
	public static final int HD_AD_SAMPLING_42 = 42;
	public static final int HD_AD_SAMPLING_43 = 43;
	public static final int HD_AD_SAMPLING_44 = 44;
	public static final int HD_AD_SAMPLING_45 = 45;
	public static final int HD_AD_SAMPLING_46 = 46;
	public static final int HD_AD_SAMPLING_47 = 47;
	public static final int HD_AD_SAMPLING_48 = 48;

	public static final int HD_AD_SAMPLING_49 = 49;
	public static final int HD_AD_SAMPLING_50 = 50;
	public static final int HD_AD_SAMPLING_51 = 51;
	public static final int HD_AD_SAMPLING_52 = 52;
	public static final int HD_AD_SAMPLING_53 = 53;
	public static final int HD_AD_SAMPLING_54 = 54;
	public static final int HD_AD_SAMPLING_55 = 55;
	public static final int HD_AD_SAMPLING_56 = 56;

	public static final int HD_AD_SAMPLING_57 = 57;
	public static final int HD_AD_SAMPLING_58 = 58;
	public static final int HD_AD_SAMPLING_59 = 59;
	public static final int HD_AD_SAMPLING_60 = 60;
	public static final int HD_AD_SAMPLING_61 = 61;
	public static final int HD_AD_SAMPLING_62 = 62;
	public static final int HD_AD_SAMPLING_63 = 63;
	public static final int HD_AD_SAMPLING_64 = 64;


	public static final int HD_AD_ZERO_1 = 257;
	public static final int HD_AD_ZERO_2 = 258;
	public static final int HD_AD_ZERO_3 = 259;
	public static final int HD_AD_ZERO_4 = 260;
	public static final int HD_AD_ZERO_5 = 261;
	public static final int HD_AD_ZERO_6 = 262;
	public static final int HD_AD_ZERO_7 = 263;
	public static final int HD_AD_ZERO_8 = 264;

	public static final int HD_AD_ZERO_9 = 265;
	public static final int HD_AD_ZERO_10 = 266;
	public static final int HD_AD_ZERO_11 = 267;
	public static final int HD_AD_ZERO_12 = 268;
	public static final int HD_AD_ZERO_13 = 269;
	public static final int HD_AD_ZERO_14 = 270;
	public static final int HD_AD_ZERO_15 = 271;
	public static final int HD_AD_ZERO_16 = 272;


	public static final int HD_AD_ZERO_17 = 273;
	public static final int HD_AD_ZERO_18 = 274;
	public static final int HD_AD_ZERO_19 = 275;
	public static final int HD_AD_ZERO_20 = 276;
	public static final int HD_AD_ZERO_21 = 277;
	public static final int HD_AD_ZERO_22 = 278;
	public static final int HD_AD_ZERO_23 = 279;
	public static final int HD_AD_ZERO_24 = 280;

	public static final int HD_AD_ZERO_25 = 281;
	public static final int HD_AD_ZERO_26 = 282;
	public static final int HD_AD_ZERO_27 = 283;
	public static final int HD_AD_ZERO_28 = 284;
	public static final int HD_AD_ZERO_29 = 285;
	public static final int HD_AD_ZERO_30 = 286;
	public static final int HD_AD_ZERO_31 = 287;
	public static final int HD_AD_ZERO_32 = 288;

	public static final int HD_AD_ZERO_33 = 289;
	public static final int HD_AD_ZERO_34 = 290;
	public static final int HD_AD_ZERO_35 = 291;
	public static final int HD_AD_ZERO_36 = 292;
	public static final int HD_AD_ZERO_37 = 293;
	public static final int HD_AD_ZERO_38 = 294;
	public static final int HD_AD_ZERO_39 = 295;
	public static final int HD_AD_ZERO_40 = 296;

	public static final int HD_AD_ZERO_41 = 297;
	public static final int HD_AD_ZERO_42 = 298;
	public static final int HD_AD_ZERO_43 = 299;
	public static final int HD_AD_ZERO_44 = 300;
	public static final int HD_AD_ZERO_45 = 301;
	public static final int HD_AD_ZERO_46 = 302;
	public static final int HD_AD_ZERO_47 = 303;
	public static final int HD_AD_ZERO_48 = 304;

	public static final int HD_AD_ZERO_49 = 305;
	public static final int HD_AD_ZERO_50 = 306;
	public static final int HD_AD_ZERO_51 = 307;
	public static final int HD_AD_ZERO_52 = 308;
	public static final int HD_AD_ZERO_53 = 309;
	public static final int HD_AD_ZERO_54 = 310;
	public static final int HD_AD_ZERO_55 = 311;
	public static final int HD_AD_ZERO_56 = 312;

	public static final int HD_AD_ZERO_57 = 313;
	public static final int HD_AD_ZERO_58 = 314;
	public static final int HD_AD_ZERO_59 = 315;
	public static final int HD_AD_ZERO_60 = 316;
	public static final int HD_AD_ZERO_61 = 317;
	public static final int HD_AD_ZERO_62 = 318;
	public static final int HD_AD_ZERO_63 = 319;
	public static final int HD_AD_ZERO_64 = 320;

	public static final int HD_AD_PARA_1 = 513;
	public static final int HD_AD_PARA_2 = 514;
	public static final int HD_AD_PARA_3 = 515;
	public static final int HD_AD_PARA_4 = 516;
	public static final int HD_AD_PARA_5 = 517;
	public static final int HD_AD_PARA_6 = 518;
	public static final int HD_AD_PARA_7 = 519;
	public static final int HD_AD_PARA_8 = 520;

	public static final int HD_AD_PARA_9 = 521;
	public static final int HD_AD_PARA_10 = 522;
	public static final int HD_AD_PARA_11 = 523;
	public static final int HD_AD_PARA_12 = 524;
	public static final int HD_AD_PARA_13 = 525;
	public static final int HD_AD_PARA_14 = 526;
	public static final int HD_AD_PARA_15 = 527;
	public static final int HD_AD_PARA_16 = 528;

	public static final int HD_AD_PARA_17 = 529;
	public static final int HD_AD_PARA_18 = 530;
	public static final int HD_AD_PARA_19 = 531;
	public static final int HD_AD_PARA_20 = 532;
	public static final int HD_AD_PARA_21 = 533;
	public static final int HD_AD_PARA_22 = 534;
	public static final int HD_AD_PARA_23 = 535;
	public static final int HD_AD_PARA_24 = 536;

	public static final int HD_AD_PARA_25 = 537;
	public static final int HD_AD_PARA_26 = 538;
	public static final int HD_AD_PARA_27 = 539;
	public static final int HD_AD_PARA_28 = 540;
	public static final int HD_AD_PARA_29 = 541;
	public static final int HD_AD_PARA_30 = 542;
	public static final int HD_AD_PARA_31 = 543;
	public static final int HD_AD_PARA_32 = 544;

	public static final int HD_AD_PARA_33 = 545;
	public static final int HD_AD_PARA_34 = 546;
	public static final int HD_AD_PARA_35 = 547;
	public static final int HD_AD_PARA_36 = 548;
	public static final int HD_AD_PARA_37 = 549;
	public static final int HD_AD_PARA_38 = 550;
	public static final int HD_AD_PARA_39 = 551;
	public static final int HD_AD_PARA_40 = 552;

	public static final int HD_AD_PARA_41 = 553;
	public static final int HD_AD_PARA_42 = 554;
	public static final int HD_AD_PARA_43 = 555;
	public static final int HD_AD_PARA_44 = 556;
	public static final int HD_AD_PARA_45 = 557;
	public static final int HD_AD_PARA_46 = 558;
	public static final int HD_AD_PARA_47 = 559;
	public static final int HD_AD_PARA_48 = 560;

	public static final int HD_AD_PARA_49 = 561;
	public static final int HD_AD_PARA_50 = 562;
	public static final int HD_AD_PARA_51 = 563;
	public static final int HD_AD_PARA_52 = 564;
	public static final int HD_AD_PARA_53 = 565;
	public static final int HD_AD_PARA_54 = 566;
	public static final int HD_AD_PARA_55 = 567;
	public static final int HD_AD_PARA_56 = 568;

	public static final int HD_AD_PARA_57 = 569;
	public static final int HD_AD_PARA_58 = 570;
	public static final int HD_AD_PARA_59 = 571;
	public static final int HD_AD_PARA_60 = 572;
	public static final int HD_AD_PARA_61 = 573;
	public static final int HD_AD_PARA_62 = 574;
	public static final int HD_AD_PARA_63 = 575;
	public static final int HD_AD_PARA_64 = 576;

	public static final int HD_WEIGHT_1 = 769;
	public static final int HD_WEIGHT_2 = 770;
	public static final int HD_WEIGHT_3 = 771;
	public static final int HD_WEIGHT_4 = 772;
	public static final int HD_WEIGHT_5 = 773;
	public static final int HD_WEIGHT_6 = 774;
	public static final int HD_WEIGHT_7 = 775;
	public static final int HD_WEIGHT_8 = 776;

	public static final int HD_WEIGHT = 1025;
	public static final int HD_BOARD_ID = 1026;

	public static final int HD_STABLE_PARA = 1027;   //稳定判定系数
	public static final int HD_HAND_BREAK = 1028;
	public static final int HD_STABLE = 1029;
	public static final int HD_VOLTAGE = 1030;
	public static final int HD_WEIGHT_TIME = 1031;
	public static final int HD_GPS_TIME = 1032;
	public static final int HD_LONGTITUDE = 1033; //经度
	public static final int HD_LATITUDE = 1034;

	public static final int HD_AZIMUTH = 1035; //向角  smg

	public static final int HD_TRUCK_SPEED = 1036;
	public static final int HD_HARD_WARE = 1037;
	public static final int HD_BOOTLOAD_SOFT_WARE = 1038;
	public static final int HD_APPLICATION_SOFT_WARE = 1039;

	public static final int HD_SOFT_WARE_STATUS = 1040;  //待定
	public static final int HD_HARD_WARE_STATUS = 1041;  //待定
	public static final int HD_SENSOR_DIAGNOSE = 1042;
	public static final int HD_CIRCUIT_DIAGNOSE = 1043;
	public static final int HD_LOAD_DISCHARGE_STATUS = 1044;  //读命令  0-无货物 1-有货物 2-装货 3-卸货
	public static final int HD_RUNNING_NUMBER = 1045;
	public static final int HD_SENSOR_ID_LIST = 1046;
	public static final int HD_SENSOR_ID_READ_OR_WRITE = 1047;
	public static final int HD_ZERO_ACTION = 1048;//清零命令
	public static final int HD_GPS_STATUS = 1053;//GPS信号强度
	public static final int HD_LOCATION_GPS = 1232;//GPS定位状态
	public static final int HD_GPS_RSSI = 1054;//GPRSrssi信号强度值0~31<15认为信号强度弱
	public static final int HD_WEIGHT_CONFIGURATION_ACTION = 1058;//重量配置命令
	public static final int HD_GPRS_STATUS = 1233;//GPRS网络状态
	public static final int HD_NET_LINE = 1239;//天线状态
	public static final int HD_READY_BIN = 1280;//准备bin升级
	public static final int HD_START_BIN = 1281;//开始bin升级
	public static final int HD_FINISH_BIN = 1282;//bin完成
}
