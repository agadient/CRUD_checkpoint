package com.example.demo;

import java.util.Map;

public class MathService {
    public String eval(Map querystring) {
        String operation = "";
        if (querystring.containsKey("operation")) {
            operation = (String) querystring.get("operation");
        }
        Integer x = 1;
        Integer y = 1;
        if (querystring.containsKey("x")) {
            x = Integer.parseInt((String) querystring.get("x"));
        }

        if (querystring.containsKey("y")) {
            y = Integer.parseInt((String) querystring.get("y"));
        }
        String result = "";
        if (operation.equals("subtract")) {
            result = String.format("%d - %d = %d", x, y, x-y);
        } else if (operation.equals("multiply")) {
            result = String.format("%d * %d = %d", x, y, x*y);
        } else if (operation.equals("divide")) {
            result = String.format("%d / %d = %d", x, y, x/y);
        } else {
            result = String.format("%d + %d = %d", x, y, x+y);
        }
        return result;
    }

}
