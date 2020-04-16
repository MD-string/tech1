package cn.hand.tech.ble.bleUtil;

/**
 * Created by Administrator on 2017/5/29 0029.
 */

public class BluetoothUtil {
    public static final String VALUE_START="7E45C0FF";
/*
1. 复位主控板
主机发送  7E 45 0x00 0x00 0x04 0x00 0x00000000 CRC16
		  7E 45 00 00 04 00 00000000 F8 29
从机返回  7E 45 0x40 0x00 0x04 0x00 0x00000000 CRC16
*/
    //public static final String CLEAR="7E 45 00 00 04 00 00000000 F8 29";
    //public static final String RESHULT_CLEAR="7E454000040000000000 ";

    public static final String CLEAR="7E 45 00 09 0000 8167";
    public static final String RESHULT_CLEAR="7E454009";//00001D09
/*
2. 读系数
主机发送  7E 45 0x80 0x01 0x0000 CRC16
		  7E 45 80 02 00 00 18 13
从机返回  7E 45 0xC0 0x01 0x0C00通道1系数 通道2系数 通道3系数 CRC16
*/
    public static final String READ_XISHU="7E 45 80 01 00 00 18 13";
    public static final String RESHULT_READ_XISHU="7E45C0011000";
/*
3. 写系数
主机发送  7E 45 0x00 0x01 0x0800 通道1系数 通道2系数 CRC16 (通道个数为N)
从机返回  7E 45 0x40 0x01 0x0800 通道1系数 通道2系数 CRC16
*/
    public static final String WRITE_XISHU="7E4500011000";
    //public static final String RESHULT_WRITE_XISHU="7E4540010800";
    public static final String RESHULT_WRITE_XISHU="7E4540011000";
/*
4. 读取重量
主机发送  7E 45 0x80 0x02 0x0000 CRC16
          7E 45 80 02 00 00 48 4A
从机返回  7E 45 0xC0 0x02 0x0400 重量 CRC16
*/

    public static final String READ_WEIGHT="7E 45 80 02 00 00 48 4A";
    public static final String RESHULT_READ_WEIGHT="7E45C0020400";
/*
5. 读取AD值
主机发送  7E 45 0x80 0x03 0x0000 CRC16
          7E 45 80 03 00 00 78 7D
从机返回  7E 45 0xC0 0x01 0x0800通道1AD 通道2AD  CRC16
*/
    public static final String READ_AD="7E 45 80 03 00 00 78 7D";

    public static final String RESHULT_READ_AD="7E45C0031000";//

    /**
     * hex字符串转byte数组<br/>
     * 2个hex转为一个byte
     * @param src
     * @return
     */
    public static byte[] hex2Bytes(String src){
        src=src.replaceAll(" ","");

        if(src.length()%2 !=0){
            return null;
        }

        byte[] res = new byte[src.length()/2];
        char[] chs = src.toCharArray();
        for(int i=0,c=0; i<chs.length; i+=2,c++){
            res[c] = (byte) (Integer.parseInt(new String(chs,i,2), 16));
        }

        return res;
    }

    public static char[] hex2Chars(String src){
        src=src.replaceAll(" ","");

        if(src.length()%2 !=0){
            return null;
        }

        char[] res = new char[src.length()/2];
        char[] chs = src.toCharArray();
        for(int i=0,c=0; i<chs.length; i+=2,c++){
           // Log.e("hex2Chars test",Integer.valueOf(chs[i]) + "");
            res[c] = (char) (Integer.parseInt(new String(chs,i,2), 16));
//            Log.e("hex2Chars test",Integer.valueOf(res[c]) + "");
        }

        return res;
    }

    public static byte[] ConvertHexToBytes(String hex)
    {
        hex = hex.trim();
        hex = hex.replace(" ", "");
        hex = hex.replace("x", "");
        hex = hex.replace("X", "");

        byte[] bytes = new byte[hex.length() / 2];

        for (int index = 0; index < bytes.length; index++)
        {
            //bytes[index] = byte.Parse(hex.substring(index * 2, 2), System.Globalization.NumberStyles.HexNumber);
        }

        return bytes;
    }

    /**
     * 浮点转换为字节
     *
     * @param f
     * @return
     */
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

    /**
     * 字节转换为浮点
     *
     * @param b 字节（至少4个字节）
     * @param index 开始位置
     * @return
     */
    public static float byte2float(byte[] b, int index) {
        int l;
        l = b[index + 0];
        l &= 0xff;
        l |= ((long) b[index + 1] << 8);
        l &= 0xffff;
        l |= ((long) b[index + 2] << 16);
        l &= 0xffffff;
        l |= ((long) b[index + 3] << 24);
        return Float.intBitsToFloat(l);
    }

    public static int byte2int(byte[] a) {
        int x = a[1] << 8 | a[0];
        return x;
    }



    public static int char2int( char[] a) {
        int x = a[1] << 8 | a[0];
        if(x > 32767)
            x -= 65536;
        return x;
    }

