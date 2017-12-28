package web;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;


public class Utils {
    public static String formatSimNo(String simNo) {
        if (simNo != null && simNo.length() < 12) {
            simNo = "0" + simNo;
        }
        return simNo;
    }

    public static byte[] byteToBytes(byte value) {
        byte[] data = new byte[1];
        data[0] = value;
        return data;
    }

    public static byte[] shortToBytes(short value) {
        byte[] data = new byte[2];
        data[1] = (byte) ((value >> 0) & 0xff);
        data[0] = (byte) ((value >> 8) & 0xff);
        return data;
    }

    public static byte[] intToBytes(int value) {
        byte[] data = new byte[4];
        data[3] = (byte) ((value >> 0) & 0xff);
        data[2] = (byte) ((value >> 8) & 0xff);
        data[1] = (byte) ((value >> 16) & 0xff);
        data[0] = (byte) ((value >> 24) & 0xff);
        return data;
    }

    public static byte[] longToBytes(long value) {
        byte[] data = new byte[8];
        data[7] = (byte) ((value >> 0) & 0xff);
        data[6] = (byte) ((value >> 8) & 0xff);
        data[5] = (byte) ((value >> 16) & 0xff);
        data[4] = (byte) ((value >> 24) & 0xff);
        data[3] = (byte) ((value >> 32) & 0xff);
        data[2] = (byte) ((value >> 40) & 0xff);
        data[1] = (byte) ((value >> 48) & 0xff);
        data[0] = (byte) ((value >> 56) & 0xff);
        return data;
    }

