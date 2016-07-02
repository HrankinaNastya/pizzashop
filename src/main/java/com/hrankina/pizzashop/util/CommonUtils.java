package com.hrankina.pizzashop.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * creation date 25.06.2016
 *
 * @author A.Hrankina
 */
public class CommonUtils {

    public static boolean isNumeric(String str) {
        try {
            double d = Double.parseDouble(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static boolean isPhoneNumberValid(String phoneNumber){
        String expression = "^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$";
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }

}