    public static int byteToInt(byte b) {
        //Java 总是把 byte 当做有符处理；我们可以通过将其和 0xFF 进行二进制与得到它的无符值
        return b & 0xFF;
    }

    public static char byteToChar(byte[] b) {
        char c = (char) (((b[0] & 0xFF) << 8) | (b[1] & 0xFF));
        return c;
    }

    public static int byteArrayToInt(byte[] b, int offset) {
        int value= 0;
        for (int i = 0; i < 4; i++) {
            int shift= (4 - 1 - i) * 8;
            value +=(b[i + offset] & 0x000000FF) << shift;
        }
        return value;
    }

    public static int bytes2int(byte[] res) {
    // 一个byte数据左移24位变成0x??000000，再右移8位变成0x00??0000

        int targets = (res[0] & 0xff) | ((res[1] << 8) & 0xff00) // | 表示安位或
                | ((res[2] << 24) >>> 8) |( (res[3] << 24)& 0xff000000);
//        int targets = ((res[3]<<24)& 0xff)|((res[2]<<16)& 0xff)|((res[1]<<8)& 0xff)|(res[0]& 0xff);
//        //long targets = (res[0]<<24)|(res[1]<<16)|(res[2]<<8)|(res[3]);
        return targets;
    }





    public static void main(String[] args) {

/*
        System.out.println(byte2int(hex2Bytes("3300")));

        System.out.println(bytes2int(hex2Bytes("965D0000")));
        System.out.println(bytes2int(hex2Bytes("D2040000")));


        System.out.println(byte2int(hex2Bytes("F4FF")));
        System.out.println(byte2int(hex2Bytes("DEFF")));
        System.out.println(byte2int(hex2Bytes("3800")));
        System.out.println(byte2int(hex2Bytes("4E00")));
        System.out.println(byte2int(hex2Bytes("F6FF")));
        System.out.println(byte2int(hex2Bytes("ECFF")));
        System.out.println(byte2int(hex2Bytes("1E00")));
        System.out.println(byte2int(hex2Bytes("2800")));


        System.out.println(byte2float(hex2Bytes("0AD7A3BB"),0));
        System.out.println(byte2float(hex2Bytes("7B148EBF"),0));
        System.out.println(byte2float(hex2Bytes("7B140E40"),0));
        System.out.println(byte2float(hex2Bytes("B81E5540"),0));
        System.out.println(byte2float(hex2Bytes("7B148E40"),0));
        System.out.println(byte2float(hex2Bytes("7B140E41"),0));


        System.out.println(byteToInt(hex2Bytes("01")[0]));
        System.out.println(byteToInt(hex2Bytes("01")[0]));
        System.out.println(byteToInt(hex2Bytes("01")[0]));
*/





        System.out.println(byte2int(hex2Bytes("1000")));
        System.out.println(byte2int(hex2Bytes("B011")));
        System.out.println(byte2int(hex2Bytes("0000")));
        System.out.println(byte2int(hex2Bytes("FF7F")));
        System.out.println(byte2int(hex2Bytes("0000")));
        System.out.println(byte2int(hex2Bytes("8EEF")));
        System.out.println(byte2int(hex2Bytes("FFFF")));
        System.out.println(byte2int(hex2Bytes("FF7F")));
        System.out.println(byte2int(hex2Bytes("0000")));

        System.out.println(byte2float(hex2Bytes("B0110000"),0));
        System.out.println(byte2float(hex2Bytes("FF7F0000"),0));
        System.out.println(byte2float(hex2Bytes("8EEFFFFF"),0));
        System.out.println(byte2float(hex2Bytes("FF7F0000"),0));

        System.out.println(byte2float(hex2Bytes("1000B011"),0));
        System.out.println(byte2float(hex2Bytes("0000FF7F"),0));
        System.out.println(byte2float(hex2Bytes("8EEFFFFF"),0));
        System.out.println(byte2float(hex2Bytes("FF7F0000"),0));
        //7E45C0031000  B011   0000  FF7F   0000  8EEF  FFFF  FF7F   0000   5F81
        /*
        7E 45 C0 FF
        33 00
        4F 0100 00
        F2 E8FE 18
        53 FD
        D2 06
        E9 FF
        8F 01
        1A FD
        1A FD
        1A FD
        1A FD*/

//        System.out.println(byte2int(hex2Bytes("53 FD")));
//        System.out.println(byte2int(hex2Bytes("D2 06")));
//        System.out.println(byte2int(hex2Bytes("E9 FF")));
//        System.out.println(byte2int(hex2Bytes("8F 01")));
//        System.out.println(byte2int(hex2Bytes("1A FD")));



        System.out.println(char2int(hex2Chars("53 FD")));
        System.out.println(char2int(hex2Chars("D2 06")));
        System.out.println(char2int(hex2Chars("E9 FF")));
        System.out.println(char2int(hex2Chars("8F 01")));
        System.out.println(char2int(hex2Chars("1A FD")));
        //AE B3 6E 41 1A F5 50 3E 9E 29 04 41 ED 2A FC 3F BB 81 8F 40 AE B3 6E 41 01 01 00 3A 01





    }

}
