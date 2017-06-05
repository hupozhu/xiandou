package cn.sampson.android.xiandou.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegularUtils {

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static final Pattern VALID_PHONE_REGEX = Pattern.compile("^((13[0-9])|(15[0-9])|(18[0-9])|(17[0-9]))\\d{8}$");

    public static boolean validateEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

    public static boolean isMobileNO(String mobiles) {
        Matcher m = VALID_PHONE_REGEX.matcher(mobiles);
        return m.find();
    }

    public static String filterCellphone(String str) {
        String original = filterUnNumber(str);
        if (original.length() < 11) {//不是一个手机号
            return "";
        } else {
            String cellphone = original.substring(original.length() - 11);
            if (isMobileNO(cellphone)) {
                return cellphone;
            } else {
                return "";
            }
        }
    }

    public static String filterUnNumber(String str) {
        // 只允数字
        String regEx = "[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        //替换与模式匹配的所有字符（即非数字的字符将被""替换）
        return m.replaceAll("").trim();
    }

}