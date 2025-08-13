package com.fenglei.common.util;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @Description 加密工具类
 * @Author zhouyiqiu
 * @Date 2021/12/29 10:12
 * @Version 1.0
 */
public class EncryptUtil {

    /**
     * 对称加密DESede
     */
    private static final String KEY_ALGORITHM = "DESede";
    private static final String KEY_ALGORITHM_PADDING = "DESede/ECB/PKCS5Padding";
    /**
     * SHA256签名
     */
    public static final String KEY_ALGORITHM_SIGN = "SHA-256";
    public static final String ENCODE = "UTF-8";
    private static char[] HEXCHAR = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * 3DES加密
     * @param data
     * @return
     * @throws Exception
     */
    public static String encryptBy3DES(String data,String key) throws Exception {
        byte[] keyByte = key.getBytes();
        SecretKey sk = new SecretKeySpec(keyByte, KEY_ALGORITHM);
        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM_PADDING);
        cipher.init(Cipher.ENCRYPT_MODE, sk);
        return toHexString(cipher.doFinal(data.getBytes(ENCODE)));
    }

    /**
     * SHA-256签名
     */
    public static String signBySHA256(String data) throws Exception {
        MessageDigest messageDigest;
        messageDigest = MessageDigest.getInstance(KEY_ALGORITHM_SIGN);
        messageDigest.update(data.getBytes(ENCODE));
        return toHexString(messageDigest.digest());
    }

    /**
     * }
     * 生成加密秘钥
     *
     * @return
     */
    private static SecretKey getSecretKey(final String key) {
        byte key_byte[] = key.getBytes();// 3DES 24 bytes key
        SecretKey k = new SecretKeySpec(key_byte, KEY_ALGORITHM);
        return k;
    }

    /**
     * 把字节数组转换成16进制字符串
     * @param bArray
     * @return
     */
    public static final String byte2hex(byte[] bArray) {
        StringBuffer sb = new StringBuffer(bArray.length);
        String sTemp;
        for (int i = 0; i < bArray.length; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2)
                sb.append(0);
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * @Title: toHexString
     * @Description: byte数组转化为十六进制
     * @author: hf
     * @param: @param b
     * @param: @return
     * @return: String
     */
    public static String toHexString(byte[] b) {
        StringBuilder sb = new StringBuilder(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            sb.append(HEXCHAR[(b[i] & 0xf0) >>> 4]);
            sb.append(HEXCHAR[b[i] & 0x0f]);
        }
        return sb.toString();
    }

    /**
     * @Title: decrypt
     * @Description: 解密
     * @author: hf
     * @param: @param data 密文数据
     * @param: @param key 密钥 ，24位
     * @param: @return
     * @param: @throws NoSuchAlgorithmException
     * @param: @throws NoSuchPaddingException
     * @param: @throws InvalidKeyException
     * @param: @throws IllegalBlockSizeException
     * @param: @throws BadPaddingException
     * @param: @throws UnsupportedEncodingException
     * @return: String
     */
    public static String decrypt(String data, String key)
            throws NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, IllegalBlockSizeException,
            BadPaddingException, UnsupportedEncodingException {
        byte[] keyByte = key.getBytes();
        SecretKey sk = new SecretKeySpec(keyByte, KEY_ALGORITHM);
        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM_PADDING);
        cipher.init(Cipher.DECRYPT_MODE, sk);
        return new String(cipher.doFinal(toBytes(data)), ENCODE);
    }

    /**
     * @Title: toBytes
     * @Description: 十六进制转化为btye数组
     * @author: hf
     * @param: @param s
     * @param: @return
     * @return: byte[]
     */
    public static final byte[] toBytes(String s) {
        byte[] bytes;
        bytes = new byte[s.length() / 2];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) Integer.parseInt(s.substring(2 * i, 2 * i + 2), 16);
        }
        return bytes;
    }

    /**
     * @param str  待签名字符串
     * @param sign 待验证签名
     * @return true通过，false不通过
     * @throws Exception
     * @Description: 验证签名
     * @author xinchen1
     * @date 2018年4月26日 下午7:03:51
     */
    public static boolean verifySign(String str, String sign) throws Exception {
        boolean flag = false;
        String tmpSign = signBySHA256(str.trim());
        flag = tmpSign.equals(sign.trim());
        return flag;
    }
}
