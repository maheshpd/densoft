package com.densoftinfotech.densoftpaysmart.app_utilities;

public class URLS {

    public final static String common_url_webroute(){
        return "http://" + Constants.ip + "//WebRoute/";
    }

    public final static String dynamic_url_webroute(String domain){
        return "http://" + domain +"/";
    }

    public static final String login_api(){
        return common_url_webroute() + "stafflogin_Api";
    }

    public static String get_salary_api(){
        return common_url_webroute() + "Get_Salary_Api";
    }

    public static String get_thumb_history_api(){
        return common_url_webroute() + "Get_InoutAtt_App";
    }
}
