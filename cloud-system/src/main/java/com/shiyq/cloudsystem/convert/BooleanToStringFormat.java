package com.shiyq.cloudsystem.convert;

public class BooleanToStringFormat {

    /**
     * Boolean --> String (0: false, 1: true)
     * @param bool 带转型Boolean值
     * @return 转型后的String值
     */
    public String booleanToString(Boolean bool){
        return bool == null ?  null : (bool ? "1" : "0");
    }

    /**
     * String --> Boolean (0: false, 1: true)
     * @param str 带转型String值
     * @return 转型后的Boolean值
     */
    public Boolean stringToBoolean(String str){
        return str.equals("1");
    }

}
