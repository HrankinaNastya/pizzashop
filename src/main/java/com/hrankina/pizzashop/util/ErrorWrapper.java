package com.hrankina.pizzashop.util;

import java.util.HashMap;
import java.util.Map;

/**
 * creation date 25.06.2016
 *
 * @author A.Hrankina
 */
public class ErrorWrapper {

    public static Map<String, String> wrap(String msg) {
        Map<String, String> result = new HashMap<>();
        result.put("msg", msg);
        return result;
    }

}