    public static byte[] stringToBytes(String str) {
        byte[] data = null;
        try {
            data = str.getBytes("gb2312");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return data;
    }

    public static byte[] stringToBytesFixSize(String str, int size, byte fill, boolean fillhead) {
        byte[] data = new byte[size];
        Arrays.fill(data, fill);
        try {
            if ((str != null) && !str.isEmpty()) {
                byte[] ds = str.getBytes("gb2312");
                int length = ds.length;
                if (length > size)
                    length = size;

                int start = 0;
                if (fillhead)
                    start = size - length;

                System.arraycopy(ds, 0, data, start, length);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public static byte[] stringToBytesFixSize(String str, int size) {
        return stringToBytesFixSize(str, size, (byte) 0x00, false);
    }

    public static byte bytesToByte(byte[] bytes) {
        if ((bytes == null) || (bytes.length < 1))
            return 0;

        return bytes[0];
    }

    public static short bytesToShort(byte[] bytes) {
        if ((bytes == null) || (bytes.length < 2))
            return 0;

        return (short) ((bytes[1] & 0xff) | ((bytes[0] & 0xff) << 8));
    }

    public static int bytesToInt(byte[] bytes) {
        if ((bytes == null) || (bytes.length < 4))
            return 0;

        return ((0xff & bytes[3]) | ((0xff & bytes[2]) << 8) | ((0xff & bytes[1]) << 16) | ((0xff & bytes[0]) << 24));
    }

    public static long bytesToLong(byte[] bytes) {
        if ((bytes == null) || (bytes.length < 8))
            return 0;

        return ((long) (0xff & bytes[7]) | ((long) (0xff & bytes[6]) << 8) | ((long) (0xff & bytes[5]) << 16)
                | ((long) (0xff & bytes[4]) << 24) | ((long) (0xff & bytes[3]) << 32) | ((long) (0xff & bytes[2]) << 40)
                | ((long) (0xff & bytes[1]) << 48) | ((long) (0xff & bytes[0]) << 56));

    }

    public static String bytesToHexString(byte[] bytes) {
        String str = String.format("%02x", bytes[0]);
        for (int i = 1; i < bytes.length; i++) {
            str = str + String.format("%02x", bytes[i]);
        }
        return str;
    }
    public static String bytesToHexStringWithSpace(byte[] bytes) {
        String str = String.format("%02x ", bytes[0]);
        for (int i = 1; i < bytes.length; i++) {
            str = str + String.format("%02x ", bytes[i]);
        }
        return str;
    }

    public static String bytesToString(byte[] bytes) {
        String str = null;
        try {
            str = new String(bytes, "gb2312");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static byte charToBCD(char c) {
        int bcd = 0;
        if ((c >= '0') && (c <= '9'))
            bcd = c - '0';
        else if ((c >= 'a') && (c <= 'z'))
            bcd = c - 'a' + 0x0a;
        else if ((c >= 'A') && (c <= 'Z'))
            bcd = c - 'A' + 0x0a;
        return (byte) bcd;
    }

    public static byte twoCharsToBCD(char ch, char cl) {
        byte bcdh = charToBCD(ch);
        byte bcdl = charToBCD(cl);
        return (byte) ((((int) bcdh & 0x0f) << 4) | ((int) bcdl & 0x0f));
    }

    public static byte[] stringToBCD(String str) {
        if ((str.length() % 2) != 0)
            str = "0" + str;

        byte[] bcd = null;
        byte[] data = stringToBytes(str);
        if (data != null) {
            bcd = new byte[data.length / 2];
            for (int i = 0; i < data.length / 2; i++)
                bcd[i] = twoCharsToBCD((char) data[2 * i], (char) data[2 * i + 1]);
        }

        return bcd;
    }

    public static byte[] stringToBCDFixSize(String str, int size, char fill, boolean fillhead) {
        byte[] bcd = null;
        byte[] data = stringToBytesFixSize(str, 2 * size, (byte) fill, fillhead);
        if (data != null) {
            bcd = new byte[size];
            for (int i = 0; i < size; i++)
                bcd[i] = twoCharsToBCD((char) data[2 * i], (char) data[2 * i + 1]);
        }
        return bcd;
    }

    public static byte[] stringToBCDFixSize(String str, int size) {
        return stringToBCDFixSize(str, size, '0', true);
    }

    public static byte[] intToBCD(int value) {
        return stringToBCDFixSize(String.format("%d", value), 2);
    }

    public static byte[] longToBCD(long value) {
        return stringToBCDFixSize(String.format("%d", value), 4);
    }

    public static String bcdToString(byte[] bcd) {
        String s = "";
        for (int i = 0; i < bcd.length; i++) {
            String t = Integer.toHexString(bcd[i] & 0xff);
            if (t.length() == 1)
                t = '0' + t;
            s += t;
        }
        return s;
    }

    public static int bcdToInt(byte[] bcd) {
        if ((bcd == null) || (bcd.length < 2))
            return (int) 0;
        return Integer.parseInt(bcdToString(bcd));
    }

    public static long bcdToLong(byte[] bcd) {
        if ((bcd == null) || (bcd.length < 4))
            return 0;
        return Long.parseLong(bcdToString(bcd));
    }

    public static String timeToString(Date d, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(d);
    }

    public static byte[] timeToBytes(Date d, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String s = sdf.format(d);
        return stringToBytes(s);
    }

    public static byte[] timeToBCD(Date d, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String s = sdf.format(d);
        return stringToBCD(s);
    }

    public static Date stringToTime(String s, String format) {
        Date d = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            d = sdf.parse(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return d;
    }

    public static Date bytesToTime(byte[] bytes, String format) {
        Date d = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            String s = bytesToString(bytes);
            d = sdf.parse(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return d;
    }

    public static Date bcdToTime(byte[] bcd, String format) {
        Date d = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            String s = bcdToString(bcd);
            d = sdf.parse(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return d;
    }

    public static String[] stringToList(String str) {
        String[] list = null;
        try {
            list = str.split(";");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static String listToString(String[] list) {
        String str = "";
        if (list != null) {
            for (int i = 0; i < list.length; i++) {
                str += list[i];
                if (i != (list.length - 1))
                    str += ";";
            }
        }
        return str;
    }

    public static byte calXor(byte[] data, int start, int end) {
        byte checksum = 0;
        for (int i = start; i <= end; i++) {
            checksum ^= data[i];
        }
        return checksum;
    }

    public static byte calSum(byte[] data, int start, int end) {
        byte checksum = 0;
        for (int i = start; i <= end; i++) {
            checksum += data[i];
        }
        return checksum;
    }

    public static void printBytes(byte[] value) {
        printBytes("", value);
    }

    public static String printBytes(String tag, byte[] value) {
        String print_str = "";
        if (value == null) {
            print_str = tag + " null";
            return print_str;
        }

        print_str = tag + ":\n";
        String hex = new String("");
        for (int i = 0; i < value.length; i++) {
            hex += String.format("%02x ", value[i]);
            if (((i + 1) % 16) == 0) {
                print_str += hex.toUpperCase() + "\n";
                hex = "";
            }
        }

        if (!hex.isEmpty())
            print_str += hex.toUpperCase() + "\n";

        return print_str;
    }

    public static String printBytes(String tag, byte[] value, int len) {
        String print_str = "";
        if ((value == null) || (len == 0)) {
            //Log.i(tag, "null");
            print_str = tag + " null";
            return print_str;
        }
        if (len > value.length) {
            print_str = tag + "len=" + len + ",but array len=" + value.length;
            len = value.length;
        }

        print_str += "\n";
        String hex = new String("");
        for (int i = 0; i < len; i++) {
            hex += String.format("%02x ", value[i]);
            if (((i + 1) % 16) == 0) {
                print_str += hex.toUpperCase();
                hex = "";
            }
        }

        if (!hex.isEmpty())
            print_str += hex.toUpperCase();

        return print_str;
    }

    public static String printBytes(String tag, String prefex, byte[] value, int len) {
        String print_str = "";
        if ((value == null) || (len == 0)) {
            //Log.i(tag, "null");
            print_str = tag + " null";
            return print_str;
        }
        if (len > value.length) {
            //Log.i(tag, "len=" + len + ",but array len=" + value.length);
            print_str = tag + "len=" + len + ",but array len=" + value.length;
            len = value.length;
        }

        print_str += "\n";
        String hex = new String("");
        int j = 0;
        for (int i = 0; i < len; i++) {
            hex += String.format("%02x ", value[i]);
            if (((i + 1) % 16) == 0) {
                //Log.i(tag, prefex + "[" + j + "]" + hex.toUpperCase());
                print_str += prefex + "[" + j + "]" + hex.toUpperCase();
                hex = "";
                j += 16;
            }
        }

        if (!hex.isEmpty()) {
            //Log.i(tag, prefex + "[" + j + "]" + hex.toUpperCase());
            print_str += prefex + "[" + j + "]" + hex.toUpperCase();
        }

        return print_str;
    }

    public static byte[] scanBytes(String str) {
        byte[] value = null;
        String[] list = null;
        try {
            list = str.split(" ");
            if (list != null) {
                value = new byte[list.length];
                for (int i = 0; i < list.length; i++)
                    value[i] = (byte) Integer.parseInt(list[i], 16);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    public static boolean delelteFile(String path) {
        try {
            File file = new File(path);
            if (file.exists())
                file.delete();
        } catch (Exception e) {
        }
        return true;
    }

    public static String getRootPath() {
        File directory = new File("");
        String rootPath = directory.getAbsolutePath();
        //Log.i("RootPath", "root path is " + rootPath);
        rootPath += File.separator + "files" + File.separator;
        //Log.i("RootPath", "finally, root path is " + rootPath);
        File filePath = new File(rootPath);
        if (!filePath.exists() && !filePath.isDirectory()) {
            //Log.i("RootPath", "files directory does not exist, create it");
            filePath.mkdir();
        }
        return rootPath;
    }

    public static void saveFile(String fileName, byte[] data) {
        //Log.i("saveFile", "saveFile fileName = " + fileName);
        try {
            FileOutputStream os = new FileOutputStream(fileName);
            os.write(data);
            os.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean copyFile(String strExistingPath, String strNewPath) {
        boolean ret = false;
        try {
            File fileExisting = new File(strExistingPath);
            if (fileExisting.exists()) {
                File fileNew = new File(strNewPath);
                FileInputStream in = new FileInputStream(fileExisting);
                FileOutputStream out = new FileOutputStream(fileNew);
                byte[] bt = new byte[1024];
                int count;
                while ((count = in.read(bt)) > 0) {
                    out.write(bt, 0, count);
                }
                in.close();
                out.close();
                ret = true;
            }
        } catch (IOException e) {
        }
        return ret;
    }

    public static boolean testBit(byte v, int pos) {

        if ((v & 1 << pos) == 1 << pos)
            return true;
        else
            return false;
    }

    public static boolean testBit(short v, int pos) {

        if ((v & 1 << pos) == 1 << pos)
            return true;
        else
            return false;
    }

    public static boolean testBit(int v, int pos) {

        if ((v & 1 << pos) == 1 << pos)
            return true;
        else
            return false;
    }

    public static String intToBinaryString(int value) {
        String binary = "";
        String valueStr = Integer.toBinaryString(value);
        String oriString = String.format("%032d", 0);
        int len = valueStr.length();
        binary = oriString.substring(len) + valueStr;
        return binary;
    }

    public static String getMD5(String s) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(s.getBytes("utf-8"));
            return toHex(bytes);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String toHex(byte[] bytes) {

        final char[] HEX_DIGITS = "0123456789ABCDEF".toCharArray();
        StringBuilder ret = new StringBuilder(bytes.length * 2);
        for (int i=0; i<bytes.length; i++) {
            ret.append(HEX_DIGITS[(bytes[i] >> 4) & 0x0f]);
            ret.append(HEX_DIGITS[bytes[i] & 0x0f]);
        }
        return ret.toString();
    }
}
