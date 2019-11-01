package com.densoftinfotech.densoftpaysmart.retrofit;

import com.densoftinfotech.densoftpaysmart.app_utilities.URLS;
import com.densoftinfotech.densoftpaysmart.classes.CalendarDetails;
import com.densoftinfotech.densoftpaysmart.classes.MarkAttendanceDetails;
import com.densoftinfotech.densoftpaysmart.classes.SalarySlip;
import com.densoftinfotech.densoftpaysmart.classes.StaffDetails;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface GetServiceInterface {

    @POST("stafflogin_Api")
    Call<ArrayList<StaffDetails>>
    request_login(@Body RequestBody fields);


    @POST("Get_Salary_Api")
    Call<ArrayList<SalarySlip>>
    request_salary(@Body RequestBody fields);

    @POST("Get_InoutAtt_App")
    Call<ArrayList<MarkAttendanceDetails>>
    request_attendance(@Body RequestBody fields);

    @POST("Get_InoutAtt_App")
    Call<ArrayList<CalendarDetails>>
    request_planner(@Body RequestBody fields);

}
