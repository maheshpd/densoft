package com.densoftinfotech.densoftpaysmart.retrofit;

import com.densoftinfotech.densoftpaysmart.model.BranchDetails;
import com.densoftinfotech.densoftpaysmart.model.CalendarDetails;
import com.densoftinfotech.densoftpaysmart.model.CareOfStaff_Employee;
import com.densoftinfotech.densoftpaysmart.model.CheckLeaveStatus;
import com.densoftinfotech.densoftpaysmart.model.LeaveAppliedDetails;
import com.densoftinfotech.densoftpaysmart.model.LeaveDetails;
import com.densoftinfotech.densoftpaysmart.model.MarkAttendanceDetails;
import com.densoftinfotech.densoftpaysmart.model.ParentEmployee;
import com.densoftinfotech.densoftpaysmart.model.SalarySlip;
import com.densoftinfotech.densoftpaysmart.model.StaffDetails;
import com.densoftinfotech.densoftpaysmart.model.TeamList;

import java.util.ArrayList;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
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

    @POST("LeaveDetails_Api")
    Call<ArrayList<LeaveDetails>>
    request_leavedetails(@Body RequestBody requestBody);

    @POST("LeaveAppliedDetails_App")
    Call<ArrayList<LeaveAppliedDetails>>
    request_leaveapplieddetails(@Body RequestBody requestBody);

    @POST("getParentEmployee_api")
    Call<ArrayList<ParentEmployee>>
    request_parent_report_to(@Body RequestBody requestBody);

    @POST("get_CareOfStaff_api")
    Call<ArrayList<CareOfStaff_Employee>>
    request_replacement_staff_during_absence(@Body RequestBody requestBody);

    @POST("IUD_LeaveApplication_Api")
    Call<ArrayList<CheckLeaveStatus>>
    request_leave_applied_success(@Body RequestBody requestBody);

    @POST("IUD_AttendanceIn_Api")
    Call<ArrayList<CheckLeaveStatus>>
    request_checkIn(@Body RequestBody requestBody);

    @POST("IUD_AttendanceOut_Api")
    Call<ArrayList<CheckLeaveStatus>>
    request_checkOut(@Body RequestBody requestBody);

    @POST("LocationPunching_App")
    Call<ArrayList<BranchDetails>>
    request_branch_details(@Body RequestBody requestBody);

    @POST("Get_StaffLeave_App")
    Call<ArrayList<TeamList>>
    request_team_list(@Body RequestBody requestBody);

}
