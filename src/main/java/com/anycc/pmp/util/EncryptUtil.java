package com.anycc.pmp.util;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by DELL on 2016/11/1.
 */
public class EncryptUtil {

    /**
     * 获取MD5加密后的字符串
     *
     * @param str 要加密的字符串
     * @return MD5加密后的字符串||null
     */
    public static String getMD5Str(String str) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(str.getBytes());
            byte[] results = digest.digest();
            if (results != null) {
                return toHex(results);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取SHA加密后的字符串
     *
     * @param str 要加密的字符串
     * @return SHA加密后的字符串||null
     */
    public static String getSHAStr(String str) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA");
            digest.update(str.getBytes());
            byte[] results = digest.digest();
            if (results != null) {
                return toHex(results);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 用来把一个byte类型的数转换成十六进制的ASCII表示
     *
     * @param buf byte类型的数转
     * @return 转换后的字符串
     */
    private static String toHex(byte[] buf) {
        StringBuilder sBuilder = new StringBuilder();
        for (int i = 0; i < buf.length; i++) {
            int high = (buf[i] >> 4) & 0x0f;
            int low = buf[i] & 0x0f;
            sBuilder.append(high > 9 ? (char) ((high - 10) + 'A')
                    : (char) (high + '0'));
            sBuilder.append(low > 9 ? (char) ((low - 10) + 'A')
                    : (char) (low + '0'));
        }
        return sBuilder.toString();
    }


    /**
     * base64编码
     * @param encode
     * @return
     */
    public static String BASE64Encode(String encode) {
        String result = null;
        try {
            byte[] bytes = encode.getBytes("utf-8");
            result = Base64.encodeBase64String(bytes);
            //result = new BASE64Encoder().encode(bytes);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * base64解码
     * @param decode
     * @return
     */
    public static String BASE64Decode(String decode) {
        String result = null;
        try {
            //byte[] bt = new BASE64Decoder().decodeBuffer(decode);
            byte[] bt = Base64.decodeBase64(decode);
            result = new String(bt,"utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static void main(String[] args) {
        System.out.println(StringUtils.isBlank(null));
        System.out.println(StringUtils.isNumeric(" 102"));
    }
}
