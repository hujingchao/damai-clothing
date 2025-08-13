package com.fenglei.service.util;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EncryptAndSign {
//    public static void main(String args[]) throws Exception {
//        //加密
//        String json_data = "{\"service\":\"apply_qry\",\"accesser_id\":\"123456\",\"sign_type\":\"SHA-256\",\"request_date\":\"20210406192030\",\"request_seq\":\"12345677\",\"ums_reg_id\":\"1111111111111111111\"}";
//        String key = "udik876ehjde32dU61edsxsf";
//        String jsonData = encrypt(json_data, key);
//        String sign = sign(json_data.trim());
//        System.out.println("jsonData密文:" + jsonData);
//        System.out.println("sign:" + sign);
//        String jsonDataDec = decrypt(jsonData, key);
//        System.out.println("jsonData明文:" + jsonDataDec);
//        System.out.println("签名验证结果：" + verifySign(jsonDataDec, sign));
//    }


    /**
     * 对称加密DESede
     */
    public static final String KEY_ALGORITHM = "DESede";
    public static final String KEY_ALGORITHM_PADDING = "DESede/ECB/PKCS5Padding";
    /**
     * SHA256签名
     */
    public static final String KEY_ALGORITHM_SIGN = "SHA-256";
    public static final String ENCODE = "UTF-8";
    private static char[] HEXCHAR = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * PKCS5Padding模式下加密后数据字节长度=（原始数据字节长度/8+1）*8
     *
     * @Title: encrypt
     * @Description: 加密
     * @author: hf
     * @param: @param data 明文数据
     * @param: @param key 密钥 ，24位
     * @param: @return
     * @param: @throws InvalidKeyException
     * @param: @throws IllegalBlockSizeException
     * @param: @throws BadPaddingException
     * @param: @throws UnsupportedEncodingException
     * @param: @throws NoSuchAlgorithmException
     * @param: @throws NoSuchPaddingException
     * @return: String 加密后转化为十六进制返回
     */
    public static String encrypt(String data, String key)
            throws InvalidKeyException, IllegalBlockSizeException,
            BadPaddingException, UnsupportedEncodingException,
            NoSuchAlgorithmException, NoSuchPaddingException {
        byte[] keyByte = key.getBytes();
        SecretKey sk = new SecretKeySpec(keyByte, KEY_ALGORITHM);
        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM_PADDING);
        cipher.init(Cipher.ENCRYPT_MODE, sk);
        return toHexString(cipher.doFinal(data.getBytes(ENCODE)));
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
     * @Title: sign
     * @Description: sha-256签名
     * @author: hf
     * @param: @param str 明文数据
     * @param: @return
     * @param: @throws NoSuchAlgorithmException
     * @param: @throws UnsupportedEncodingException
     * @return: String 签名后转化为十六进制返回
     */
    public static String sign(String str) throws NoSuchAlgorithmException,
            UnsupportedEncodingException {
        MessageDigest messageDigest;
        messageDigest = MessageDigest.getInstance(KEY_ALGORITHM_SIGN);
        messageDigest.update(str.getBytes(ENCODE));
        return toHexString(messageDigest.digest());
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
        String tmpSign = sign(str.trim());
        flag = tmpSign.equals(sign.trim());
        return flag;
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
}

