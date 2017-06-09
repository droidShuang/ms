package com.junova.ms.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by junova on 2017-02-22.
 */

public class RegexUtil {
    /**
     * @param password 密码
     * @describe 检测密码是否符合1-9 A-Z a-z
     * @author 杨爽
     * @time
     * @version 1.0
     */
    public static boolean isPassword(String password) {
        String str = "[A-Za-z0-9]{6,12}";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(password);
        return m.matches();
    }
}
