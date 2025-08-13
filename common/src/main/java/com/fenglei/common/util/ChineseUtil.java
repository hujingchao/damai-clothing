package com.fenglei.common.util;

import net.sourceforge.pinyin4j.PinyinHelper;

public class ChineseUtil {
    public static String getFirstLetter(String chinese) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < chinese.length(); i++) {
            String c = String.valueOf(chinese.charAt(i));
            if (c.matches("[\\u4E00-\\u9FA5]")) {
                sb.append(convertToPinyin(c));
            }
        }
        return sb.toString();
    }

    private static String convertToPinyin(String chinese) {
        String[] pinyin = PinyinHelper.toHanyuPinyinStringArray(chinese.charAt(0));
        if (pinyin != null && pinyin.length > 0) {
            return String.valueOf(pinyin[0].charAt(0)).toUpperCase();
        }
        return "";
    }
}
