package com.fenglei.service.workFlow.util;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * User: yzy
 * Date: 2019/6/16 0016
 * Time: 8:36
 * Description: 数学运算工具类
 */
public class OperationUtil {

    /**
     * 大十进制数的相加
     * @param num1 前一个数
     * @param num2 后一个数
     * @return 结果
     */
    public static BigDecimal add(BigDecimal num1, BigDecimal num2) {
        return num1.add(num2);
    }

    /**
     * 大十进制数的相减
     * @param num1 前一个数
     * @param num2 后一个数
     * @return 结果
     */
    public static BigDecimal subtract(BigDecimal num1, BigDecimal num2) {
        return num1.subtract(num2);
    }

    /**
     * 大十进制数的相乘
     * @param num1 前一个数
     * @param num2 后一个数
     * @return 结果
     */
    public static BigDecimal multiply(BigDecimal num1, BigDecimal num2) {
        return  num1.multiply(num2);
    }

    /**
     * 大十进制数的相除（四舍五入）
     * @param num1 前一个数
     * @param num2 后一个数
     * @return 结果
     */
    public static BigDecimal divide(BigDecimal num1, BigDecimal num2){
        return num1.divide(num2, 5,BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 大十进制数的相除（向下取整）
     * @param num1 前一个数
     * @param num2 后一个数
     * @return 结果
     */
    public static BigDecimal divideUp(BigDecimal num1, BigDecimal num2){
        return num1.divide(num2, 0,BigDecimal.ROUND_DOWN);
    }

    /**
     *  大十进制的绝对值
     * @param num 原数据
     * @return 结果
     */
    public static  BigDecimal abs(BigDecimal num) {
        return num.abs();
    }

    /**
     * 大十进制比较大小 （1 大于  0 等于  -1 小于）
     * @param num1 前一个数
     * @param num2 后一个数
     * @return 结果
     */
    public static int compareTo(BigDecimal num1, BigDecimal num2) {
        return num1.compareTo(num2);
    }

    /**
     * 加法操作
     * @param num1 前一个数
     * @param num2 后一个数
     * @return 结果
     */
    public static BigInteger add(BigInteger num1, BigInteger num2) {
        return num1.add(num2);
    }

    /**
     * 减法操作
     * @param num1 前一个数
     * @param num2 后一个数
     * @return 结果
     */
    public static BigInteger subtract(BigInteger num1, BigInteger num2) {
        return num1.subtract(num2);
    }

    /**
     * 乘法操作
     * @param num1 前一个数
     * @param num2 后一个数
     * @return 结果
     */
    public static BigInteger multiply(BigInteger num1, BigInteger num2){
        return num1.multiply(num2);
    }

    /**
     * 除法操作
     * @param num1 前一个数
     * @param num2 后一个数
     * @return 结果
     */
    public static BigInteger divide(BigInteger num1, BigInteger num2) {
        return num1.divide(num2);
    }

    /**
     * 求出最小数
     * @param num1 前一个数
     * @param num2 后一个数
     * @return 结果
     */
    public static BigDecimal min(BigDecimal num1, BigDecimal num2) {
        return num1.min(num2);
    }

    /**
     * 求出最大数
     * @param num1 前一个数
     * @param num2 后一个数
     * @return 结果
     */
    public static BigDecimal max(BigDecimal num1, BigDecimal num2) {
        return num1.max(num2);
    }

    /**
     * 求出余数的除法操作
     * 商是：result[0]
     * 余数是：result[1]
     * @param num1 前一个数
     * @param num2 后一个数
     * @return 结果
     */
    public static BigInteger[] divideAndRemainder(BigInteger num1, BigInteger num2) {
        return num1.divideAndRemainder(num2);
    }

}
